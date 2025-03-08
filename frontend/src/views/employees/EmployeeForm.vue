<template>
  <div class="employee-form">
    <el-page-header :icon="Back" @back="$router.back()">
      <template #title>返回</template>
      <template #content>{{ isEdit ? '编辑员工' : '添加员工' }}</template>
    </el-page-header>

    <div class="mt-6">
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        class="max-w-2xl"
      >
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>

        <el-form-item label="部门" prop="department">
          <el-select v-model="form.department" placeholder="请选择部门">
            <el-option
              v-for="dept in departments"
              :key="dept.id"
              :label="dept.name"
              :value="dept.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="职位" prop="position">
          <el-input v-model="form.position" />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" type="email" />
        </el-form-item>

        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone" />
        </el-form-item>

        <el-form-item label="入职日期" prop="joinDate">
          <el-date-picker
            v-model="form.joinDate"
            type="date"
            placeholder="选择日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit">保存</el-button>
          <el-button @click="$router.back()">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Back } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'

const route = useRoute()
const router = useRouter()
const formRef = ref<FormInstance>()

const isEdit = () => route.params.id !== undefined


// 表单数据
const form = ref({
  name: '',
  department: '',
  position: '',
  email: '',
  phone: '',
  joinDate: '',
})

// 部门数据
const departments = ref([
  { id: 1, name: '技术部' },
  { id: 2, name: '人事部' },
  { id: 3, name: '财务部' },
])

// 表单验证规则
const rules = ref<FormRules>({
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  department: [
    { required: true, message: '请选择部门', trigger: 'change' }
  ],
  position: [
    { required: true, message: '请输入职位', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入电话号码', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  joinDate: [
    { required: true, message: '请选择入职日期', trigger: 'change' }
  ]
})

// 如果是编辑模式，加载员工数据
onMounted(async () => {
  if (isEdit.value) {
    try {
      // 这里应该调用API获取员工数据
      const employeeData = {
        name: '张三',
        department: 1,
        position: '前端开发工程师',
        email: 'zhangsan@example.com',
        phone: '13800138000',
        joinDate: '2023-01-01'
      }
      Object.assign(form.value, employeeData)
    } catch (error) {
      ElMessage.error('获取员工信息失败')
    }
  }
})

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 这里应该调用API保存数据
        ElMessage.success(isEdit.value ? '更新成功' : '添加成功')
        router.push('/employees')
      } catch (error) {
        ElMessage.error(isEdit.value ? '更新失败' : '添加失败')
      }
    }
  })
}
</script> 