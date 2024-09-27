<template>
  <main id="main">
    <section id="section">
      <slot />
    </section>
  </main>
  <Alert />
</template>

<script setup>
import Alert from "~/components/extends/alert/Alert.vue";
</script>
