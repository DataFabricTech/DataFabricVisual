<template>
  <div class="h-screen">
    <NuxtLayout>
      <NuxtPage />
    </NuxtLayout>
    <Loader />
  </div>
</template>
<script setup lang="ts">
import Loader from '@functional/loader/loader'
</script>
