import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Employee } from '@/utils/types'
import { ElMessage } from 'element-plus';
import { projectService } from '@/services/projects/projectService';
import { PROJECT_STATUS } from '@/utils/constants';

export const useProjectsStore = defineStore('projects', {
  state: () => ({
    projects: []
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
