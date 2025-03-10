<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <div class="flex items-center">
              <el-icon class="mr-2">
                <User />
              </el-icon>
              员工总数
            </div>
          </template>
          <div class="text-3xl font-bold text-center">{{ statistics.totalEmployees }}</div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <div class="flex items-center">
              <el-icon class="mr-2">
                <Box />
              </el-icon>
              部门数量
            </div>
          </template>
          <div class="text-3xl font-bold text-center">{{ statistics.totalDepartments }}</div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <div class="flex items-center">
              <el-icon class="mr-2">
                <Calendar />
              </el-icon>
              本月新入职
            </div>
          </template>
          <div class="text-3xl font-bold text-center">{{ statistics.newEmployees }}</div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <div class="flex items-center">
              <el-icon class="mr-2">
                <Promotion />
              </el-icon>
              本月离职
            </div>
          </template>
          <div class="text-3xl font-bold text-center">{{ statistics.leavingEmployees }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-6">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="flex items-center">
              <el-icon class="mr-2">
                <PieChart />
              </el-icon>
              部门人员分布
            </div>
          </template>
          <div ref="departmentChartRef" style="height: 300px"></div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="flex items-center">
              <el-icon class="mr-2">
                <TrendCharts />
              </el-icon>
              员工变动趋势
            </div>
          </template>
          <div ref="trendChartRef" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { User, Box, Calendar, Promotion, PieChart, TrendCharts } from '@element-plus/icons-vue'
import * as echarts from 'echarts'

const statistics = ref({
  totalEmployees: 156,
  totalDepartments: 8,
  newEmployees: 12,
  leavingEmployees: 3
})

const departmentChartRef = ref<HTMLElement>()
const trendChartRef = ref<HTMLElement>()

onMounted(() => {
  // 初始化部门分布图表
  if (departmentChartRef.value) {
    const departmentChart = echarts.init(departmentChartRef.value)
    departmentChart.setOption({
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          type: 'pie',
          radius: '50%',
          data: [
            { value: 45, name: '技术部' },
            { value: 25, name: '人事部' },
            { value: 30, name: '财务部' },
            { value: 35, name: '市场部' },
            { value: 21, name: '运营部' }
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    })
  }

  // 初始化趋势图表
  if (trendChartRef.value) {
    const trendChart = echarts.init(trendChartRef.value)
    trendChart.setOption({
      tooltip: {
        trigger: 'axis'
      },
      legend: {
        data: ['入职人数', '离职人数']
      },
      xAxis: {
        type: 'category',
        data: ['1月', '2月', '3月', '4月', '5月', '6月']
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '入职人数',
          type: 'line',
          data: [10, 8, 12, 15, 12, 12]
        },
        {
          name: '离职人数',
          type: 'line',
          data: [2, 3, 1, 4, 2, 3]
        }
      ]
    })
  }
})
</script>