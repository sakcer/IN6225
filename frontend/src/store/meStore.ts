import { defineStore } from 'pinia'
import type { Employee } from '@/utils/types/employee'
import { employeeService } from '@/services/employees/employeeService'
import { USER_ROLES } from '@/utils/constants'
import { handleAxiosError } from '@/utils/errorMsg';

export const useUserStore = defineStore('me', {
  state: () => ({
    userInfo: {} as Employee,
    role: USER_ROLES.USER,
    token: null as string | null,
  }),
  getters: {
    getMe: (state) => state.userInfo,  // getter 方法
    isAdmin: (state) => state.role === USER_ROLES.ADMIN,
    isLeader: (state) => state.role === USER_ROLES.LEADER,
  },
  actions: {
    clearUser() {
      this.userInfo = {} as Employee;
      this.role = USER_ROLES.USER;
      this.token = null;
    },
    setUser(data: any) {
      this.userInfo = data.user;
      this.role = data.role;
      this.token = data.token;
    },
    async fetchUserInfo() {
      try { 
        const res = await employeeService.getEmployeeMe();
        this.userInfo = res;
        this.role = res.role;
      } catch (error) {
        handleAxiosError(error)
      }
    },
  },
  persist: true,
});