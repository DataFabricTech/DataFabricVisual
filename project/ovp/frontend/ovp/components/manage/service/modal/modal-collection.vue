<!--TODO:[개발] 모달클래스 .modal-padding-16 추가-->
<template>
  <!-- TODO: [개발]  width:620px,height:580px-->
  <div class="modal-fixed vfm--fixed vfm--inset" v-if="showModalLog">
    <div class="modal modal-padding-16" style="width: 620px">
      <div class="modal-head">
        <div class="modal-head-text">
          <span class="modal-head-title">로그</span>
        </div>
        <button
          class="button link-button button-sm"
          type="button"
          @click="showModalLog = false"
        >
          <span class="hidden-text">닫기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
      <div class="modal-body">
        <div class="flex justify-end w-full gap-2">
          <button class="button button-neutral-stroke">새로고침</button>
          <button class="button button-neutral-stroke">복사</button>
        </div>
        <!-- TODO: [개발] 로그 화면 구현 -->
        <div class="log-view"></div>
      </div>
      <div class="modal-foot">
        <div class="modal-foot-group">
          <button class="button button-primary button-lg">확인</button>
        </div>
      </div>
    </div>
  </div>
</template>
