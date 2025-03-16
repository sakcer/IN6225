import axios from 'axios';
import { API_ENDPOINTS } from '@/services/constants';
import type { Employee } from '@/utils/types/employee';

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

export const employeeService = {
  async getAllEmployees() {
    const response = await axios.get(API_ENDPOINTS.EMPLOYEES_ALL);
    return response.data;
  },
  async createEmployee(employee: Employee) {
    const { id, ...rest } = employee;
    console.log(id);
    const response = await axios.post(API_ENDPOINTS.EMPLOYEES_ADD, rest);
    return response.data;
  },

  async updateEmployee(employee: Employee) {
    // console.log(employee);
    const response = await axios.put(`${API_ENDPOINTS.EMPLOYEES_UPDATE}`, employee);
    return response.data;
  },

  async deleteEmployee(id: number) {
    const response = await axios.delete(`${API_ENDPOINTS.EMPLOYEES}/${id}`);
    return response.data;
  },

  async getEmployeeById(id: number) {
    const response = await axios.get(`${API_ENDPOINTS.EMPLOYEES}/${id}`);
    return response.data;
  },

  async updateEmployeePassword(id: number, password: Object) {
    const response = await axios.post(`${API_ENDPOINTS.EMPLOYEES_UPDATE_PASSWORD}/${id}`, password);
    return response.data;
  },
}; 