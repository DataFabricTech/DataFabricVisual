<template>
    <section class="l-section gap-[20px]">
      <h3 class="hidden">데이터 검색</h3>
      <div class="data-search">
        <SearchField></SearchField>
        <BaseButton class="button-icon button-normal" title="상세 검색">
          <span class="hidden">상세 검색</span>
          <svg-icon name="sort" class="svg-icon"></svg-icon>
        </BaseButton>
      </div>
      <div class="data-search-filter">
        <SearchFilter></SearchFilter>
      </div>
    </section>
</template>
<script lang="ts" setup>
</script>
