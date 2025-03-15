<template>
  <div >
    <!-- 面包屑导航 -->
    <Breadcrumb label="Users" />

    <h1 class="text-3xl font-bold mb-6">Users Panel</h1>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mb-6">
      <el-col :span="6" v-for="(stat, index) in statistics" :key="index">
        <statistics-card :stat="stat" />
      </el-col>
    </el-row>

    <!-- 操作栏 -->
    <el-card shadow="hover" class="mb-6">

      <!-- 工具栏 -->
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

      <!-- 用户列表 -->
      <employee-row :data="pageUsers" @edit-employee="handleEdit" @delete-employee="handleDelete" @sort-employee="handleSort"/>

      <!-- 分页 -->
      <pagination v-model:current-page="currentPage" v-model:page-size="pageSize"
        :total="total" />
    </el-card>

    <!-- 添加/编辑用户对话框 -->
    <employee-form v-model="dialogVisible" v-model:loading="loading" :form="form" :form-type="formType"
      @submit-employee="handleSubmit" @close-employee="handleClose" />
  </div>
</template>

<script setup lang="ts">
import { computed, ref, onMounted } from 'vue';
import { User, UserFilled, Promotion, TrendCharts } from '@element-plus/icons-vue';
import StatisticsCard from '@/components/StatisticsCard.vue';
import AddButton from '@/components/AddButton.vue';
import SearchInput from '@/components/SearchInput.vue';
import StatusToggle from '@/components/StatusToggle.vue';
import EmployeeRow from './widgets/EmployeeRow.vue';
import Pagination from '@/components/Pagination.vue';
import EmployeeForm from './widgets/EmployeeForm.vue';
import Breadcrumb from '@/components/Breadcrumb.vue';
import { PAGE_SIZES, USER_STATUS, USER_ROLES } from '@/utils/constants';
import { useUsersStore } from '@/store/userStore';
import { employeeService } from '@/services/employees/employeeService';
import { ElMessageBox, ElMessage } from 'element-plus';
import { useMeStore } from '@/store/meStore';

const usersStore = useUsersStore();
const users = computed(() => usersStore.getUsers);

const state = ref(false);
const status = ref(USER_STATUS.ALL);
const searchQuery = ref('');
const loading = ref(false);
const dialogVisible = ref(false);
const isEdit = ref(false);
const formType = ref(0);
const form = ref<Employee>({
  id: 0,
  name: '',
  email: '',
  phone: '',
  department: '',
  title: '',
  status: USER_STATUS.ACTIVE,
  role: USER_ROLES.EMPLOYEE,
  joinDate: '',
  avatar: '',
});

const currentPage = ref(1)
const pageSize = ref(PAGE_SIZES[0])
const total = computed(() => filteredUsers.value.length)

const total_users = computed(() => users.value.length);
const total_active_users = computed(() => users.value.filter(user => user.status === USER_STATUS.ACTIVE).length);
const total_new_users = computed(() => {
  const todayInShanghai = new Date().toLocaleString('en-US', { timeZone: 'Asia/Shanghai' }).split(' ')[0];
  return users.value.filter(user => {
    const userJoinDateInShanghai = new Date(user.joinDate).toLocaleString('en-US', { timeZone: 'Asia/Shanghai' }).split(' ')[0];
    return userJoinDateInShanghai === todayInShanghai;
  }).length;
});
const total_active_rate = computed(() => (total_users.value > 0 ? total_active_users.value / total_users.value : 0).toFixed(2));

const statistics = computed(() => [
  { label: 'Total Users', icon: User, value: total_users, type: 'primary' },
  { label: 'Active Users', icon: UserFilled, value: total_active_users, type: 'success' },
  { label: 'New Users', icon: Promotion, value: total_new_users, type: 'warning' },
  { label: 'Active Rate', icon: TrendCharts, value: total_active_rate, type: 'danger' },
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
  console.log("xx");
  return filteredUsers.value.slice((currentPage.value - 1) * pageSize.value, currentPage.value * pageSize.value);
});

const handleAddEmployee = () => {
  console.log("处理添加员工的逻辑")
  formType.value = 0;
  dialogVisible.value = true;
}

const handleDelete = async (user: Employee) => {
  console.log("处理删除员工的逻辑")
  console.log(user);

  try {
    await ElMessageBox.confirm(`确定要删除用户 ${user.name} 吗？`, '警告', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' });
    const data = await employeeService.deleteEmployee(user.id!);
    ElMessage.success('删除员工[' + user.employeeId + ']成功: ' + data.message);
    usersStore.refetchUsers()
    state.value = !state.value;
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败');
      console.error(error);
    }
  }
}

const handleEdit = (user: Employee) => {
  console.log("处理编辑员工的逻辑")
  console.log(user);
  formType.value = 1;
  form.value = { ...user };
  dialogVisible.value = true;
}

const handleSubmit = async (form) => {
  console.log("处理提交员工的逻辑")
  console.log(form)

  try {
    loading.value = true;
    if (formType.value === 1) {
      const data = await employeeService.updateEmployee(form.id, form);
      ElMessage.success('编辑员工[' + data.employeeId + ']成功');
    } else {
      delete form.id;
      const data = await employeeService.createEmployee(form);
      ElMessage.success('添加员工[' + data.employeeId + ']成功');
    }
    usersStore.refetchUsers()
    const meStore = useMeStore();
    meStore.refetchMe();
  } catch {
    ElMessage.error('添加失败');
  } finally {
    setTimeout(() => {
      dialogVisible.value = false;
      loading.value = false;
    }, 500);
  }
}

const handleClose = () => {
  console.log("处理关闭员工的逻辑")
  dialogVisible.value = false;
}

const handleSort = (sort) => {
  const { prop, order } = sort;
  if (order === 'ascending') {
    users.value.sort((a, b) => a[prop] > b[prop] ? 1 : -1);
  } else if (order === 'descending') {
    users.value.sort((a, b) => a[prop] < b[prop] ? 1 : -1);
  }
  console.log(users.value);
};


onMounted(() => {
  usersStore.refetchUsers()
})
</script>