<template>
  <el-dialog
    v-model="visible"
    title="修改个人信息"
    width="400px"
  >
    <el-form
      ref="formRef"
      :model="form"
      label-width="100px"
    >
      <el-form-item label="名字" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="职位" prop="title">
        <el-input v-model="form.title" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="flex justify-end gap-2">
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="changingInfo">
          确认修改
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { employeeService } from '@/services/employees/employeeService';
import { ElMessage } from 'element-plus';
import { useMeStore } from '@/store/meStore';
import { useUsersStore } from '@/store/userStore';

const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true
  },
});

const meStore = useMeStore();
const usersStore = useUsersStore();
const userInfo = meStore.getMe;

const visible = defineModel<boolean>('modelValue', { required: true });
const changingInfo = ref(false);
const form = ref({ ...userInfo });

async function handleSubmit() {
  changingInfo.value = true;
  try {
    const data = await employeeService.updateEmployee(form.value.id, form.value);
    console.log('data', data);

    visible.value = false;
    meStore.refetchMe();
    usersStore.refetchUsers();
  } catch (error) {
    ElMessage.error('修改失败');
    console.error('修改失败', error);
  } finally {
    changingInfo.value = false;
  }
}
</script>
