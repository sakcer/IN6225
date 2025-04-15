<template>
  <div>
    <Breadcrumb label="Dashboard" />

    <el-row :gutter="20" class="mb-6">
      <el-col :span="6" v-for="(stat, index) in statsData" :key="index">
        <statistics-card v-if="stat" :stat="stat" />
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-6">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="flex items-center">
              <el-icon class="mr-2"><PieChart /></el-icon> Department Distribution
            </div>
          </template>
          <div ref="departmentChartRef" style="height: 300px"></div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="flex items-center">
              <el-icon class="mr-2"><TrendCharts /></el-icon> Employee Change Trend
            </div>
          </template>
          <div ref="trendChartRef" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { User, Box, Calendar, Promotion, PieChart, TrendCharts } from '@element-plus/icons-vue'
import Breadcrumb from '@/components/Breadcrumb.vue'
import StatisticsCard from '@/components/StatisticsCard.vue';
import * as echarts from 'echarts'
import { statsService } from '@/services/stats/statsService'
import type { Statistics } from '@/utils/types/statistics';

const STAT_ITEMS = [
  { key: 'totalEmployees', label: 'Total Employees', icon: User, type: 'primary' },
  { key: 'newEmployees', label: 'New Employees', icon: Calendar, type: 'success'},
  { key: 'totalDepartments', label: 'Total Departments', icon: Box, type: 'warning'},
  { key: 'totalProjects', label: 'Total Projects', icon: Promotion, type: 'danger' }
]

const stats = ref({
  totalEmployees: 0,
  totalProjects: 0,
  totalDepartments: 0,
  newEmployees: 0
})

const statsData = computed(() => 
  STAT_ITEMS.map(item => ({
    ...item,
    value: stats.value[item.key as keyof typeof stats.value],
  }) as Statistics)
)

const departmentChartRef = ref(null)
const trendChartRef = ref(null)

onMounted(async () => {
  const [statsOverview, departmentDist, employeeTrend] = await Promise.all([
    statsService.getStatsOverview(),
    statsService.getDepartmentDist(),
    statsService.getEmployeeTrend()
  ])

  Object.assign(stats.value, statsOverview)

  if (departmentChartRef.value) {
    const chart = echarts.init(departmentChartRef.value)
    chart.setOption({
      tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
      legend: { orient: 'vertical', left: 'left' },
      series: [{ type: 'pie', radius: '50%', data: departmentDist.departments }]
    })
    // watch(departmentData, () => chart.setOption({ series: [{ data: departmentDist.departments }] }))
    const resizeHandler = () => chart.resize()
    window.addEventListener('resize', resizeHandler)
  }

  if (trendChartRef.value) {
    const chart = echarts.init(trendChartRef.value)
    chart.setOption({
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category', data: employeeTrend.dates },
      yAxis: { type: 'value' },
      series: [{ name: 'Join Count', type: 'line', data: employeeTrend.joinCounts }]
    })
    // watch(employeeTrend.joinDate, () => chart.setOption({ series: [{ data: employeeTrend.joinCounts }] }))
    const resizeHandler = () => chart.resize()
    window.addEventListener('resize', resizeHandler)
  }
})

</script>
