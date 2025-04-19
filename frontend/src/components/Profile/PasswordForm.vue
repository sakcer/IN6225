<template>
    <el-dialog v-model="dialogVisible" title="Change Password" width="400px" @close="cancelPasswordChange">
        <el-form ref="passwordFormRef" :model="passwordData" :rules="passwordRules" label-width="160px">
            <el-form-item label="Current Password" prop="currentPassword">
                <el-input v-model="passwordData.currentPassword" type="password" show-password />
            </el-form-item>
            <el-form-item label="New Password" prop="newPassword">
                <el-input v-model="passwordData.newPassword" type="password" show-password />
            </el-form-item>
            <el-form-item label="Confirm Password" prop="confirmPassword">
                <el-input v-model="passwordData.confirmPassword" type="password" show-password />
            </el-form-item>
        </el-form>
        <template #footer>
            <el-button @click="cancelPasswordChange">Cancel</el-button>
            <el-button type="primary" @click="submitPasswordChange">Confirm</el-button>
        </template>
    </el-dialog>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { ElForm, ElFormItem, ElInput, ElButton, ElDialog } from 'element-plus';
import { passwordRules } from '@/utils/constants/rules';

const dialogVisible = defineModel<boolean>('modelValue', { required: true })
const passwordFormRef = ref<InstanceType<typeof ElForm>>()

const emit = defineEmits(['save-password', 'cancel-password'])

const passwordData = ref({
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
})


const cancelPasswordChange = () => {
    passwordFormRef.value?.resetFields()
    emit('cancel-password')
}

const submitPasswordChange = async () => {
    console.log('Password data:', passwordData.value)
    passwordFormRef.value?.validate((valid) => {
        if (valid) {
        emit('save-password', passwordData.value)
        }
    })
}

</script>