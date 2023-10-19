<!-- 데이터패브릭 - 메인 -->
<template>
  <div class="l-fixed">
    <h2 class="hidden">데이터패브릭 메인</h2>
    <h3>NODE API 샘플</h3>
    <button class="button button--secondary" @click="onClickApi">NODE API 샘플</button>
  </div>
</template>
<script lang="ts" setup>
import { onBeforeMount } from "vue";

definePageMeta({
  layout: "default-layout",
});

onBeforeMount(() => {
  $fetch("/api/hello").then((res) => {
    console.log(res);
  });
  // todo: api 동시 호출 시 loader 버그
  // $fetch("/api/hetest").then((res) => {
  //   console.log(res);
  // });
})


function onClickApi() {
  $fetch("/api/error").then((res) => {
    console.log(res);
  });
}
</script>
