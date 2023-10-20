<!-- 데이터패브릭 - 검색 -->
<template>
  <div class="l-common">
    <div class="l-inner">
      <h2 class="hidden">테이버패브릭 검색</h2>
      <section>
        <h3 class="hidden">데이터 검색</h3>
        <div class="data-search">
          <!--            TODO: [퍼블리싱] search-field 대체 예정-->
          <BaseTextInput></BaseTextInput>
          <BaseButton class="button-icon button-link">
            <svg-icon name="filter" class="svg-icon"></svg-icon>
          </BaseButton>
        </div>
      </section>
      <section>
        <h3 class="hidden">데이터 검색 결과</h3>
        <BaseTab theme="round-tab"></BaseTab>
        <div class="result-info">
          <h4 class="result-summary">검색 결과는 <em>4</em> 건 입니다.</h4>
          <div class="result-sort">
            <div class="view-type">

            </div>
          </div>
        </div>
        <strong class="result-category">

        </strong>
        <ul class="result-list">
          <li class="result-item">
            <Card>
              <template v-slot:img>
                <svg-icon name="data" class="svg-icon"></svg-icon>
              </template>
            </Card>
          </li>
        </ul>
        <!--      ...-->
      </section>
    </div>
  </div>
</template>
<script lang="ts" setup>
definePageMeta({
  layout: "default-layout"
});

</script>


