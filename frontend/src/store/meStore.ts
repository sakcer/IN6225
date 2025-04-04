import { defineStore } from 'pinia'
import type { Employee } from '@/utils/types/employee'
import { employeeService } from '@/services/employees/employeeService'
import { USER_ROLES } from '@/utils/constants'
import { handleAxiosError } from '@/utils/errorMsg';
import { authService } from '@/services/auth/authService';

export const useUserStore = defineStore('me', {
  state: () => ({
    userInfo: {} as Employee,
    role: USER_ROLES.USER,
    accessToken: null as string | null,
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
      this.accessToken = null;
    },
    setUser(data: any) {
      this.userInfo = data.user;
      this.role = data.role;
      this.accessToken = data.accessToken;
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
    async refreshAccessToken() {
      try {
        const res = await authService.refreshAccessToken();
        console.log(res)
        this.accessToken = res.message;
        return res.message;
      } catch (error) {
        handleAxiosError(error)
        this.clearUser()
        return null
      }
    }
  },
  // persist: true,
});