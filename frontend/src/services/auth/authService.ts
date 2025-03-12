import axios from 'axios';
import { API_ENDPOINTS, TOKEN } from '../constants';
import type { Employee, EmployeeForm } from '../types';
import { useMeStore } from '@/store/meStore';

export const authService = {
  async login(employeeId: string, password: string) {
    console.log('Login Data:');
    console.log(`employeeId: ${employeeId}`);
    console.log(`password: ${password}`);
    const response = await axios.post(API_ENDPOINTS.LOGIN, { employeeId, password });


    const meStore = useMeStore();
    meStore.setMe(response.data.user);
    console.log(meStore.getMe);

    if (response.data) {
        localStorage.setItem('token', response.data.token);
        localStorage.setItem('userInfo', JSON.stringify(response.data.user));
        localStorage.setItem('role', response.data.role);
        localStorage.setItem('id', response.data.user.id);
    }
    console.log(localStorage);
    console.log(response.data);
    return response.data;
  },
}; 