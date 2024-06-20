<template>
  <div class="wrap">
    <Header />
    <main id="main">
      <Sidebar></Sidebar>
      <section id="section">
        <div class="filters">
          <div class="select select-clean" v-for="n in 5" :key="n">
            <button class="select-button">
              <span class="select-button-title">카테고리</span>
              <div class="badge badge-primary-lighter">
                <p class="badge-text">10</p>
              </div>
<!--              <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>-->
              <svg class="svg-icon select-indicator" xmlns="http://www.w3.org/2000/svg" width="33" height="32" fill="currentColor" viewBox="0 0 33 32">
                <path fill-rule="evenodd" d="M8 12.768 9.768 11l6.616 6.616L23 11l1.768 1.768-8.384 8.384L8 12.768Z" clip-rule="evenodd"/>
              </svg>
            </button>
          </div>
          <button class="button button-error-lighter button-sm" type="button">
            <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
            <span class="button-title">초기화</span>
          </button>
        </div>
        <div class="result">
          <div class="result-top-bar">
            <span class="">총 <strong>68건</strong>의 검색 결과가 있습니다.</span>
            <div class="h-group gap-2">

            </div>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script>
import Header from "../layouts/header.vue";
import Sidebar from "../layouts/sidebar.vue";



export default {
  name: "data-list",
  components: { Sidebar, Header }
};
</script>

<style scoped>

</style>