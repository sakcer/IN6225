import { API_ENDPOINTS, axiosInstance } from '@/services/utils';
import type { Project } from '@/utils/types/project';

export const projectService = {
  async getAllProjects() {
    const response = await axiosInstance.get(API_ENDPOINTS.PROJECTS_ALL);
    return response.data;
  },

  async createProject(project: Project) {
    // console.log('createProject', project);
    const { id, ...rest } = project;
    const response = await axiosInstance.post(API_ENDPOINTS.PROJECTS_ADD, rest);
    return response.data;
  },

  async updateProject(project: Project) {
    // console.log('updateProject', project);
    const response = await axiosInstance.put(`${API_ENDPOINTS.PROJECTS_UPDATE}/${project.id}`, project);
    return response.data;
  },

  async deleteProject(id: number) {
    const response = await axiosInstance.delete(`${API_ENDPOINTS.PROJECTS}/${id}`);
    return response.data;
  },

  async getProjectById(id: number) {
    const response = await axiosInstance.get(`${API_ENDPOINTS.PROJECTS}/${id}`);
    return response.data;
  },

  async getAllProjectsByMe() {
    const response = await axiosInstance.get(`${API_ENDPOINTS.PROJECTS_ALL_ME}`);
    return response.data;
  } 
}; 