import { USER_STATUS, USER_ROLES } from '../constants';
import { markRaw } from 'vue';

export type UserStatus = typeof USER_STATUS[keyof typeof USER_STATUS];
export type UserRole = typeof USER_ROLES[keyof typeof USER_ROLES];

export interface Employee {
  id: number;
  name: string;
  email: string;
  employeeId: string;
  role: UserRole;
  status: UserStatus;
  department: string;
  title?: string;
  avatar?: string;
  joinDate?: string;
}

export interface EmployeeForm {
  name: string;
  email: string;
  employeeId: string;
  role: UserRole;
  status: UserStatus;
  department: string;
  avatar: string;
}

export interface Statistic {
  label: string;
  value: number;
  icon: ReturnType<typeof markRaw>; 
  type: 'primary' | 'success' | 'warning' | 'danger';
}

export interface PaginationConfig {
  currentPage: number;
  pageSize: number;
  total: number;
} 
