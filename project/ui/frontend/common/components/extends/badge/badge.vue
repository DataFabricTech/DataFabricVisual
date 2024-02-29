<template>
  <div class="badge" tabindex="0">
    <svg-icon v-if="isIcon" class="badge-icon" name="folder"></svg-icon>
    <p class="badge-text">badge</p>
<!--    TODO: [개발] badge클래스에 .icon-badge가 적용되는 경우만 hidden-text 출력-->
    <span class="hidden-text">아이콘 뱃지 설명</span>
  </div>
</template>

<script lang="ts" setup>
const props = defineProps({
  // TODO: [개발] 상태에 따른 아이콘 출력
  isIcon: {
    type: Boolean,
    default: false
  }
});
</script>

<style scoped>

</style>