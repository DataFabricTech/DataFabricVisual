<template>
  <div class="notification">
    <div class="notification-content">
      <svg-icon :class="'svg-icon-' + notificationType" class="notification-icon" name="info"></svg-icon>
      <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
      <button class="button button-neutral-ghost button-xs ml-auto" type="button">
        <span class="hidden-text">닫기</span>
        <svg-icon class="button-icon" name="close"></svg-icon>
      </button>
    </div>
    <div v-if="isExtends" class="notification-content">
      <a class="link-button link-button-underline ml-[28px]" href="#none">바로가기</a>
      <div class="capsule-pagination ml-auto">
        <button class="button button-neutral-ghost button-xs" type="button">
          <span class="hidden-text">이전 페이지로 이동</span>
          <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
        </button>
        <dl class="description">
          <dt class="hidden-text">현재 페이지</dt>
          <dd class="is-description-selected">1</dd>
          <dt class="hidden-text">총 페이지 수</dt>
          <dd>3</dd>
        </dl>
        <button class="button button-neutral-ghost button-xs" type="button">
          <span class="hidden-text">다음 페이지로 이동</span>
          <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
        </button>
        <button class="button button-neutral-ghost button-xs" type="button">
          <span class="hidden-text">새로고침</span>
          <svg-icon class="button-icon" name="refresh"></svg-icon>
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
const props = defineProps({
  // TODO: [개발] 상태에 따른 아이콘 출력
  isExtends: {
    type: Boolean,
    default: false
  },
  notificationType: {
    type: String,
    default: "neutral"
  }
});
</script>

<style lang="scss">
// css 컴파일 순서가 Components > Assets 이어서 에러가 출력됩니다. 우선 import를 main.scss로 했고, 추가 검토가 필요합니다.
//@import "Notification";
</style>