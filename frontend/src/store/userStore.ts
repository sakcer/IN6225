import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Employee } from '@/utils/types'
import { employeeService } from '@/services/employees/employeeService'

export const useUserStore = defineStore('user', () => {
  const users = ref<Employee[]>([
    { id: 1, name: 'John Doe', role: 'Developer' },
    { id: 2, name: 'Jane Smith', role: 'Designer' },
    { id: 3, name: 'Mike Johnson', role: 'Manager' },
    { id: 4, name: 'Sarah Williams', role: 'Developer' }
  ])

  // 获取用户列表
  async function fetchUsers() {
    try {
      const { employees } = await employeeService.getAllEmployees();
      users.value = employees;
    } catch (error) {
      ElMessage.error('获取用户列表失败');
      console.error(error);
    } finally {
    //   loading.value = false;
    }
  }

  fetchUsers();

  return { users, fetchUsers }
})
