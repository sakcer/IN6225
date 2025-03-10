<template>
  <el-card 
    shadow="hover" 
    :body-style="{ padding: '20px' }"
    :class="{'border-primary': project.status === 'active'}"
  >
    <!-- 项目状态标签 -->
    <div class="absolute top-2 right-2">
      <el-tag 
        :type="project.status === 'active' ? 'success' : 'info'"
        size="small"
      >
        {{ project.status }}
      </el-tag>
    </div>

    <!-- 项目基本信息 -->
    <h3 class="text-lg font-bold mb-2">{{ project.name }}</h3>
    <p class="text-gray-600 text-sm mb-4">{{ project.description }}</p>

    <!-- 进度条 -->
    <el-progress 
      :percentage="project.progress" 
      :status="project.progress === 100 ? 'success' : ''"
      class="mb-4"
    />

    <!-- 项目时间信息 -->
    <div class="flex items-center text-sm text-gray-500 mb-4">
      <el-icon class="mr-1"><Calendar /></el-icon>
      <span>{{ project.startDate }} - {{ project.endDate }}</span>
    </div>

    <!-- 项目负责人 -->
    <div class="flex items-center mb-4">
      <span class="text-sm text-gray-600 mr-2">Leader:</span>
      <el-tooltip 
        :content="project.leader.name"
        placement="top"
      >
        <el-avatar 
          :size="32"
          :src="project.leader.avatar"
          :style="{ backgroundColor: getAvatarColor(project.leader.name) }"
        >
          {{ getAvatarText(project.leader.name) }}
        </el-avatar>
      </el-tooltip>
    </div>

    <!-- 项目成员 -->
    <div class="flex items-center mb-4">
      <span class="text-sm text-gray-600 mr-2">Team:</span>
      <div class="flex -space-x-2">
        <el-tooltip
          v-for="member in project.members.slice(0, 3)"
          :key="member.id"
          :content="member.name"
          placement="top"
        >
          <el-avatar 
            :size="32"
            :src="member.avatar"
            :style="{ backgroundColor: getAvatarColor(member.name) }"
          >
            {{ getAvatarText(member.name) }}
          </el-avatar>
        </el-tooltip>
        <el-tooltip
          v-if="project.members.length > 3"
          :content="project.members.slice(3).map(m => m.name).join(', ')"
          placement="top"
        >
          <el-avatar :size="32">
            +{{ project.members.length - 3 }}
          </el-avatar>
        </el-tooltip>
      </div>
    </div>

    <!-- 操作按钮 -->
    <div class="flex justify-end gap-2 mt-4">
      <el-button-group>
        <el-tooltip content="Edit Project" placement="top">
          <el-button 
            type="primary" 
            :icon="Edit"
            circle
            @click="handleEditProject(project)"
          />
        </el-tooltip>
        <el-tooltip content="Delete Project" placement="top">
          <el-button 
            type="danger" 
            :icon="Delete"
            circle
            @click="handleDeleteProject(project)"
          />
        </el-tooltip>
      </el-button-group>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import type { Project } from '@/utils/types';
import { ElCard, ElTag, ElProgress, ElButtonGroup, ElButton, ElTooltip, ElAvatar, ElIcon } from 'element-plus';
import { Edit, Delete, Calendar } from '@element-plus/icons-vue';
import { getAvatarColor, getAvatarText } from '@/utils/avatar';

// 接收父组件传递的 props
defineProps<{
  project: Project;
  handleEditProject: (project: Project) => void;
  handleDeleteProject: (project: Project) => void;
}>();

</script>
