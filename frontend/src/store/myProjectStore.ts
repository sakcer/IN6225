import { defineStore } from 'pinia'
import type { Project } from '@/utils/types/project'
import { projectService } from '@/services/projects/projectService';
import { useMeStore } from '@/store/meStore';

export const useMyProjectStore = defineStore('myProjects', {
  state: () => ({
    projects: [] as Project[]
  }),
  getters: {
    getProjects: (state) => state.projects,
  },
  actions: {
    async refetchProjects() {
      try {
        const meStore = useMeStore();
        if (meStore.getMe?.id) {
          const res = await projectService.getProjectsByUserId(meStore.getMe.id);
          console.log(res);
          this.projects = res;
        }
      } catch (error) {
        console.error("Failed to fetch projects info:", error);
      }
    },
  },
});
