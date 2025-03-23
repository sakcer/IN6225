import { defineStore } from 'pinia'
import type { Employee } from '@/utils/types/employee'
import { employeeService } from '@/services/employees/employeeService'

export const useUsersStore = defineStore('users', {
  state: () => ({
    users: [] as Employee[]
  }),
  getters: {
    getUsers: (state) => state.users, 
  },
  actions: {
    async refetchUsers() {
      try {
        const res = await employeeService.getAllEmployees();
        this.users = res;
      } catch (error) {
        console.error("Failed to fetch users info:", error);
      }
    },
  },
});