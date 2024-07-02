<template>
  <div class="section-top-bar">
    <div class="l-top-bar">
      <select-box class="select-clean select-lg"></select-box>
      <button class="button button-primary w-20">저장</button>
    </div>
  </div>
  <div class="section-contents bg-white">
    <div class="l-split">
      <div class="l-panel">
        <div class="l-top-bar">
          <span class="">선택된 데이터 모델 (0)</span>
          <button class="button button-secondary-stroke">추가</button>
        </div>
        <!-- 결과 없을 시 no-result 표시 -->
        <div class="no-result">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">등록된 정보가 없습니다.</p>
          </div>
        </div>
      </div>
      <div class="split-page">
        <div class="l-top-bar">
          <span class="">선택된 데이터 모델 (0)</span>
          <button class="button button-secondary-stroke">추가</button>
        </div>
        <div class="no-result">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">등록된 정보가 없습니다.</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import Header from "../layouts/header.vue";
import Sidebar from "../layouts/sidebar.vue";
import SelectBox from "../../../../common/components/extends/select-box/SelectBox.vue";
import Notification from "../../../../common/components/extends/notification/Notification.vue";


export default {
  name: "creation",
  components: { Sidebar, Header, SelectBox, Notification }
};
</script>

<style scoped>

</style>