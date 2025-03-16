import axios from 'axios';
import { API_ENDPOINTS } from '../constants';
import { useMeStore } from '@/store/meStore';

export const authService = {
  async login(employeeId: string, password: string) {
    // console.log('Login Data:');
    // console.log(`employeeId: ${employeeId}`);
    // console.log(`password: ${password}`);
    const response = await axios.post(API_ENDPOINTS.LOGIN, { employeeId, password });


    const meStore = useMeStore();
    meStore.setMe(response.data);

    return response.data;
  },
}; 