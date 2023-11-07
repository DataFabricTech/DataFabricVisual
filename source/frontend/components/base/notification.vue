<template>
  <!--      기본: normal, 정보: info, 성공: success, 경고: warning, 에러: error (icon도 동일명)-->
  <div class="notification" :class="'notification-'+props.theme">
    <div class="notification-icon">
      <svg-icon :name="props.theme" class="svg-icon"></svg-icon>
    </div>
    <div class="notification-content">
      <div class="notification-body">
        <div class="notification-message">
          <slot></slot>
        </div>
        <BaseButton class="button-icon button-link button-sm" title="닫기" v-if="props.useClose">
          <span class="hidden-text">닫기</span>
          <svg-icon name="close" class="svg-icon"></svg-icon>
        </BaseButton>
      </div>
      <div class="notification-footer">
        <a href="#" class="notification-link">
          바로가기
        </a>
        <div class="h-group">
          <base-button class="button-link no-underline">
            &lt;
          </base-button>
          <div class="notification-page-num">
            <!-- 현재 페이지일떄 활성화 클래스 is-current 추가 -->
            <span class="is-current">1</span>
            <span>/</span>
            <span>4</span>
          </div>
          <base-button class="button-link no-underline">
            &gt;
          </base-button>
          <base-button class="button-link button-sm">
            <span class="hidden-text">동기화</span>
            <svg-icon class="svg-icon" name="update"></svg-icon>
          </base-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  theme: { type: String, default: "normal" },
  useClose: { type: Boolean, default: false }
});
</script>
