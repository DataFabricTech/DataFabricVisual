<template>
  <div id="app" class="guide">
    <guide-lnb></guide-lnb>
    <div class="guide-container">
      <main class="guide-main">
        <router-view></router-view>
<!--        <guide-button></guide-button>-->
      </main>
    </div>
  </div>
</template>

<script setup>
import GuideLnb from "../design-guide/pages/guide-lnb/guide-lnb.vue";
// import GuideButton from "./pages/button/guide-button.vue"

</script>

<style lang="css" scoped></style>
