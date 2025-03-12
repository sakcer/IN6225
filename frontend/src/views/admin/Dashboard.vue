<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col v-for="(item, index) in statsData" :key="index" :span="6">
        <el-card shadow="hover">
          <template #header>
            <div class="flex items-center">
              <el-icon class="mr-2">
                <component :is="item.icon" />
              </el-icon>
              {{ item.label }}
            </div>
          </template>
          <div class="text-3xl font-bold text-center">{{ item.value }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-6">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="flex items-center">
              <el-icon class="mr-2"><PieChart /></el-icon> 部门人员分布
            </div>
          </template>
          <div ref="departmentChartRef" style="height: 300px"></div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="flex items-center">
              <el-icon class="mr-2"><TrendCharts /></el-icon> 员工变动趋势
            </div>
          </template>
          <div ref="trendChartRef" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, watch } from 'vue'
import { User, Box, Calendar, Promotion, PieChart, TrendCharts } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { useUsersStore } from '@/store/userStore'
import { useProjectsStore } from '@/store/projectStore'

const usersStore = useUsersStore()
const projectsStore = useProjectsStore()

const users = computed(() => usersStore.getUsers)
const projects = computed(() => projectsStore.getProjects)

const totalEmployees = computed(() => users.value.length)
const totalDepartments = computed(() => new Set(users.value.map(user => user.department)).size)
const totalProjects = computed(() => projects.value.length)
const newEmployees = computed(() => {
  const today = new Date().toISOString().split('T')[0]
  return users.value.filter(user => user.joinDate?.split('T')[0] === today).length
})

const statsData = computed(() => [
  { label: '员工总数', icon: User, value: totalEmployees.value },
  { label: '本月新入职', icon: Calendar, value: newEmployees.value },
  { label: '部门数量', icon: Box, value: totalDepartments.value },
  { label: '项目总数', icon: Promotion, value: totalProjects.value }
])

const departmentData = computed(() => {
  const counts = users.value.reduce((acc, user) => {
    acc[user.department] = (acc[user.department] || 0) + 1
    return acc
  }, {})
  return Object.entries(counts).map(([name, value]) => ({ name, value }))
})

const trendData = computed(() => {
  const today = new Date()
  return Array.from({ length: 7 }, (_, i) => {
    const date = new Date(today)
    date.setDate(today.getDate() - (6 - i))
    return `${date.getMonth() + 1}月${date.getDate()}日`
  })
})

const joinCounts = computed(() => {
  const counts = trendData.value.reduce((acc, date) => ({ ...acc, [date]: 0 }), {})
  users.value.forEach(user => {
    const joinDate = new Date(user.joinDate)
    const formattedDate = `${joinDate.getMonth() + 1}月${joinDate.getDate()}日`
    if (formattedDate in counts) counts[formattedDate]++
  })
  return trendData.value.map(date => counts[date])
})

const departmentChartRef = ref<HTMLElement>()
const trendChartRef = ref<HTMLElement>()

onMounted(() => {
  if (departmentChartRef.value) {
    const chart = echarts.init(departmentChartRef.value)
    chart.setOption({
      tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
      legend: { orient: 'vertical', left: 'left' },
      series: [{ type: 'pie', radius: '50%', data: departmentData.value }]
    })
    watch(departmentData, () => chart.setOption({ series: [{ data: departmentData.value }] }))
  }

  if (trendChartRef.value) {
    const chart = echarts.init(trendChartRef.value)
    chart.setOption({
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category', data: trendData.value },
      yAxis: { type: 'value' },
      series: [{ name: '入职人数', type: 'line', data: joinCounts.value }]
    })
    watch(joinCounts, () => chart.setOption({ series: [{ data: joinCounts.value }] }))
  }
})
</script>
