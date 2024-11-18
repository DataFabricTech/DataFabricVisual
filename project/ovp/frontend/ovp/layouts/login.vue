<template>
  <slot :key="route.fullPath" />
  <Loading class="loader-lg"></Loading>

  <ModalsContainer />
  <Alert />
</template>
<script setup lang="ts">
import { useRoute } from "nuxt/app";
import Loading from "~/components/common/Loading.vue";
import Alert from "@extends/alert/Alert.vue";
import { ModalsContainer } from "vue-final-modal";
const route = useRoute();
</script>
