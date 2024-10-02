<template>
  <div class="wrap">
    <Header />
    <main id="main">
      <Loading class="loader-lg"></Loading>
      <!-- TODO : h2 태그에 페이지 명이 들어가는데, 추후에 menu 에서 페이지 명을 받아와서 처리하는걸로 수정필요.-->
      <h2 class="hidden-text"></h2>
      <Sidebar></Sidebar>
      <section id="section">
        <slot />
      </section>
    </main>
  </div>
  <ModalsContainer />
  <Alert />
</template>
<script setup lang="ts">
import Header from "./header.vue";
import Sidebar from "./sidebar.vue";

import { useRoute, useHead } from "nuxt/app";
import Loading from "~/components/common/Loading.vue";
import { ModalsContainer } from "vue-final-modal";
import Alert from "@extends/alert/Alert.vue";

const route = useRoute();
</script>
