<template>
    <section class="l-section gap-[28px]">
      <h3 class="hidden">데이터 검색 결과</h3>
      <BaseTab theme="round-tab" class="round-tab-sm"></BaseTab>
      <div class="result-info">
        <strong class="result-summary">검색 결과는 <em>4</em>건 입니다.</strong>
        <div class="result-sort">
          <div class="toggle toggle-icon toggle-lg">
            <BaseRadio name="rI" id="ic-01">
              <span class="hidden">리스트형 보기</span>
              <svg-icon name="list-view" class="svg-icon"></svg-icon>
            </BaseRadio>
            <BaseRadio name="rI" id="ic-03">
              <span class="hidden">썸네일형 보기</span>
              <svg-icon name="thumbnail-view" class="svg-icon"></svg-icon>
            </BaseRadio>
          </div>
          <BaseSelect class="select-lg"></BaseSelect>
          <BaseSelect class="select-lg"></BaseSelect>
        </div>
      </div>
      <h4 class="category-name">데이터모델 (6)</h4>
      <ul class="card-list">
        <li class="card-item">
          <Card>
            <template v-slot:img>
              <svg-icon name="data" class="svg-icon"></svg-icon>
            </template>
          </Card>
        </li>
        <li class="card-item">
          <Card class="card-primary">
            <template v-slot:img>
              <svg-icon name="data" class="svg-icon"></svg-icon>
            </template>
          </Card>
        </li>
        <li class="card-item">
          <Card class="card-primary">
            <template v-slot:img>
              <svg-icon name="data" class="svg-icon"></svg-icon>
            </template>
          </Card>
        </li>
        <li class="card-item">
          <Card class="card-negative">
            <template v-slot:img>
              <svg-icon name="data" class="svg-icon"></svg-icon>
            </template>
          </Card>
        </li>
      </ul>
      <BasePagination></BasePagination>
      <h4 class="category-name">데이터모델 (6)</h4>
      <div class="no-result">
        <storng class="no-result-title">검색 결과가 없습니다.</storng>
      </div>
      <!--      ...-->
    </section>
</template>
<script lang="ts" setup>
</script>
