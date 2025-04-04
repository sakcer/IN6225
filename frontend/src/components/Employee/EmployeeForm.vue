<template>
  <el-dialog :title="formType === FORM_TYPES.EDIT ? 'Edit Employee' : 'Add Employee'" v-model="dialogVisible" width="600px" @close="handleClose">
    <el-form ref="formRef" :model="form" label-width="100px" :rules="employeeRules">

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="Name" prop="name">
            <el-input v-model="form.name" placeholder="Enter name" />
          </el-form-item>
        </el-col>
        
        <el-col :span="12">
          <el-form-item label="Email" prop="email">
            <el-input v-model="form.email" placeholder="Enter email" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="EID" prop="employeeId">
            <el-input v-model="form.employeeId" placeholder="Enter employee ID" />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="Department" prop="department">
            <el-select v-model="form.department" placeholder="Select department">
              <el-option
                v-for="(value, key) in DEPARTMENTS"
                :key="key"
                :label="key"
                :value="value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- New position field -->
      <el-form-item label="Title" prop="title">
        <el-input v-model="form.title" placeholder="Enter title" />
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="Role" prop="role">
            <el-select v-model="form.role" placeholder="Select role" class="w-full">
              <el-option v-for="role in USER_ROLES" :key="role" :label="role" :value="role" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="Status" prop="status">
            <el-radio-group v-model="form.status">
              <el-radio :value="USER_STATUS.ACTIVE">Active</el-radio>
              <el-radio :value="USER_STATUS.INACTIVE">Inactive</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>

    </el-form>

    <template #footer>
      <div class="flex justify-end gap-2">
        <el-button @click="handleClose">Cancel</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="loading"> Confirm </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
// Import necessary constants and libraries
import { USER_ROLES, USER_STATUS, DEPARTMENTS, FORM_TYPES } from '@/utils/constants';
import { ref } from 'vue';
import { employeeRules } from '@/utils/constants/rules';
import { ElForm } from 'element-plus';
import type { Employee } from '@/utils/types/employee';


const props = defineProps<{
  form: Employee, // Employee data for the form
  formType: FORM_TYPES,
}>();

const dialogVisible = defineModel<boolean>('modelValue', { required: true }) // Dialog visibility state
const loading = defineModel<boolean>('loading', { required: true }) // Loading state
const formRef = ref<InstanceType<typeof ElForm>>() // Reference to the form

const emit = defineEmits(['submit-employee', 'close-employee']); // Define emitted events

// Handle form submission
const handleSubmit = () => {
  formRef.value?.validate((valid) => {
    if (valid) {
      emit('submit-employee', props.form); // Emit submit event with form data
    }
  });
}

// Handle closing the dialog
const handleClose = () => {
  formRef.value?.resetFields(); // Reset form fields
  emit('close-employee', props.form); // Emit close event with form data
}
</script>