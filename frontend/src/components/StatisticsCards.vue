<template>
  <el-row :gutter="20" class="mb-6">
    <el-col :span="6" v-for="(stat, index) in data" :key="index">
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
          {{ stat.value }}{{ stat.suffix || '' }}
        </span>
      </el-card>
    </el-col>
  </el-row>
</template>

<script setup lang="ts">
import type { Statistic } from '@/utils/types';

const props = defineProps<{
  data: Statistic[];
}>();
</script>

<style scoped>
.text-primary {
  color: var(--el-color-primary);
}

.text-success {
  color: var(--el-color-success);
}

.text-warning {
  color: var(--el-color-warning);
}

.text-danger {
  color: var(--el-color-danger);
}
</style> 