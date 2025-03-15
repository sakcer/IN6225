import { defineStore } from 'pinia'
import { ref, watch, computed } from 'vue'
import type { Employee } from '@/utils/types'
import { employeeService } from '@/services/employees/employeeService'

export const useMeStore = defineStore('me', {
  state: () => ({
    me: null as Employee | null,
    role: null as string | null,
    token: null as string | null,
  }),
  getters: {
    getMe: (state) => state.me,  // getter 方法
  },
  actions: {
    clearMe() {
      this.me = {};
      this.role = USER_ROLES.USER;
      this.token = null
    },
    setMe(data: any) {
      this.me = data.user;
      this.role = data.role;
      this.token = data.token;
    },
    async refetchMe() {
      try { 
        const res = await employeeService.getEmployeeById(this.me.id);
        this.me = res;
        this.role = res.role;
      } catch (error) {
        console.error("Failed to fetch user info:", error);
      }
    },
  },
  persist: true,
});