export const AVATAR_COLORS = [
  '#f56c6c',
  '#e6a23c',
  '#67c23a',
  '#409eff',
  '#909399'
] as const;

export enum USER_STATUS {
  ALL = 'ALL',
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE'
}

export enum PROJECT_STATUS {
  ALL = 'ALL',
  ACTIVE = 'ACTIVE',
  COMPLETED = 'COMPLETED',
}

export enum USER_ROLES {
  LEADER = 'LEADER',
  ADMIN = 'ADMIN',
  USER = 'USER'
}

export const PAGE_SIZES = [5, 10, 20, 50, 100] as const;

export enum DEPARTMENTS {
  TCH = 'TCH',
  HR = 'HR',
  SLS = 'SLS',
  MKT = 'MKT',
  FIN = 'FIN',
  LGL = 'LGL',
  OPS = 'OPS',
  CS = 'CS'
}

export enum FORM_TYPES {
  ADD = 'ADD',
  EDIT = 'EDIT'
}
