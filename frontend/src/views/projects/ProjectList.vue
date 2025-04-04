
<template>
  <div>
    <!-- Breadcrumb navigation -->
    <Breadcrumb label="Projects" />

    <h1 class="text-3xl font-bold mb-6">Projects</h1>

    <!-- Statistics cards -->
    <el-row :gutter="20" class="mb-6">
      <el-col :span="12" v-for="(stat, index) in statsData" :key="index">
        <statistics-card v-if="stat" :stat="stat" />
      </el-col>
    </el-row>

    <el-card shadow="hover" class="mb-6">

      <div class="w-full mb-6">
        <!-- Status toggle -->
        <div class="flex items-center mb-4">
          <status-toggle v-model="status" :status="PROJECT_STATUS" />

          <div class="flex-grow ml-4">
            <!-- Search box -->
            <search-input v-model="searchQuery" placeholder="Search projects" />
          </div>
        </div>

        <!-- Add project button -->
        <div class="flex justify-end">
          <add-button button-text="Add Project" @add="handleAddProject" />
        </div>
      </div>

      <!-- List view -->
      <project-row :projects="pageProjects" 
        @edit-project="handleEditProject"
        @delete-project="handleDeleteProject"
        @sort-project="handleSort"
      />

      <pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :total="total" />

    </el-card>

    <!-- Add/Edit project dialog -->
    <project-form-card v-model:dialogVisible="dialogVisible" 
      v-model:loading="loading"
      :form-type="formType"
      :form="form"
      @close-project="handleClose"
      @submit-project="handleSubmit" />

  </div>
</template>

<script setup lang="ts">
// Import necessary components and services
import Breadcrumb from '@/components/Breadcrumb.vue'
import StatusToggle from '@/components/StatusToggle.vue'
import SearchInput from '@/components/SearchInput.vue'
import AddButton from '@/components/AddButton.vue'
import ProjectFormCard from '@/components/Project/ProjectForm.vue'
import Pagination from '@/components/Pagination.vue'
import ProjectRow from '@/components/Project/ProjectRow.vue'
import { projectService } from '@/services/projects/projectService'
import { FORM_TYPES, PROJECT_STATUS } from '@/utils/constants'
import { ref, computed, onMounted, watch } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import type { Project } from '@/utils/types/project'
import type { Employee } from '@/utils/types/employee'
import { statsService } from '@/services/stats/statsService'
import { PAGE_SIZES } from '@/utils/constants'
import StatisticsCard from '@/components/StatisticsCard.vue';
import { Document, Check } from '@element-plus/icons-vue';
import { handleAxiosError } from '@/utils/errorMsg'
import type { Statistics } from '@/utils/types/statistics'

// State management
const status = ref(PROJECT_STATUS.ALL) // Current project status
const searchQuery = ref('') // Search query for filtering projects
const loading = ref(false)
const dialogVisible = ref(false) // Visibility of the project dialog
const formType = ref(FORM_TYPES.ADD)

const currentPage = ref(1) // Current page for pagination
const pageSize = ref(PAGE_SIZES[0]) // Page size for pagination
const total = ref(0)
const sortParam = ref("name") // Sorting parameter
const sortOrder = ref("ascending") // Sorting order

// Form data for adding/editing projects
const form = ref({} as Project)

const STAT_ITEMS = [
  { key: 'totalProjects', label: 'Total Projects', icon: Document, type: 'primary' },
  { key: 'activeProjects', label: 'Active Projects', icon: Check, type: 'success' },
]

const stats = ref({
  totalProjects: 0,
  activeProjects: 0,
})

const statsData = computed(() => 
  STAT_ITEMS.map(item => ({
    ...item,
    value: stats.value[item.key as keyof typeof stats.value]
  } as Statistics))
)

// Projects to display on the current page
const pageProjects = ref([])

// Handle editing a project
const handleEditProject = (row: Project) => {
    console.log("Handling edit project logic")
    console.log(row);
    form.value = {...row}
    formType.value = FORM_TYPES.EDIT; // Set form type to edit
    dialogVisible.value = true
};

// Handle adding a new project
const handleAddProject = () => {
  console.log("Handling add project logic")
  formType.value = FORM_TYPES.ADD; // Set form type to add
  dialogVisible.value = true
  form.value = {
    leader: {} as Employee,
    members: [] as Employee[]
  } as Project;
}

// Handle closing the project dialog
const handleClose = (project: Project) => {
  console.log("Handling close project logic")
  console.log(project)
  dialogVisible.value = false
  form.value = {
    leader: {} as Employee,
    members: [] as Employee[]
  } as Project;
}

// Handle deleting a project
const handleDeleteProject = async (project: Project) => {
    console.log("Handling delete project logic")
    console.log(project);
    try {
      await ElMessageBox.confirm(`Are you sure you want to delete project ${project.name}?`, 'Warning', { confirmButtonText: 'Yes', cancelButtonText: 'No', type: 'warning' });
      const data = await projectService.deleteProject(project.id!);
      ElMessage.success(data.message);
      refetch()
    } catch (error) {
      handleAxiosError(error);
    }
};


// Handle submitting the project form
const handleSubmit = async (form: Project) => {
  console.log("Handling submit project logic")
  console.log(form);

  try {
    loading.value = true;

    let data;
    if (formType.value === FORM_TYPES.EDIT) {
      data = await projectService.updateProject(form); // Update project
    } else {
      data = await projectService.createProject(form); // Create new project
    }
    ElMessage.success(data.message);
    refetch()
    setTimeout(() => {
      dialogVisible.value = false; // Close dialog after submission
    }, 500);
  } catch (error) {
    handleAxiosError(error);
    console.log(error)
  } finally {
    loading.value = false;
  }
}

// Handle sorting projects
const handleSort = (sort: { prop: keyof Project, order: string }) => {
  const { prop, order } = sort;
  sortParam.value = prop;
  sortOrder.value = order;
}

const refetch = async () => {
  try {
    const data = await projectService.getProjectsByFilter(currentPage.value - 1, pageSize.value, `${sortParam.value},${sortOrder.value}`, searchQuery.value, status.value)
    pageProjects.value = data.projects
    total.value = data.total
    const statsOverview = await statsService.getStatsOverview()
    Object.assign(stats.value, statsOverview)
  } catch (error) {
    handleAxiosError(error);
  }
}

watch([currentPage, pageSize, sortParam, sortOrder, searchQuery, status], () => {
  refetch()
})

// Fetch data on component mount
onMounted(async () => {
  const [statsOverview] = await Promise.all([
    statsService.getStatsOverview(),
  ])
  Object.assign(stats.value, statsOverview)

  refetch()
})
</script>
