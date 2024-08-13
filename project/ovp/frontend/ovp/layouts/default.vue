<template>
  <div class="wrap">
    <Header />
    <main id="main">
      <Loading class="loader-lg"></Loading>
      <!-- TODO : h2 태그에 페이지 명이 들어가는데, 추후에 menu 에서 페이지 명을 받아와서 처리하는걸로 수정필요.-->
      <h2 class="hidden-text"></h2>
      <Sidebar></Sidebar>
      <section id="section">
        <slot :key="route.fullPath" />
      </section>
    </main>
  </div>
</template>
<script setup lang="ts">
import Header from "./header.vue";
import Sidebar from "./sidebar.vue";

import { useRoute } from "nuxt/app";
import Loading from "~/components/common/Loading.vue";

const route = useRoute();
</script>
