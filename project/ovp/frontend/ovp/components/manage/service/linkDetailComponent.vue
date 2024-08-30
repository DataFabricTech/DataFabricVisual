<template>
  <a
    :href="link"
    class="link-button link-button-underline"
    title="상세 페이지 이동"
  >
    {{ params.data.name }}
  </a>
</template>

<script setup lang="ts">
import { defineProps } from "vue";
import { useRouter } from "vue-router";

const props = defineProps<{
  params: any;
}>();

const router = useRouter();

// 링크 생성
const link = computed(() => {
  return `/portal/search/detail?type=${props.params.data.type}&id=${props.params.data.id}&fqn=${props.params.data.fqn}`;
});
</script>

<style scoped>
.link-button {
  color: #1a73e8;
  text-decoration: none;
  cursor: pointer;
}
.link-button:hover {
  text-decoration: underline;
}
</style>
