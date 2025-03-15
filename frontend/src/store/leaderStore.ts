import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Employee } from '@/utils/types'
import { employeeService } from '@/services/employees/employeeService'
import { ElMessage } from 'element-plus';
import { USER_ROLES } from '@/utils/constants';

export const useLeadersStore = defineStore('leaders', {
  state: () => ({
    leaders: []
  }),
  getters: {
    getLeaders: (state) => state.leaders, 
  },
  actions: {
    async refetchLeaders() {
      try {
        const res = await employeeService.getAllEmployees();
        this.leaders = res.users.filter(user => user.role === USER_ROLES.OWNER);
      } catch (error) {
        console.error("Failed to fetch users info:", error);
      }
    },
  },
});