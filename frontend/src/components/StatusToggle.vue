<template>
  <el-button-group>
    <el-button
      :type="modelValue === USER_STATUS.ACTIVE ? 'primary' : 'default'"
      @click="$emit('update:modelValue', USER_STATUS.ACTIVE); $emit('search')"
    >
      Active
    </el-button>
    <el-button
      :type="modelValue === USER_STATUS.INACTIVE ? 'primary' : 'default'"
      @click="$emit('update:modelValue', USER_STATUS.INACTIVE); $emit('search')"
    >
      Inactive
    </el-button>
  </el-button-group>
</template>

<script setup lang="ts">
import { USER_STATUS } from '@/utils/constants';

// 接收父组件传递的值
defineProps<{
  modelValue: string;
}>();

defineEmits<{
  (e: 'update:modelValue', value: string): void;
  (e: 'search'): void;
}>();
</script>