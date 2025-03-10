<template>
  <el-card shadow="hover">
    <template #header>
      <div class="flex justify-between items-center">
        <span class="text-lg font-bold">详细信息</span>
        <div>
          <el-button type="primary" @click="handleSaveProfile" :loading="saving">
            保存修改
          </el-button>
        </div>
      </div>
    </template>

    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="姓名" prop="name">
            <el-input v-model="form.name" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="昵称" prop="nickname">
            <el-input v-model="form.nickname" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="form.email" type="email" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="手机" prop="phone">
            <el-input v-model="form.phone" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="性别" prop="gender">
            <el-select v-model="form.gender" class="w-full">
              <el-option label="男" value="male" />
              <el-option label="女" value="female" />
              <el-option label="保密" value="other" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="生日" prop="birthday">
            <el-date-picker v-model="form.birthday" type="date" class="w-full" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="所在地" prop="location">
        <el-cascader v-model="form.location" :options="locationOptions" class="w-full" />
      </el-form-item>

      <el-form-item label="个人简介" prop="bio">
        <el-input v-model="form.bio" type="textarea" :rows="4" placeholder="介绍一下自己..." />
      </el-form-item>

      <el-form-item label="技能标签" prop="skills">
        <el-select
          v-model="form.skills"
          multiple
          filterable
          allow-create
          default-first-option
          class="w-full"
          placeholder="请选择或输入技能标签"
        >
          <el-option v-for="skill in skillOptions" :key="skill" :label="skill" :value="skill" />
        </el-select>
      </el-form-item>

      <el-form-item label="社交账号">
        <div class="space-y-4">
          <div class="flex items-center">
            <el-icon class="mr-2"><Link /></el-icon>
            <el-input v-model="form.social.github" placeholder="GitHub" />
          </div>
          <div class="flex items-center">
            <el-icon class="mr-2"><Link /></el-icon>
            <el-input v-model="form.social.linkedin" placeholder="LinkedIn" />
          </div>
          <div class="flex items-center">
            <el-icon class="mr-2"><Link /></el-icon>
            <el-input v-model="form.social.twitter" placeholder="Twitter" />
          </div>
        </div>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup lang="ts">

const props = defineProps({
  form: {
    type: Object,
    required: true,
  },
  rules: {
    type: Object,
    required: true,
  },
  locationOptions: {
    type: Array,
    required: true,
  },
  skillOptions: {
    type: Array,
    required: true,
  },
  saving: {
    type: Boolean,
    required: true,
  }
})

const emit = defineEmits()

const handleSaveProfile = () => {
  emit('saveProfile')
}
</script>
