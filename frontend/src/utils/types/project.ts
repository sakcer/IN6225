import { PROJECT_STATUS } from '../constants';

export type ProjectStatus = typeof PROJECT_STATUS[keyof typeof PROJECT_STATUS];

export interface Project {
  id: number;
  name: string;
  description: string;
  status: ProjectStatus;  // 项目状态
  progress: number;  // 项目进度百分比
  startDate: string;  // 开始日期
  endDate: string;  // 结束日期
  leader: Employee;  // 项目负责人
  members: Employee[];  // 项目成员列表
}

export interface ProjectForm {
  name: number;
  description: string;
  status: ProjectStatus;  // 项目状态
  progress: number;  // 项目进度百分比
  startDate: string;  // 开始日期
  endDate: string;  // 结束日期
  leaderId: number;  // 项目负责人
  memberIds: number[];  // 项目成员列表
}