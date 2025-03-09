<template>
  <el-dialog 
    :title="isEdit ? '编辑员工' : '添加员工'"
    v-model="dialogVisible"
    width="500px"
    @close="handleClose"
  >
    <el-form ref="formRef" :model="form" label-width="100px">
      <el-form-item label="姓名" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>

      <el-form-item label="邮箱" prop="email">
        <el-input v-model="form.email" />
      </el-form-item>

      <el-form-item label="工号" prop="employeeId">
        <el-input v-model="form.employeeId" />
      </el-form-item>

      <el-form-item label="部门" prop="department">
        <el-input v-model="form.department" />
      </el-form-item>

      <el-form-item label="角色" prop="role">
        <el-select v-model="form.role" class="w-full">
          <el-option v-for="role in roles" :key="role" :label="role" :value="role" />
        </el-select>
      </el-form-item>

      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="form.status">
          <el-radio :value="active">Active</el-radio>
          <el-radio :value="inactive">Inactive</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="flex justify-end gap-2">
        <el-button @click="closeDialog">取消</el-button>
        <el-button type="primary" @click="submitForm" :loading="saving"> 确定 </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { defineProps, defineEmits, ref, watch } from 'vue';
import { USER_ROLES } from '@/utils/constants';

const props = defineProps({
  dialogVisible: Boolean,
  isEdit: Boolean,
  form: Object,
  departments: Array,
});

const active = ref('active')
const inactive = ref('inactive')
const roles = ref(USER_ROLES)

const emit = defineEmits(['update:modelValue', 'submit']);
const dialogVisible = ref(props.dialogVisible);
const saving = ref(false);

const handleClose = () => {
  emit('update:modelValue', false);
};

const closeDialog = () => {
  emit('update:modelValue', false);
};

const submitForm = () => {
  saving.value = true;
  emit('submit');
  setTimeout(() => {
    saving.value = false;
    closeDialog();
  }, 500);
};
</script>
