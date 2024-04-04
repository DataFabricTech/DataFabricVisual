<template>
  <div class="alert">
    <div class="alert-body">
      <div class="notification-content">
        <!-- TODO: [개발] 상태에 따른 아이콘 출력      -->
        <svg-icon class="notification-icon" name="info"></svg-icon>
        <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
      </div>
    </div>
    <div class="alert-foot">
      <div class="alert-foot-group">
        <button class="button button-ghost button-lg">
          취소
        </button>
      <!-- TODO: [개발] warning,error 유형은 .button-error로 출력 -->
        <button class="button button-primary button-lg">
          확인
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import InlineNotification from "../components/extends/notification/Notification.vue";

export default {
  name: "alert",
  components: { InlineNotification }
};
</script>

<style scoped>

</style>