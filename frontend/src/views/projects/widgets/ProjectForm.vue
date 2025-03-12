<template>
  <!-- 添加/编辑项目对话框 -->
  <el-dialog v-model="dialogVisible" :title="isEdit ? 'Edit Project' : 'Add Project'" width="50%">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <!-- 项目名称 -->
      <el-form-item label="Name" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>

      <!-- 项目描述 -->
      <el-form-item label="Description" prop="description">
        <el-input v-model="form.description" type="textarea" :rows="3" />
      </el-form-item>

      <!-- 项目状态 -->
      <el-form-item label="Status" prop="status">
        <el-select v-model="form.status" class="w-full">
          <el-option label="Active" :value="PROJECT_STATUS.ACTIVE" />
          <el-option label="Completed" :value="PROJECT_STATUS.COMPLETED" />
        </el-select>
      </el-form-item>

      <!-- 项目进度 -->
      <el-form-item label="Progress" prop="progress">
        <el-slider v-model="form.progress" :step="10" show-stops />
      </el-form-item>

      <!-- 项目日期范围 -->
      <el-form-item label="Date Range" prop="dateRange">
        <el-date-picker v-model="form.dateRange" type="daterange" range-separator="To" start-placeholder="Start date"
          end-placeholder="End date" class="w-full" />
      </el-form-item>

      <!-- 项目负责人 -->
      <el-form-item label="Leader" prop="leader">
        <el-select v-model="form.leaderId" class="w-full" filterable>
          <el-option v-for="user in users" :key="user.id" :label="user.name" :value="user.id">
            <div class="flex items-center">
              <el-avatar :size="24" :style="{ backgroundColor: getAvatarColor(user.name) }" class="mr-2">
                {{ getAvatarText(user.name) }}
              </el-avatar>
              {{ user.name }}
            </div>
          </el-option>
        </el-select>
      </el-form-item>

      <!-- 项目成员 -->
      <el-form-item label="Members" prop="members">
        <el-select v-model="form.memberIds" multiple filterable class="w-full">
          <el-option v-for="user in users" :key="user.id" :label="user.name" :value="user.id">
            <div class="flex items-center">
              <el-avatar :size="24" :style="{ backgroundColor: getAvatarColor(user.name) }" class="mr-2">
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
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { getAvatarColor, getAvatarText } from '@/utils/avatar';
import { ProjectForm } from '@/utils/types/project';
import type { FormRules, FormInstance } from 'element-plus';
import { projectService } from '@/services/projects/projectService';
import { ElMessage } from 'element-plus';
import { PROJECT_STATUS } from '@/utils/constants';
import { useProjectsStore } from '@/store/projectStore';

const props = defineProps<{
  isEdit: boolean;
  rules: FormRules;
  form: ProjectForm;
  users: Employee[];
}>();

const projectsStore = useProjectsStore()

const loading = ref(false);
// const state = defineModel<boolean>('state', { required: true });
const formRef = ref<FormInstance>();

async function handleClose() {
  dialogVisible.value = false;
  formRef.value?.resetFields();
}

async function handleSubmit() {
  formRef.value?.validate(async (valid) => {
    if (valid) {
      try {
        loading.value = true;

        if (props.form.dateRange && props.form.dateRange.length == 2) {
          props.form.startDate = props.form.dateRange[0];
          props.form.endDate = props.form.dateRange[1];
        }
        delete props.form.dateRange;
        let data;
        if (props.isEdit) {
          data = await projectService.updateProject(props.form.id!, props.form);
        } else {
          delete props.form.id;
          data = await projectService.createProject(props.form);
        }
        ElMessage.success('添加项目[' + data.id + ']成功');
        projectsStore.refetchProjects()
        // state.value = !state.value;
      } catch (error) {
        ElMessage.error('添加失败');
        console.log(error);
      } finally {
        setTimeout(() => {
          dialogVisible.value = false;
          loading.value = false;
        }, 500);
      }
    }
  });
}

const dialogVisible = defineModel<boolean>('modelValue', { required: true });

</script>