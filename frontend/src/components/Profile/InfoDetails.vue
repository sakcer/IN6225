<template>
<el-card shadow="hover" class="mb-6">
          <template #header>
            <div class="flex justify-between items-center">
              <span class="font-bold">Personal Information</span>
              <div class="flex">
                <el-button type="primary" @click="emitEvent('edit-info', employee)">
                  Edit Info
                </el-button>
                <el-button type="primary" @click="emitEvent('edit-password', employee)">
                  Edit Password
                </el-button>
              </div>
            </div>
          </template>

          <el-descriptions :column="2" border>
            <el-descriptions-item label="Name">
              {{ employee.name }}
            </el-descriptions-item>
            <el-descriptions-item label="Employee ID">
              {{ employee.employeeId }}
            </el-descriptions-item>
            <el-descriptions-item label="Department">
              {{ employee.department }}
            </el-descriptions-item>
            <el-descriptions-item label="Title">
              {{ employee.title || 'N/A' }}
            </el-descriptions-item>
            <el-descriptions-item label="Role">
              {{ employee.role }}
            </el-descriptions-item>
            <el-descriptions-item label="Status">
              <el-tag :type="employee.status === USER_STATUS.ACTIVE ? 'success' : 'info'">
                {{ employee.status }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="Email" :span="2">
              {{ employee.email }}
            </el-descriptions-item>
            <el-descriptions-item label="Join Date" :span="2">
              {{ employee.joinDate?.split('T')[0] || 'N/A' }}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
</template>

<script setup lang="ts">
import type { Employee } from '@/utils/types/employee'
import { USER_STATUS } from '@/utils/constants'


defineProps<{
    employee: Employee
}>()

const emit = defineEmits(['edit-info', 'edit-password']);

const emitEvent = (event: 'edit-info' | 'edit-password', row: Employee) => {
    emit(event, row)
}

</script>
