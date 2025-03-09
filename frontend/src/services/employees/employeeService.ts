import axios from 'axios';
import { API_ENDPOINTS } from '../constants';
import type { Employee, EmployeeForm } from '../types';

export const employeeService = {
  async getAllEmployees() {
    const response = await axios.get(API_ENDPOINTS.EMPLOYEES_ALL);
    return response.data;
  },

  async createEmployee(employee: EmployeeForm) {
    const response = await axios.post(API_ENDPOINTS.EMPLOYEES_ADD, employee);
    return response.data;
  },

  async updateEmployee(id: string, employee: Partial<EmployeeForm>) {
    const response = await axios.put(`${API_ENDPOINTS.EMPLOYEES_UPDATE}`, employee);
    return response.data;
  },

  async deleteEmployee(id: string) {
    const response = await axios.delete(`${API_ENDPOINTS.EMPLOYEES}/${id}`);
    return response.data;
  },

  async getEmployeeById(id: string) {
    const response = await axios.get(`${API_ENDPOINTS.EMPLOYEES}/${id}`);
    return response.data;
  }
}; 