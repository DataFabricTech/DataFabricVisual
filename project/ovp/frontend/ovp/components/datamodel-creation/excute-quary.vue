<template>
  <div class="work-page">
    <div class="l-top-bar">
      <button class="button button-neutral-stroke" @click="play">
        <svg-icon class="svg-icon" name="play"></svg-icon>
        실행
      </button>
      <button class="button button-error-lighter" @click="queryReset">
        초기화
      </button>
    </div>
    <div class="code-box">
      <!-- TODO: 추후 쿼리 텍스트 컴포넌트 적용 필요 -->
      <textarea
        v-model="query"
        @input="editQueryText($event.target.value)"
      ></textarea>
    </div>
  </div>
</template>
<script setup lang="ts">
import { useCreationStore } from "@/store/datamodel-creation/index";
const store = useCreationStore();
const { querySuccess, isExcuteQuery, query, isFirstExcute } =
  storeToRefs(store);
const { getExcuteResult } = store;

const editQueryText = () => {
  isExcuteQuery.value = false;
};

const play = async () => {
  await getExcuteResult(query.value);
};

const queryReset = () => {
  isFirstExcute.value = false;
  querySuccess.value = false;
  isExcuteQuery.value = false;
  query.value = "";
};
</script>
<style lang="scss" scoped></style>
