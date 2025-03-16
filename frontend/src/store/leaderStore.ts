import { defineStore } from 'pinia'
import type { Employee } from '@/utils/types/employee'
import { employeeService } from '@/services/employees/employeeService'
import { USER_ROLES } from '@/utils/constants';

export const useLeadersStore = defineStore('leaders', {
  state: () => ({
    leaders: [] as Employee[]
  }),
  getters: {
    getLeaders: (state) => state.leaders, 
  },
  actions: {
    async refetchLeaders() {
      try {
        const res = await employeeService.getAllEmployees();
        this.leaders = res.users.filter((user: Employee) => user.role === USER_ROLES.LEADER);
      } catch (error) {
        console.error("Failed to fetch users info:", error);
      }
    },
  },
});