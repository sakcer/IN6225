import axios from 'axios';
import { API_ENDPOINTS, TOKEN } from '../constants';
import type { Employee, EmployeeForm } from '../types';
import { useMeStore } from '@/store/meStore';

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
    console.log(response.headers['content-type']);
    console.log(response.data);
    return response.data;
  },

  async createEmployee(employee: EmployeeForm) {
    console.log('Employee Data:');
    Object.entries(employee).forEach(([key, value]) => {
        console.log(`${key}: ${value}`);
    });

    const response = await axios.post(API_ENDPOINTS.EMPLOYEES_ADD, employee);
    return response.data;
  },

  async updateEmployee(id: string, employee: Partial<EmployeeForm>) {
    console.log('Employee Data:');
    Object.entries(employee).forEach(([key, value]) => {
        console.log(`${key}: ${value}`);
    });
    const response = await axios.put(`${API_ENDPOINTS.EMPLOYEES_UPDATE}`, employee);
    return response.data;
  },

  async deleteEmployee(id: string) {
    const response = await axios.delete(`${API_ENDPOINTS.EMPLOYEES}/${id}`);
    return response.data;
  },

  async getEmployeeById(id: number) {
    console.log('getEmployeeById', id);
    const response = await axios.get(`${API_ENDPOINTS.EMPLOYEES}/${id}`);
    return response.data;
  },

  async updateEmployeePassword(id: number, password: Object) {
    const response = await axios.post(`${API_ENDPOINTS.EMPLOYEES_UPDATE_PASSWORD}/${id}`, password);
    return response.data;
  },
}; 