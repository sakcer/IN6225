export const API_ENDPOINTS = {
  EMPLOYEES: '/employee',
  EMPLOYEES_ALL: '/employee/all',
  EMPLOYEES_UPDATE: '/employee',
  EMPLOYEES_ADD: '/employee',
  EMPLOYEES_DELETE: '/employee',
  EMPLOYEES_ME: '/employee/me',
  EMPLOYEES_UPDATE_PASSWORD: '/employees/password',
  PROJECTS: '/project',
  PROJECTS_ALL: '/project/all',
  PROJECTS_UPDATE: '/project',
  PROJECTS_ADD: '/project',
  PROJECTS_DELETE: '/project',
  PROJECTS_ALL_ME: '/project/all/me',
  LOGIN: '/api/auth/login',
  REFRESH_TOKEN: '/api/auth/refresh',
  STATS_OVERVIEW: '/stats/overview',
  STATS_DEPARTMENTS_DIST: '/stats/departments/distribution',
  STATS_EMPLOYEES_TREND: '/stats/employees/trend',
} as const;


import axios from 'axios';
import { useUserStore } from '@/store/meStore'
import { handleAxiosError } from '@/utils/errorMsg';

export const axiosInstance = axios.create({
  baseURL: '/api',
  withCredentials: true,
});


axiosInstance.interceptors.request.use(
  (config) => {
    const userStore = useUserStore();
    if (userStore.accessToken) {
      config.headers.Authorization = `Bearer ${userStore.accessToken}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const userStore = useUserStore();

    if (error.response?.status === 401) {
      try {
        const newAccessToken = await userStore.refreshAccessToken();
        if (newAccessToken) {
          error.config.headers.Authorization = `Bearer ${newAccessToken}`;
          return axiosInstance.request(error.config);
        }
      } catch (error) {
        handleAxiosError(error);
        userStore.clearUser();
        return Promise.reject(error);
      }
    } 

    return Promise.reject(error);
  }
);

// export default axiosInstance;