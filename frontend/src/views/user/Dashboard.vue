<template>
  <div class="container mx-auto p-6">

    <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
    <el-card shadow="hover">
        <div class="flex justify-between items-center">
          <div>
            <div class="text-sm text-gray-500">{{ weekDay }}, {{ currentDate }}</div>
            <div class="text-3xl font-semibold text-gray-800 tracking-wide">{{ currentTime }}</div>
            <div class="text-sm text-gray-400 mt-1">Have a great day ahead! 🎉</div>
          </div>
        </div>
      </el-card>
      <el-card shadow="hover">
        <div class="flex items-center">
          <el-avatar
            :size="60"
            :src="userStore.userInfo.avatar || ''"
            :style="{ backgroundColor: getAvatarColor(userStore.userInfo.name) }"
          >
            {{ getAvatarText(userStore.userInfo.name) }}
          </el-avatar>
          <div class="ml-4 space-y-1">
            <div class="flex items-center gap-3">
              <div class="text-xl font-semibold text-gray-900">{{ userStore.userInfo.name }}</div>
              <el-tag :type="userStore.userInfo.status === USER_STATUS.ACTIVE ? 'success' : 'info'" class="text-sm">
                {{ userStore.userInfo.status }}
              </el-tag>
            </div>
            <div class="text-sm text-gray-600">Title: <span class="font-medium">{{ userStore.userInfo.title || 'N.A.' }}</span></div>
            <div class="text-sm text-gray-500">Email: <span class="font-medium">{{ userStore.userInfo.email }}</span></div>
          </div>
        </div>
      </el-card>
    </div>

    <div class="mb-6">
      <h2 class="text-xl font-bold mb-4">My Projects</h2>

      <div class="mb-6 w-full">
        <div class="flex items-center mb-4">
          <status-toggle v-model="status" :status="PROJECT_STATUS" />
          <div class="flex-grow ml-4">
            <search-input v-model="searchQuery" placeholder="Search Projects" />
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
              @edit-project="handleEditProject"
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
          @edit-project="handleEditProject" />
      </div>
    </div>

    <pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :total="total" />

    <project-form-card 
      v-model:dialogVisible="dialogVisible"
      v-model:loading="loading"
      :form="form"
      :form-type="formType"
      @close-project="handleClose"
      @submit-project="handleSubmit" />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import ProjectCard from '@/components/Project/ProjectCard.vue'
import ProjectRow from '@/components/Project/ProjectRow.vue'
import ProjectFormCard from '@/components/Project/ProjectForm.vue'
import SearchInput from '@/components/SearchInput.vue'
import StatusToggle from '@/components/StatusToggle.vue'
import Pagination from '@/components/Pagination.vue'
import { useUserStore } from '@/store/userStore'
import { PROJECT_STATUS, PAGE_SIZES, FORM_TYPES, USER_STATUS } from '@/utils/constants'
import { getAvatarColor, getAvatarText } from '@/utils/avatar'
import { projectService } from '@/services/projects/projectService'
import { ElMessage } from 'element-plus'
import type { Project } from '@/utils/types/project'
import { handleAxiosError } from '@/utils/errorMsg'

// States
const dialogVisible = ref(false)
const formType = ref(FORM_TYPES.ADD)
const form = ref({} as Project)
const status = ref(PROJECT_STATUS.ALL)
const currentPage = ref(1)
const pageSize = ref(PAGE_SIZES[0])
const total = ref(0)
const loading = ref(false)
const viewMode = ref('card')

const sortParam = ref('name')
const sortOrder = ref('ascending')
const searchQuery = ref('')

// Time-related
const currentDate = ref(new Date().toLocaleDateString('en-US'))
const currentTime = ref(new Date().toLocaleTimeString('en-US'))
const weekDay = ref(['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'][new Date().getDay()])

// Core-datas
const userStore = useUserStore()
const pageProjects = ref([] as Project[])

const handleEditProject = (project: Project) => {
  formType.value = FORM_TYPES.EDIT
  form.value = {...project}
  dialogVisible.value = true
}

const handleClose = () => {
  dialogVisible.value = false
}

const handleSubmit = async (form: Project) => {
  try {
    loading.value = true
    let data;
    if (formType.value === FORM_TYPES.EDIT) {
      data = await projectService.updateProject(form);
    } else {
      data = await projectService.createProject(form);
    }
    refetch()
    setTimeout(() => {
      ElMessage.success('Project [' + data.id + '] added successfully');
      dialogVisible.value = false;
    }, 500);
  } catch (error) {
    handleAxiosError(error);
  } finally {
    loading.value = false;
  }
}

const refetch = async () => {
  try {
    const data = await projectService.getProjectsByFilter(currentPage.value - 1, pageSize.value, `${sortParam.value},${sortOrder.value}`, searchQuery.value, status.value)
    pageProjects.value = data.projects
    total.value = data.total
  } catch (error) {
    handleAxiosError(error);
  }
}

watch([currentPage, pageSize, sortParam, sortOrder, searchQuery, status], () => {
  refetch()
})

const updateTime = () => {
  const now = new Date()
  currentDate.value = now.toLocaleDateString('en-US')
  currentTime.value = now.toLocaleTimeString('en-US')
  weekDay.value = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'][now.getDay()]
}

onMounted(async () => {
  updateTime()
  setInterval(updateTime, 1000)

  refetch()
})
</script>
