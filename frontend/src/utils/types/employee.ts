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
  projects?: string;
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

export const rules = {
  // name: [
  //   { required: true, message: '姓名不能为空', trigger: 'blur' },
  //   { min: 2, max: 20, message: '姓名长度必须在 2 到 20 个字符之间', trigger: 'blur' },
  // ],
  // email: [
  //   { required: true, message: '邮箱不能为空', trigger: 'blur' },
  //   { type: 'email', message: '请输入有效的邮箱地址', trigger: ['blur', 'change'] },
  // ],
  // employeeId: [
  //   { required: true, message: '工号不能为空', trigger: 'blur' },
  //   // { pattern: /^[0-9]{6}$/, message: '工号必须是6位数字', trigger: 'blur' },
  // ],
  // role: [
  //   { required: true, message: '请选择角色', trigger: 'change' },
  // ],
  // status: [
  //   { required: true, message: '请选择状态', trigger: 'change' },
  // ],
};