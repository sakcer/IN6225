<template>
  <el-container class="layout-container">
    <el-aside width="200px">
      <el-menu :router="true" :default-active="$route.path" class="h-full">

        <el-menu-item v-for="route in routes" :key="route.path" :index="(userStore.isAdmin ? '/admin/' : '/employee/') + route.path">
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
          <h1 class="text-xl font-bold">Enterprise Management System</h1>
          <el-dropdown>
            <span class="flex items-center cursor-pointer">
              <el-avatar :size="32" :style="{ backgroundColor: getAvatarColor(userStore.userInfo.name || ''), color: '#fff' }">
                {{ getAvatarText(userStore.userInfo.name || '') }}
              </el-avatar>
              <span class="ml-2">{{ userStore.userInfo.name || '' }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleProfile">Profile</el-dropdown-item>
                <el-dropdown-item @click="handleReturn">Home</el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">Logout</el-dropdown-item>
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
import { adminRoutes, employeeRoutes } from '@/router/index'
import { getAvatarColor, getAvatarText } from '@/utils/avatar'
import { useUserStore } from '@/store/userStore'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import { handleAxiosError } from '@/utils/errorMsg'

const userStore = useUserStore();
const routes = userStore.isAdmin ? adminRoutes[0].children : employeeRoutes[0].children
const router = useRouter()

const handleLogout = () => {
  ElMessageBox.confirm('Are you sure you want to log out?', 'Prompt', {
    confirmButtonText: 'Yes',
    cancelButtonText: 'No',
    type: 'warning'
  }).then(() => {
    userStore.logout()
    ElMessage.success('Logout successful')
  }).catch((error) => {
    handleAxiosError(error)
  })
}

const handleReturn = () => {
  if (userStore.isAdmin) { 
    router.push('/admin/dashboard')
  } else {
    router.push('/employee/dashboard')
  }
}

const handleProfile = () => {
  if (userStore.isAdmin) {
    router.push('/admin/profile')
  } else {
    router.push('/employee/profile')
  }
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
