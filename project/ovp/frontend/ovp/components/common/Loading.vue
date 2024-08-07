<template>
  <Loading
    v-if="isLoading"
    :class="props.class"
    :use-hidden-text="props.useHiddenText"
    :use-loader-overlay="props.useLoaderOverlay"
  ></Loading>
</template>

<script setup lang="ts">
import Loading from "@base/loading/Loading.vue";

import { useLoading } from "@/composables/loadingHelper";
const { isLoading } = useLoading();

const props = defineProps({
  class: {
    type: String,
  },
  useHiddenText: {
    type: Boolean,
    default: false,
  },
  useLoaderOverlay: {
    type: Boolean,
    default: false,
  },
});
</script>
