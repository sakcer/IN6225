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
                :src="userInfo?.avatar"
                :style="{ backgroundColor: getAvatarColor(userInfo?.name || ''), fontSize: '30px' }"
              >
                {{ getAvatarText(userInfo?.name || '') }}
              </el-avatar>
              <div class="absolute inset-0 flex items-center justify-center bg-black bg-opacity-50 rounded-full opacity-0 group-hover:opacity-100 transition-opacity">
                <el-icon class="text-white text-xl"><Camera /></el-icon>
              </div>
            </div>
          </el-upload>
        </div>

        <div class="flex flex-col justify-center">
          <h2 class="text-[28px] md:text-[32px] font-bold">{{ userInfo?.name }}</h2>

          <div class="flex text-[13px] space-x-1">
            <p>Position in</p>
            <p>{{ userInfo?.role }}</p>
          </div>
        </div>
      </div>
    </template>

    <div class="flex justify-between">
      <div>
        <div class="flex items-center mb-4">
          <el-icon class="mr-2"><User /></el-icon>
          <span class="text-gray-600">{{ userInfo?.name }}</span>
        </div>
        <div class="flex items-center mb-4">
          <el-icon class="mr-2"><Key /></el-icon>
          <span class="text-gray-600">{{ userInfo?.employeeId }}</span>
        </div>
        <div class="flex items-center mb-4">
          <el-icon class="mr-2"><Briefcase /></el-icon>
          <span class="text-gray-600">{{ userInfo?.title }}</span>
        </div>
        <div class="flex items-center mb-4">
          <el-icon class="mr-2"><OfficeBuilding /></el-icon>
          <span class="text-gray-600">{{ userInfo?.department }}</span>
        </div>
        <div class="flex items-center">
          <el-icon class="mr-2"><Calendar /></el-icon>
          <span class="text-gray-600">加入时间：{{ new Date(userInfo?.joinDate || '').toLocaleString('zh-CN', { timeZone: 'Asia/Shanghai' }).split(' ')[0] }}</span>
        </div>
      </div>
      <el-button class="mb-auto" link type="primary" @click="showInfoDialog = true">
        修改
      </el-button>
    </div>

  </el-card>
</template>

<script setup lang="ts">
import { getAvatarColor, getAvatarText } from '@/utils/avatar'
import { Camera, User, Key, Briefcase, OfficeBuilding, Calendar } from '@element-plus/icons-vue'
import { useMeStore } from '@/store/meStore'
import { defineModel, computed } from 'vue'

const meStore = useMeStore();
const userInfo = computed(() => meStore.getMe);

const showInfoDialog = defineModel<boolean>('showInfoDialog', { required: true })

const beforeAvatarUpload = (rawFile: File) => {
    console.log(rawFile)
}

const handleAvatarUpload = (rawFile: File) => {
  console.log(rawFile)
}
</script>
