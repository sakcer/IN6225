import axios from 'axios';
import { API_ENDPOINTS } from '@/services/utils';
import { useUserStore } from '@/store/userStore';
import type { LoginForm } from '@/utils/types/auth';

export const authService = {
  async login(loginForm: LoginForm) {
    const response = await axios.post(API_ENDPOINTS.LOGIN, loginForm);

    const meStore = useUserStore();
    meStore.setUser(response.data);

    return response.data;
  },
  async refreshAccessToken() {
    const response = await axios.post(API_ENDPOINTS.REFRESH_TOKEN, null, {withCredentials: true});
    return response.data;
  },
  async logout() {
    const response = await axios.post(API_ENDPOINTS.LOGOUT, null, {withCredentials: true});
    return response.data;
  }
}; 