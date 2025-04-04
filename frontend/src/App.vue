<script setup lang="ts">
// import { useProjectsStore } from '@/store/projectStore';
// import { onBeforeMount } from 'vue';
// import { useUserStore } from './store/meStore';
// import { useMeStore } from '@/store/meStore';
// import { useMyProjectStore } from '@/store/myProjectStore'; 

// const projectsStore = useProjectsStore();
// const usersStore = useUsersStore();
// const meStore = useMeStore();
// const myProjectStore = useMyProjectStore();

// onBeforeMount(async () => {
//   const userStore = useUserStore();
//   await userStore.refreshAccessToken();

// });

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
