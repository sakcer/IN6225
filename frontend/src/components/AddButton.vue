<template>
  <el-button type="primary" @click="$emit('add')">
    <el-icon class="mr-2">
      <Plus />
    </el-icon>
    {{ buttonText }}
  </el-button>
</template>

<script setup lang="ts">
import { Plus } from '@element-plus/icons-vue';

defineProps<{
  buttonText: string;
}>();

defineEmits<{
  (e: 'add'): void;
}>();

</script>
