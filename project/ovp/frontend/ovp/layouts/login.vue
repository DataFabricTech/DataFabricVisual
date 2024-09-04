<template>
  <slot :key="route.fullPath" />
  <Loading class="loader-lg"></Loading>
</template>
<script setup lang="ts">
import { useRoute } from "nuxt/app";
import Loading from "~/components/common/Loading.vue";
const route = useRoute();
</script>
