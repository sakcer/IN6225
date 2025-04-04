import axios from 'axios';
import { API_ENDPOINTS } from '@/services/utils';
import { useUserStore } from '@/store/meStore';

export const authService = {
  async login(employeeId: string, password: string) {
    const response = await axios.post(API_ENDPOINTS.LOGIN, { employeeId, password });

    const meStore = useUserStore();
    meStore.setUser(response.data);

    return response.data;
  },
  async refreshAccessToken() {
    const response = await axios.post(API_ENDPOINTS.REFRESH_TOKEN, null, {withCredentials: true});
    return response.data;
  },
}; 