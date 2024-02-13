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
        <svg v-for="index in 5" :key="index" class="guide-icon" xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 32 32">
          <path fill-rule="evenodd" d="m26 13.702-10-7.66-10 7.66v12.255h6V19.83c0-1.128.895-2.043 2-2.043h4c1.105 0 2 .915 2 2.043v6.127h6V13.702Zm-8 6.128v6.127C18 26.98 18.5 28 20 28h6c1.105 0 2-.915 2-2.043V13.702c0-.643-.296-1.248-.8-1.634l-10-7.66a1.967 1.967 0 0 0-2.4 0l-10 7.66c-.504.386-.8.991-.8 1.634v12.255C4 27.085 4.895 28 6 28h6c1.5 0 2-1.021 2-2.043V19.83h4Z" clip-rule="evenodd"/>
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
