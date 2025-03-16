<template>
  <el-card shadow="hover" class="text-center">
    <template #header>
      <div class="flex items-center justify-center">
        <el-icon class="mr-2">
          <component :is="stat.icon" />
        </el-icon>
        {{ stat.label }}
      </div>
    </template>
    <span :class="['text-3xl font-bold', `text-${stat.type}`]">
      {{ stat.value }}
    </span>
  </el-card>
</template>

<script setup lang="ts">
import type { Statistics } from "@/utils/types/statistics";

defineProps<{
  stat: Statistics;
}>();
</script>
