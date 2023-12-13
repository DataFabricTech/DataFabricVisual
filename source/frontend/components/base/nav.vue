<template>
  <nav class="nav">
    <!--        현재 위치인 경우 is-selected 클래스 추가 -->
    <div class="nav-item" :class="item.isSelected? 'is-selected' : ''" v-for="item in state.navList" :key="item.id">
      <button type="button" class="nav-button">
        <svg-icon name="data" class="svg-icon" />
        <span class="nav-title">{{ item.title }}</span>
      </button>
      <button type="button" class="nav-close-button" title="닫기">
        <svg-icon name="close" class="svg-icon" />
        <span class="hidden-text">닫기</span>
      </button>
    </div>
  </nav>
</template>

<script setup lang="ts">
const state = reactive({
  navList: [
    {
      id: 1,
      title: "데이터 모델 BrowserBrowserBrowser",
      isSelected: true
    },
    {
      id: 2,
      title: "데이터 모델 A",
      isSelected: false
    },
    {
      id: 3,
      title: "데이터 모델 B",
      isSelected: false
    },
    {
      id: 4,
      title: "데이터 모델 CCC",
      isSelected: false
    }
  ]
});


</script>
