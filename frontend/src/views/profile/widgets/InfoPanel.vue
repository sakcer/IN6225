<template>
  <el-card shadow="hover" class="mb-4">
    <template #header>
    <div class="flex">
        <div class="mr-4">
            <el-upload
            class="avatar-uploader"
            action="#"
            :show-file-list="false"
            :before-upload="beforeAvatarUpload"
            :http-request="handleAvatarUpload"
            >
            <div class="relative group cursor-pointer">
                <el-avatar 
                :size="80"
                :src="userInfo.avatar"
                :style="{ backgroundColor: getAvatarColor(userInfo.name) }"
                >
                {{ getAvatarText(userInfo.name) }}
                </el-avatar>
                <div class="absolute inset-0 flex items-center justify-center bg-black bg-opacity-50 rounded-full opacity-0 group-hover:opacity-100 transition-opacity">
                <el-icon class="text-white text-xl"><Camera /></el-icon>
                </div>
            </div>
            </el-upload>
        </div>

        <div class="flex flex-col justify-center">
            <h2 class="text-[28px] md:text-[32px] font-bold">{{ userInfo.name }}</h2>

            <div class="flex text-[13px] space-x-1">
                <p>Position in</p>
                <p>{{ userInfo.role }}</p>
            </div>
        </div>
    </div>
    </template>

    <div class="flex justify-between">
        <div>
        <div class="flex items-center mb-4">
            <el-icon class="mr-2"><Message /></el-icon>
            <span class="text-gray-600">{{ userInfo.email }}</span>
        </div>
        <div class="flex items-center mb-4">
            <el-icon class="mr-2"><Phone /></el-icon>
            <span class="text-gray-600">{{ userInfo.phone }}</span>
        </div>
        <div class="flex items-center mb-4">
            <el-icon class="mr-2"><Location /></el-icon>
            <span class="text-gray-600">{{ userInfo.location }}</span>
        </div>
        <div class="flex items-center">
            <el-icon class="mr-2"><Calendar /></el-icon>
            <span class="text-gray-600">加入时间：{{ userInfo.joinDate }}</span>
        </div>
        </div>
        <el-button class="mb-auto" link type="primary" @click="showPasswordDialog = true">
          修改
        </el-button>
    </div>

  </el-card>
</template>

<script setup lang="ts">
import type { Employee } from '@/utils/types'
import { getAvatarColor, getAvatarText } from '@/utils/avatar'
import { Edit } from '@element-plus/icons-vue'

const props = defineProps({
  userInfo: Object as PropType<Employee>,
  beforeAvatarUpload: Function,
  handleAvatarUpload: Function,
})

const showPasswordDialog = defineModel<boolean>('showPasswordDialog', { required: true })
</script>