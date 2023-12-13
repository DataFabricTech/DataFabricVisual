<template>
  <div class="alert" :class="'alert-'+props.theme">
    <div class="alert-body">
      <svg-icon :name="props.theme" class="svg-icon" />
      <div class="alert-contents">
        <p class="alert-text">
          이 얼럿은 사용자에게 정보 상태를 알리기 위해 사용합니다.
        </p>
      </div>
    </div>
    <div class="alert-foot">
      <div class="alert-foot-main-buttons">
        <baseButton class="button-lg button-normal">
          <span class="button-text">취소</span>
        </baseButton>
        <slot>
          <!-- 경고, 에러에서는 button-primary 대신 button-negative 클래스로 대체되어야 합니다. -->
          <baseButton class="button-lg button-primary">
            <span class="button-text">확인</span>
          </baseButton>
        </slot>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  theme: { type: String, default: "normal" }
});
</script>
