<template>
  <el-dialog :title="formType === 1 ? '编辑员工' : '添加员工'" v-model="dialogVisible" width="600px" @close="handleClose">
    <el-form ref="formRef" :model="form" label-width="100px" :rules="employeeRules">

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="姓名" prop="name" >
            <el-input v-model="form.name" placeholder="请输入姓名" />
          </el-form-item>
        </el-col>
        
        <el-col :span="12">
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="form.email" placeholder="请输入邮箱" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="工号" prop="employeeId">
            <el-input v-model="form.employeeId" placeholder="请输入工号" />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="部门" prop="department">
            <el-select v-model="form.department" placeholder="请选择部门">
              <el-option
                v-for="(value, key) in Department"
                :key="key"
                :label="key"
                :value="value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 新增职位字段 -->
      <el-form-item label="职位" prop="title">
        <el-input v-model="form.title" placeholder="请输入职位" />
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="角色" prop="role">
            <el-select v-model="form.role" placeholder="选择角色" class="w-full">
              <el-option v-for="role in USER_ROLES" :key="role" :label="role" :value="role" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="状态" prop="status">
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
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="loading"> 确定 </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { USER_ROLES, USER_STATUS } from '@/utils/constants';
import { ref } from 'vue';
import { ElMessage } from 'element-plus';
import { employeeService } from '@/services/employees/employeeService';
import { employeeRules } from '@/utils/constants/rules';
import { Department } from '@/utils/constants';
import { useUsersStore } from '@/store/userStore';

const usersStore = useUsersStore()

const props = defineProps({
  form: Object,
  departments: Array,
  formType: Number,
});

const dialogVisible = defineModel<boolean>('modelValue', { required: true })
const loading = defineModel<boolean>('loading', { required: true })
const formRef = ref<InstanceType<typeof ElForm>>()

const emit = defineEmits(['submit-employee', 'close-employee']);

const handleSubmit = () => {
  formRef.value?.validate((valid) => {
    if (valid) {
      emit('submit-employee', props.form);
    }
  });
}

const handleClose = () => {
  formRef.value?.resetFields();
  emit('close-employee', props.form);
}

</script>