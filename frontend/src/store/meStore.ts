import { defineStore } from 'pinia'
import { ref, watch, computed } from 'vue'
import type { Employee } from '@/utils/types'
import { employeeService } from '@/services/employees/employeeService'
import { localUser } from '@/utils/localUser'

export const useMeStore = defineStore('me', {
  state: () => ({
    me: null as Employee | null,
    role: null as string | null,
    token: null as string | null,
    // me: JSON.parse(localStorage.getItem('userInfo') || '{}') as Employee | null,
  }),
  getters: {
    getMe: (state) => state.me,  // getter 方法
  },
  actions: {
    setMe(user: Employee) {
      console.log('setMe', user);
      this.me = user;

      localStorage.setItem('userInfo', JSON.stringify(user));
      console.log('localStorage.setItem', localStorage.getItem('userInfo'));
    },
    async refetchMe() {
      try { 
        const res = await employeeService.getEmployeeById(localStorage.getItem('id'));
        console.log('refetchMe', res, localStorage.getItem('id'));
        this.me = res;
        this.role = res.role;
        this.token = localStorage.getItem('token');

      } catch (error) {
        console.error("Failed to fetch user info:", error);
      }
    },
  },
  persist: true,
});