import { API_ENDPOINTS, axiosInstance } from '@/services/utils';
import type { Employee } from '@/utils/types/employee';
import { useMeStore} from '@/store/meStore'
import { USER_ROLES, USER_STATUS } from '@/utils/constants';

export const employeeService = {
  async getAllEmployees() {
    const response = await axiosInstance.get(API_ENDPOINTS.EMPLOYEES_ALL);
    return response.data;
  },
  async createEmployee(employee: Employee) {
    const { id, ...rest } = employee;
    rest.type = "userDetailsDTO";
    const response = await axiosInstance.post(API_ENDPOINTS.EMPLOYEES_ADD, rest);
    return response.data;
  },

  async deleteEmployee(id: number) {
    const response = await axiosInstance.delete(`${API_ENDPOINTS.EMPLOYEES}/${id}`);
    return response.data;
  },

  async getEmployeeMe() {
    const response = await axiosInstance.get(`${API_ENDPOINTS.EMPLOYEES_ME}`);
    return response.data;
  },

  async updateEmployee(employee: Employee) {
    const meStore = useMeStore();
    const {me} = meStore;
    const data= {...employee};
    if (me.role === USER_ROLES.ADMIN) {
      data.type = "userDetailsDTO";
    }
    const response = await axiosInstance.put(`${API_ENDPOINTS.EMPLOYEES_UPDATE}/${data.id}`, data);
    return response.data;
  },

  async updateEmployeePassword(id: number, password: Object) {
    console.log(password, id);
    const response = await axiosInstance.put(`${API_ENDPOINTS.EMPLOYEES_UPDATE}/${id}/password`, password);
    return response.data;
  },

  async getEmployeesByFilter(page: number, size: number, sort: string, query: string, status: USER_STATUS) {
    let url = `${API_ENDPOINTS.EMPLOYEES}?page=${page}&size=${size}&sort=${sort}`;
    if (query) {
      url += `&query=${query}`;
    }
    if (status !== undefined && status !== USER_STATUS.ALL) {
      url += `&status=${status}`;
    }
    const response = await axiosInstance.get(url);
    return response.data;
  }
}; 