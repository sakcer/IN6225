import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Employee } from '@/utils/types'
import { employeeService } from '@/services/employees/employeeService'
import { ElMessage } from 'element-plus';
import { USER_STATUS } from '@/utils/constants';

export const useUsersStore = defineStore('users', {
  state: () => ({
    users: []
  }),
  getters: {
    getUsers: (state) => state.users, 
  },
  actions: {
    async refetchUsers() {
      try {
        const res = await employeeService.getAllEmployees();
        this.users = res.users;
      } catch (error) {
        console.error("Failed to fetch users info:", error);
      }
    },
  },
});