<template>
  <div class="container mx-auto p-6">
    <!-- 顶部信息栏 -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
      <!-- 时间卡片 -->
      <el-card shadow="hover" class="bg-gradient-to-r from-blue-500 to-blue-600">
        <div class="text-white">
          <div class="text-sm">{{ currentDate }}</div>
          <div class="text-2xl font-bold">{{ currentTime }}</div>
          <div class="text-sm">{{ weekDay }}</div>
        </div>
      </el-card>

      <!-- 个人信息卡片 -->
      <el-card shadow="hover" class="bg-gradient-to-r from-purple-500 to-purple-600">
        <div class="flex items-center text-white">
          <el-avatar :size="50" :src="mockUser.avatar"
            :style="{ backgroundColor: getAvatarColor(mockUser.name || '') }">
            {{ getAvatarText(mockUser.name) }}
          </el-avatar>
          <div class="ml-4">
            <div class="text-lg font-bold">{{ mockUser.name }}</div>
            <div class="text-sm">{{ mockUser.role }}</div>
            <div class="text-sm">{{ mockUser.department }}</div>
          </div>
        </div>
      </el-card>

      <!-- 项目统计卡片 -->
      <el-card shadow="hover" class="bg-gradient-to-r from-green-500 to-green-600">
        <div class="text-white">
          <div class="text-lg font-bold">项目统计</div>
          <div class="flex justify-between mt-2">
            <div>
              <div class="text-sm">参与项目</div>
              <div class="text-xl font-bold">{{ mockProjects.length }}</div>
            </div>
            <div>
              <div class="text-sm">管理项目</div>
              <div class="text-xl font-bold">{{ managedProjects.length }}</div>
            </div>
          </div>
        </div>
      </el-card>
    </div>


    <!-- 管理的项目 -->
    <div class="mb-6 " >
      <h2 class="text-xl font-bold mb-4">我的项目</h2>

      <div class="mb-6 w-full">
          <div class="flex items-center mb-4">
          <status-toggle v-model="status" :status="PROJECT_STATUS" />
            <div class="flex-grow ml-4">
              <search-input v-model="searchString" placeholder="搜索项目" />
            </div>
          </div>

          <div class="flex justify-end">
          <div class="ml-auto">
          <div class="view-switch">

          <el-radio-group v-model="viewMode" >
              <el-radio-button value="card">卡片视图</el-radio-button>
              <el-radio-button value="list">列表视图</el-radio-button>
          </el-radio-group>
          </div>
          </div>
          </div>
      </div>

      <!-- 卡片视图 -->
      <div v-if="viewMode === 'card'" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        <div v-for="project in pageProjects" :key="project.id">
          <ProjectCard 
            :project="project"
            :users="users"
            :me="me"
            @edit-project="handleEditProject"
            @view-project="handleViewProject"
          />
        </div>
      </div>

      <!-- 列表视图 -->
      <div v-else>
          <project-row 
          :projects="pageProjects"
          :users="users" 
          :me="me" 
          @edit-project="handleEditProject" 
          @view-project="handleViewProject" />
      </div>
    </div>

    <pagination v-model:current-page="currentPage" v-model:page-size="pageSize"
    :total="total" />

    <project-form 
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
import ProjectCard from './widgets/ProjectCard.vue'
import ProjectRow from '@/views/projects/widgets/ProjectRow.vue'
import ProjectForm from '@/views/projects/widgets/ProjectForm.vue'
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

// 状态
const dialogVisible = ref(false)
const formType = ref(0)
const formRef = ref()
const form = ref<Project>({})

const status = ref(PROJECT_STATUS.ALL)
const meStore = useMeStore()
const usersStore = useUsersStore()

const myProjectStore = useMyProjectStore()
const users = computed(() => usersStore.users)
const me = computed(() => meStore.me)

const currentPage = ref(1)
const pageSize = ref(PAGE_SIZES[0])
const total = computed(() => filteredProjects.value.length)

// 视图模式
const viewMode = ref('card') // 'card' | 'list'
const searchString = ref('');

// 模拟数据
const mockUser: User = computed(() => meStore.me)

const mockProjects: Project[] = computed(() => myProjectStore.getProjects)

const filteredProjects = computed(() => {
  return mockProjects.value.filter(project => {
    const matchesStatus = status.value === PROJECT_STATUS.ALL || project.status === status.value
    const matchesSearch = project.name.toLowerCase().includes(searchString.value.toLowerCase()) ||
      project.description.toLowerCase().includes(searchString.value.toLowerCase())
    return matchesStatus && matchesSearch
  })
})

const pageProjects = computed(() => {
  return filteredProjects.value.slice((currentPage.value - 1) * pageSize.value, currentPage.value * pageSize.value)
})

// 计算属性
const managedProjects = computed(() => {
    console.log(mockProjects.value);
    return mockProjects.value.filter(p => p.leader.id === mockUser.value.id)
})

const participatedProjects = computed(() => 
    mockProjects.value
)

// 时间相关
const currentDate = ref(new Date().toLocaleDateString('zh-CN'))
const currentTime = ref(new Date().toLocaleTimeString('zh-CN'))
const weekDay = ref(['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'][new Date().getDay()])

// 方法
const updateTime = () => {
  const now = new Date()
  currentDate.value = now.toLocaleDateString('zh-CN')
  currentTime.value = now.toLocaleTimeString('zh-CN')
  weekDay.value = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'][now.getDay()]
}

const handleEditProject = (project: Project) => {
  console.log("处理编辑项目的逻辑")
  console.log(project);
  formType.value = 1

  form.value = project
  
  // isEdit.value = true
  // form.value = { ...project }
  dialogVisible.value = true
}

const handleViewProject = (project: Project) => {
  console.log("处理查看项目的逻辑")
  console.log(project);
  formType.value = 0

  form.value = project
  dialogVisible.value = true
}

const handleClose = (project: Project) => {
  console.log("处理关闭项目的逻辑")
  console.log(project);
  dialogVisible.value = false
}

const handleSubmit = async (form: Project) => {
  console.log("处理提交项目的逻辑")
  console.log(form);

  try {
    // loading.value = true;
    form.members = form.memberIds.map(id => ({ id: id, name: "xxx"}));
    console.log(form.members);
    delete form.memberIds;
    console.log(form);
    console.log(formType.value);

    let data;
    if (formType.value === 1) {
      data = await projectService.updateProject(form.id!, form);
    } else if (formType.value === 3) {
      data = await projectService.createProject(form);
    } else {
      ElMessage.error('添加失败');
      return;
    }
    ElMessage.success('添加项目[' + data.id + ']成功');
    myProjectStore.refetchProjects()
  } catch (error) {
    ElMessage.error('添加失败');
    console.log(error);
  } finally {
    setTimeout(() => {
      dialogVisible.value = false;
      // loading.value = false;
    }, 500);
  }
}

// 生命周期
onMounted(() => {
  updateTime()
  setInterval(updateTime, 1000)
  usersStore.refetchUsers()
  myProjectStore.refetchProjects()
})
</script>

<style scoped>
.el-card {
  margin-bottom: 1rem;
}

.el-table {
  margin-bottom: 1rem;
}
</style>
