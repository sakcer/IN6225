<template>
  <!-- Add/Edit Project Dialog -->
  <el-dialog v-model="dialogVisible" :title="formType === 1 ? 'Edit Project' : formType === 0 ? 'View Project' : 'Add Project'" width="50%" @close="handleClose">
    <el-form ref="formRef" :model="form" :rules="projectRules" label-width="100px">
      <!-- Project Name -->
      <el-form-item label="Name" prop="name">
        <el-input v-model="form.name" :disabled="formType === 0"/>
      </el-form-item>

      <!-- Project Description -->
      <el-form-item label="Description" prop="description">
        <el-input v-model="form.description" type="textarea" :rows="3" :disabled="formType === 0"/>
      </el-form-item>

      <!-- Project Status -->
      <el-form-item label="Status" prop="status">
        <el-select v-model="form.status" class="w-full" :disabled="formType === 0">
          <el-option label="Active" :value="PROJECT_STATUS.ACTIVE" />
          <el-option label="Completed" :value="PROJECT_STATUS.COMPLETED" />
        </el-select>
      </el-form-item>

      <!-- Project Progress -->
      <el-form-item label="Progress" prop="progress">
        <el-slider v-model="form.progress" :step="10" show-stops :disabled="formType === 0"/>
      </el-form-item>

      <!-- Project Leader -->
      <el-form-item label="Leader" prop="leader">
        <el-select v-model="form.leader.id" class="w-full" filterable :disabled="me.role !== USER_ROLES.ADMIN || formType === 0">
          <el-option v-for="leader in leaders" :key="leader.id" :label="leader.name" :value="leader.id">
            <div class="flex items-center">
              <el-avatar :size="24" :src="leader.avatar" :style="{ backgroundColor: getAvatarColor(leader.name) }" class="mr-2">
                {{ getAvatarText(leader.name) }}
              </el-avatar>
              {{ leader.name }}
            </div>
          </el-option>
        </el-select>
      </el-form-item>

      <!-- Project Members -->
      <el-form-item label="Members" prop="memberIds">
        <el-select v-model="form.memberIds" multiple filterable class="w-full" :disabled="formType === 0">
          <el-option v-for="user in users" :key="user.id" :label="user.name" :value="user.id">
            <div class="flex items-center">
              <el-avatar :size="24" :src="user.avatar" :style="{ backgroundColor: getAvatarColor(user.name) }" class="mr-2">
                {{ getAvatarText(user.name) }}
              </el-avatar>
              {{ user.name }}
            </div>
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="flex justify-end gap-2">
        <el-button @click="handleClose">Cancel</el-button>
        <template v-if="formType !== 0"> 
          <el-button type="primary" @click="handleSubmit">Confirm</el-button>
        </template>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
// Import necessary libraries and types
import { ref } from 'vue';
import { getAvatarColor, getAvatarText } from '@/utils/avatar';
import type { Project } from '@/utils/types/project';
import type { Employee } from '@/utils/types/employee';
import { PROJECT_STATUS, USER_ROLES } from '@/utils/constants';
import { projectRules } from '@/utils/constants/rules';
import { ElForm } from 'element-plus';

// Define props for the component
const props = defineProps<{
  formType: number; // Type of form (add/edit/view)
  form: Project; // Project form data
  users: Employee[]; // List of users
  leaders?: Employee[]; // List of leaders
  me: Employee; // Current user
}>();

const formRef = ref<InstanceType<typeof ElForm>>() // Reference to the form
const dialogVisible = defineModel<boolean>('dialogVisible', { required: true }); // Dialog visibility state

// Define emitted events
const emit = defineEmits(
  ['close-project', 'submit-project']
);

// Handle form submission
const handleSubmit = () => {
  console.log(props.form);
  formRef.value?.validate((valid) => {
    if (valid) {
      emit('submit-project', props.form); // Emit submit event with form data
    }
  });
}

// Handle closing the dialog
const handleClose = () => {
  formRef.value?.resetFields(); // Reset form fields
  emit('close-project', props.form); // Emit close event with form data
}

</script>