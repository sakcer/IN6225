<script setup lang="ts">
import { onMounted } from 'vue';
import { Edit, Delete } from '@element-plus/icons-vue';
import StatisticsCards from '@/components/StatisticsCards.vue';
import ToolBar from './widgets/ToolBar.vue';
import EmployeeCards from './widgets/EmployeeCards.vue';
import Pagination from './widgets/Pagination.vue';
import EmployeeForm from './widgets/EmployeeForm.vue';
import Breadcrumb from '@/components/Breadcrumb.vue';
import AddButton from '@/components/AddButton.vue';
import SearchInput from '@/components/SearchInput.vue';
import StatusToggle from '@/components/StatusToggle.vue';

import { USER_STATUS } from '@/utils/constants';

import { useEmployees } from './composables/useEmployees';
import { getAvatarColor, getAvatarText } from '@/utils/avatar';

const {
  // 状态
  form,
  isEdit,
  status,
  loading,
  statistics,
  searchQuery,
  filteredUsers,
  dialogVisible,
  pagination,

  // 方法
  updateUsers,
  handleAddUser,
  handleEdit,
  handleDelete,
  handleSubmit,
  handleSearch,
  handleCurrentChange,
  handleSizeChange,
} = useEmployees()

onMounted(() => {
  updateUsers();
});
</script>

<template>
  <div class="employee-list">
    <!-- 面包屑导航 -->
    <Breadcrumb label="Users" />

    <h1 class="text-3xl font-bold mb-6">Users Panel</h1>

    <!-- 统计卡片 -->
    <statistics-cards :data="statistics" />

    <!-- 操作栏 -->
    <el-card shadow="hover" class="mb-6">

      <!-- 工具栏 -->
      <div class="w-full mb-6">
        <div class="flex items-center mb-4">
          <status-toggle v-model="status" :status="USER_STATUS" @search="handleSearch" />
          <div class="flex-grow ml-4">
            <search-input v-model="searchQuery" placeholder="Search" @search="handleSearch" />
          </div>
        </div>
        <div class="flex justify-end">
          <add-button button-text="Add User" @add="handleAddUser" />
        </div>
      </div>

      <!-- 用户列表 -->
      <employee-cards :filtered-users="filteredUsers" :loading="loading" :get-avatar-color="getAvatarColor"
        :get-avatar-text="getAvatarText" :handle-edit="handleEdit" :handle-delete="handleDelete" />

      <!-- 分页 -->
      <pagination v-model:current-page="pagination.currentPage" v-model:page-size="pagination.pageSize"
        :total="pagination.total" @update:current-page="handleCurrentChange" @update:page-size="handleSizeChange" />
    </el-card>

    <!-- 添加/编辑用户对话框 -->
    <employee-form v-model="dialogVisible" :is-edit="isEdit" :form="form" @submit="handleSubmit" />
  </div>
</template>

<style scoped>
.employee-list {
  padding: 24px;
}
</style>