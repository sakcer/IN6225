<template>
  <div class="container mx-auto p-6">
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- 左侧个人信息卡片 -->
      <div class="lg:col-span-1">
        <info-card :employee="employee" />
      </div>

      <!-- 右侧详细信息 -->
      <div class="lg:col-span-2">
        <info-details :employee="employee" @edit-info="handleEdit" />

        <!-- 可以在这里添加更多卡片，比如最近活动、技能标签等 -->
        <el-card shadow="hover">
          <template #header>
            <div class="font-bold">最近活动</div>
          </template>
          <el-empty description="暂无活动记录" />
        </el-card>
      </div>
    </div>

    <!-- 编辑对话框 -->
    <info-form v-model="dialogVisible" v-model:form="form" :employee="employee" @save-info="handleSave" @cancel-info="handleCancel" />

  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { Message, User, Calendar, Plus } from '@element-plus/icons-vue'
import type { Employee } from '@/utils/types/employee'
import { getAvatarColor, getAvatarText } from '@/utils/avatar'
import { useMeStore } from '@/store/meStore'
import InfoCard from './widgets/InfoCard.vue'
import InfoDetails from './widgets/InfoDetails.vue'
import InfoForm from './widgets/InfoForm.vue'
import { employeeService } from '@/services/employees/employeeService'
import { ElMessage } from 'element-plus'

const meStore = useMeStore()
const employee = computed(() => meStore.me)

const dialogVisible = ref(false)
const form = ref<Partial<Employee>>({})

const handleEdit = (employee: Employee) => {
  form.value = { ...employee }
  dialogVisible.value = true
}

const handleCancel = () => {
  dialogVisible.value = false
}

const handleSave = async () => {
  console.log("处理保存")
  console.log(form.value)
  try {
    const data = await employeeService.updateEmployee(form.value.id, form.value)
    meStore.refetchMe()
    dialogVisible.value = false
    ElMessage.success("保存成功")
  } catch (error) {
    console.error("保存失败", error)
    ElMessage.error("保存失败")
  }
}

</script>
