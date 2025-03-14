<template>
    <el-dialog v-model="dialogVisible" title="编辑个人信息" width="50%" @close="handleCancel(form)">
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
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="职位" prop="title">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleCancel">取消</el-button>
          <el-button type="primary" @click="handleSave">保存</el-button>
        </span>
      </template>
    </el-dialog>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import type { ElForm } from 'element-plus'
import { infoRules } from '@/utils/constants/rules'
import type { Employee } from '@/utils/types/employee'

const props = defineProps<{
    form: Employee
}>()

const dialogVisible = defineModel<boolean>('modelValue', { required: true })
const formRef = ref<InstanceType<typeof ElForm>>()

const emit = defineEmits(['save-info', 'cancel-info']);

const handleCancel = () => {
  formRef.value?.resetFields()
  emit('cancel-info', props.form)
}

const handleSave = () => {
  formRef.value?.validate((valid) => {
    if (valid) {
      emit('save-info', props.form)
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
