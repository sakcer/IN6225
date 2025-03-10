<template>
  <el-button-group>
    <el-button
      v-for="(label, key) in status"
      :key="key"
      :type="modelValue === label ? 'primary' : 'default'"
      @click="$emit('update:modelValue', label); $emit('search')"
    >
      {{ key }}
    </el-button>
  </el-button-group>
</template>

<script setup lang="ts">
import { defineProps, defineEmits } from 'vue';

defineProps<{
  modelValue: string;
  status: Record<string, string>;
}>();

defineEmits<{
  (e: 'update:modelValue', value: string): void;
  (e: 'search'): void;
}>();
</script>
