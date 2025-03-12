<template>
  <div class="min-h-screen flex flex-col justify-center items-center bg-gray-50 px-4">
    <div class="max-w-lg w-full text-center">
      <!-- 404图片 -->
      <img 
        src="@/assets/vue.svg" 
        alt="404" 
        class="w-full max-w-md mx-auto mb-8 animate-float"
      />

      <!-- 错误信息 -->
      <h1 class="text-6xl font-bold text-gray-800 mb-4">
        404
      </h1>
      <h2 class="text-2xl font-medium text-gray-600 mb-8">
        抱歉，您访问的页面不存在
      </h2>

      <!-- 提示文本 -->
      <p class="text-gray-500 mb-8">
        页面可能已被移动、删除或暂时无法访问
      </p>

      <!-- 按钮组 -->
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
          返回上一页
        </el-button>

        <el-button 
          size="large"
          class="w-full sm:w-auto"
          @click="goHome"
        >
          <template #icon>
            <el-icon class="mr-1"><House /></el-icon>
          </template>
          返回首页
        </el-button>
      </div>

      <!-- 额外帮助 -->
      <div class="mt-12 text-gray-500">
        <p>需要帮助？请联系</p>
        <el-button type="primary" link class="mt-1">
          <template #icon>
            <el-icon><Service /></el-icon>
          </template>
          系统管理员
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ArrowLeft, House, Service } from '@element-plus/icons-vue'
import { USER_ROLES } from '@/utils/constants'
const router = useRouter()

// 返回上一页
const goBack = () => {
  router.back()
}

// 返回首页
const goHome = () => {
  const userInfo = localStorage.getItem('userInfo')
  let redirectPath = '/login'

  if (userInfo) {
    try {
      const { role } = JSON.parse(userInfo)
      redirectPath = role === USER_ROLES.ADMIN ? '/admin/dashboard' : '/employee/dashboard'
    } catch (error) {
      console.error('解析用户信息失败:', error)
    }
  }

  router.push(redirectPath)
}
</script>

<style>
/* 仅添加浮动动画效果 */
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