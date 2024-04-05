<template>
  <div class="alert">
    <div class="alert-body">
      <!-- TODO: [개발] 얼럿 유형에 따른 적절한 노티피케이션 유형 출력      -->
      <div class="notification">
        <svg-icon class="notification-icon" name="info"></svg-icon>
        <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
      </div>
    </div>
    <div class="alert-foot">
      <div class="alert-foot-group">
        <button class="button button-ghost button-lg">취소</button>
        <!-- TODO: [개발] warning,error 유형은 .button-error로 출력 -->
        <button class="button button-primary button-lg">확인</button>
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

<style scoped></style>
