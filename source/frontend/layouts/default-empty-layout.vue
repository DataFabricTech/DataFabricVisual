<template>
  <div id="wrap">
    <main>
      <slot />
    </main>
    <loader></loader>
  </div>
</template>

<script lang="ts" setup>
import Loader from "../layouts/loader/loader.vue";

</script>
