<template>
  <div class="mu-modal" v-if="props.airflowUrl">
    <div class="mu-dialog-background"></div>
    <div class="mu-dialog" style="width: 90%; height: 90%">
      <div class="mu-dialog-head">
        <p class="title">수집 이력</p>
      </div>
      <div class="mu-dialog-body">
        <div v-if="props.airflowUrl">
          <iframe
            :src="props.airflowUrl"
            width="100%"
            height="900px" />
        </div>
      </div>
      <div class="mu-dialog-foot">
        <button type="button" class="mu-btn" @click="onClose">
          <span>닫기</span>
        </button>
      </div>
      <!-- 접근성 : 내용을 읽고 나서 닫기 나중으로 빼준다  -->
      <div class="mu-dialog-close-btn">
        <button type="button" class="mu-btn mu-btn-close" title="해당 팝업 닫기" @click="onClose">
          <i class="mu-icon-svg close"></i><span>팝업닫기</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">

const emit = defineEmits(["onCloseModal"]);
const props = defineProps([
  "airflowUrl",
  "toggleCollectorModal", // 부서 선택 모달 토글 제어 flag
]);

// 모달 닫기 이벤트 핸들러
const onClose = () => {
  emit("onCloseModal");
};

</script>
<style scoped>
/* Chrome, Safari, Edge, Opera */
input[type="number"]::-webkit-inner-spin-button,
input[type="number"]::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}
</style>
