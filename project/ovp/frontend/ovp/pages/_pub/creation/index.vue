<template>
  <div class="wrap">
    <Header />
    <main id="main">
      <h2 class="hidden-text">데이터 생성</h2>
      <Sidebar></Sidebar>
      <section id="section">
        <div class=""></div>
        <div class="l-split gap-0"></div>
      </section>
    </main>
  </div>
</template>

<script>
import Header from "../layouts/header.vue";
import Sidebar from "../layouts/sidebar.vue";


export default {
  name: "creation",
  components: { Sidebar, Header }
};
</script>

<style scoped>

</style>