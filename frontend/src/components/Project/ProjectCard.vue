<template>
  <el-card shadow="hover" :body-style="{ padding: '20px' }"
    :class="{ 'border-primary': project.status === PROJECT_STATUS.ACTIVE }" style="position: relative;">
    <!-- Project status tag -->
    <div class="absolute top-2 right-2">
      <el-tag :type="project.status === PROJECT_STATUS.ACTIVE ? 'success' : 'info'" size="small">
        {{ project.status }}
      </el-tag>
    </div>

    <!-- Basic project information -->
    <h3 class="text-lg font-bold mb-2">{{ project.name }}</h3>
    <p class="text-gray-600 text-sm mb-4">{{ project.description || 'No description' }}</p>

    <!-- Progress bar -->
    <el-progress :percentage="project.progress" :status="project.progress === 100 ? 'success' : ''" class="mb-4" />

    <!-- Project time information -->
    <div class="flex items-center text-sm text-gray-500 mb-4">
      <el-icon class="mr-1">
        <Calendar />
      </el-icon>
      <span>{{ project.startDate?.split('T')[0] || 'N/A' }}</span>
    </div>

    <!-- Project leader -->
    <div class="flex items-center mb-4">
      <span class="text-sm text-gray-600 mr-2">Leader:</span>
      <el-tooltip :content="project.leader.name" placement="top">
        <el-avatar :size="32" :src="project.leader.avatar"
          :style="{ backgroundColor: getAvatarColor(project.leader.name) }">
          {{ getAvatarText(project.leader.name) }}
        </el-avatar>
      </el-tooltip>
    </div>

    <!-- Project members -->
    <div class="flex items-center mb-4">
      <span class="text-sm text-gray-600 mr-2">Team:</span>
      <div class="flex -space-x-2">
        <el-tooltip v-for="member in project.members.slice(0, 3)" :key="member.id" :content="member.name" placement="top">
          <template v-if="member">
            <el-avatar :size="32" :src="member.avatar"
              :style="{ backgroundColor: getAvatarColor(member.name) }">
              {{ getAvatarText(member.name) }}
            </el-avatar>
          </template>
        </el-tooltip>
        <el-tooltip v-if="project.members.length > 3" :content="project.members.slice(3).map(m => m.name).join(', ')" placement="top">
          <el-avatar :size="32">
            +{{ project.members.length - 3 }}
          </el-avatar>
        </el-tooltip>
      </div>
    </div>

    <!-- Action buttons -->
    <div class="flex justify-end gap-2 mt-4">
      <el-button-group>
        <!-- <el-tooltip content="View Project" placement="top">
          <el-button type="info" :icon="View" circle @click="emitEvent('view-project', project)" />
        </el-tooltip> -->
        <el-tooltip content="Edit Project" placement="top">
          <el-button type="primary" :icon="Edit" circle @click="emitEvent('edit-project', project)" :disabled="userStore.userInfo.id !== project.leader.id"/>
        </el-tooltip>
      </el-button-group>
    </div>
  </el-card>
</template>

<script setup lang="ts">
// Import necessary libraries and types
import type { Project } from '@/utils/types/project';
import { ElCard, ElTag, ElProgress, ElButtonGroup, ElButton, ElTooltip, ElAvatar, ElIcon } from 'element-plus';
import { Edit, Calendar } from '@element-plus/icons-vue';
import { getAvatarColor, getAvatarText } from '@/utils/avatar';
import { PROJECT_STATUS } from '@/utils/constants';
import { useUserStore } from '@/store/userStore';

// Receive props passed from the parent component
defineProps<{
  project: Project; // Project data
}>();

const userStore = useUserStore();

const emit = defineEmits(['edit-project', 'view-project']); // Define emitted events
const emitEvent = (event: 'edit-project' | 'view-project', row: Project) => {
  emit(event, {...row, memberIds: row.members.map(m => m.id)}); // Emit event with project data
};

</script>
