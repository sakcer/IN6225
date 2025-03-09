<template>
  <div class="w-full mb-6">
    <!-- 状态切换和搜索框放在同一行 -->
    <div class="flex items-center mb-4">
      <el-button-group>
        <el-button
          :type="modelValue === USER_STATUS.ACTIVE ? 'primary' : 'default'"
          @click="$emit('update:modelValue', USER_STATUS.ACTIVE), $emit('search')"
        >
          Active
        </el-button>
        <el-button
          :type="modelValue === USER_STATUS.INACTIVE ? 'primary' : 'default'"
          @click="$emit('update:modelValue', USER_STATUS.INACTIVE), $emit('search')"
        >
          Inactive
        </el-button>
      </el-button-group>

      <div class="flex-grow ml-4">
        <el-input
          :model-value="searchQuery"
          placeholder="Search"
          class="w-full"
          clearable
          @input="$emit('update:searchQuery', $event); $emit('search')"
        >
          <template #prefix>
            <el-icon>
              <Search />
            </el-icon>
          </template>
        </el-input>
      </div>
    </div>

    <!-- 添加用户按钮放在新的一行，靠右 -->
    <div class="flex justify-end">
      <el-button type="primary" @click="$emit('add')">
        <el-icon class="mr-2">
          <Plus />
        </el-icon>
        Add User
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Search, Plus } from '@element-plus/icons-vue';
import { USER_STATUS } from '@/utils/constants';

defineProps<{
  modelValue: string;
  searchQuery: string;
}>();

defineEmits<{
  (e: 'update:modelValue', value: string): void;
  (e: 'update:searchQuery', value: string): void;
  (e: 'search'): void;
  (e: 'add'): void;
}>();
</script> 