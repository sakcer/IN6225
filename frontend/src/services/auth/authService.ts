import axios from 'axios';
import { API_ENDPOINTS } from '@/services/utils';
import { useMeStore } from '@/store/meStore';

export const authService = {
  async login(employeeId: string, password: string) {
    const response = await axios.post(API_ENDPOINTS.LOGIN, { employeeId, password });

    const meStore = useMeStore();
    meStore.setMe(response.data);

    return response.data;
  },
}; 