<template>
  <div class="mt-4 flex items-center justify-between">
    <span class="text-sm text-gray-600 truncate">
      <template v-if="currentPage && pageSize">
        Total {{ total }} records, currently displaying {{ (currentPage - 1) * pageSize + 1 }}-{{ currentPage * pageSize }} records
      </template>
      <template v-else>
        Loading data...
      </template>
    </span>
    <div class="flex items-center gap-4">
      <span class="text-sm text-gray-600 min-w-max">
        Show 
      </span>
      <el-select
        v-model="pageSize"
        class="min-w-20"
        @change="emit('update:pageSize', $event)"
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
        @current-change="emit('update:currentPage', $event)"
        @size-change="emit('update:pageSize', $event)"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
// Import necessary libraries and constants
import { ref } from 'vue';
import { PAGE_SIZES } from '@/utils/constants';

// Define props for the component
const props = defineProps<{
  currentPage: number, // Current page number
  pageSize: number, // Number of items per page
  total: number // Total number of items
}>();

const pageSize = ref(props.pageSize); // Local state for page size

// Define emitted events
const emit = defineEmits<{
  (e: 'update:currentPage', value: number): void; // Event for updating current page
  (e: 'update:pageSize', value: number): void; // Event for updating page size
}>();
</script>
