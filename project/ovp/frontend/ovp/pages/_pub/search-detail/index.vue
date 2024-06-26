<template>
  <div class="wrap">
    <Header />
    <main id="main">
      <h2 class="text-hidden">데이터 상세</h2>
      <Sidebar></Sidebar>
      <section id="section">
        <div class="section-contents">
          <Card class="card-full"></Card>
<!--          <Tab></Tab>-->
        </div>
      </section>
    </main>
  </div>
</template>

<script>
import Header from "../layouts/header.vue";
import Sidebar from "../layouts/sidebar.vue";
import Card from "../../ovp/components/card/card.vue";
// import Tab from "../../common/components/extends/tab/Tab.vue"

export default {
  name: "data-list-detail",
  components: { Sidebar, Header, Card }
};
</script>

<style scoped>
.section-contents{
  background: #fff;
}
</style>