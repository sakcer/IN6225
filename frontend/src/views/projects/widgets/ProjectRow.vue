<template>
<el-table :data="projects" style="width: 100%" @sort-change="emitEvent('sort-project', $event)">
<!-- 项目名称列 -->
<el-table-column prop="name" label="项目名称" min-width="150" sortable>
    <template #default="{ row }">
    <div class="flex items-center">
        {{ row.name }}
    </div>
    </template>
</el-table-column>

<!-- 项目状态列 -->
<el-table-column prop="status" label="项目状态" min-width="150" sortable>
    <template #default="{ row }">
    <div class="flex items-center">
        <el-tag :type="row.status === PROJECT_STATUS.ACTIVE ? 'success' : 'info'" size="small" class="mr-2">
        {{ row.status }}
        </el-tag>
    </div>
    </template>
</el-table-column>

<!-- 描述列 -->
<el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />

<!-- 进度列 -->
<el-table-column prop="progress" label="进度" width="200" sortable>
    <template #default="{ row }">
    <el-progress :percentage="row.progress" :status="row.progress === 100 ? 'success' : ''" />
    </template>
</el-table-column>

<!-- 时间列 -->
<el-table-column label="时间" width="200" sortable>
    <template #default="{ row }">
    <div class="text-sm text-gray-500">
        {{ row.startDate }} - {{ row.endDate }}
    </div>
    </template>
</el-table-column>

<!-- 项目负责人列 -->
<el-table-column label="项目负责人" width="100">
    <template #default="{ row }">
    <el-tooltip :content="row.leader.name" placement="top">
        <el-avatar :size="30" :src="row.leader.avatar" :style="{ backgroundColor: getAvatarColor(row.leader.name) }">
        {{ getAvatarText(row.leader.name) }}
        </el-avatar>
    </el-tooltip>
    </template>
</el-table-column>

<!-- 项目成员列 -->
<el-table-column label="项目成员" width="150">
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

<!-- 操作列 -->
<el-table-column label="操作" width="120" fixed="right">
    <template #default="{ row }">
    <el-button-group>
    <el-tooltip content="查看详情" placement="top">
      <el-button type="info" :icon="View" circle @click="emitEvent('view-project', row)" />
    </el-tooltip>
    <el-tooltip content="编辑项目" placement="top">
      <el-button type="primary" :icon="Edit" circle @click="emitEvent('edit-project', row)" :disabled="me.role !== USER_ROLES.ADMIN && row.leader.id !== me.id" />
    </el-tooltip>
    <template v-if="me.role === USER_ROLES.ADMIN">
      <el-tooltip content="删除项目" placement="top">
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
  users: {
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
