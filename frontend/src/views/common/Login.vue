<template>
  <div class="min-h-screen flex flex-col justify-center items-center bg-gray-50">
    <div class="w-full max-w-md mx-4 md:mx-auto p-8 bg-white rounded-lg shadow-lg">
      <!-- Logo和标题 -->
      <div class="text-center mb-8">
        <img src="@/assets/vue.svg" alt="Logo" class="mx-auto h-12 mb-4" />
        <h1 class="text-2xl font-bold text-gray-800">员工管理系统</h1>
        <p class="text-gray-500 mt-2">欢迎回来，请登录您的账号</p>
      </div>

      <!-- 登录表单 -->
      <el-form
        ref="formRef"
        :model="loginForm"
        :rules="rules"
        class="space-y-6"
        @keyup.enter="handleLogin"
      >
        <!-- 用户名输入框 -->
        <el-form-item prop="employeeId">
          <el-input
            v-model="loginForm.employeeId"
            placeholder="请输入员工号"
            :prefix-icon="User"
            size="large"
            class="w-full"
          />
        </el-form-item>

        <!-- 密码输入框 -->
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            :prefix-icon="Lock"
            size="large"
            show-password
            class="w-full"
          />
        </el-form-item>

        <!-- 记住我和忘记密码 -->
        <div class="flex justify-between items-center">
          <el-checkbox v-model="loginForm.remember">
            记住我
          </el-checkbox>
          <el-button type="primary" link @click="handleForgotPassword">
            忘记密码？
          </el-button>
        </div>

        <!-- 登录按钮 -->
        <el-button
          type="primary"
          class="w-full"
          size="large"
          :loading="loading"
          @click="handleLogin"
        >
          {{ loading ? '登录中...' : '登录' }}
        </el-button>

        <!-- 提示信息 -->
        <div class="text-center text-sm text-gray-500 space-y-2">
          <p>首次登录请使用员工号作为用户名和初始密码</p>
          <p>
            遇到问题？请联系
            <el-button type="primary" link class="px-0">系统管理员</el-button>
          </p>
        </div>
      </el-form>
    </div>

    <!-- 页脚 -->
    <div class="mt-8 text-center">
      <p class="text-gray-500 text-sm">
        © {{ new Date().getFullYear() }} 员工管理系统. All rights reserved.
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
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

const meStore = useMeStore()
const usersStore = useUsersStore()
const projectsStore = useProjectsStore()
const myProjectStore = useMyProjectStore()
const router = useRouter()
const formRef = ref<FormInstance>()
const loading = ref(false)


// 登录表单数据
const loginForm = reactive({
  employeeId: '',
  password: '',
  remember: false
})

// 表单验证规则
const rules = {
  employeeId: [
    { required: true, message: '请输入员工号', trigger: 'blur' },
    // { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    // { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ]
}

// 处理登录
const handleLogin = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    loading.value = true

    console.log(loginForm.employeeId, loginForm.password);
    const response = await authService.login(loginForm.employeeId, loginForm.password);
    console.log(response);

    // 根据角色跳转
    const isAdmin = response.user.role === USER_ROLES.ADMIN
    const redirectPath = isAdmin ? '/admin/dashboard' : '/employee/dashboard'
    router.push(redirectPath)

    
    if (isAdmin) {  
      await meStore.refetchMe()
      await usersStore.refetchUsers()
      await projectsStore.refetchProjects()
      // await myProjectStore.refetchProjects()
    } else {
      await meStore.refetchMe()
      await usersStore.refetchUsers()
      await myProjectStore.refetchProjects()
    }

    ElMessage.success('登录成功')
  } catch (error) {
    console.error('登录失败:', error)
    ElMessage.error('用户名或密码错误')
  } finally {
    loading.value = false
  }
}

// 处理忘记密码
const handleForgotPassword = () => {
  ElMessage.info('请联系系统管理员重置密码')
}
</script>