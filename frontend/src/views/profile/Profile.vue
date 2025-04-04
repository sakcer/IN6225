<template>
  <div class="container mx-auto p-6">
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- Left side personal information card -->
      <div class="lg:col-span-1">
        <info-card v-if="meDetails" :employee="meDetails" as Employee />
      </div>

      <!-- Right side detailed information -->
      <div class="lg:col-span-2">
        <info-details v-if="meDetails" :employee="meDetails" as Employee @edit-info="handleEditInfo" @edit-password="handleEditPassword" />

        <!-- Additional cards can be added here, such as recent activities, skill tags, etc. -->
        <el-card shadow="hover">
          <template #header>
            <div class="font-bold">Recent Activities</div>
          </template>
          <el-empty description="No activity records available" />
        </el-card>
      </div>
    </div>

    <!-- Edit dialog -->
    <info-form v-model="dialogVisible" v-if="form" :form="form" as Employee @save-info="handleSave" @cancel-info="handleCancel" />
    <password-form v-model="passwordDialogVisible" @save-password="handleSavePassword" @cancel-password="handleCancelPassword" />

  </div>
</template>

<script setup lang="ts">
// Import necessary libraries and components
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/store/userStore'
import InfoCard from '@/components/Profile/InfoCard.vue'
import InfoDetails from '@/components/Profile/InfoDetails.vue'
import InfoForm from '@/components/Profile/InfoForm.vue'
import PasswordForm from '@/components/Profile/PasswordForm.vue'
import { employeeService } from '@/services/employees/employeeService'
import { ElMessage } from 'element-plus'
import type { Employee } from '@/utils/types/employee'
import { handleAxiosError } from '@/utils/errorMsg'

// Store reference for current user
const meStore = useUserStore()
const meDetails = computed(() => meStore.getMe) // Get current employee data

const dialogVisible = ref(false) // State for dialog visibility
const form = ref({} as Employee) // Form data for editing employee information
const passwordDialogVisible = ref(false) // State for password dialog visibility

// Handle edit action
const handleEditInfo = (employee: Employee) => {
  form.value = { ...employee } // Populate form with employee data
  dialogVisible.value = true // Show the dialog
}

const handleEditPassword = () => {
  passwordDialogVisible.value = true
}

// Handle cancel action
const handleCancel = () => {
  dialogVisible.value = false // Hide the dialog
}

// Handle save action
const handleSave = async () => {
  console.log("Handling save")
  console.log(form.value)
  try {
    const data = await employeeService.updateEmployee(form.value as Employee) // Update employee data
    meStore.fetchUserInfo() // Refresh current user data
    dialogVisible.value = false // Hide the dialog
    ElMessage.success(data.message) // Show success message
  } catch (error) {
    handleAxiosError(error) // Handle error
  }
}

const handleSavePassword = async (passwordData: any) => {
  console.log("Handling save password")
  console.log(passwordData)
  try {
    const data = await employeeService.updateEmployeePassword(meDetails.value?.id, passwordData)
    passwordDialogVisible.value = false
    ElMessage.success(data.message)
  } catch (error) {
    handleAxiosError(error) // Handle error
  }
}

const handleCancelPassword = () => {
  passwordDialogVisible.value = false
}

// Fetch current user data on component mount
onMounted(() => {
  meStore.fetchUserInfo()
})
</script>
