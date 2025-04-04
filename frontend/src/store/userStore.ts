import { defineStore } from 'pinia'
import type { Employee } from '@/utils/types/employee'
import { employeeService } from '@/services/employees/employeeService'
import { USER_ROLES } from '@/utils/constants'
import { handleAxiosError } from '@/utils/errorMsg';
import { authService } from '@/services/auth/authService';
import router from '@/router';

export const useUserStore = defineStore('me', {
  state: () => ({
    userInfo: {} as Employee,
    accessToken: null as string | null,
  }),
  getters: {
    getMe: (state) => state.userInfo,
    isAdmin: (state) => state.userInfo.role === USER_ROLES.ADMIN,
    isLeader: (state) => state.userInfo.role === USER_ROLES.LEADER,
  },
  actions: {
    async isLoggedIn() {
      if (this.accessToken && this.userInfo) {
        return true;
      }
      try {
        await this.refreshAccessToken();
        await this.fetchUserInfo();
        return true;
      } catch(error) {
        console.log(error)
        return false;
      }
    },
    async logout() {
      try {
        await authService.logout();
        this.clearUser();
        router.push('/login');
      } catch (error) {
        handleAxiosError(error)
      }
    },
    clearUser() {
      this.userInfo = {} as Employee;
      this.accessToken = null;
    },
    setUser(data: any) {
      this.userInfo = data.user;
      this.accessToken = data.accessToken;
    },
    async fetchUserInfo() {
      try { 
        const res = await employeeService.getEmployeeMe();
        this.userInfo = res;
      } catch (error) {
        handleAxiosError(error)
      }
    },
    async refreshAccessToken() {
      const res = await authService.refreshAccessToken();
      console.log(res)
      this.accessToken = res.message;
      return res.message;
    }
  },
});