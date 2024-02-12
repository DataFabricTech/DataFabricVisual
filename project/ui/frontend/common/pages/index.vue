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
      <h2 class="section-title">Icongraphy</h2>
      <div class="">
        <span class="guide-icon">데이터패브릭에서 제작한 모든 서비스와 문서에는 Pretendard 폰트를 사용합니다.</span>
      </div>
    </section>




    <ul>
      <li>
        <NuxtLink to="/dayjs">Dayjs</NuxtLink>
      </li>
      <li>
        <NuxtLink to="/highcharts">HighCharts</NuxtLink>
      </li>
      <li>
        <NuxtLink to="/vue-final-modal">Vue Final Modal</NuxtLink>
      </li>
      <li>
        <NuxtLink to="/ag-grid">Ag-Grid</NuxtLink>
      </li>
      <li>
        <NuxtLink to="/vue-datepicker-next">Vue Datepicker Next</NuxtLink>
      </li>
    </ul>
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
