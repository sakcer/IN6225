<template>
  <div class="not-found">
    <div class="content">
      <!-- <img src="@/assets/404.png" alt="404" class="mb-8" /> -->
      <h1 class="text-4xl font-bold text-gray-800 mb-4">404</h1>
      <p class="text-gray-600 mb-8">抱歉，您访问的页面不存在</p>
      <div class="flex gap-4">
        <el-button type="primary" @click="goBack">
          返回上一页
        </el-button>
        <el-button @click="goHome">
          返回首页
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const goBack = () => {
  router.back()
}

const goHome = () => {
  const userRole = localStorage.getItem('userRole')
  if (userRole === 'admin') {
    router.push('/admin/dashboard')
  } else {
    router.push('/employee/dashboard')
  }
}
</script>

<style scoped>
.not-found {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f0f2f5;
}

.content {
  text-align: center;
  padding: 40px;
}

.content img {
  max-width: 400px;
  height: auto;
}
</style> 