<!-- 데이터패브릭포털 - 검색 -->
<template>
  <div class="l-common">
    <h2 class="hidden">데이터패브릭포털 검색</h2>
    <div class="l-inner flex-col">
      <section class="l-section">
        <h3 class="hidden">데이터 검색</h3>
        <div class="w-full flex items-center flex-col">
        <SearchField class="search-field-lg"></SearchField>
          <div class="flex">
            <span>연관 검색어</span>
            <div>
              <span>CCTV 위치</span>
              <span>행정동별 주정차단속구간</span>
              <span>주정차단속민원</span>
              <span>CCTV민원현황</span>
            </div>
          </div>
        </div>
      </section>
      <section class="l-section gap-[28px]">
        <h3 class="hidden">데이터패브릭포털 검색 결과</h3>
        <BaseTab theme="round-tab" class=""></BaseTab>
        <div class="result-info">
          <strong class="result-summary">검색 결과는 <em>4</em>건 입니다.</strong>
        </div>
      </section>
    </div>
  </div>
</template>
<script lang="ts" setup>

definePageMeta({
  layout: "default-portal-layout",
});

</script>


