import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Employee } from '@/utils/types'
import { ElMessage } from 'element-plus';
import { projectService } from '@/services/projects/projectService';
import { PROJECT_STATUS } from '@/utils/constants';

export const useMyProjectStore = defineStore('myProjects', {
  state: () => ({
    projects: [{
      id: -1,
      name: '',
      description: '',
      startDate: '',
      endDate: '',
      status: PROJECT_STATUS.ACTIVE,
      progress: 0,
      leaderId: -1,
      members: [],
    }]
  }),
  getters: {
    getProjects: (state) => state.projects,
  },
  actions: {
    async refetchProjects() {
      try {
        const res = await projectService.getAllProjects();
        this.projects = res.projects;
      } catch (error) {
        console.error("Failed to fetch projects info:", error);
      }
    },
  },
});
