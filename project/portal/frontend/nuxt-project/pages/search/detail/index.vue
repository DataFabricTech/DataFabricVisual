<template>
  <search-detail :isAdmin="false" />
</template>

<script setup lang="ts">
import SearchDetail from "@organisms/searchDetail/search-detail";
import { useUserStore } from "~/store/user";

definePageMeta({
  name: "searchDetailByUser",
  layout: "user",
});

const userStore = useUserStore();

// 다른 화면으로 이동하기 전 store 내 guid 정리
onBeforeRouteLeave((to, from, next) => {
  userStore.removeStoreGuid({ to: to.path, from: from.path });
  next();
});
</script>
