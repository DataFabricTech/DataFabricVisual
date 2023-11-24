<!--TODO: [개발] 미사용 layout으로 지우셔도 됩니다.-->
<template>
  <div id="wrap">
    <header id="header">
      <TheHeader></TheHeader>
      <TheTab></TheTab>
    </header>
    <main id="main">
      <slot />
    </main>
    <ModalsContainer/>
  </div>
</template>

<script lang="ts" setup>
import TheHeader from "/layouts/data-fabric/the-header.vue";
import TheTab from "/layouts/data-fabric/the-tab.vue";
import { ModalsContainer } from "vue-final-modal";
</script>
