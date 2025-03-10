<script setup lang="ts">
import Breadcrumb from '@/components/Breadcrumb.vue'
import StatusToggle from '@/components/StatusToggle.vue'
import SearchInput from '@/components/SearchInput.vue'
import AddButton from '@/components/AddButton.vue'
import ProjectCard from './widgets/ProjectCard.vue'
import ProjectForm from './widgets/ProjectForm.vue'
import { PROJECT_STATUS } from '@/utils/constants'

import { ref, computed, watch } from 'vue'
import { Edit, Delete, Search, Plus, Calendar } from '@element-plus/icons-vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'

import { rules } from '@/utils/types/project'
import { useUserStore } from '@/store/userStore'

// 状态管理
const status = ref('all')
const searchQuery = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref<FormInstance>()

// 表单数据
const form = ref({
  name: '',
  description: '',
  status: 'active',
  progress: 0,
  dateRange: [],
  leader: '',
  members: []
})

// 模拟用户数据
const { users } = useUserStore();

// 模拟项目数据
const projects = ref([
  {
    id: 1,
    name: 'Website Redesign',
    description: 'Redesign company website with modern UI/UX',
    status: 'active',
    progress: 75,
    startDate: '2024-01-01',
    endDate: '2024-06-30',
    leader: { id: 1, name: 'John Doe' },
    members: [
      { id: 1, name: 'John Doe' },
      { id: 2, name: 'Jane Smith' },
      { id: 3, name: 'Mike Johnson' },
      { id: 4, name: 'Sarah Williams' }
    ]
  },
  {
    id: 2,
    name: 'Mobile App Development',
    description: 'Develop iOS and Android mobile applications',
    status: 'active',
    progress: 30,
    startDate: '2024-02-01',
    endDate: '2024-08-31',
    leader: { id: 3, name: 'Mike Johnson' },
    members: [
      { id: 1, name: 'John Doe' },
      { id: 4, name: 'Sarah Williams' }
    ]
  }
])

// 过滤项目列表
const filteredProjects = computed(() => {
  return projects.value.filter(project => {
    const matchesStatus = status.value === 'all' || project.status === status.value
    const matchesSearch = project.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
                         project.description.toLowerCase().includes(searchQuery.value.toLowerCase())
    return matchesStatus && matchesSearch
  })
})

// 获取头像颜色（复用EmployeeList的方法）
const getAvatarColor = (name: string) => {
  const colors = ['#f56c6c', '#e6a23c', '#67c23a', '#409eff', '#909399']
  let hash = 0
  for (let i = 0; i < name.length; i++) {
    hash = name.charCodeAt(i) + ((hash << 5) - hash)
  }
  return colors[Math.abs(hash) % colors.length]
}

// 获取头像文本（复用EmployeeList的方法）
const getAvatarText = (name: string) => {
  if (!name) return ''
  const parts = name.split(' ')
  if (parts.length >= 2) {
    return (parts[0][0] + parts[1][0]).toUpperCase()
  }
  return name.slice(0, 2).toUpperCase()
}

// 处理搜索
const handleSearch = () => {
  // 实现搜索逻辑
}

// 处理添加项目
const handleAddProject = () => {
  isEdit.value = false
  form.value = {
    name: '',
    description: '',
    status: 'active',
    progress: 0,
    dateRange: [],
    leader: '',
    members: []
  }
  dialogVisible.value = true
}

// 处理编辑项目
const handleEditProject = (project: any) => {
  isEdit.value = true
  form.value = {
    name: project.name,
    description: project.description,
    status: project.status,
    progress: project.progress,
    dateRange: [project.startDate, project.endDate],
    leader: project.leader.id,
    members: project.members.map((m: any) => m.id)
  }
  dialogVisible.value = true
}

// 处理删除项目
const handleDeleteProject = (project: any) => {
  ElMessageBox.confirm(
    `确定要删除项目 "${project.name}" 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    // 实现删除逻辑
    ElMessage.success('删除成功')
  }).catch(() => {
    // 取消删除
  })
}

// 处理表单提交
const handleSubmit = async () => {
  console.log('handleSubmit', formRef.value);
  console.log('users', users.value);

  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 这里应该调用API保存数据
        ElMessage.success(isEdit.value ? '更新成功' : '添加成功')
        dialogVisible.value = false
      } catch (error) {
        ElMessage.error(isEdit.value ? '更新失败' : '添加失败')
      }
    }
  })
}

// 处理关闭对话框
const handleClose = () => {
  dialogVisible.value = false
}

// 监听dialogVisible的变化
watch(dialogVisible, (val) => {
  if (!val) {
    formRef.value?.resetFields();
  }
});
</script>

<template>
  <div class="project-list">
    <!-- 面包屑导航 -->
    <Breadcrumb label="Projects" />

    <h1 class="text-3xl font-bold mb-6">Projects</h1>

    <div class="mb-6 flex flex-wrap gap-4">
      <!-- 状态切换 -->
      <status-toggle v-model="status" :status="PROJECT_STATUS" />

      <!-- 搜索框 -->
      <search-input v-model="searchQuery" placeholder="Search projects" @search="handleSearch" />

      <!-- 添加项目按钮 -->
      <div class="ml-auto">
        <add-button button-text="Add Project" @click="handleAddProject" />
      </div>
    </div>

    <!-- 项目卡片网格 -->
    <el-row :gutter="20">
      <el-col 
        v-for="project in filteredProjects" 
        :key="project.id" 
        :xs="24" 
        :sm="12" 
        :md="8" 
        :lg="6" 
        class="mb-4"
      >
        <project-card :project="project" :handle-edit-project="handleEditProject" :handle-delete-project="handleDeleteProject"/>
      </el-col>

    </el-row>

    <!-- 添加/编辑项目对话框 -->
    <project-form
      v-model="dialogVisible"
      v-model:formRef="formRef"
      :is-edit="isEdit"
      :rules="rules"
      :form="form"
      :users="users"  
      :handle-submit="handleSubmit"
      :handle-close="handleClose"
    />

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
