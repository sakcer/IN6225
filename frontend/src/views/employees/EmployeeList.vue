<template>
  <div>
    <!-- Breadcrumb navigation -->
    <Breadcrumb label="Users" />

    <h1 class="text-3xl font-bold mb-6">Users Panel</h1>

    <!-- Statistics cards -->
    <el-row :gutter="20" class="mb-6">
      <el-col :span="6" v-for="(stat, index) in statistics" :key="index">
        <statistics-card v-if="stat" :stat="stat" as Statistics />
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
          <add-button button-text="Add User" @add="handleAddEmployee" />
        </div>
      </div>

      <!-- User list -->
      <employee-row :data="pageUsers" @edit-employee="handleEdit" @delete-employee="handleDelete" @sort-employee="handleSort"/>

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
import { computed, ref, onMounted } from 'vue';
import { User, UserFilled, Promotion, TrendCharts } from '@element-plus/icons-vue';
import StatisticsCard from '@/components/StatisticsCard.vue';
import AddButton from '@/components/AddButton.vue';
import SearchInput from '@/components/SearchInput.vue';
import StatusToggle from '@/components/StatusToggle.vue';
import EmployeeRow from '@/components/Employee/EmployeeRow.vue';
import Pagination from '@/components/Pagination.vue';
import EmployeeForm from '@/components/Employee/EmployeeForm.vue';
import Breadcrumb from '@/components/Breadcrumb.vue';
import { PAGE_SIZES, USER_STATUS } from '@/utils/constants';
import { useUsersStore } from '@/store/userStore';
import { employeeService } from '@/services/employees/employeeService';
import { ElMessageBox, ElMessage } from 'element-plus';
import { useMeStore } from '@/store/meStore';
import type { Employee } from '@/utils/types/employee';
import type { Statistics } from '@/utils/types/statistics';

// Store reference for users
const usersStore = useUsersStore();
const users = computed(() => usersStore.getUsers as Employee[]);

const state = ref(false);
const status = ref(USER_STATUS.ALL);
const searchQuery = ref('');
const loading = ref(false);
const dialogVisible = ref(false);
const formType = ref(0);
const form = ref({} as Employee)
// const form = ref<EmployeeForm>({
//   id: null,
//   name: '',
//   email: '',
//   phone: '',
//   department: '',
//   title: '',
//   status: USER_STATUS.ACTIVE,
//   role: USER_ROLES.USER,
//   joinDate: '',
//   avatar: '',
// });

const currentPage = ref(1)
const pageSize = ref(PAGE_SIZES[0])
const total = computed(() => filteredUsers.value.length)

const total_users = computed(() => users.value.length);
const total_active_users = computed(() => users.value.filter(user => user.status === USER_STATUS.ACTIVE).length);
const total_new_users = computed(() => {
  const todayInShanghai = new Date().toLocaleString('en-US', { timeZone: 'Asia/Shanghai' }).split(' ')[0];
  return users.value.filter(user => {
    const userJoinDateInShanghai = new Date(user.joinDate || '').toLocaleString('en-US', { timeZone: 'Asia/Shanghai' }).split(' ')[0];
    return userJoinDateInShanghai === todayInShanghai;
  }).length;
});
const total_active_rate = computed(() => (total_users.value > 0 ? total_active_users.value / total_users.value : 0).toFixed(2));

const statistics = computed(() => [
  { label: 'Total Users', icon: User, value: total_users.value, type: 'primary' } as Statistics,
  { label: 'Active Users', icon: UserFilled, value: total_active_users.value, type: 'success' } as Statistics,
  { label: 'New Users', icon: Promotion, value: total_new_users.value, type: 'warning' } as Statistics,
  { label: 'Active Rate', icon: TrendCharts, value: parseFloat(total_active_rate.value), type: 'danger' } as Statistics,
]);

const filteredUsers = computed(() => {
  const query = searchQuery.value.toLowerCase();
  return users.value.filter(user => {
    const matchesStatus = status.value === USER_STATUS.ALL || user.status === status.value;
    const matchesSearch = query
      ? [user.name, user.email, user.employeeId].some(field => field?.toLowerCase().includes(query))
      : true;
    return matchesStatus && matchesSearch;
  });
});

const pageUsers = computed(() => {
  return filteredUsers.value.slice((currentPage.value - 1) * pageSize.value, currentPage.value * pageSize.value);
});

// Handle adding a new employee
const handleAddEmployee = () => {
  console.log("Handling add employee logic")
  formType.value = 0; // Set form type to add
  form.value = {} as Employee
  // form.value = {
  //   id: null,
  //   name: '',
  //   email: '',
  //   phone: '',
  //   department: '',
  //   title: '',
  //   status: USER_STATUS.ACTIVE,
  //   role: USER_ROLES.USER,
  //   joinDate: '',
  //   avatar: '',
  // }
  dialogVisible.value = true; // Show dialog
}

// Handle deleting an employee
const handleDelete = async (user: Employee) => {
  console.log("Handling delete employee logic")
  console.log(user);

  try {
    await ElMessageBox.confirm(`Are you sure you want to delete user ${user.name}?`, 'Warning', { confirmButtonText: 'Yes', cancelButtonText: 'No', type: 'warning' });
    await employeeService.deleteEmployee(user.id!);
    ElMessage.success('Successfully deleted employee [' + user.employeeId + ']');
    usersStore.refetchUsers() // Refresh user list
    state.value = !state.value; // Trigger reactivity
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('Deletion failed');
      console.error(error);
    }
  }
}

// Handle editing an employee
const handleEdit = (user: Employee) => {
  console.log("Handling edit employee logic")
  console.log(user);
  formType.value = 1; // Set form type to edit
  form.value = { ...user }; // Populate form with user data
  dialogVisible.value = true; // Show dialog
}

// Handle form submission
const handleSubmit = async (form: Employee) => {
  console.log("Handling submit employee logic")
  console.log(form)

  try {
    loading.value = true; // Show loading state
    if (formType.value === 1) {
      const data = await employeeService.updateEmployee(form); // Update employee
      ElMessage.success('Successfully edited employee [' + data.employeeId + ']');
    } else {
      const data = await employeeService.createEmployee(form); // Create new employee
      ElMessage.success('Successfully added employee [' + data.employeeId + ']');
    }
    usersStore.refetchUsers() // Refresh user list
    const meStore = useMeStore();
    meStore.refetchMe(); // Refresh current user data
  } catch {
    ElMessage.error('Addition failed');
  } finally {
    setTimeout(() => {
      dialogVisible.value = false; // Hide dialog
      loading.value = false; // Hide loading state
    }, 500);
  }
}

// Handle closing the dialog
const handleClose = () => {
  console.log("Handling close employee logic")
  dialogVisible.value = false; // Hide dialog
}

// Handle sorting employees
const handleSort = (sort: { prop: keyof Employee, order: string }) => {
  const { prop, order } = sort;
  if (order === 'ascending') {
    users.value.sort((a, b) => a[prop] > b[prop] ? 1 : -1);
  } else if (order === 'descending') {
    users.value.sort((a, b) => a[prop] < b[prop] ? 1 : -1);
  }
};

// Fetch users on component mount
onMounted(() => {
  usersStore.refetchUsers() // Refresh user list
})
</script>