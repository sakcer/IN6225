<template>
<el-card shadow="hover" class="mb-6">
          <template #header>
            <div class="flex justify-between items-center">
              <span class="font-bold">个人信息</span>
              <el-button type="primary" @click="emit('edit-info', employee)">
                编辑信息
              </el-button>
            </div>
          </template>

          <el-descriptions :column="2" border>
            <el-descriptions-item label="姓名">
              {{ employee.name }}
            </el-descriptions-item>
            <el-descriptions-item label="工号">
              {{ employee.employeeId }}
            </el-descriptions-item>
            <el-descriptions-item label="部门">
              {{ employee.department }}
            </el-descriptions-item>
            <el-descriptions-item label="职位">
              {{ employee.title || '暂无' }}
            </el-descriptions-item>
            <el-descriptions-item label="角色">
              {{ employee.role }}
            </el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="getStatusType(employee.status)">
                {{ employee.status }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="邮箱" :span="2">
              {{ employee.email }}
            </el-descriptions-item>
            <el-descriptions-item label="入职时间" :span="2">
              {{ employee.joinDate || '暂无' }}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
</template>

<script setup lang="ts">
import type { Employee } from '@/utils/types/employee'
import { ref } from 'vue'

const props = defineProps<{
    employee: Employee
}>()

const getStatusType = (status: string) => {
    const types = {
        'ACTIVE': 'success',
        'INACTIVE': 'info',
        'SUSPENDED': 'warning',
        'TERMINATED': 'danger'
    }
    return types[status] || 'info'
}

const emit = defineEmits(['edit-info']);

const handleEdit = (row) => {
    emit('edit-info', row)
}

</script>
