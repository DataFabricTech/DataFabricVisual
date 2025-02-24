<template>
  <div class="category">
    <em class="category-label" :title="auth">{{ auth }}</em>
  </div>
</template>

<script setup lang="ts">
import {useNuxtApp} from "nuxt/app";

const { $ofetch } = useNuxtApp();
const emit = defineEmits(["onClickTableBodyButtons"]);
const props = defineProps({
  item: {
    type: Object
  },
  auth: {
    type: String,
    default: ""
  },
  readOnly: {
    type: Boolean,
    default: false
  }
});
</script>
