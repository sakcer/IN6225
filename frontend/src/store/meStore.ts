import { defineStore } from 'pinia'
import type { Employee } from '@/utils/types/employee'
import { employeeService } from '@/services/employees/employeeService'
import { USER_ROLES } from '@/utils/constants'

export const useMeStore = defineStore('me', {
  state: () => ({
    me: {} as Employee,
    role: USER_ROLES.USER,
    token: null as string | null,
  }),
  getters: {
    getMe: (state) => state.me,  // getter 方法
  },
  actions: {
    clearMe() {
      this.me = {} as Employee;
      this.role = USER_ROLES.USER;
      this.token = null;
    },
    setMe(data: any) {
      this.me = data.user;
      this.role = data.role;
      this.token = data.token;
    },
    async refetchMe() {
      try { 
        const res = await employeeService.getEmployeeById(this.me.id);
        console.log("res", res);
        this.me = res;
        this.role = res.role;
      } catch (error) {
        console.error("Failed to fetch user info:", error);
      }
    },
  },
  persist: true,
});