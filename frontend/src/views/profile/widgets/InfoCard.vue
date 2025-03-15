<template>
    <el-card shadow="hover" class="profile-card">
        <div class="text-center">
            <el-avatar :size="120" :src="employee.avatar" class="mb-4"
                :style="{ backgroundColor: getAvatarColor(employee.name), fontSize: '30px' }">
                {{ getAvatarText(employee.name) }}
            </el-avatar>
            <h2 class="text-xl font-bold mb-2">{{ employee.name }}</h2>
            <el-tag class="mb-4" :type="employee.status === USER_STATUS.ACTIVE ? 'success' : 'info'">
                {{ employee.status }}
            </el-tag>
            <div class="text-gray-600 mb-2">{{ employee.title || employee.role }}</div>
            <div class="text-gray-500 text-sm">{{ employee.department }}</div>
        </div>

        <el-divider />

        <div class="space-y-4">
            <div class="flex items-center">
                <el-icon class="mr-2">
                    <Message />
                </el-icon>
                <span class="text-gray-600">{{ employee.email }}</span>
            </div>
            <div class="flex items-center">
                <el-icon class="mr-2">
                    <User />
                </el-icon>
                <span class="text-gray-600">工号：{{ employee.employeeId }}</span>
            </div>
            <div class="flex items-center">
                <el-icon class="mr-2">
                    <Calendar />
                </el-icon>
                <span class="text-gray-600">入职时间：{{ employee.joinDate?.split('T')[0] || '暂无' }}</span>
            </div>
        </div>
    </el-card>
</template>

<script setup lang="ts">
import { getAvatarColor, getAvatarText } from '@/utils/avatar'
import { Message, User, Calendar } from '@element-plus/icons-vue'
import type { Employee } from '@/utils/types/employee'
import { USER_STATUS } from '@/utils/constants'

const props = defineProps<{
    employee: Employee
}>()

</script>
