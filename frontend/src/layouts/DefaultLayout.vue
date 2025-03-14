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
              <el-avatar :size="32" :style="{ backgroundColor: getAvatarColor(me.name), color: '#fff' }">
                {{ getAvatarText(me.name) }}
              </el-avatar>
              <span class="ml-2">{{ me.name }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="$router.push('/admin/profile')">个人信息</el-dropdown-item>
                <el-dropdown-item @click="$router.push('/admin/dashboard')">回到首页</el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
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
import { adminRoutes, employeeRoutes } from '@/router/index'
import { localUser } from '@/utils/localUser'
import { getAvatarColor, getAvatarText } from '@/utils/avatar'
import { useMeStore } from '@/store/meStore'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import { USER_ROLES } from '@/utils/constants'
import { computed } from 'vue'

const isAdmin =  localStorage.getItem('role') === USER_ROLES.ADMIN

const routes = isAdmin ? adminRoutes[0].children : employeeRoutes[0].children
const router = useRouter()
const meStore = useMeStore();

const me = computed(() => meStore.getMe)

const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    localStorage.removeItem('token')
    localStorage.removeItem('role')
    localStorage.removeItem('userInfo')
    router.push('/login')
    ElMessage.success('退出登录成功')
  })
}
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
