<template>
  <div class="employee-list">
    <div class="mb-4 flex justify-between items-center">
      <div class="flex gap-4">
        <el-input
          v-model="searchQuery"
          placeholder="搜索员工姓名"
          class="w-64"
          clearable
          @input="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-select v-model="departmentFilter" placeholder="选择部门" clearable @change="handleSearch">
          <el-option
            v-for="dept in departments"
            :key="dept.id"
            :label="dept.name"
            :value="dept.id"
          />
        </el-select>
      </div>
      <el-button type="primary" @click="$router.push('/employees/add')">
        <el-icon><Plus /></el-icon>添加员工
      </el-button>
    </div>

    <el-table :data="employees" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="department" label="部门" />
      <el-table-column prop="position" label="职位" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="phone" label="电话" />
      <el-table-column prop="joinDate" label="入职日期" />
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button-group>
            <el-button type="primary" @click="$router.push(`/employees/edit/${row.id}`)">
              编辑
            </el-button>
            <el-button type="danger" @click="handleDelete(row)">
              删除
            </el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>

    <div class="mt-4 flex justify-end">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { Search, Plus } from '@element-plus/icons-vue'
import { ElMessageBox, ElMessage } from 'element-plus'

// 模拟数据
const departments = ref([
  { id: 1, name: '技术部' },
  { id: 2, name: '人事部' },
  { id: 3, name: '财务部' },
])

const employees = ref([
  {
    id: 1,
    name: '张三',
    department: '技术部',
    position: '前端开发工程师',
    email: 'zhangsan@example.com',
    phone: '13800138000',
    joinDate: '2023-01-01',
  },
  // 更多员工数据...
])

const searchQuery = ref('')
const departmentFilter = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(100)

const handleSearch = () => {
  // 实现搜索逻辑
}

const handleSizeChange = (val: number) => {
  pageSize.value = val
  // 重新加载数据
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  // 重新加载数据
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm(
    `确定要删除员工 ${row.name} 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    // 实现删除逻辑
    ElMessage.success('删除成功')
  }).catch(() => {
    // 取消删除
  })
}
</script> 