<template>
  <div class="work-page">
    <div class="l-top-bar">
      <button class="button button-neutral-stroke" @click="emitExecute">
        <svg-icon class="svg-icon" name="play"></svg-icon>
        실행
      </button>
      <button class="button button-error-lighter" @click="emitReset">
        초기화
      </button>
    </div>
    <div class="code-box">
      <!--      <textarea v-model="localQuery.query" @input="emitEdit"></textarea>-->
      <MonacoEditor
        v-model="localQuery.query"
        @input="emitEdit"
        lang="sql"
        :options="editorOptions"
        style="height: 100%"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  query: {
    type: String,
  },
});

const emit = defineEmits<{
  (e: "execute", value: string): void;
  (e: "reset"): void;
  (e: "edit", value: string): void;
}>();

const localQuery: ComputedRef<String> = computed(() => {
  return {
    query: props.query,
  };
});

const emitExecute = () => {
  emit("execute", localQuery.value.query);
};

const emitReset = () => {
  emit("reset", localQuery.value.query);
};

const emitEdit = () => {
  emit("edit", localQuery.value.query);
};

// Monaco Editor options 설정
const editorOptions = {
  language: "sql", // SQL 구문 강조
  automaticLayout: true, // 창 크기 변경 시 자동 레이아웃
  minimap: {
    enabled: false, // 미니맵 비활성화
  },
  scrollBeyondLastLine: false,
  readOnly: false, // 읽기 전용 여부
  wordWrap: "on", // 자동 줄바꿈
};
</script>

<style lang="scss" scoped></style>
