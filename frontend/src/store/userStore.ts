import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Employee } from '@/utils/types'
import { employeeService } from '@/services/employees/employeeService'
import { ElMessage } from 'element-plus';

export const useUsersStore = defineStore('users', {
  state: () => ({
    users: [{
      id: '',
      name: '',
      department: '',
      joinDate: '',
      title: '',
      status: '',
      email: '',
      phone: '',
      avatar: '',
    }]
  }),
  getters: {
    getUsers: (state) => state.users,  // getter 方法
  },
  actions: {
    async refetchUsers() {
      try {
        const res = await employeeService.getAllEmployees();
        this.users = res.users;  // 假设返回的对象包含 `users` 数组
        console.log('refetchUsers', this.users);
      } catch (error) {
        console.error("Failed to fetch users info:", error);
      }
    },
  },
});