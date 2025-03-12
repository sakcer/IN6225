<template>
  <div class="project-list">
    <!-- 面包屑导航 -->
    <Breadcrumb label="Projects" />

    <h1 class="text-3xl font-bold mb-6">Projects</h1>

    <div class="mb-6 flex flex-wrap gap-4">
      <!-- 状态切换 -->
      <status-toggle v-model="status" :status="PROJECT_STATUS" />

      <!-- 搜索框 -->
      <search-input v-model="searchQuery" placeholder="Search projects" />

      <!-- 添加项目按钮 -->
      <div class="ml-auto">
        <add-button button-text="Add Project" v-model:dialogVisible="dialogVisible" v-model:isEdit="isEdit"
          v-model:form="form" />
      </div>
    </div>

    <!-- 项目卡片网格 -->
    <el-row :gutter="20" max-height="300px">
      <el-col v-for="project in filteredProjects" :key="project.id" :xs="24" :sm="12" :md="8" :lg="6" class="mb-4">
        <project-card :users="users" :project="project" v-model:dialogVisible="dialogVisible" v-model:isEdit="isEdit"
          v-model:form="form" />
      </el-col>
    </el-row>

    <!-- 添加/编辑项目对话框 -->
    <project-form v-model="dialogVisible" :is-edit="isEdit" :rules="rules" :form="form" :users="users" />

  </div>
</template>

<style scoped>
.border-primary {
  border-color: var(--el-color-primary);
}

.el-card {
  height: 100%;
  position: relative;
}

.el-progress {
  margin-bottom: 1rem;
}

.el-avatar {
  border: 2px solid white;
}
</style>

<script setup lang="ts">
import Breadcrumb from '@/components/Breadcrumb.vue'
import StatusToggle from '@/components/StatusToggle.vue'
import SearchInput from '@/components/SearchInput.vue'
import AddButton from '@/components/AddButton.vue'
import ProjectCard from './widgets/ProjectCard.vue'
import ProjectForm from './widgets/ProjectForm.vue'
import { employeeService } from '@/services/employees/employeeService'
import { projectService } from '@/services/projects/projectService'
import { PROJECT_STATUS } from '@/utils/constants'
import { reactive, ref, computed, watch, onMounted } from 'vue'
import { Edit, Delete, Search, Plus, Calendar } from '@element-plus/icons-vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { rules } from '@/utils/types/project'
import { useProjectsStore } from '@/store/projectStore'
import { useUsersStore } from '@/store/userStore'

// 状态管理
const status = ref(PROJECT_STATUS.ALL)
const searchQuery = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)

const projectsStore = useProjectsStore()
const usersStore = useUsersStore()
const users = computed(() => usersStore.getUsers)
const projects = computed(() => projectsStore.getProjects)

// 表单数据
const form = ref({
  name: '',
  id: 0,
  description: '',
  status: PROJECT_STATUS.ACTIVE,
  progress: 0,
  dateRange: [],
  leaderId: 0,
  memberIds: []
})

// 过滤项目列表
const filteredProjects = computed(() => {
  return projects.value.filter(project => {
    const matchesStatus = status.value === PROJECT_STATUS.ALL || project.status === status.value
    const matchesSearch = project.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      project.description.toLowerCase().includes(searchQuery.value.toLowerCase())
    return matchesStatus && matchesSearch
  })
})

</script>
