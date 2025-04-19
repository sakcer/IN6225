<template>
  <div>
    <!-- Breadcrumb navigation -->
    <Breadcrumb label="Employees" />

    <h1 class="text-3xl font-bold mb-6">Employees</h1>

    <!-- Statistics cards -->
    <el-row :gutter="20" class="mb-6">
      <el-col :span="6" v-for="(stat, index) in statsData" :key="index">
        <statistics-card v-if="stat" :stat="stat" />
      </el-col>
    </el-row>

    <!-- Action bar -->
    <el-card shadow="hover" class="mb-6">

      <!-- Toolbar -->
      <div class="w-full mb-6">
        <div class="flex items-center mb-4">
          <status-toggle v-model="status" :status="USER_STATUS" />
          <div class="flex-grow ml-4">
            <search-input v-model="searchQuery" placeholder="Search" />
          </div>
        </div>
        <div class="flex justify-end">
          <add-button button-text="Add Employee" @add="handleAddEmployee" />
        </div>
      </div>

      <!-- User list -->
      <employee-row :data="pageEmployees" @edit-employee="handleEdit" @delete-employee="handleDelete" @sort-employee="handleSort"/>

      <!-- Pagination -->
      <pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :total="total" />
    </el-card>

    <!-- Add/Edit user dialog -->
    <employee-form v-model="dialogVisible" v-model:loading="loading" :form="form" :form-type="formType"
      @submit-employee="handleSubmit" @close-employee="handleClose" />
  </div>
</template>

<script setup lang="ts">
// Import necessary libraries and components
import { computed, ref, onMounted, watch } from 'vue';
import { User, UserFilled, Promotion, TrendCharts } from '@element-plus/icons-vue';
import StatisticsCard from '@/components/StatisticsCard.vue';
import AddButton from '@/components/AddButton.vue';
import SearchInput from '@/components/SearchInput.vue';
import StatusToggle from '@/components/StatusToggle.vue';
import EmployeeRow from '@/components/Employee/EmployeeRow.vue';
import Pagination from '@/components/Pagination.vue';
import EmployeeForm from '@/components/Employee/EmployeeForm.vue';
import Breadcrumb from '@/components/Breadcrumb.vue';
import { FORM_TYPES, PAGE_SIZES, USER_STATUS } from '@/utils/constants';
import { employeeService } from '@/services/employees/employeeService';
import { statsService } from '@/services/stats/statsService'
import { ElMessageBox, ElMessage } from 'element-plus';
import { useUserStore } from '@/store/userStore';
import type { Employee } from '@/utils/types/employee';
import type { Statistics } from '@/utils/types/statistics';
import { handleAxiosError } from '@/utils/errorMsg';

// Store reference for users
const status = ref(USER_STATUS.ALL);
const searchQuery = ref('');
const loading = ref(false);
const dialogVisible = ref(false);
const formType = ref(FORM_TYPES.ADD);
const form = ref({} as Employee)

const currentPage = ref(1)
const pageSize = ref(PAGE_SIZES[0])
const total = ref(0)
const sortParam = ref("name")
const sortOrder = ref("ascending")

const STAT_ITEMS = [
  { key: 'totalEmployees', label: 'Total Employees', icon: User, type: 'primary' },
  { key: 'newEmployees', label: 'New Employees', icon: Promotion, type: 'success'},
  { key: 'activeEmployees', label: 'Active Employees', icon: UserFilled, type: 'warning'},
  { key: 'activeRate', label: 'Active Rate', icon: TrendCharts, type: 'danger' }
]

const stats = ref({
  totalEmployees: 0,
  activeEmployees: 0,
  newEmployees: 0,
  activeRate: 0
})

const statsData = computed(() => 
  STAT_ITEMS.map(item => ({
    ...item,
    value: stats.value[item.key as keyof typeof stats.value]
  } as Statistics))
)

const pageEmployees = ref([])

// Handle closing the dialog
const handleClose = () => {
  console.log("Handling close employee logic")
  dialogVisible.value = false; // Hide dialog
  form.value = {} as Employee
}

// Handle sorting employees
const handleSort = async (sort: { prop: keyof Employee, order: string }) => {
  const { prop, order } = sort;
  sortParam.value = prop;
  sortOrder.value = order;
};

// Handle adding a new employee
const handleAddEmployee = () => {
  console.log("Handling add employee logic")
  formType.value = FORM_TYPES.ADD; // Set form type to add
  form.value = {} as Employee
  dialogVisible.value = true; // Show dialog
}

// Handle editing an employee
const handleEdit = (employee: Employee) => {
  console.log("Handling edit employee logic")
  formType.value = FORM_TYPES.EDIT; // Set form type to edit
  form.value = { ...employee }; // Populate form with user data
  dialogVisible.value = true; // Show dialog
}

// Handle deleting an employee
const handleDelete = async (user: Employee) => {
  console.log("Handling delete employee logic")
  try {
    await ElMessageBox.confirm(`Are you sure you want to delete user ${user.name}?`, 'Warning', { confirmButtonText: 'Yes', cancelButtonText: 'No', type: 'warning' });
    const data = await employeeService.deleteEmployee(user.id!);
    ElMessage.success(data.message);
    refetch()
  } catch (error) {
    handleAxiosError(error);
  }
}

// Handle form submission
const handleSubmit = async (form: Employee) => {
  console.log("Handling submit employee logic")
  console.log(form)
  try {
    loading.value = true; // Show loading state
    let data;
    if (formType.value === FORM_TYPES.EDIT) {
      data = await employeeService.updateEmployee(form); // Update employee
    } else {
      data = await employeeService.createEmployee(form); // Create new employee
    }
    ElMessage.success(data.message);
    refetch()

    setTimeout(() => {
      dialogVisible.value = false; // Hide dialog
    }, 500);
  } catch (error) {
    handleAxiosError(error);
  } finally {
    loading.value = false; // Hide loading state
  }
}


const refetch = async () => {
  try {
    const data = await employeeService.getEmployeesByFilter(currentPage.value - 1, pageSize.value, `${sortParam.value},${sortOrder.value}`, searchQuery.value, status.value)
    pageEmployees.value = data.employees
    total.value = data.total

    const statsOverview = await statsService.getStatsOverview()
    Object.assign(stats.value, statsOverview)
  } catch (error) {
    handleAxiosError(error);
  }

  const meStore = useUserStore();
  meStore.fetchUserInfo(); // Refresh current user data
}

watch([currentPage, pageSize, sortParam, sortOrder, searchQuery, status], () => {
  refetch()
});

// Fetch users on component mount
onMounted(async () => {
  const [statsOverview] = await Promise.all([
    statsService.getStatsOverview(),
  ])
  Object.assign(stats.value, statsOverview)
  stats.value.activeRate = parseFloat((statsOverview.activeEmployees / statsOverview.totalEmployees).toFixed(2));

  refetch()

})
</script>