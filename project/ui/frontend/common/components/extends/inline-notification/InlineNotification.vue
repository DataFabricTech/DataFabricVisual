<template>
  <div class="inline-notification">
    <svg-icon class="inline-notification-icon" name="info"></svg-icon>
    <p class="inline-notification-detail">얼럿 메세지를 입력해주세요.</p>
  </div>
</template>

<script lang="ts" setup>
const props = defineProps({
  // TODO: [개발] 상태에 따른 아이콘 출력
});
</script>

<style lang="scss">
// css 컴파일 순서가 Components > Assets 이어서 에러가 출력됩니다. 우선 import를 main.scss로 했고, 추가 검토가 필요합니다.
//@import "InlineNotification";
</style>