<template>
<el-table :data="data" @sort-change="emitEvent('sort-employee', $event)">
  <el-table-column label="FULL NAME" prop="name" min-width="200" sortable>
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
    <el-table-column prop="employeeId" label="EID" min-width="100" sortable/>
    <el-table-column prop="email" label="EMAIL" min-width="200" sortable/>
    <el-table-column prop="department" label="DEPARTMENT" min-width="150" sortable>
      <template #default="{ row }">
        <span v-if="row.department">{{ row.department }}</span>
        <span v-else class="text-gray-500">No department</span>
      </template>
    </el-table-column>
    <el-table-column label="ROLE" prop="role" min-width="120" sortable>
      <template #default="{ row }">
        <el-tag :type="row.role === USER_ROLES.ADMIN ? 'danger' : row.role === USER_ROLES.LEADER ? 'primary' : 'info'" effect="plain">
          {{ row.role }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column label="STATUS" prop="status" width="100" sortable>
      <template #default="{ row }">
        <el-tag :type="row.status === USER_STATUS.ACTIVE ? 'success' : 'info'" effect="light" size="small">
          {{ row.status || USER_STATUS.INACTIVE }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column label="ACTIONS" width="100" fixed="right">
      <template #default="{ row }">
        <el-button-group>
          <el-tooltip content="Edit User" placement="top">
            <el-button type="primary" :icon="Edit" circle @click="emitEvent('edit-employee', row)" />
          </el-tooltip>
          <el-tooltip content="Delete User" placement="top">
            <el-button type="danger" :icon="Delete" circle @click="emitEvent('delete-employee', row)" />
          </el-tooltip>
        </el-button-group>
      </template>
    </el-table-column>
</el-table>
</template>

<script setup lang="ts">
import { getAvatarColor, getAvatarText } from '@/utils/avatar';
import { Edit, Delete } from '@element-plus/icons-vue';
import { USER_STATUS, USER_ROLES } from '@/utils/constants';
import type { Employee } from '@/utils/types/employee';

defineProps<{
  data: Employee[];
}>();

const emit = defineEmits(['edit-employee', 'delete-employee', 'sort-employee']);
const emitEvent = (event: 'edit-employee' | 'delete-employee' | 'sort-employee', row: Employee | Object) => {
  emit(event, row);
};

</script>
