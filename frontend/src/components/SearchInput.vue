<template>
  <el-input
    :model-value="modelValue"
    :placeholder="placeholder"
    class="w-full"
    clearable
    @input="$emit('update:modelValue', $event); $emit('search')"
  >
    <template #prefix>
      <el-icon>
        <Search />
      </el-icon>
    </template>
  </el-input>
</template>

<script setup lang="ts">
import { Search } from '@element-plus/icons-vue';

defineProps<{
  modelValue: string;
  placeholder: string;
}>();

defineEmits<{
  (e: 'update:modelValue', value: string): void;
  (e: 'search'): void;
}>();
</script>
