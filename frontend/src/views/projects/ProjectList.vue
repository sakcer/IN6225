
<template>
  <div>
    <!-- Breadcrumb navigation -->
    <Breadcrumb label="Projects" />

    <h1 class="text-3xl font-bold mb-6">Projects</h1>

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
      <project-row :projects="pageProjects" :users="users" :me="me" 
        @view-project="handleViewProject"
        @edit-project="handleEditProject"
        @delete-project="handleDeleteProject"
        @sort-project="handleSort"
      />

      <pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :total="total" />

    </el-card>

    <!-- Add/Edit project dialog -->
    <project-form-card v-model:dialogVisible="dialogVisible" 
      :form-type="formType"
      :form="form"
      :me="me"
      :users="users"
      :leaders="leaders"
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
import { PROJECT_STATUS } from '@/utils/constants'
import { AxiosError } from 'axios';
import { ref, computed, onMounted } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import type { Project } from '@/utils/types/project'
import type { Employee } from '@/utils/types/employee'
import { useProjectsStore } from '@/store/projectStore'
import { useUsersStore } from '@/store/userStore'
import { useMeStore } from '@/store/meStore'
import { PAGE_SIZES } from '@/utils/constants'
import { useLeadersStore } from '@/store/leaderStore'

// State management
const status = ref(PROJECT_STATUS.ALL) // Current project status
const searchQuery = ref('') // Search query for filtering projects
const dialogVisible = ref(false) // Visibility of the project dialog
const formType = ref(0) // Type of form (add/edit)

// Store references
const projectsStore = useProjectsStore()
const usersStore = useUsersStore()
const leadersStore = useLeadersStore()
const meStore = useMeStore()
const users = computed(() => usersStore.getUsers) // List of users
const leaders = computed(() => leadersStore.getLeaders) // List of leaders
const projects = computed(() => projectsStore.getProjects) // List of projects
const me = computed(() => meStore.getMe) // Current user

const currentPage = ref(1) // Current page for pagination
const pageSize = ref(PAGE_SIZES[0]) // Page size for pagination
const total = computed(() => filteredProjects.value.length) // Total number of filtered projects

// Form data for adding/editing projects
const form = ref({} as Project)

// Filtered project list based on status and search query
const filteredProjects = computed(() => {
  return projects.value.filter(project => {
    const matchesStatus = status.value === PROJECT_STATUS.ALL || project.status === status.value
    const matchesSearch = project.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      project.description.toLowerCase().includes(searchQuery.value.toLowerCase())
    return matchesStatus && matchesSearch
  })
})

// Projects to display on the current page
const pageProjects = computed(() => {
  return filteredProjects.value.slice((currentPage.value - 1) * pageSize.value, currentPage.value * pageSize.value)
})

// Handle viewing a project
const handleViewProject = (row: Project) => {
    console.log("Handling view project logic")
    form.value = row
    formType.value = 0; // Set form type to view
    dialogVisible.value = true
    console.log(row);
};

// Handle editing a project
const handleEditProject = (row: Project) => {
    console.log("Handling edit project logic")
    console.log(row);
    form.value = row
    formType.value = 1; // Set form type to edit
    dialogVisible.value = true
};

// Handle deleting a project
const handleDeleteProject = async (row: Project) => {
    console.log("Handling delete project logic")
    console.log(row);
    try {
      await ElMessageBox.confirm(`Are you sure you want to delete project ${row.name}?`, 'Warning', { confirmButtonText: 'Yes', cancelButtonText: 'No', type: 'warning' });
      const data = await projectService.deleteProject(row.id!);
      ElMessage.success(data.message);
      projectsStore.refetchProjects() // Refresh project list
    } catch (error) {
      ElMessage.error((error as AxiosError).response?.data?.message || (error as AxiosError).message || 'An unexpected error occurred');
    }
};

// Handle adding a new project
const handleAddProject = () => {
  console.log("Handling add project logic")
  formType.value = 3; // Set form type to add
  dialogVisible.value = true
  form.value = {
    leader: {} as Employee,
    members: [] as Employee[]
  } as Project;
}

// Handle closing the project dialog
const handleClose = (row: Project) => {
  console.log("Handling close project logic")
  console.log(row)
  dialogVisible.value = false
  form.value = {
    leader: {} as Employee,
    members: [] as Employee[]
  } as Project;
}

// Handle submitting the project form
const handleSubmit = async (form: Project) => {
  console.log("Handling submit project logic")
  console.log(form);

  try {
    form.members = form.memberIds.map((id: number) => ({ id: id} as Employee)); // Map member IDs to member objects
    console.log(form.members);
    console.log(form);
    console.log(formType.value);

    let data;
    if (formType.value === 1) {
      data = await projectService.updateProject(form); // Update project
    } else {
      data = await projectService.createProject(form); // Create new project
    }
    ElMessage.success(data.message);
    projectsStore.refetchProjects() // Refresh project list
    setTimeout(() => {
      dialogVisible.value = false; // Close dialog after submission
    }, 500);
  } catch (error) {
    ElMessage.error((error as AxiosError).response?.data?.message || (error as AxiosError).message || 'An unexpected error occurred');
    console.log(error);
  } finally {
    setTimeout(() => {
    }, 500);
  }
}

// Handle sorting projects
const handleSort = (sort: { prop: keyof Project, order: string }) => {
  const { prop, order } = sort;
  console.log(prop, order);
  if (order === 'ascending') {
    projects.value.sort((a, b) => a[prop] > b[prop] ? 1 : -1);
  } else if (order === 'descending') {
    projects.value.sort((a, b) => a[prop] < b[prop] ? 1 : -1);
  }
}

// Fetch data on component mount
onMounted(() => {
  usersStore.refetchUsers() // Refresh users
  projectsStore.refetchProjects() // Refresh projects
  leadersStore.refetchLeaders() // Refresh leaders
})
</script>

