<script setup lang="ts">
import InfoCard from './widgets/InfoCard.vue'
import Breadcrumb from '@/components/Breadcrumb.vue'
import InfoPanel from './widgets/InfoPanel.vue'
import SecurityCard from './widgets/SecurityCard.vue'
import PasswordForm from './widgets/PasswordForm.vue'
import { useMeStore } from '@/store/useMe'

import { ref } from 'vue'
import { 
  Message, Phone, Location, Calendar, Lock, Bell, Camera,
  Link
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'

// 用户信息
const userInfo = ref({
  name: 'John Doe',
  role: '高级开发工程师',
  email: 'john.doe@example.com',
  phone: '13800138000',
  location: '上海市',
  joinDate: '2023-01-01',
  avatar: '',
  twoFactorAuth: true
})

const { me } = useMeStore()


// 通知设置
const notifications = ref({
  system: true,
  project: true,
  task: true,
  email: true
})

// 表单数据
const form = ref({
  name: userInfo.value.name,
  nickname: 'Johnny',
  email: userInfo.value.email,
  phone: userInfo.value.phone,
  gender: 'male',
  birthday: '',
  location: ['上海市', '市辖区', '浦东新区'],
  bio: '热爱技术，专注于前端开发，有5年以上开发经验...',
  skills: ['Vue', 'React', 'TypeScript', 'Node.js'],
  social: {
    github: 'https://github.com/johndoe',
    linkedin: 'https://linkedin.com/in/johndoe',
    twitter: 'https://twitter.com/johndoe'
  }
})

// 修改密码表单
const passwordForm = ref({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 状态变量
const formRef = ref<FormInstance>()
const passwordFormRef = ref<FormInstance>()
const showPasswordDialog = ref(false)
const saving = ref(false)
const changingPassword = ref(false)

// 技能选项
const skillOptions = [
  'Vue', 'React', 'Angular', 'TypeScript', 'JavaScript',
  'Node.js', 'Python', 'Java', 'Go', 'Docker', 'Kubernetes'
]

// 地区选项（示例数据）
const locationOptions = [
  {
    value: '上海市',
    label: '上海市',
    children: [
      {
        value: '市辖区',
        label: '市辖区',
        children: [
          { value: '浦东新区', label: '浦东新区' },
          { value: '黄浦区', label: '黄浦区' }
        ]
      }
    ]
  }
]

// 表单验证规则
const rules: FormRules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号码', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
}

// 密码验证规则
const passwordRules: FormRules = {
  currentPassword: [
    { required: true, message: '请输入当前密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.value.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 头像上传前的验证
const beforeAvatarUpload = (file: File) => {
  const isJPG = file.type === 'image/jpeg'
  const isPNG = file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG && !isPNG) {
    ElMessage.error('头像只能是 JPG 或 PNG 格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('头像大小不能超过 2MB!')
    return false
  }
  return true
}

// 处理头像上传
const handleAvatarUpload = async (options: any) => {
  try {
    // 这里应该调用上传API
    ElMessage.success('头像上传成功')
  } catch (error) {
    ElMessage.error('头像上传失败')
  }
}

// 获取头像颜色（复用之前的方法）
const getAvatarColor = (name: string) => {
  const colors = ['#f56c6c', '#e6a23c', '#67c23a', '#409eff', '#909399']
  let hash = 0
  for (let i = 0; i < name.length; i++) {
    hash = name.charCodeAt(i) + ((hash << 5) - hash)
  }
  return colors[Math.abs(hash) % colors.length]
}

// 获取头像文本（复用之前的方法）
const getAvatarText = (name: string) => {
  if (!name) return ''
  const parts = name.split(' ')
  if (parts.length >= 2) {
    return (parts[0][0] + parts[1][0]).toUpperCase()
  }
  return name.slice(0, 2).toUpperCase()
}

// 保存个人信息
const handleSaveProfile = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      saving.value = true
      try {
        // 这里应该调用API保存数据
        await new Promise(resolve => setTimeout(resolve, 1000))
        ElMessage.success('保存成功')
      } catch (error) {
        ElMessage.error('保存失败')
      } finally {
        saving.value = false
      }
    }
  })
}

// 修改密码
const handleChangePassword = async () => {
  if (!passwordFormRef.value) return

  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      changingPassword.value = true
      try {
        // 这里应该调用API修改密码
        await new Promise(resolve => setTimeout(resolve, 1000))
        ElMessage.success('密码修改成功')
        showPasswordDialog.value = false
        passwordForm.value = {
          currentPassword: '',
          newPassword: '',
          confirmPassword: ''
        }
      } catch (error) {
        ElMessage.error('密码修改失败')
      } finally {
        changingPassword.value = false
      }
    }
  })
}
</script>

<template>
  <div class="user-info">
    <!-- 面包屑导航 -->
    <Breadcrumb label="Profile" />

    <h1 class="text-3xl font-bold mb-6">Profile</h1>

    <!-- <el-row :gutter="20"> -->
      <!-- 左侧个人信息卡片 -->
      <el-col >
        <info-panel :user-info="me" v-model:showPasswordDialog="showPasswordDialog" />

        <!-- 账号安全卡片 -->
        <!-- <info-card :user-info="me" v-model:showPasswordDialog="showPasswordDialog"/> -->

        <!-- 账号安全卡片 -->
        <security-card :user-info="me" v-model:showPasswordDialog="showPasswordDialog"/>

        <!-- 通知设置卡片 -->
      </el-col>

      <!-- 右侧详细信息表单 -->


      <!-- <el-col :span="16">
        <el-card shadow="hover">
          <template #header>
            <div class="flex justify-between items-center">
              <span class="text-lg font-bold">详细信息</span>
              <div>
                <el-button type="primary" @click="handleSaveProfile" :loading="saving">
                  保存修改
                </el-button>
              </div>
            </div>
          </template>

          <el-form
            ref="formRef"
            :model="form"
            :rules="rules"
            label-width="100px"
          >
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="姓名" prop="name">
                  <el-input v-model="form.name" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="昵称" prop="nickname">
                  <el-input v-model="form.nickname" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="邮箱" prop="email">
                  <el-input v-model="form.email" type="email" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="手机" prop="phone">
                  <el-input v-model="form.phone" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="性别" prop="gender">
                  <el-select v-model="form.gender" class="w-full">
                    <el-option label="男" value="male" />
                    <el-option label="女" value="female" />
                    <el-option label="保密" value="other" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="生日" prop="birthday">
                  <el-date-picker
                    v-model="form.birthday"
                    type="date"
                    class="w-full"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item label="所在地" prop="location">
              <el-cascader
                v-model="form.location"
                :options="locationOptions"
                class="w-full"
              />
            </el-form-item>

            <el-form-item label="个人简介" prop="bio">
              <el-input
                v-model="form.bio"
                type="textarea"
                :rows="4"
                placeholder="介绍一下自己..."
              />
            </el-form-item>

            <el-form-item label="技能标签" prop="skills">
              <el-select
                v-model="form.skills"
                multiple
                filterable
                allow-create
                default-first-option
                class="w-full"
                placeholder="请选择或输入技能标签"
              >
                <el-option
                  v-for="skill in skillOptions"
                  :key="skill"
                  :label="skill"
                  :value="skill"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="社交账号">
              <div class="space-y-4">
                <div class="flex items-center">
                  <el-icon class="mr-2"><Link /></el-icon>
                  <el-input v-model="form.social.github" placeholder="GitHub" />
                </div>
                <div class="flex items-center">
                  <el-icon class="mr-2"><Link /></el-icon>
                  <el-input v-model="form.social.linkedin" placeholder="LinkedIn" />
                </div>
                <div class="flex items-center">
                  <el-icon class="mr-2"><Link /></el-icon>
                  <el-input v-model="form.social.twitter" placeholder="Twitter" />
                </div>
              </div>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col> -->
    <!-- </el-row> -->

    <!-- 修改密码对话框 -->
    <password-form 
      v-model="showPasswordDialog"
      v-model:passwordFormRef="passwordFormRef"
      :rules="passwordRules" 
      :changingPassword="changingPassword"
      :handleChangePassword="handleChangePassword" 
      :passwordForm="passwordForm" />

  </div>
</template>


<style scoped>
.avatar-uploader {
  display: inline-block;
}

.avatar-uploader :deep(.el-upload) {
  cursor: pointer;
}

.el-card {
  margin-bottom: 1rem;
}

.el-form-item:last-child {
  margin-bottom: 0;
}
</style> 