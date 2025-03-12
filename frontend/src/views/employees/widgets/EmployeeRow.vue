<template>
  <el-table-column label="FULL NAME" min-width="200">
      <template #default="{ row }">
        <div class="flex items-center gap-2">
          <el-avatar :size="32" :style="{ backgroundColor: getAvatarColor(row.name) }">
            {{ getAvatarText(row.name) }}
          </el-avatar>
          <div>
            <div>{{ row.name }}</div>
            <div class="text-xs text-gray-500">{{ row.title || 'No title' }}</div>
          </div>
        </div>
      </template>
    </el-table-column>
    <el-table-column prop="employeeId" label="EID" min-width="100" />
    <el-table-column prop="email" label="EMAIL" min-width="200" />
    <el-table-column prop="department" label="DEPARTMENT" min-width="150">
      <template #default="{ row }">
        <span v-if="row.department">{{ row.department }}</span>
        <span v-else class="text-gray-500">No department</span>
      </template>
    </el-table-column>
    <el-table-column label="ROLE" min-width="120">
      <template #default="{ row }">
        <el-tag :type="row.role === 'OWNER' ? 'warning' : 'info'" effect="plain">
          {{ row.role }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column label="STATUS" width="100">
      <template #default="{ row }">
        <el-tag :type="row.status === 'active' ? 'success' : 'info'" effect="light" size="small">
          {{ row.status || 'active' }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column label="ACTIONS" width="120" fixed="right">
      <template #default="{ row }">
        <div class="flex gap-2">
          <el-tooltip content="Edit User" placement="top">
            <el-button type="primary" :icon="Edit" circle @click="handleEdit(row)" />
          </el-tooltip>
          <el-tooltip content="Delete User" placement="top">
            <el-button type="danger" :icon="Delete" circle @click="handleDelete(row)" />
          </el-tooltip>
        </div>
      </template>
    </el-table-column>
</template>

<script setup lang="ts">
import { getAvatarColor, getAvatarText } from '@/utils/avatar';
import { Edit, Delete } from '@element-plus/icons-vue';
import type { Employee } from '@/utils/types';
import { ElMessage, ElMessageBox } from 'element-plus';
import { employeeService } from '@/services/employees/employeeService';

const isEdit = defineModel<boolean>('isEdit', { required: true });
const dialogVisible = defineModel<boolean>('dialogVisible', { required: true });
const form = defineModel<Employee>('form', { required: true });
const state = defineModel<boolean>('state', { required: true });

const handleDelete = async (user: Employee) => {
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
  isEdit.value = true;
  dialogVisible.value = true;
  form.value = { ...user };
} 
</script>
