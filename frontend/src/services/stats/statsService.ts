import axios from 'axios';
import { API_ENDPOINTS, axiosInstance } from '@/services/utils';
import { useUserStore } from '@/store/meStore';

export const statsService = {
  async getStatsOverview() {
    const response = await axiosInstance.get(API_ENDPOINTS.STATS_OVERVIEW);
    return response.data;
  },

  async getDepartmentDist() {
    const response = await axiosInstance.get(API_ENDPOINTS.STATS_DEPARTMENTS_DIST);
    return response.data;
  },

  async getEmployeeTrend() {
    const response = await axiosInstance.get(API_ENDPOINTS.STATS_EMPLOYEES_TREND);
    return response.data;
  },
};