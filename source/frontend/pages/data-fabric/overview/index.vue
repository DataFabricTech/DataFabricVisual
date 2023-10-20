<!-- 저장소 관리 Overview-->
<template>
  <div class="l-split">
    <div class="l-inner">
      <h2 class="hidden">저장소 관리</h2>
      <div class="snb">
        <h3 class="hidden">필터</h3>
        <div class="search-tree">
          <div class="v-group w-full">
            <div class="h-group ml-auto">
              <BaseButton class="button-icon button-link button-sm" title="정렬">
                <span class="hidden">정렬</span>
                <svg-icon name="sort" class="svg-icon"></svg-icon>
              </BaseButton>
              <BaseButton class="button-icon button-link button-sm" title="필터">
                <span class="hidden">필터</span>
                <svg-icon name="filter" class="svg-icon"></svg-icon>
              </BaseButton>
            </div>
            <baseTextInput></baseTextInput>
          </div>
          <div class="search-tree-contents">트리영역</div>
          <BaseButton class="button-normal button-lg ml-auto">
            <span class="button-text">연결정보 등록</span>
          </BaseButton>
        </div>
      </div>
      <div class="main-content">
        <h3 class="hidden">검색 결과 목록</h3>
        CONTENTS
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>

definePageMeta({
  layout: "default-tab-layout",
});

</script>

