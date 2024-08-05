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
      <textarea v-model="localQuery" @input="emitEdit"></textarea>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from "vue";

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

const localQuery = ref(props.query);

watch(
  () => props.query,
  (newQuery) => {
    localQuery.value = newQuery;
  },
);

const emitExecute = () => {
  emit("execute", localQuery.value);
};

const emitReset = () => {
  emit("reset", localQuery.value);
};

const emitEdit = () => {
  emit("edit", localQuery.value);
};
</script>

<style lang="scss" scoped></style>
