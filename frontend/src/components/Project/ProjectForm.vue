<template>
  <!-- Add/Edit Project Dialog -->
  <el-dialog v-model="dialogVisible" :title="formType === FORM_TYPES.EDIT ? 'Edit Project' : 'Add Project'" width="50%" @close="handleClose">
    <el-form ref="formRef" :model="form" :rules="projectRules" label-width="100px">
      <!-- Project Name -->
      <el-form-item label="Name" prop="name">
        <el-input v-model="form.name"/>
      </el-form-item>

      <!-- Project Description -->
      <el-form-item label="Description" prop="description">
        <el-input v-model="form.description" type="textarea" :rows="3"/>
      </el-form-item>

      <!-- Project Status -->
      <el-form-item label="Status" prop="status">
        <el-select v-model="form.status" class="w-full">
          <el-option label="Active" :value="PROJECT_STATUS.ACTIVE" />
          <el-option label="Completed" :value="PROJECT_STATUS.COMPLETED" />
        </el-select>
      </el-form-item>

      <!-- Project Progress -->
      <el-form-item label="Progress" prop="progress">
        <el-slider v-model="form.progress" :step="10" show-stops/>
      </el-form-item>

      <!-- Project Leader -->
      <el-form-item label="Leader" prop="leader">
        <el-select v-model="form.leader.id" class="w-full" filterable :disabled="!userStore.isAdmin">
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
      <el-form-item label="Members" prop="members">
        <el-select v-model="form.members" value-key="id" multiple filterable class="w-full">
          <el-option v-for="user in users" :key="user.id" :label="user.name" :value="user">
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
        <el-button type="primary" @click="handleSubmit" :loading="loading">Confirm</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
// Import necessary libraries and types
import { ref, onMounted } from 'vue';
import { getAvatarColor, getAvatarText } from '@/utils/avatar';
import type { Project } from '@/utils/types/project';
import type { Employee } from '@/utils/types/employee';
import { FORM_TYPES, PROJECT_STATUS } from '@/utils/constants';
import { projectRules } from '@/utils/constants/rules';
import { ElForm } from 'element-plus';
import { employeeService } from '@/services/employees/employeeService';
import { useUserStore } from '@/store/userStore';

// Define props for the component
const props = defineProps<{
  formType: FORM_TYPES; // Type of form (add/edit/view)
  form: Project; // Project form data
}>();

const userStore = useUserStore();
const formRef = ref<InstanceType<typeof ElForm>>() // Reference to the form
const dialogVisible = defineModel<boolean>('dialogVisible', { required: true }); // Dialog visibility state
const loading = defineModel<boolean>('loading', { required: true }); // Loading state
const users = ref([] as Employee[]);
const leaders = ref([] as Employee[]);

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

onMounted(async () => {

  const [employees] = await Promise.all([
      employeeService.getAllEmployees()
  ])

  users.value = employees.users;
  leaders.value = employees.leaders;
})

</script>