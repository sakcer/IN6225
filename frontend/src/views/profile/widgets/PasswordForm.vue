<template>
  <el-dialog
    v-model="visible"
    title="修改密码"
    width="400px"
  >
    <el-form
      ref="passwordFormRef"
      :model="passwordForm"
      :rules="rules"
      label-width="100px"
    >
      <el-form-item label="当前密码" prop="currentPassword">
        <el-input
          v-model="passwordForm.currentPassword"
          type="password"
          show-password
        />
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input
          v-model="passwordForm.newPassword"
          type="password"
          show-password
        />
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input
          v-model="passwordForm.confirmPassword"
          type="password"
          show-password
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="flex justify-end gap-2">
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="handleChangePassword" :loading="changingPassword">
          确认修改
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';

const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true
  },
  changingPassword: {
    type: Boolean,
    required: true
  },
  rules: {
    type: Object,
    required: true
  },
  handleChangePassword: {
    type: Function,
    required: true
  },
  passwordForm: {
    type: Object,
    required: true
  }
});

const visible = defineModel<boolean>('modelValue', { required: true })
const passwordFormRef = defineModel<ElFormInstance>('passwordFormRef', { required: true })

</script>