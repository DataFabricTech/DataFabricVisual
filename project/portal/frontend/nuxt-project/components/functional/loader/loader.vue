<template>
  <div class="mu-modal " v-show="loading">
    <div class="mu-dialog-background loader-background"></div>
    <div class="mu-preloader-wrap">
      <div class="mu-preloader">
        <i class="loader"></i>
        <p>불러오는 중입니다.<span></span></p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
const nuxtApp = useNuxtApp();
const loading = ref(false);
let loadingList = []
nuxtApp.hook("page:start", () => {
  loadingList.push(true);
  if (loadingList.length > 0) {
    loading.value = true;
  }

});
nuxtApp.hook("page:finish", () => {
  setTimeout(() => {
    loadingList.pop()
    if (loadingList.length < 1) {
      loading.value = false;
    }
  }, 500);
});
</script>

<style lang="scss" scoped>
@import "loader";

.loader-background {
  opacity: 0.3
}
</style>
