<template>
  <el-container class="layout-container">
    <el-aside width="200px">
      <el-menu :router="true" class="h-full" default-active="/">

        <el-menu-item v-for="route in routes" :key="route.path" :index="route.path">
          <el-icon>
            <component :is="route.meta.icon" />
          </el-icon>
          <span>{{ route.meta.title }}</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header>
        <div class="flex justify-between items-center h-full">
          <h1 class="text-xl font-bold">企业员工管理系统</h1>
          <el-dropdown>
            <span class="flex items-center cursor-pointer">
              <el-avatar :size="32" />
              <span class="ml-2">管理员</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>个人信息</el-dropdown-item>
                <el-dropdown-item>修改密码</el-dropdown-item>
                <el-dropdown-item divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { Monitor, User, Folder, OfficeBuilding } from '@element-plus/icons-vue'
import { adminRoutes } from '@/router/index'

const routes = adminRoutes[0].children

</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.el-header {
  background-color: white;
  border-bottom: 1px solid #dcdfe6;
}

.el-aside {
  background-color: #304156;
}

.el-menu {
  border-right: none;
}
</style>
