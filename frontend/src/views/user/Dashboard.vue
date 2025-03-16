<template>
  <div class="container mx-auto p-6">
    <!-- Top information bar -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
      <!-- Time Card -->
      <el-card shadow="hover" class="bg-gradient-to-r from-blue-500 to-blue-600">
        <div class="text-white">
          <div class="text-sm">{{ currentDate }}</div>
          <div class="text-2xl font-bold">{{ currentTime }}</div>
          <div class="text-sm">{{ weekDay }}</div>
        </div>
      </el-card>

      <!-- Personal Information Card -->
      <el-card shadow="hover" class="bg-gradient-to-r from-purple-500 to-purple-600">
        <div class="flex items-center text-white">
          <el-avatar :size="50" :src="me?.avatar || ''"
            :style="{ backgroundColor: getAvatarColor(me?.name || '') }">
            {{ getAvatarText(me?.name || '') }}
          </el-avatar>
          <div class="ml-4">
            <div class="text-lg font-bold">{{ me?.name || 'No name' }}</div>
            <div class="text-sm">{{ me?.role || 'No role' }}</div>
            <div class="text-sm">{{ me?.department || 'No department' }}</div>
          </div>
        </div>
      </el-card>

      <!-- Project Statistics Card -->
      <el-card shadow="hover" class="bg-gradient-to-r from-green-500 to-green-600">
        <div class="text-white">
          <div class="text-lg font-bold">Project Statistics</div>
          <div class="flex justify-between mt-2">
            <div>
              <div class="text-sm">Participated</div>
              <div class="text-xl font-bold">{{ myProjects.length }}</div>
            </div>
            <div>
              <div class="text-sm">Managed</div>
              <div class="text-xl font-bold">{{ managedProjects.length }}</div>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- Managed Projects -->
    <div class="mb-6">
      <h2 class="text-xl font-bold mb-4">My Projects</h2>

      <div class="mb-6 w-full">
        <div class="flex items-center mb-4">
          <status-toggle v-model="status" :status="PROJECT_STATUS" />
          <div class="flex-grow ml-4">
            <search-input v-model="searchString" placeholder="Search Projects" />
          </div>
        </div>

        <div class="flex justify-end">
          <div class="ml-auto">
            <div class="view-switch">
              <el-radio-group v-model="viewMode">
                <el-radio-button value="card">Card View</el-radio-button>
                <el-radio-button value="list">List View</el-radio-button>
              </el-radio-group>
            </div>
          </div>
        </div>
      </div>

      <!-- Card View -->
      <div v-if="viewMode === 'card'" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        <template v-if="pageProjects.length > 0">
          <div v-for="project in pageProjects" :key="project.id">
            <ProjectCard 
              :project="project"
              :users="users"
              :me="me" as Employee
              @edit-project="handleEditProject"
              @view-project="handleViewProject"
            />
          </div>
        </template>
        <div v-else class="col-span-full text-center text-gray-500 py-4">
          No Data
        </div>
      </div>

      <!-- List View -->
      <div v-else>
        <project-row 
          :projects="pageProjects"
          :users="users" 
          :me="me" 
          @edit-project="handleEditProject" 
          @view-project="handleViewProject" />
      </div>
    </div>

    <pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :total="total" />

    <project-form-card 
      v-model:dialogVisible="dialogVisible"
      :form="form"
      :users="users" 
      :me="me"
      :form-type="formType"
      @close-project="handleClose"
      @submit-project="handleSubmit" />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import ProjectCard from '@/components/Project/ProjectCard.vue'
import ProjectRow from '@/components/Project/ProjectRow.vue'
import ProjectFormCard from '@/components/Project/ProjectForm.vue'
import SearchInput from '@/components/SearchInput.vue'
import StatusToggle from '@/components/StatusToggle.vue'
import Pagination from '@/components/Pagination.vue'
import { useMeStore } from '@/store/meStore'
import { useUsersStore } from '@/store/userStore'
import { useMyProjectStore } from '@/store/myProjectStore'
import { PROJECT_STATUS, PAGE_SIZES } from '@/utils/constants'
import { getAvatarColor, getAvatarText } from '@/utils/avatar'
import { projectService } from '@/services/projects/projectService'
import { ElMessage } from 'element-plus'
import type { Project } from '@/utils/types/project'
import type { Employee } from '@/utils/types/employee'

// States
const dialogVisible = ref(false)
const formType = ref(0)
const form = ref({} as Project)
const status = ref(PROJECT_STATUS.ALL)
const currentPage = ref(1)
const pageSize = ref(PAGE_SIZES[0])
const total = computed(() => filteredProjects.value.length)
const viewMode = ref('card')
const searchString = ref('');

// Time-related
const currentDate = ref(new Date().toLocaleDateString('en-US'))
const currentTime = ref(new Date().toLocaleTimeString('en-US'))
const weekDay = ref(['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'][new Date().getDay()])

// Core-datas
const meStore = useMeStore()
const usersStore = useUsersStore()

const myProjectStore = useMyProjectStore()
const users = computed(() => usersStore.users)
const me = computed(() => meStore.me)

const myProjects = computed(() => myProjectStore.getProjects as Project[])

const managedProjects = computed(() => {
  return (myProjects.value ?? [] as Project[]).filter((p: Project) => p.leader.id === me.value?.id)
})

const filteredProjects = computed(() => {
  return (myProjects.value ?? [] as Project[]).filter((project: Project) => {
    const matchesStatus = status.value === PROJECT_STATUS.ALL || project.status === status.value
    const matchesSearch = project.name.toLowerCase().includes(searchString.value.toLowerCase()) ||
      project.description.toLowerCase().includes(searchString.value.toLowerCase())
    return matchesStatus && matchesSearch
  })
})

const pageProjects = computed(() => {
  return filteredProjects.value.slice((currentPage.value - 1) * pageSize.value, currentPage.value * pageSize.value)
})

const handleEditProject = (project: Project) => {
  formType.value = 1
  form.value = project
  dialogVisible.value = true
}

const handleViewProject = (project: Project) => {
  formType.value = 0
  form.value = project as Project
  dialogVisible.value = true
}

const handleClose = () => {
  dialogVisible.value = false
}

const handleSubmit = async (form: Project) => {
  try {
    form.members = form.memberIds.map((id: number) => ({ id: id} as Employee));
    let data;
    if (formType.value === 1) {
      data = await projectService.updateProject(form);
    } else if (formType.value === 3) {
      data = await projectService.createProject(form);
    } else {
      ElMessage.error('Add failed');
      return;
    }
    ElMessage.success('Project [' + data.id + '] added successfully');
    myProjectStore.refetchProjects()
  } catch (error) {
    ElMessage.error('Add failed');
    console.log(error);
  } finally {
    setTimeout(() => {
      dialogVisible.value = false;
    }, 500);
  }
}

const updateTime = () => {
  const now = new Date()
  currentDate.value = now.toLocaleDateString('en-US')
  currentTime.value = now.toLocaleTimeString('en-US')
  weekDay.value = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'][now.getDay()]
}

onMounted(() => {
  updateTime()
  setInterval(updateTime, 1000)
  usersStore.refetchUsers()
  myProjectStore.refetchProjects()
})
</script>
