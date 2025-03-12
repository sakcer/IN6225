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
        <el-button type="primary" @click="handleSubmit" :loading="changingPassword">
          确认修改
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { ElMessage } from 'element-plus';
import { employeeService } from '@/services/employees/employeeService';
import { useMeStore } from '@/store/meStore';

const meStore = useMeStore();
const userInfo = meStore.getMe;

const visible = defineModel<boolean>('modelValue', { required: true })
const passwordFormRef = ref<ElFormInstance>();
const passwordForm = ref({});
const changingPassword = ref(false);
const rules = ref({});

async function handleSubmit() {
  changingPassword.value = true;
  try {
    const data = await employeeService.updateEmployeePassword(userInfo.id, passwordForm.value);
    visible.value = false;
  } catch (error) {
    ElMessage.error('密码修改失败');
    console.error('密码修改失败', error);
  } finally {
    changingPassword.value = false;
  }
}

</script>