<script setup lang="ts">
import { useProjectsStore } from '@/store/projectStore';
import { onBeforeMount } from 'vue';
import { useUsersStore } from '@/store/userStore';
import { useMeStore } from '@/store/meStore';
import { useMyProjectStore } from '@/store/myProjectStore'; 

const projectsStore = useProjectsStore();
const usersStore = useUsersStore();
const meStore = useMeStore();
const myProjectStore = useMyProjectStore();

onBeforeMount(async () => {
  await projectsStore.refetchProjects();
  await usersStore.refetchUsers();
  await meStore.refetchMe();
  await myProjectStore.refetchProjects();
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
