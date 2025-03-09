<template>
  <div class="mt-4 flex items-center justify-between">
    <span class="text-sm text-gray-600">
      共 {{ total }} 条记录，当前显示 {{ startIndex }}-{{ endIndex }} 条
    </span>
    <div class="flex items-center gap-4">
      <span class="text-sm text-gray-600">
        每页显示
      </span>
      <el-select
        v-model="pageSize"
        class="w-20"
        @change="handleSizeChange"
      >
        <el-option
          v-for="size in PAGE_SIZES"
          :key="size"
          :value="size"
          :label="size"
        />
      </el-select>
      <el-pagination
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        :page-sizes="PAGE_SIZES"
        layout="prev, pager, next, jumper"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { PAGE_SIZES } from '@/utils/constants';

const props = defineProps<{
  currentPage: number;
  pageSize: number;
  total: number;
}>();

const pageSize = ref(props.pageSize);

const emit = defineEmits<{
  (e: 'update:currentPage', value: number): void;
  (e: 'update:pageSize', value: number): void;
}>();

// 计算当前显示的记录范围
const startIndex = computed(() => {
  return (props.currentPage - 1) * props.pageSize + 1;
});

const endIndex = computed(() => {
  return Math.min(props.currentPage * props.pageSize, props.total);
});

const handleCurrentChange = (page: number) => {
  emit('update:currentPage', page);
};

const handleSizeChange = (size: number) => {
  emit('update:pageSize', size);
};
</script> 