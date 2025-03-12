<template>
  <el-card shadow="hover" :body-style="{ padding: '20px' }"
    :class="{ 'border-primary': project.status === PROJECT_STATUS.ACTIVE }">
    <!-- 项目状态标签 -->
    <div class="absolute top-2 right-2">
      <el-tag :type="project.status === PROJECT_STATUS.ACTIVE ? 'success' : 'info'" size="small">
        {{ project.status }}
      </el-tag>
    </div>

    <!-- 项目基本信息 -->
    <h3 class="text-lg font-bold mb-2">{{ project.name }}</h3>
    <p class="text-gray-600 text-sm mb-4">{{ project.description }}</p>

    <!-- 进度条 -->
    <el-progress :percentage="project.progress" :status="project.progress === 100 ? 'success' : ''" class="mb-4" />

    <!-- 项目时间信息 -->
    <div class="flex items-center text-sm text-gray-500 mb-4">
      <el-icon class="mr-1">
        <Calendar />
      </el-icon>
      <span>{{ project.startDate }} - {{ project.endDate }}</span>
    </div>

    <!-- 项目负责人 -->
    <div class="flex items-center mb-4">
      <span class="text-sm text-gray-600 mr-2">Leader:</span>
      <el-tooltip :content="users.find(user => user.id === project.leaderId)?.name" placement="top">
        <!-- <el-avatar :size="32" :src="project.leader.avatar" -->
        <el-avatar :size="32" :src="users.find(user => user.id === project.leaderId)?.avatar"
          :style="{ backgroundColor: getAvatarColor(users.find(user => user.id === project.leaderId)?.name || '') }">
          {{ getAvatarText(users.find(user => user.id === project.leaderId)?.name) }}
        </el-avatar>
      </el-tooltip>
    </div>

    <!-- 项目成员 -->
    <div class="flex items-center mb-4">
      <span class="text-sm text-gray-600 mr-2">Team:</span>
      <div class="flex -space-x-2">
        <el-tooltip v-for="member in project.memberIds?.slice(0, 3)" :key="member.id" :content="member.name" placement="top">
          <template v-if="member">
            <!-- Get matching user -->
            <template v-if="users.some(user => user.id === member)">
              <el-avatar :size="32" :src="users.find(user => user.id === member)?.avatar"
                :style="{ backgroundColor: getAvatarColor(users.find(user => user.id === member)?.name) }">
                {{ getAvatarText(users.find(user => user.id === member)?.name) }}
              </el-avatar>
            </template>
          </template>
        </el-tooltip>
        <el-tooltip v-if="project.memberIds?.length > 3" :content="project.memberIds?.slice(3).map(m => users.find(user => user.id === m)?.name).join(', ')"
          placement="top">
          <el-avatar :size="32">
            +{{ project.memberIds?.length - 3 }}
          </el-avatar>
        </el-tooltip>
      </div>
    </div>

    <!-- 操作按钮 -->
    <div class="flex justify-end gap-2 mt-4">
      <el-button-group>
        <el-tooltip content="Edit Project" placement="top">
          <el-button type="primary" :icon="Edit" circle @click="handleEditProject(project)" />
        </el-tooltip>
        <el-tooltip content="Delete Project" placement="top">
          <el-button type="danger" :icon="Delete" circle @click="handleDeleteProject(project)" />
        </el-tooltip>
      </el-button-group>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import type { Project } from '@/utils/types';
import { ElCard, ElTag, ElProgress, ElButtonGroup, ElButton, ElTooltip, ElAvatar, ElIcon, ElMessageBox, ElMessage } from 'element-plus';
import { Edit, Delete, Calendar } from '@element-plus/icons-vue';
import { getAvatarColor, getAvatarText } from '@/utils/avatar';
import { projectService } from '@/services/projects/projectService';
import { PROJECT_STATUS } from '@/utils/constants';
import { useProjectsStore } from '@/store/projectStore';
import { useUsersStore } from '@/store/userStore';

// 接收父组件传递的 props
defineProps<{
  project: Project;
  // users: Employee[];
}>();

const dialogVisible = defineModel<boolean>('dialogVisible', { required: true });
const isEdit = defineModel<boolean>('isEdit', { required: true });
const form = defineModel<Project>('form', { required: true });
// const state = defineModel<boolean>('state', { required: true });

const projectsStore = useProjectsStore()
const usersStore = useUsersStore()
const users = computed(() => usersStore.getUsers)
// const projects = computed(() => projectsStore.getProjects)

const handleEditProject = (project: Project) => {
  isEdit.value = true
  form.value = {
    id: project.id,
    name: project.name,
    description: project.description,
    status: project.status,
    progress: project.progress,
    dateRange: [project.startDate, project.endDate],
    leaderId: project.leaderId,
    memberIds: project.memberIds
  }
  dialogVisible.value = true
  console.log('handleEditProject', project);
}


const handleDeleteProject = async (project: Project) => {
  try {
    await ElMessageBox.confirm(`确定要删除项目 ${project.name} 吗？`, '警告', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' });
    const data = await projectService.deleteProject(project.id!);
    ElMessage.success('删除项目[' + project.projectId + ']成功: ' + data.message);
    projectsStore.refetchProjects()
    // state.value = !state.value;
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败');
      console.error(error);
    }
  }
}

</script>
