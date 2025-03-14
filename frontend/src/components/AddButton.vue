<template>
  <el-button type="primary" @click="emitEvent('add')">
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

const emit = defineEmits(['add']);
const emitEvent = (event: string) => {
  emit(event);
};

</script>
