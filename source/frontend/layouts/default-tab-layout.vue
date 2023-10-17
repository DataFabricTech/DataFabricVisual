<template>
  <div id="wrap">
    <TheHeader></TheHeader>
     <main class="l-full">
      <TheTab></TheTab>
      <slot />
    </main>
    <loader></loader>
  </div>
</template>

<script lang="ts" setup>
import TheHeader from "./data-fabric/the-header.vue";
import TheTab from "./data-fabric/the-tab.vue";
import Loader from "../layouts/loader/loader.vue";

</script>
