const API_URL = import.meta.env['VITE_API_URL']

export const API_ENDPOINTS = {
  // for employees
  EMPLOYEES: '/employees',
  EMPLOYEES_ALL: '/employees/all',
  EMPLOYEES_ME: '/employees/me',
  // for projects
  PROJECTS: '/projects',
  // for statistics
  STATS_OVERVIEW: '/stats/overview',
  STATS_DEPARTMENTS_DIST: '/stats/departments/distribution',
  STATS_EMPLOYEES_TREND: '/stats/employees/trend',
  // for Authentication
  LOGIN: API_URL + '/auth/login',
  LOGOUT: API_URL + '/auth/logout',
  REFRESH_TOKEN: API_URL + '/auth/refresh',
} as const;


import axios from 'axios';
import { useUserStore } from '@/store/userStore'
import { handleAxiosError } from '@/utils/errorMsg';

export const axiosInstance = axios.create({
  baseURL: API_URL,
  // withCredentials: true,
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