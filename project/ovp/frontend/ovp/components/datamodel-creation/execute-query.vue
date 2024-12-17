<template>
  <div class="work-page">
    <div class="l-top-bar">
      <button class="button button-neutral-stroke" @click="runQuery">
        <svg-icon class="svg-icon" name="play"></svg-icon>
        실행
      </button>
      <button class="button button-error-lighter" @click="resetQuery">
        초기화
      </button>
    </div>
    <div class="code-box">
      <MonacoEditor
        v-model="query"
        lang="sql"
        :options="editorOptions"
        style="height: 100%"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { useCreationStore } from "@/store/datamodel-creation";
import _ from "lodash";

const creationStore = useCreationStore();

const { query, isExecuteQuery, isQueryEmpty } = storeToRefs(creationStore);
const { runQuery, resetQuery } = creationStore;

// Monaco Editor options 설정
const editorOptions = {
  theme: "vs",
  language: "sql", // SQL 구문 강조
  automaticLayout: true, // 창 크기 변경 시 자동 레이아웃
  minimap: {
    enabled: false, // 미니맵 비활성화
  },
  scrollBeyondLastLine: false,
  readOnly: false, // 읽기 전용 여부
  wordWrap: "on", // 자동 줄바꿈
};

watchEffect(() => {
  if (query.value) {
    isQueryEmpty.value = _.isEmpty(query.value);
    isExecuteQuery.value = false;
  }
});
</script>

<style lang="scss" scoped></style>
