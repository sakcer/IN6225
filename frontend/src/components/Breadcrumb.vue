<template>
  <div class="flex items-center mb-4">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/' }">Home</el-breadcrumb-item>
      <el-breadcrumb-item>{{ label }}</el-breadcrumb-item>
    </el-breadcrumb>
  </div>
</template>

<script setup>
const props = defineProps({
  label: {
    type: String,
    required: true
  }
});
</script>
