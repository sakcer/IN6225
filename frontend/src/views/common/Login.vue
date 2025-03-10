<template>
  <div class="login-container">
    <div class="login-content">
      <div class="login-header text-center mb-8">
        <!-- <img src="@/assets/logo.png" alt="Logo" class="mx-auto mb-4 h-12" /> -->
        <h1 class="text-2xl font-bold text-gray-800">员工管理系统</h1>
        <p class="text-gray-500 mt-2">欢迎回来，请登录您的账号</p>
      </div>

      <el-form
        ref="loginForm"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="用户名/工号"
            prefix-icon="User"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="密码"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>

        <div class="flex justify-between items-center mb-4">
          <el-checkbox v-model="rememberMe">记住我</el-checkbox>
          <el-button type="text" @click="handleForgotPassword">
            忘记密码？
          </el-button>
        </div>

        <el-form-item>
          <el-button
            type="primary"
            class="w-full"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>

      <div class="text-center text-gray-500 text-sm">
        <p>首次登录请使用工号作为用户名和初始密码</p>
        <p class="mt-2">如有问题请联系系统管理员</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'

const router = useRouter()

// 登录表单
const loginForm = ref({
  username: '',
  password: ''
})

// 表单验证规则
const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ]
}

// 其他状态
const loading = ref(false)
const rememberMe = ref(false)

// 处理登录
const handleLogin = async () => {
  loading.value = true
  try {
    // 模拟登录请求
    await new Promise(resolve => setTimeout(resolve, 1000))

    // 模拟登录成功
    const userRole = loginForm.value.username.includes('admin') ? 'admin' : 'employee'
    
    // 存储token和用户角色
    localStorage.setItem('token', 'mock-token')
    localStorage.setItem('userRole', userRole)

    ElMessage.success('登录成功')

    // 根据角色跳转到不同的首页
    if (userRole === 'admin') {
      router.push('/admin/dashboard')
    } else {
      router.push('/employee/dashboard')
    }
  } catch (error) {
    ElMessage.error('登录失败，请检查用户名和密码')
  } finally {
    loading.value = false
  }
}

// 处理忘记密码
const handleForgotPassword = () => {
  ElMessage.info('请联系系统管理员重置密码')
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #1890ff 0%, #36cfc9 100%);
}

.login-content {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.login-form {
  margin-bottom: 24px;
}

:deep(.el-input__wrapper) {
  padding-left: 11px;
}

:deep(.el-input__prefix) {
  left: 12px;
}
</style> 