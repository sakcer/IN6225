// export const API_ENDPOINTS = {
//   EMPLOYEES: '/api/employee',
//   EMPLOYEES_ALL: '/api/employee/all',
//   EMPLOYEES_UPDATE: '/api/employee',
//   EMPLOYEES_ADD: '/api/employee',
//   EMPLOYEES_DELETE: '/api/employee',
//   EMPLOYEES_UPDATE_PASSWORD: '/api/employees/password',
//   PROJECTS: '/api/project',
//   PROJECTS_ALL: '/api/project/all',
//   PROJECTS_UPDATE: '/api/project',
//   PROJECTS_ADD: '/api/project',
//   PROJECTS_DELETE: '/api/project',
//   LOGIN: '/api/auth/login',
// } as const; 

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
  STATS_OVERVIEW: '/stats/overview',
  STATS_DEPARTMENTS_DIST: '/stats/departments/distribution',
  STATS_EMPLOYEES_TREND: '/stats/employees/trend',
} as const;


import axios from 'axios';
import { useUserStore } from '@/store/meStore'

export const axiosInstance = axios.create({
  baseURL: '/api', // 根据需要设置 baseURL
});


axiosInstance.interceptors.request.use(
  (config) => {
    const meStore = useUserStore();
    const {token} = meStore;

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// axiosInstance.interceptors.response.use(
//   (response) => response,
//   (error) => {
//     return Promise.reject(error);
//   }
// );

// export default axiosInstance;