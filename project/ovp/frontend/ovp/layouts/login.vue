<template>
  <slot :key="route.fullPath" />
</template>
<script setup lang="ts">
import { useRoute } from "nuxt/app";
const route = useRoute();
</script>
