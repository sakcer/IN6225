import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Employee } from '@/utils/types'
import { ElMessage } from 'element-plus';
import { projectService } from '@/services/projects/projectService';
import { PROJECT_STATUS } from '@/utils/constants';
import { useMeStore } from '@/store/meStore';

export const useMyProjectStore = defineStore('myProjects', {
  state: () => ({
    projects: []
  }),
  getters: {
    getProjects: (state) => state.projects,
  },
  actions: {
    async refetchProjects() {
      try {
        const meStore = useMeStore();
        const res = await projectService.getProjectsByUserId(meStore.getMe.id);
        console.log(res);
        this.projects = res;
      } catch (error) {
        console.error("Failed to fetch projects info:", error);
      }
    },
  },
});
