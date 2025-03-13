import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Employee } from '@/utils/types'
import { ElMessage } from 'element-plus';
import { projectService } from '@/services/projects/projectService';


export const useMyProjectStore = defineStore('myProjects', {
  state: () => ({
    projects: [{
      id: '',
      name: '',
      description: '',
      startDate: '',
      endDate: '',
      status: '',
      progress: '',
      leaderId: '',
      memberIds: [],
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
        console.log('refetchProjects', this.projects);
      } catch (error) {
        console.error("Failed to fetch projects info:", error);
      }
    },
  },
});
