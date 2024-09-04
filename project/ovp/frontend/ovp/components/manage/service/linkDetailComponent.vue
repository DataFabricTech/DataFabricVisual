<template>
  <a
    href="javascript:void(0);"
    class="link-button link-button-underline"
    title="상세 페이지 이동"
    @click="rowClick()"
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
const rowClick = () => {
  router.push(
    `/portal/search/detail?type=${props.params.data.serviceType}&id=${props.params.data.id}&fqn=${props.params.data.fqn}`,
  );
};
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
