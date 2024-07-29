<template>
  <div class="wrap">
    <div class="no-result no-result-lg no-result-bg">
      <svg-icon class="svg-icon" name="denied"></svg-icon>
      <div class="no-result-group">
        <span class="no-result-title">죄송합니다.</span>
        <span class="no-result-desc">페이지가 없거나 오류가 발생하였습니다.<br>
            현재 페이지가 존재 하지 않거나 이용할 수 없는 페이지 입니다.
        </span>
      </div>
      <button class="button button-primary button-lg">
        로그인 페이지로 돌아가기
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
// import { useI18n } from "vue-i18n";
// import messages from "./index.json";
//
// const { t } = useI18n({
//   messages
// });
</script>

<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
