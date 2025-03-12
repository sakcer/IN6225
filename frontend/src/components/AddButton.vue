<template>
  <el-button type="primary" @click="handleAdd">
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

// defineEmits<{
//   (e: 'add'): void;
// }>();

const dialogVisible = defineModel<boolean>('dialogVisible', { required: true });
const isEdit = defineModel<boolean>('isEdit', { required: true });

const handleAdd = () => {
  isEdit.value = false
  // form.value = {
  //   name: '',
  //   description: '',
  //   status: PROJECT_STATUS.ACTIVE,
  //   progress: 0,
  //   dateRange: [],
  //   leaderId: 0,
  //   memberIds: []
  // }
  dialogVisible.value = true
}

</script>
