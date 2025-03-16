import axios from 'axios';
import { API_ENDPOINTS } from '@/services/constants';
import type { Project } from '@/utils/types/project';

axios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token"); // 从 localStorage 获取 Token
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export const projectService = {
  async getAllProjects() {
    const response = await axios.get(API_ENDPOINTS.PROJECTS_ALL);
    return response.data;
  },

  async createProject(project: Project) {
    // console.log('createProject', project);
    const { id, ...rest } = project;
    const response = await axios.post(API_ENDPOINTS.PROJECTS_ADD, rest);
    return response.data;
  },

  async updateProject(project: Project) {
    // console.log('updateProject', project);
    const response = await axios.put(`${API_ENDPOINTS.PROJECTS_UPDATE}`, project);
    return response.data;
  },

  async deleteProject(id: number) {
    const response = await axios.delete(`${API_ENDPOINTS.PROJECTS}/${id}`);
    return response.data;
  },

  async getProjectById(id: number) {
    const response = await axios.get(`${API_ENDPOINTS.PROJECTS}/${id}`);
    return response.data;
  },

  async getProjectsByUserId(userId: number) {
    const response = await axios.get(`${API_ENDPOINTS.PROJECTS_ALL}/${userId}`);
    return response.data;
  } 
}; 