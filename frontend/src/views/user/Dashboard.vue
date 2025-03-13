<template>
  <div class="container mx-auto p-6">
    <!-- 顶部信息栏 -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
      <!-- 时间卡片 -->
      <el-card shadow="hover" class="bg-gradient-to-r from-blue-500 to-blue-600">
        <div class="text-white">
          <div class="text-sm">{{ currentDate }}</div>
          <div class="text-2xl font-bold">{{ currentTime }}</div>
          <div class="text-sm">{{ weekDay }}</div>
        </div>
      </el-card>

      <!-- 个人信息卡片 -->
      <el-card shadow="hover" class="bg-gradient-to-r from-purple-500 to-purple-600">
        <div class="flex items-center text-white">
          <el-avatar :size="50" :src="mockUser.avatar"
            :style="{ backgroundColor: getAvatarColor(mockUser.name || '') }">
            {{ getAvatarText(mockUser.name) }}
          </el-avatar>
          <div class="ml-4">
            <div class="text-lg font-bold">{{ mockUser.name }}</div>
            <div class="text-sm">{{ mockUser.role }}</div>
            <div class="text-sm">{{ mockUser.department }}</div>
          </div>
        </div>
      </el-card>

      <!-- 项目统计卡片 -->
      <el-card shadow="hover" class="bg-gradient-to-r from-green-500 to-green-600">
        <div class="text-white">
          <div class="text-lg font-bold">项目统计</div>
          <div class="flex justify-between mt-2">
            <div>
              <div class="text-sm">参与项目</div>
              <div class="text-xl font-bold">{{ mockProjects.length }}</div>
            </div>
            <div>
              <div class="text-sm">管理项目</div>
              <div class="text-xl font-bold">{{ managedProjects.length }}</div>
            </div>
          </div>
        </div>
      </el-card>
    </div>


    <!-- 管理的项目 -->
    <div class="mb-6">
      <h2 class="text-xl font-bold mb-4">我管理的项目</h2>

      <div class="flex justify-between items-center mb-4">

        <div class="mb-6 flex flex-wrap gap-4">

            <status-toggle v-model="status" :status="PROJECT_STATUS" />
            <search-input v-model="searchString" placeholder="搜索项目" />

            <div class="ml-auto">
            <div class="view-switch">
            <el-radio-group v-model="viewMode" size="small">
                <el-radio-button label="card">卡片视图</el-radio-button>
                <el-radio-button label="list">列表视图</el-radio-button>
            </el-radio-group>

            </div>
            </div>
        </div>
      </div>

      <!-- 卡片视图 -->
      <div v-if="viewMode === 'card'" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        <div v-for="project in filteredProjects" :key="project.id">
          <ProjectCard 
            :project="project"
            :is-manager="true"
            @edit="handleEditProject"
            @view="handleViewProject"
          />
        </div>
      </div>

      <!-- 列表视图 -->
      <div v-else>
        <el-table :data="filteredProjects" style="width: 100%">
          <el-table-column prop="name" label="项目名称" min-width="150">
            <template #default="{ row }">
              <div class="flex items-center">
                {{ row.name }}
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="项目状态" min-width="150">
            <template #default="{ row }">
              <div class="flex items-center">
                <el-tag 
                  :type="row.status === PROJECT_STATUS.ACTIVE ? 'success' : 'info'"
                  size="small" 
                  class="mr-2"
                >
                  {{ row.status }}
                </el-tag>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
          <el-table-column prop="progress" label="进度" width="200">
            <template #default="{ row }">
              <el-progress 
                :percentage="row.progress"
                :status="row.progress === 100 ? 'success' : ''"
              />
            </template>
          </el-table-column>
          <el-table-column label="时间" width="200">
            <template #default="{ row }">
              <div class="text-sm text-gray-500">
                {{ row.startDate }} - {{ row.endDate }}
              </div>
            </template>
          </el-table-column>
            <el-table-column label="项目负责人" width="100">
            <template #default="{ row }">
                <el-tooltip :content="users.find(user => user.id === row.leaderId)?.name" placement="top">
                    <el-avatar :size="30" :src="users.find(user => user.id === row.leaderId)?.avatar"
                    :style="{ backgroundColor: getAvatarColor(users.find(user => user.id === row.leaderId)?.name || '') }">
                    {{ getAvatarText(users.find(user => user.id === row.leaderId)?.name) }}
                    </el-avatar>
                </el-tooltip>
            </template>
            </el-table-column>
            <el-table-column label="项目成员" width="150" >
            <template #default="{ row }" >
            <el-tooltip v-for="member in row.memberIds?.slice(0, 2)" :key="member.id" :content="users.find(user => user.id === member)?.name" placement="top">
            <template v-if="member">
                <!-- Get matching user -->
                <template v-if="users.some(user => user.id === member)">
                <el-avatar :size="32" :src="users.find(user => user.id === member)?.avatar"
                    :style="{ backgroundColor: getAvatarColor(users.find(user => user.id === member)?.name) }">
                    {{ getAvatarText(users.find(user => user.id === member)?.name) }}
                </el-avatar>
                </template>
            </template>
            </el-tooltip>
            <el-tooltip v-if="row.memberIds?.length > 2" :content="row.memberIds?.slice(2).map(m => users.find(user => user.id === m)?.name).join(', ')"
                placement="top">
                <el-avatar :size="32">
                    +{{ row.memberIds?.length - 2 }}
                </el-avatar>
            </el-tooltip>
            </template>
            </el-table-column>
          <el-table-column label="操作" width="120" fixed="right">
            <template #default="{ row }">
              <el-button-group>
                <el-tooltip content="查看详情" placement="top">
                  <el-button 
                    type="info" 
                    :icon="View" 
                    circle 
                    @click="handleViewProject(row)"
                  />
                </el-tooltip>
                <el-tooltip content="编辑项目" placement="top">
                  <el-button 
                    type="primary" 
                    :icon="Edit" 
                    circle 
                    @click="handleEditProject(row)"
                    :disabled="row.leaderId !== mockUser.id"
                  />
                </el-tooltip>
              </el-button-group>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <!-- 项目编辑/查看对话框 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? 'Edit Project' : 'Add Project'" width="50%">
        <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <!-- 项目名称 -->
        <el-form-item label="Name" prop="name">
            <el-input v-model="form.name" />
        </el-form-item>

        <!-- 项目描述 -->
        <el-form-item label="Description" prop="description">
            <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>

        <!-- 项目状态 -->
        <el-form-item label="Status" prop="status">
            <el-select v-model="form.status" class="w-full">
            <el-option label="Active" :value="PROJECT_STATUS.ACTIVE" />
            <el-option label="Completed" :value="PROJECT_STATUS.COMPLETED" />
            </el-select>
        </el-form-item>

        <!-- 项目进度 -->
        <el-form-item label="Progress" prop="progress">
            <el-slider v-model="form.progress" :step="10" show-stops />
        </el-form-item>

        <!-- 项目负责人 -->
        <el-form-item label="Leader" prop="leader">
            <el-select v-model="form.leaderId" class="w-full" filterable  :disabled="true">
            <el-option v-for="user in users" :key="user.id" :label="user.name" :value="user.id">
                <div class="flex items-center">
                <el-avatar :size="24" :style="{ backgroundColor: getAvatarColor(user.name) }" class="mr-2">
                    {{ getAvatarText(user.name) }}
                </el-avatar>
                {{ user.name }}
                </div>
            </el-option>
            </el-select>
        </el-form-item>

        <!-- 项目成员 -->
        <el-form-item label="Members" prop="members">
            <el-select v-model="form.memberIds" multiple filterable class="w-full">
            <el-option v-for="user in users" :key="user.id" :label="user.name" :value="user.id">
                <div class="flex items-center">
                <el-avatar :size="24" :style="{ backgroundColor: getAvatarColor(user.name) }" class="mr-2">
                    {{ getAvatarText(user.name) }}
                </el-avatar>
                {{ user.name }}
                </div>
            </el-option>
            </el-select>
        </el-form-item>
        </el-form>

        <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave" v-if="isEdit">保存</el-button>
        </span>
      </template>
    </el-dialog>
    <!-- <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑项目' : '项目详情'" width="50%">
      <el-form :model="form" label-width="120px">
        <el-form-item label="项目名称">
          <el-input v-model="form.name" :disabled="!isEdit" />
        </el-form-item>
        <el-form-item label="项目描述">
          <el-input v-model="form.description" type="textarea" :disabled="!isEdit" />
        </el-form-item>
        <el-form-item label="项目状态">
          <el-select v-model="form.status" :disabled="!isEdit">
            <el-option v-for="status in PROJECT_STATUS" :key="status" :label="status" :value="status" />
          </el-select>
        </el-form-item>
        <el-form-item label="项目进度">
          <el-slider v-model="form.progress" :disabled="!isEdit" />
        </el-form-item>

      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave" v-if="isEdit">保存</el-button>
        </span>
      </template>
    </el-dialog> -->
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import ProjectCard from '@/views/projects/widgets/ProjectCard.vue'
import SearchInput from '@/components/SearchInput.vue'
import StatusToggle from '@/components/StatusToggle.vue'
import { View, Edit } from '@element-plus/icons-vue'
import type { Project, User } from '@/types'
import { onMounted } from 'vue'
import { useMeStore } from '@/store/meStore'
import { useUsersStore } from '@/store/userStore'
import { useMyProjectStore } from '@/store/myProjectStore'
import { PROJECT_STATUS } from '@/utils/constants'
import { getAvatarColor, getAvatarText } from '@/utils/avatar'
import { projectService } from '@/services/projects/projectService'
import { ElMessage } from 'element-plus'

// 状态
const dialogVisible = ref(false)
const isEdit = ref(false)
const rules = ref({})
const formRef = ref()
const form = ref<Project>({})

const status = ref(PROJECT_STATUS.ALL)
const meStore = useMeStore()
const usersStore = useUsersStore()

const myProjectStore = useMyProjectStore()
const users = computed(() => usersStore.users)

// 视图模式
const viewMode = ref('card') // 'card' | 'list'
const searchString = ref('');

// 模拟数据
const mockUser: User = computed(() => meStore.me)

const mockProjects: Project[] = computed(() => myProjectStore.projects)

const filteredProjects = computed(() => {
  return mockProjects.value.filter(project => {
    const matchesStatus = status.value === PROJECT_STATUS.ALL || project.status === status.value
    const matchesSearch = project.name.toLowerCase().includes(searchString.value.toLowerCase()) ||
      project.description.toLowerCase().includes(searchString.value.toLowerCase())
    return matchesStatus && matchesSearch
  })
})

// 计算属性
const managedProjects = computed(() => {
    console.log(mockProjects.value);
    return mockProjects.value.filter(p => p.leaderId === mockUser.value.id)
})

const participatedProjects = computed(() => 
    mockProjects.value
)

// 时间相关
const currentDate = ref(new Date().toLocaleDateString('zh-CN'))
const currentTime = ref(new Date().toLocaleTimeString('zh-CN'))
const weekDay = ref(['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'][new Date().getDay()])

// 方法
const updateTime = () => {
  const now = new Date()
  currentDate.value = now.toLocaleDateString('zh-CN')
  currentTime.value = now.toLocaleTimeString('zh-CN')
  weekDay.value = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'][now.getDay()]
}

const handleEditProject = (project: Project) => {
  isEdit.value = true
  form.value = { ...project }
  dialogVisible.value = true
}

const handleViewProject = (project: Project) => {
  isEdit.value = false
  form.value = { ...project }
  dialogVisible.value = true
}

const handleSave = async () => {
  try {
    // console.log(form.value)
    const data = await projectService.updateProject(form.value.id, form.value)
    console.log(data)
    dialogVisible.value = false
    myProjectStore.refetchProjects()
    ElMessage.success('项目更新成功')
  } catch (error) {
    console.error(error)
    ElMessage.error('项目更新失败')
  }
}

// 生命周期
onMounted(() => {
  updateTime()
  setInterval(updateTime, 1000)
})
</script>

<style scoped>
.el-card {
  margin-bottom: 1rem;
}

.el-table {
  margin-bottom: 1rem;
}
</style>
