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

export const rules: FormRules = {
  // name: [
  //   { required: true, message: '请输入项目名称', trigger: 'blur' },
  //   { min: 1, max: 50, message: '长度在 3 到 50 个字符', trigger: 'blur' }
  // ],
  // // description: [
  // //   { required: true, message: '请输入项目描述', trigger: 'blur' }
  // // ],
  // status: [
  //   { required: true, message: '请选择项目状态', trigger: 'change' }
  // ],
  // dateRange: [
  //   { required: true, message: '请选择项目起止时间', trigger: 'change' }
  // ],
  // leaderId: [
  //   { required: true, message: '请选择项目负责人', trigger: 'change' }
  // ],
  // memberIds: [
  //   { required: true, message: '请选择项目成员', trigger: 'change' }
  // ]
}