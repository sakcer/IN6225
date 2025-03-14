import axios from 'axios';
import { API_ENDPOINTS } from '../constants';
import type { Project, ProjectForm } from '../types';


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
    console.log(response.headers['content-type']);
    console.log(response.data);
    return response.data;
  },

  async createProject(project: ProjectForm) {
    console.log('Project Data:');
    Object.entries(project).forEach(([key, value]) => {
        console.log(`${key}: ${value}`);
    });
    const { id, ...rest } = project;
    const response = await axios.post(API_ENDPOINTS.PROJECTS_ADD, rest);
    return response.data;
  },

  async updateProject(id: number, project: Partial<ProjectForm>) {
    console.log('Project Data:');
    Object.entries(project).forEach(([key, value]) => {
        console.log(`${key}: ${value}`);
    });
    const response = await axios.put(`${API_ENDPOINTS.PROJECTS_UPDATE}`, project);
    return response.data;
  },

  async deleteProject(id: string) {
    const response = await axios.delete(`${API_ENDPOINTS.PROJECTS}/${id}`);
    return response.data;
  },

  async getProjectById(id: string) {
    const response = await axios.get(`${API_ENDPOINTS.PROJECTS}/${id}`);
    return response.data;
  }
}; 