import type { FormRules } from 'element-plus'

export const infoRules: FormRules = {
  name: [
    { required: true, message: 'Name is required', trigger: 'blur' },
    { min: 1, max: 10, message: 'Less than 10 characters', trigger: 'blur' },
  ],
  email: [
    { required: true, message: 'Email is required', trigger: 'blur' },
    { type: 'email', message: 'Invalid email address', trigger: ['blur', 'change'] },
  ],
};

export const projectRules: FormRules = {
  name: [
    { required: true, message: 'Please enter project name', trigger: 'blur' },
    { min: 1, max: 10, message: 'Length must less than 10 characters', trigger: 'blur' }
  ],
  description: [
    { required: false, message: 'Please enter project description', trigger: 'blur' },
    { min: 0, max: 50, message: 'Length must less than 50 characters', trigger: 'blur' }
  ],
  status: [
    { required: true, message: 'Please select project status', trigger: 'change' }
  ],
  leader: [
    { required: true, message: 'Please select project leader', trigger: 'change' }
  ],
  memberIds: [
    { required: false, message: 'Please select project members', trigger: 'change' }
  ]
}

export const employeeRules: FormRules = {
  name: [
    { required: true, message: 'Name is required', trigger: 'blur' },
    { min: 1, max: 10, message: 'Less than 10 characters', trigger: 'blur' },
  ],
  email: [
    { required: true, message: 'Email is required', trigger: 'blur' },
    { type: 'email', message: 'Invalid email address', trigger: ['blur', 'change'] },
  ],
  employeeId: [
    { required: true, message: 'Employee ID is required', trigger: 'blur' },
    { pattern: /^EID_[0-9a-zA-Z]{5}$/, message: 'Invalid format EID_00000', trigger: 'blur' },
  ],
  role: [
    { required: true, message: 'Please select a role', trigger: 'change' },
  ],
  department: [
    { required: true, message: 'Please select a department', trigger: 'change' },
  ],
  status: [
    { required: true, message: 'Please select a status', trigger: 'change' },
  ],
};


export const passwordRules: FormRules = {
    currentPassword: [
        { required: true, message: 'Current password is required' }
    ],
    newPassword: [
        { required: true, message: 'New password is required' },
        { min: 6, max: 20, message: 'Between 6 and 20 characters' }
    ],
    confirmPassword: [
        { required: true, message: 'Confirm password is required' },
    ]
}