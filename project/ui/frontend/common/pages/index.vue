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
        <svg-icon name="home" v-for="index in 5" :key="index" class="svg-icon icon-home"></svg-icon>
        <svg-icon class="svg-icon icon-trash" name="trash"></svg-icon>
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
</style>
