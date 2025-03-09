<template>
  <el-table :data="filteredUsers" border>
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
  </el-table>
</template>

<script setup>
const props = defineProps({
  filteredUsers: Array,
  getAvatarColor: Function,
  getAvatarText: Function,
  handleEdit: Function,
  handleDelete: Function,
  Edit: Object,
  Delete: Object
});

import { Edit, Delete } from '@element-plus/icons-vue';

</script>
