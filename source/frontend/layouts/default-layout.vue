<template>
  <div id="wrap">
    <TheHeader></TheHeader>
    <main>
      <slot />
    </main>
    <loader></loader>
  </div>
</template>

<script lang="ts" setup>
import TheHeader from "./data-fabric/the-header.vue";
import Loader from "../layouts/loader/loader.vue";

</script>
