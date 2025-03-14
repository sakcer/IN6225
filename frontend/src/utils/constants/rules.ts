import type { FormRules } from 'element-plus'

export const infoRules: FormRules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  title: [{ required: true, message: '请输入职位', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
};

export const projectRules: FormRules = {
  name: [
    { required: true, message: '请输入项目名称', trigger: 'blur' },
    { min: 1, max: 50, message: '长度在 3 到 50 个字符', trigger: 'blur' }
  ],
  description: [
    { required: false, message: '请输入项目描述', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择项目状态', trigger: 'change' }
  ],
//   dateRange: [
//     { required: true, message: '请选择项目起止时间', trigger: 'change' }
//   ],
  leaderId: [
    { required: true, message: '请选择项目负责人', trigger: 'change' }
  ],
  memberIds: [
    { required: false, message: '请选择项目成员', trigger: 'change' }
  ]
}

export const employeeRules: FormRules = {
  name: [
    { required: true, message: '姓名不能为空', trigger: 'blur' },
    { min: 1, max: 20, message: '姓名长度必须在 2 到 20 个字符之间', trigger: 'blur' },
  ],
  email: [
    { required: true, message: '邮箱不能为空', trigger: 'blur' },
    { type: 'email', message: '请输入有效的邮箱地址', trigger: ['blur', 'change'] },
  ],
  employeeId: [
    { required: true, message: '工号不能为空', trigger: 'blur' },
    // { pattern: /^[0-9]{6}$/, message: '工号必须是6位数字', trigger: 'blur' },
  ],
  role: [
    { required: true, message: '请选择角色', trigger: 'change' },
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' },
  ],
};