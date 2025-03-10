<template>
    <el-card shadow="hover">
        <template #header>
            <div class="flex items-center">
                <el-icon class="mr-3">
                    <Bell />
                </el-icon>
                通知设置
            </div>
        </template>
        <div class="space-y-5">
            <div class="flex justify-between items-center">
                <span>系统通知</span>
                <el-switch v-model="notifications.system" />
            </div>
            <div class="flex justify-between items-center">
                <span>项目更新提醒</span>
                <el-switch v-model="notifications.project" />
            </div>
            <div class="flex justify-between items-center">
                <span>任务分配提醒</span>
                <el-switch v-model="notifications.task" />
            </div>
            <div class="flex justify-between items-center">
                <span>邮件通知</span>
                <el-switch v-model="notifications.email" />
            </div>
        </div>
    </el-card>
</template>
