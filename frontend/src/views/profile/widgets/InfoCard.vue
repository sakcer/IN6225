<template>
  <el-card shadow="hover" class="mb-4">
    <template #header>
      <div class="flex items-center">
        <el-icon class="mr-2"><Lock /></el-icon>
        个人信息
      </div>
    </template>
    <div class="space-y-4">
      <!-- <div class="flex justify-between items-center"> -->
      <div class="flex flex-col md:flex-row space-y-2 md:space-y-0 md:space-x-6 min-h-[36px] leading-5">
        <span>Name</span>
        <div class="flex-1">
            <div class="flex items-center">
                <span class="text-gray-600">{{ userInfo.email }}</span>
            </div>

        </div>
        <el-button class="ml-auto" link type="primary" @click="showPasswordDialog = true">
          修改
        </el-button>
      </div>
      <!-- <div class="flex justify-between items-center"> -->
      <div class="flex flex-col md:flex-row space-y-2 md:space-y-0 md:space-x-6 min-h-[36px] leading-5">
        <span>Description</span>
        <div class="flex-1">
            <div class="flex items-center">
                <span class="text-gray-600">{{ userInfo.email }}</span>
            </div>

        </div>
        <el-tag type="success" effect="plain">已绑定</el-tag>
      </div>
      <div class="flex justify-between items-center">
        <span>邮箱绑定</span>
        <el-tag type="success" effect="plain">已绑定</el-tag>
      </div>
      <div class="flex justify-between items-center">
        <span>双因素认证</span>
        <el-switch v-model="userInfo.twoFactorAuth" />
      </div>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import type { Employee } from '@/utils/types'

const props = defineProps({
  userInfo: {
    type: Object as PropType<Employee>,
    required: true,
  },
})

const showPasswordDialog = defineModel<boolean>('showPasswordDialog', { required: true })

</script>
