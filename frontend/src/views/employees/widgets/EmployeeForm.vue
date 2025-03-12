<template>
  <el-dialog :title="isEdit ? '编辑员工' : '添加员工'" v-model="dialogVisible" width="600px" @close="handleClose">
    <el-form ref="formRef" :model="form" label-width="100px" :rules="rules">

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="姓名" prop="name">
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
import { rules } from '@/utils/types/employee';
import { Department } from '@/utils/constants';
import { useUsersStore } from '@/store/userStore';

const usersStore = useUsersStore()

const props = defineProps({
  isEdit: Boolean,
  form: Object,
  departments: Array,
});

const dialogVisible = defineModel<boolean>('modelValue', { required: true })
const loading = defineModel<boolean>('loading', { required: true })
const state = defineModel<boolean>('state', { required: true })
const formRef = ref<ElFormInstance>();

const handleSubmit = async () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        loading.value = true;
        if (props.isEdit) {
          const data = await employeeService.updateEmployee(props.form.id, props.form);
          ElMessage.success('编辑员工[' + data.employeeId + ']成功');
        } else {
          delete props.form.id;
          const data = await employeeService.createEmployee(props.form);
          ElMessage.success('添加员工[' + data.employeeId + ']成功');
        }
        usersStore.refetchUsers()
        // state.value = !state.value;
      } catch {
        ElMessage.error('添加失败');
      } finally {
        setTimeout(() => {
          dialogVisible.value = false;
          loading.value = false;
        }, 500);
      }
    } else {
      ElMessage.error('表单验证失败');
      return false; // 阻止提交
    }
  });
}

const handleClose = () => {
  dialogVisible.value = false;
}
</script>