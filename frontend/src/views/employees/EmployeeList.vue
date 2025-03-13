<template>
  <div class="employee-list">
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
          <add-button button-text="Add User" v-model:dialogVisible="dialogVisible" v-model:isEdit="isEdit" v-model:form="form"/>
        </div>
      </div>

      <!-- 用户列表 -->
      <el-table :data="pageUsers">
        <employee-row v-model:isEdit="isEdit" v-model:dialogVisible="dialogVisible" v-model:form="form" v-model:state="state" />
      </el-table>

      <!-- 分页 -->
      <pagination v-model:current-page="pagination.currentPage" v-model:page-size="pagination.pageSize"
        :total="pagination.total" />
    </el-card>

    <!-- 添加/编辑用户对话框 -->
    <employee-form v-model="dialogVisible" v-model:loading="loading" :is-edit="isEdit" :form="form" v-model:state="state"/>
  </div>
</template>

<style scoped>
.employee-list {
  padding: 24px;
}
</style>

<script setup lang="ts">
import { computed, ref } from 'vue';
import { User, UserFilled, Promotion, TrendCharts } from '@element-plus/icons-vue';
import StatisticsCard from '@/components/StatisticsCard.vue';
import AddButton from '@/components/AddButton.vue';
import SearchInput from '@/components/SearchInput.vue';
import StatusToggle from '@/components/StatusToggle.vue';
import EmployeeRow from './widgets/EmployeeRow.vue';
import Pagination from '@/components/Pagination.vue';
import EmployeeForm from './widgets/EmployeeForm.vue';
import Breadcrumb from '@/components/Breadcrumb.vue';
import { PAGE_SIZES, USER_STATUS } from '@/utils/constants';
import { useUsersStore } from '@/store/userStore';

const usersStore = useUsersStore();
const users = computed(() => usersStore.getUsers);

const state = ref(false);
const status = ref(USER_STATUS.ALL);
const searchQuery = ref('');
const loading = ref(false);
const dialogVisible = ref(false);
const isEdit = ref(false);
const form = ref<Employee>({
  id: '',
  name: '',
  email: '',
  phone: '',
  department: '',
  title: '',
  status: '',
});

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

const pagination = ref<PaginationConfig>({
  currentPage: 1,
  pageSize: PAGE_SIZES[0],
  total: computed(() => filteredUsers.value.length),
});

const pageUsers = computed(() => {
  const { currentPage, pageSize } = pagination.value;
  return filteredUsers.value.slice((currentPage - 1) * pageSize, currentPage * pageSize);
});
</script>