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
        <add-button button-text="Add Project"
        @add="handleAddProject"/>
      </div>
    </div>

    <!-- 项目卡片网格 -->
    <!-- <el-row :gutter="20" max-height="300px">
      <el-col v-for="project in filteredProjects" :key="project.id" :xs="24" :sm="12" :md="8" :lg="6" class="mb-4">
        <project-card :users="users" :project="project" v-model:dialogVisible="dialogVisible" v-model:isEdit="isEdit"
          v-model:form="form" />
      </el-col>
    </el-row> -->

    <div class="mb-6">
      <!-- 列表视图 -->
      <project-row :projects="pageProjects" :users="users" :me="me" 
      @view-project="handleViewProject"
      @edit-project="handleEditProject"
      @delete-project="handleDeleteProject"
      />
    </div>

    <pagination v-model:current-page="currentPage" v-model:page-size="pageSize"
    :total="total" />


    <!-- 添加/编辑项目对话框 -->
    <project-form v-model:dialogVisible="dialogVisible" 
      :form-type="formType"
      :form="form" 
      :me="me"
      :users="users"
      @close-project="handleClose"
      @submit-project="handleSubmit" />

  </div>
</template>

<script setup lang="ts">
import Breadcrumb from '@/components/Breadcrumb.vue'
import StatusToggle from '@/components/StatusToggle.vue'
import SearchInput from '@/components/SearchInput.vue'
import AddButton from '@/components/AddButton.vue'
import ProjectCard from './widgets/ProjectCard.vue'
import ProjectForm from './widgets/ProjectForm.vue'
import Pagination from '@/components/Pagination.vue'
import ProjectRow from './widgets/ProjectRow.vue'
import { employeeService } from '@/services/employees/employeeService'
import { projectService } from '@/services/projects/projectService'
import { PROJECT_STATUS } from '@/utils/constants'
import { reactive, ref, computed, watch, onMounted } from 'vue'
import { Edit, Delete, Search, Plus, Calendar } from '@element-plus/icons-vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { useProjectsStore } from '@/store/projectStore'
import { useUsersStore } from '@/store/userStore'
import { useMeStore } from '@/store/meStore'
import { PAGE_SIZES } from '@/utils/constants'

// 状态管理
const status = ref(PROJECT_STATUS.ALL)
const searchQuery = ref('')
const dialogVisible = ref(false)
const formType = ref(0)

const projectsStore = useProjectsStore()
const usersStore = useUsersStore()
const meStore = useMeStore()
const users = computed(() => usersStore.getUsers)
const projects = computed(() => projectsStore.getProjects)
const me = computed(() => meStore.getMe)

const currentPage = ref(1)
const pageSize = ref(PAGE_SIZES[0])
const total = computed(() => filteredProjects.value.length)


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

const pageProjects = computed(() => {
  return filteredProjects.value.slice((currentPage.value - 1) * pageSize.value, currentPage.value * pageSize.value)
})

const handleViewProject = (row) => {
    console.log("处理查看项目的逻辑")
    form.value = row
    formType.value = 0;
    dialogVisible.value = true
    console.log(row);
};

const handleEditProject = (row) => {
    console.log("处理编辑项目的逻辑")
    console.log(row);
    form.value = row
    formType.value = 1;
    dialogVisible.value = true
};

const handleDeleteProject = async (row) => {
    console.log("处理删除项目的逻辑")
    console.log(row);
    try {
      await ElMessageBox.confirm(`确定要删除项目 ${row.name} 吗？`, '警告', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' });
      const data = await projectService.deleteProject(row.id!);
      ElMessage.success('删除项目[' + row.projectId + ']成功: ' + data.message);
      projectsStore.refetchProjects()
    } catch (error) {
      if (error !== 'cancel') {
        ElMessage.error('删除失败');
        console.error(error);
      }
    }
};

const handleAddProject = () => {
  console.log("处理添加项目的逻辑")
  formType.value = 3;
  dialogVisible.value = true
}

const handleClose = (form) => {
  console.log("处理关闭项目的逻辑")
  console.log(form);
  dialogVisible.value = false
}

const handleSubmit = async (form) => {
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
    } else {
      data = await projectService.createProject(form);
    }
    ElMessage.success('添加项目[' + data.id + ']成功');
    projectsStore.refetchProjects()
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

</script>
