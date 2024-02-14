<template>
  <div>
    <h1>
      Fabric-design
    </h1>
    <section class="guide-section">
      <h2 class="section-title">color system</h2>
      <ol class="test">
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
      </ol>
    </section>
    <section class="guide-section">
      <h2 class="section-title">shadows</h2>
      <ol class="test shadows">
        <li>shadow-sm</li>
        <li>shadow-md</li>
        <li>shadow-lg</li>
        <li>shadow-xl</li>
      </ol>
    </section>
    <section class="guide-section">
      <h2 class="section-title">typography</h2>
      <div class="">
        <p class="guide-typo" v-for="index in 14" :key="index">데이터패브릭에서 제작한 모든 서비스와 문서에는 Pretendard 폰트를 사용합니다.</p>
      </div>
    </section>
    <section class="guide-section">
      <h2 class="section-title">icongraphy</h2>
      <div class="h-group">
        <svg-icon name="home" v-for="index in 5" :key="index" class="svg-icon icon-home" xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 32 32">
        </svg-icon>
        <svg class="icon-trash" xmlns="http://www.w3.org/2000/svg" width="33" height="32" fill="currentColor" viewBox="0 0 33 32">
          <path fill-rule="evenodd" d="M11.9 5h9v2h-9V5Zm-2 2V5a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v2h5.5v2h-2.211c0 .066-.004.133-.01.2l-1.815 18a2 2 0 0 1-1.99 1.8H10.431a2 2 0 0 1-1.99-1.799l-1.819-18A2.032 2.032 0 0 1 6.612 9H4.4V7h5.5Zm11 2H8.612l1.819 18h11.943l1.814-18H20.9Zm-8.5 5.216L14.388 14l.863 7.953-1.989.216-.862-7.953Zm7.89-.019L18.3 14l-.788 7.961 1.99.197.788-7.96Z" clip-rule="evenodd"/>
        </svg>
      </div>
    </section>



    <section class="guide-section">
      <h2 class="section-title">components</h2>
      <div class="link-buttons">
        <button>
          <NuxtLink to="/dayjs">Dayjs</NuxtLink>
        </button>
        <button>
          <NuxtLink to="/highcharts">HighCharts</NuxtLink>
        </button>
        <button>
          <NuxtLink to="/vue-final-modal">Vue Final Modal</NuxtLink>
        </button>
        <button>
          <NuxtLink to="/ag-grid">Ag-Grid</NuxtLink>
        </button>
        <button>
          <NuxtLink to="/vue-datepicker-next">Vue Datepicker Next</NuxtLink>
        </button>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">

</script>

<style lang="css" scoped>


/*ul {*/
/*  list-style: none;*/
/*  margin: 0;*/
/*  padding: 0;*/
/*  display: flex;*/
/*}*/

/*li {*/
/*  margin-right: 10px;*/
/*  padding: 0;*/
/*  border: 0;*/
/*  float: left;*/
/*  color: white;*/
/*  font-size: 30px;*/
/*  color: black;*/
/*}*/

/*li:hover {*/
/*  background-color: #98E0AD;*/
/*  color: black;*/
/*}*/
</style>
