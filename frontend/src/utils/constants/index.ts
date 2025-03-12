export const AVATAR_COLORS = [
  '#f56c6c',
  '#e6a23c',
  '#67c23a',
  '#409eff',
  '#909399'
] as const;

export const USER_STATUS = {
  ALL: 'ALL',
  ACTIVE: 'ACTIVE',
  INACTIVE: 'INACTIVE'
} as const;

export const PROJECT_STATUS = {
  ALL: 'ALL',
  ACTIVE: 'ACTIVE',
  COMPLETED: 'COMPLETED'
} as const;

export const USER_ROLES = {
  OWNER: 'OWNER',
  ADMIN: 'ADMIN',
  USER: 'USER'
} as const;

export const PAGE_SIZES = [5, 10, 20, 50, 100] as const;

export enum Department {
  TCH = 'TCH',
  HR = 'HR',
  SLS = 'SLS',
  MKT = 'MKT',
  FIN = 'FIN',
  LGL = 'LGL',
  OPS = 'OPS',
  CS = 'CS'
}
