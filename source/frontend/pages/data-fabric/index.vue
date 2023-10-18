<!-- 데이터패브릭 - 메인 -->
<template>
  <div class="l-fixed">
    <h2 class="hidden">데이터패브릭 메인</h2>
    <h3>NODE API 샘플</h3>
    <button class="button button--secondary" @click="onClickApi">NODE API 샘플</button>
    <baseLoader></baseLoader>
    <baseLoader></baseLoader>
  </div>
</template>
<script lang="ts" setup>
definePageMeta({
  layout: "default-layout",
});


function onClickApi() {
  $fetch("/api/hello").then((res) => {
    console.log(res);
  });
}
</script>
