<script setup lang="ts">
import { useProjectsStore } from '@/store/projectStore';
import { onBeforeMount } from 'vue';
import { useUsersStore } from '@/store/userStore';
import { useMeStore } from '@/store/meStore';

const projectsStore = useProjectsStore();
const usersStore = useUsersStore();
const meStore = useMeStore();

onBeforeMount(async () => {
  await projectsStore.refetchProjects();
  await usersStore.refetchUsers();
  await meStore.refetchMe();
});
</script>

<template>
  <router-view></router-view>
</template>

<style>
#app {
  height: 100vh;
  width: 100vw;
}
</style>
