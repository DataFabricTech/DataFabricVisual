<script setup lang="ts">
const { $loader } = useNuxtApp();

definePageMeta({
  layout: "default-layout",
});

onMounted(() => {
  fetchApiHello();
  fetchApiHetest();
})

function fetchApiHello() {
  $fetch("/api/hello").then((res) => {
    console.log("/api/hello -", res);
  });
}

function fetchApiHetest() {
  $fetch("/api/hetest").then((res) => {
    console.log("/api/hetest -", res);
  });
}

function fetchApiError() {
  $fetch("/api/error").then((res) => {
    console.log("/api/error - ", res);
  });
}

function loaderSimpleTest() {
  console.log("start");
  $loader.start();

  setTimeout(() => {
    console.log("finish 1");
    $loader.finish();
  }, 500)
}

function loaderTest() {
  console.log("start 1");
  $loader.start();
  setTimeout(() => {
    console.log("start 2");
    $loader.start();
  }, 100)
  setTimeout(() => {
    console.log("start 3");
    $loader.start();
  }, 500)
  setTimeout(() => {
    console.log("start 4");
    $loader.start();
  }, 700)

  setTimeout(() => {
    console.log("finish 1");
    $loader.finish();
  }, 50)
  setTimeout(() => {
    console.log("finish 2");
    $loader.finish();
  }, 100)
  setTimeout(() => {
    // loading.hide()
    console.log("finish 3");
    $loader.finish();
  }, 1000)
  setTimeout(() => {
    // loading.hide()
    console.log("finish 4");
    $loader.finish();
  }, 1500)
}
</script>

<template>
  <div class="l-fixed">
    <h2 class="hidden">Loader/API 샘플</h2>
    <h3>Loader/API 샘플</h3>
    <button class="button button--secondary" @click="fetchApiHello">call api hello</button>
    <button class="button button--secondary" @click="fetchApiError">call api error</button>
    <button class="button button--secondary" @click="loaderSimpleTest">loader simple test</button>
    <button class="button button--secondary" @click="loaderTest">loader test</button>
  </div>
</template>

<style scoped lang="scss">

</style>
