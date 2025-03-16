<template>
  <div class="min-h-screen flex flex-col justify-center items-center bg-gray-50 px-4">
    <div class="max-w-lg w-full text-center">
      <!-- 404 Image -->
      <img 
        src="@/assets/vue.svg" 
        alt="404" 
        class="w-full max-w-md mx-auto mb-8 animate-float"
      />

      <!-- Error Message -->
      <h1 class="text-6xl font-bold text-gray-800 mb-4">
        404
      </h1>
      <h2 class="text-2xl font-medium text-gray-600 mb-8">
        Sorry, the page you are looking for does not exist
      </h2>

      <!-- Hint Text -->
      <p class="text-gray-500 mb-8">
        The page may have been moved, deleted, or temporarily unavailable
      </p>

      <!-- Button Group -->
      <div class="flex flex-col sm:flex-row justify-center items-center gap-4">
        <el-button 
          type="primary" 
          size="large"
          class="w-full sm:w-auto"
          @click="goBack"
        >
          <template #icon>
            <el-icon class="mr-1"><ArrowLeft /></el-icon>
          </template>
          Go Back
        </el-button>

        <el-button 
          size="large"
          class="w-full sm:w-auto"
          @click="goHome"
        >
          <template #icon>
            <el-icon class="mr-1"><House /></el-icon>
          </template>
          Go Home
        </el-button>
      </div>

      <!-- Additional Help -->
      <div class="mt-12 text-gray-500">
        <p>Need help? Please contact</p>
        <el-button type="primary" link class="mt-1">
          <template #icon>
            <el-icon><Service /></el-icon>
          </template>
          System Administrator
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// Import necessary libraries
import { useRouter } from 'vue-router'
import { ArrowLeft, House, Service } from '@element-plus/icons-vue'
import { USER_ROLES } from '@/utils/constants'
const router = useRouter()

// Go back to the previous page
const goBack = () => {
  router.back()
}

// Go back to the home page
const goHome = () => {
  const userInfo = localStorage.getItem('userInfo')
  let redirectPath = '/login'

  if (userInfo) {
    try {
      const { role } = JSON.parse(userInfo)
      redirectPath = role === USER_ROLES.ADMIN ? '/admin/dashboard' : '/employee/dashboard'
    } catch (error) {
      console.error('Failed to parse user information:', error)
    }
  }

  router.push(redirectPath)
}
</script>

<style>
/* Add floating animation effect */
@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

.animate-float {
  animation: float 3s ease-in-out infinite;
}
</style> 