import { defineStore } from 'pinia'
import type { Project } from '@/utils/types/project'
import { projectService } from '@/services/projects/projectService';

export const useProjectsStore = defineStore('projects', {
  state: () => ({
    projects: [] as Project[]
  }),
  getters: {
    getProjects: (state) => state.projects,
  },
  actions: {
    async refetchProjects() {
      try {
        const res = await projectService.getAllProjects();
        this.projects = res;
      } catch (error) {
        console.error("Failed to fetch projects info:", error);
      }
    },
  },
});
