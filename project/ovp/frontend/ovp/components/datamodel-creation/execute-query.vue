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
      <textarea v-model="localQuery.query" @input="emitEdit"></textarea>
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
    query: props.query
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
</script>

<style lang="scss" scoped></style>
