<template>
  <div class="user-info">
    <!-- 面包屑导航 -->
    <Breadcrumb label="Profile" />

    <h1 class="text-3xl font-bold mb-6">Profile</h1>

    <el-col>
      <!-- 左侧个人信息卡片 -->
      <info-card v-model:showInfoDialog="showInfoDialog"/>

      <!-- 账号安全卡片 -->
      <security-card v-model:showPasswordDialog="showPasswordDialog"/>

      <!-- 通知设置卡片 -->
    </el-col>

    <!-- 修改密码对话框 -->
    <password-form v-model="showPasswordDialog" />

    <!-- 个人信息表单对话框 -->
    <info-form v-if="showInfoDialog" v-model="showInfoDialog" />
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

<script setup lang="ts">
import Breadcrumb from '@/components/Breadcrumb.vue'
import SecurityCard from './widgets/SecurityCard.vue'
import PasswordForm from './widgets/PasswordForm.vue'
import InfoCard from './widgets/InfoCard.vue'
import InfoForm from './widgets/InfoForm.vue'
import { ref } from 'vue'

const showPasswordDialog = ref(false)
const showInfoDialog = ref(false)
</script>
