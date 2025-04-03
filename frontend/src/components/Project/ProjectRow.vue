<template>
<el-table :data="projects" style="width: 100%" @sort-change="emitEvent('sort-project', $event)">
<!-- Project Name Column -->
<el-table-column prop="name" label="Project Name" min-width="150" sortable>
    <template #default="{ row }">
    <div class="flex items-center">
        {{ row.name }}
    </div>
    </template>
</el-table-column>

<!-- Project Status Column -->
<el-table-column prop="status" label="Project Status" min-width="150" sortable>
    <template #default="{ row }">
    <div class="flex items-center">
        <el-tag :type="row.status === PROJECT_STATUS.ACTIVE ? 'success' : 'info'" size="small" class="mr-2">
        {{ row.status }}
        </el-tag>
    </div>
    </template>
</el-table-column>

<!-- Description Column -->
<el-table-column prop="description" label="Description" min-width="200" show-overflow-tooltip />

<!-- Progress Column -->
<el-table-column prop="progress" label="Progress" width="200" sortable>
    <template #default="{ row }">
    <el-progress :percentage="row.progress" :status="row.progress === 100 ? 'success' : ''" />
    </template>
</el-table-column>

<!-- Time Column -->
<el-table-column label="Time" width="150" sortable>
    <template #default="{ row }">
    <div class="text-sm text-gray-500">
        {{ row.startDate?.split('T')[0] || 'N/A' }}
    </div>
    </template>
</el-table-column>

<!-- Project Leader Column -->
<el-table-column label="Project Leader" width="150">
    <template #default="{ row }">
    <el-tooltip :content="row.leader.name" placement="top">
        <el-avatar :size="30" :src="row.leader.avatar" :style="{ backgroundColor: getAvatarColor(row.leader.name) }">
        {{ getAvatarText(row.leader.name) }}
        </el-avatar>
    </el-tooltip>
    </template>
</el-table-column>

<!-- Project Members Column -->
<el-table-column label="Project Members" width="150">
    <template #default="{ row }">
    <el-tooltip v-for="member in row.members?.slice(0, 2)" :key="member.id" :content="member.name" placement="top">
        <template v-if="member">
        <el-avatar :size="32" :src="member.avatar" :style="{ backgroundColor: getAvatarColor(member.name) }">
            {{ getAvatarText(member.name) }}
        </el-avatar>
        </template>
    </el-tooltip>
    <el-tooltip v-if="row.members?.length > 2" :content="row.members?.slice(2).map(m => m.name).join(', ')" placement="top">
        <el-avatar :size="32">+{{ row.members?.length - 2 }}</el-avatar>
    </el-tooltip>
    </template>
</el-table-column>

<!-- Actions Column -->
<el-table-column label="Actions" width="120" fixed="right">
    <template #default="{ row }">
    <el-button-group>
    <el-tooltip content="View Details" placement="top">
      <el-button type="info" :icon="View" circle @click="emitEvent('view-project', row)" />
    </el-tooltip>
    <el-tooltip content="Edit Project" placement="top">
      <el-button type="primary" :icon="Edit" circle @click="emitEvent('edit-project', row)" :disabled="me.role !== USER_ROLES.ADMIN && row.leader.id !== me.id" />
    </el-tooltip>
    <template v-if="me.role === USER_ROLES.ADMIN">
      <el-tooltip content="Delete Project" placement="top">
        <el-button type="danger" :icon="Delete" circle @click="emitEvent('delete-project', row)" />
      </el-tooltip>
    </template>
    </el-button-group>
    </template>
</el-table-column>
</el-table>
</template>

<script setup>
import { computed } from 'vue';
import { getAvatarColor, getAvatarText } from '@/utils/avatar';
import { View, Edit, Delete } from '@element-plus/icons-vue';
import { PROJECT_STATUS, USER_ROLES } from '@/utils/constants';

defineProps({
  projects: {
    type: Array,
    required: true,
  },
  me: {
    type: Object,
    required: true,
  },
});

const emit = defineEmits(
  ['view-project', 'edit-project', 'delete-project', 'sort-project']
);
const emitEvent = (event, row) => {
  if (row.members) {
    emit(event, {...row, memberIds: row.members.map(m => m.id)});
  } else {
    emit(event, row);
  }
};

</script>
