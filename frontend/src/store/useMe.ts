import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Employee } from '@/utils/types'
import { employeeService } from '@/services/employees/employeeService'

export const useMeStore = defineStore('me', () => {
  const me = ref<Employee>(
    { id: 1, name: 'IN6225', role: 'Developer', email: 'john.doe@example.com', phone: '13800138000', location: '上海市', joinDate: '2023-01-01', avatar: '', twoFactorAuth: true },
  )

  return { me }
})
