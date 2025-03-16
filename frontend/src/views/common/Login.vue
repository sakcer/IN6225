<template>
  <div class="min-h-screen flex flex-col justify-center items-center bg-gray-50">
    <div class="w-full max-w-md mx-4 md:mx-auto p-8 bg-white rounded-lg shadow-lg">
      <!-- Logo and Title -->
      <div class="text-center mb-8">
        <img src="@/assets/vue.svg" alt="Logo" class="mx-auto h-12 mb-4" />
        <h1 class="text-2xl font-bold text-gray-800">Employee Management System</h1>
        <p class="text-gray-500 mt-2">Welcome back, please log in to your account</p>
      </div>

      <!-- Login Form -->
      <el-form
        ref="formRef"
        :model="loginForm"
        :rules="rules"
        class="space-y-6"
        @keyup.enter="handleLogin"
      >
        <!-- Username Input -->
        <el-form-item prop="employeeId">
          <el-input
            v-model="loginForm.employeeId"
            placeholder="Enter Employee ID"
            :prefix-icon="User"
            size="large"
            class="w-full"
          />
        </el-form-item>

        <!-- Password Input -->
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="Enter Password"
            :prefix-icon="Lock"
            size="large"
            show-password
            class="w-full"
          />
        </el-form-item>

        <!-- Remember Me and Forgot Password -->
        <div class="flex justify-between items-center">
          <el-checkbox v-model="loginForm.remember">
            Remember Me
          </el-checkbox>
          <el-button type="primary" link @click="handleForgotPassword">
            Forgot Password?
          </el-button>
        </div>

        <!-- Login Button -->
        <el-button
          type="primary"
          class="w-full"
          size="large"
          :loading="loading"
          @click="handleLogin"
        >
          {{ loading ? 'Logging in...' : 'Login' }}
        </el-button>

        <!-- Hint Information -->
        <div class="text-center text-sm text-gray-500 space-y-2">
          <p>For first-time login, please use your Employee ID as the username and initial password</p>
          <p>
            Having issues? Please contact
            <el-button type="primary" link class="px-0">System Administrator</el-button>
          </p>
        </div>
      </el-form>
    </div>

    <!-- Footer -->
    <div class="mt-8 text-center">
      <p class="text-gray-500 text-sm">
        © {{ new Date().getFullYear() }} Employee Management System. All rights reserved.
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
// Import necessary libraries and services
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import type { FormInstance } from 'element-plus'
import { authService } from '@/services/auth/authService'
import { USER_ROLES } from '@/utils/constants'
import { useMeStore } from '@/store/meStore' 
import { useUsersStore } from '@/store/userStore'
import { useProjectsStore } from '@/store/projectStore'
import { useMyProjectStore } from '@/store/myProjectStore'

// Store references
const meStore = useMeStore()
const usersStore = useUsersStore()
const projectsStore = useProjectsStore()
const myProjectStore = useMyProjectStore()
const router = useRouter()
const formRef = ref<FormInstance>()
const loading = ref(false)

// Login form data
const loginForm = reactive({
  employeeId: '',
  password: '',
  remember: false
})

// Form validation rules
const rules = {
  employeeId: [
    { required: true, message: 'Please enter Employee ID', trigger: 'blur' },
  ],
  password: [
    { required: true, message: 'Please enter password', trigger: 'blur' },
  ]
}

// Handle login
const handleLogin = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    loading.value = true

    console.log(loginForm.employeeId, loginForm.password);
    const response = await authService.login(loginForm.employeeId, loginForm.password);
    console.log(response);

    // Redirect based on role
    const isAdmin = response.user.role === USER_ROLES.ADMIN
    const redirectPath = isAdmin ? '/admin/dashboard' : '/employee/dashboard'
    router.push(redirectPath)

    // Refresh data based on role
    if (isAdmin) {  
      await meStore.refetchMe()
      await usersStore.refetchUsers()
      await projectsStore.refetchProjects()
    } else {
      await meStore.refetchMe()
      await usersStore.refetchUsers()
      await myProjectStore.refetchProjects()
    }

    ElMessage.success('Login successful')
  } catch (error) {
    console.error('Login failed:', error)
    ElMessage.error('Incorrect username or password')
  } finally {
    loading.value = false
  }
}

// Handle forgot password
const handleForgotPassword = () => {
  ElMessage.info('Please contact the system administrator to reset your password')
}
</script>