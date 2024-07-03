<template>
  <div style="height: 400px; width: 100%">
    <MonacoEditor v-model="computedValue" :lang="lang" :options="options" />
  </div>
</template>

<script lang="ts" setup>
import { computed, defineProps, defineEmits } from "vue";

const props = defineProps({
  modelValue: {
    type: String,
    required: true
  },
  lang: {
    type: String,
    default: "sql"
  },
  options: {
    type: Object,
    default: () => ({
      automaticLayout: true
    })
  }
});

const emit = defineEmits(["update:modelValue"]);

const computedValue = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
  }
});
</script>

<style scoped>
/* Monaco Editor 컨테이너의 높이와 너비를 설정 */
div {
  height: 400px;
  width: 100%;
}

/* Monaco Editor 자체의 스타일을 정의 */
.vs-dark {
  background-color: #1e1e1e;
  color: #d4d4d4;
}
</style>
