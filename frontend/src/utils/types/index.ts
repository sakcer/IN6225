import { USER_STATUS, USER_ROLES } from '../constants';
import { markRaw } from 'vue';

export type UserStatus = typeof USER_STATUS[keyof typeof USER_STATUS];
export type UserRole = typeof USER_ROLES[keyof typeof USER_ROLES];

export interface Employee {
  id: string;
  name: string;
  email: string;
  employeeId: string;
  role: UserRole;
  status: UserStatus;
  department: string;
  title?: string;
  avatar?: string;
  projects?: string;
  joinDate?: string;
}

export interface EmployeeForm {
  avatar: string;
  name: string;
  email: string;
  employeeId: string;
  department: string;

  role: UserRole;
  status: UserStatus;
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