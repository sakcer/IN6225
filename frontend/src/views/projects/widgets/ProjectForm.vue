<template>
  <!-- 添加/编辑项目对话框 -->
  <el-dialog
    v-model="dialogVisible"
    :title="isEdit ? 'Edit Project' : 'Add Project'"
    width="50%"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="100px"
    >
      <!-- 项目名称 -->
      <el-form-item label="Name" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>

      <!-- 项目描述 -->
      <el-form-item label="Description" prop="description">
        <el-input
          v-model="form.description"
          type="textarea"
          :rows="3"
        />
      </el-form-item>

      <!-- 项目状态 -->
      <el-form-item label="Status" prop="status">
        <el-select v-model="form.status" class="w-full">
          <el-option label="Active" value="active" />
          <el-option label="Completed" value="completed" />
        </el-select>
      </el-form-item>

      <!-- 项目进度 -->
      <el-form-item label="Progress" prop="progress">
        <el-slider v-model="form.progress" :step="10" show-stops />
      </el-form-item>

      <!-- 项目日期范围 -->
      <el-form-item label="Date Range" prop="dateRange">
        <el-date-picker
          v-model="form.dateRange"
          type="daterange"
          range-separator="To"
          start-placeholder="Start date"
          end-placeholder="End date"
          class="w-full"
        />
      </el-form-item>

      <!-- 项目负责人 -->
      <el-form-item label="Leader" prop="leader">
        <el-select 
          v-model="form.leader"
          class="w-full"
          filterable
        >
          <el-option
            v-for="user in users"
            :key="user.id"
            :label="user.name"
            :value="user.id"
          >
            <div class="flex items-center">
              <el-avatar 
                :size="24"
                :style="{ backgroundColor: getAvatarColor(user.name) }"
                class="mr-2"
              >
                {{ getAvatarText(user.name) }}
              </el-avatar>
              {{ user.name }}
            </div>
          </el-option>
        </el-select>
      </el-form-item>

      <!-- 项目成员 -->
      <el-form-item label="Members" prop="members">
        <el-select
          v-model="form.members"
          multiple
          filterable
          class="w-full"
        >
          <el-option
            v-for="user in users"
            :key="user.id"
            :label="user.name"
            :value="user.id"
          >
            <div class="flex items-center">
              <el-avatar 
                :size="24"
                :style="{ backgroundColor: getAvatarColor(user.name) }"
                class="mr-2"
              >
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

const props = defineProps<{
  isEdit: boolean;
  rules: FormRules;
  form: ProjectForm;
  users: Employee[];
  handleSubmit: () => void;
  handleClose: () => void;
}>();

const dialogVisible = defineModel<boolean>('modelValue', { required: true });
const formRef = defineModel<FormInstance>('formRef', { required: false });

</script>