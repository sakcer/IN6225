import { PROJECT_STATUS } from '../constants';
import type { Employee } from './employee';

export type ProjectStatus = typeof PROJECT_STATUS[keyof typeof PROJECT_STATUS];

export interface Project {
  id: number;
  name: string;
  description: string;
  status: ProjectStatus;
  progress: number;
  startDate: string;
  endDate: string;
  leader: Employee;
  members: Employee[];
}