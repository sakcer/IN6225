<template>
    <el-dialog v-model="dialogVisible" title="Edit Personal Information" width="50%" @close="handleCancel">
      <el-form ref="formRef" :model="form" :rules="infoRules" label-width="100px">
        <!-- <el-form-item label="头像">
          <el-upload
            class="avatar-uploader"
            action="#"
            :show-file-list="false"
            :auto-upload="false"
            :on-change="handleAvatarChange"
          >
            <el-avatar v-if="form.avatar" :size="100" :src="form.avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item> -->
        <el-form-item label="Name" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="Title" prop="title">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="Email" prop="email">
          <el-input v-model="form.email" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleCancel">Cancel</el-button>
          <el-button type="primary" @click="handleSave">Save</el-button>
        </span>
      </template>
    </el-dialog>
</template>

<script setup lang="ts">
// Import necessary libraries and types
import { ref } from 'vue'
import type { ElForm } from 'element-plus'
import { infoRules } from '@/utils/constants/rules'
import type { Employee } from '@/utils/types/employee'
// Define props for the component
const props = defineProps<{
    form: Employee // Employee data for the form
}>()

const dialogVisible = defineModel<boolean>('modelValue', { required: true }) // Dialog visibility state
const formRef = ref<InstanceType<typeof ElForm>>() // Reference to the form

const emit = defineEmits(['save-info', 'cancel-info']); // Define emitted events

// Handle cancel action
const handleCancel = () => {
  formRef.value?.resetFields() // Reset form fields
  emit('cancel-info', props.form) // Emit cancel event with form data
}

// Handle save action
const handleSave = () => {
  formRef.value?.validate((valid) => {
    if (valid) {
      emit('save-info', props.form) // Emit save event with form data
    }
  })
}

</script>

<style scoped>
/* .profile-card {
  .el-divider {
    margin: 20px 0;
  }
} */

.avatar-uploader {
  text-align: center;
  
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 100px;
    height: 100px;
    border: 1px dashed #d9d9d9;
    border-radius: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
    
    &:hover {
      border-color: var(--el-color-primary);
      color: var(--el-color-primary);
    }
  }
}
</style>
