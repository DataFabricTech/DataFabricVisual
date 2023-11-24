<template>
  <div class="text-input-group">
    <span class="text-input-icon">
      <svg-icon class="svg-icon" name="search"></svg-icon>
    </span>
    <input
      class="text-input"
      id="inp-group"
      type="text"
      :value="props.text"
      placeholder="검색어를 입력하세요."
      @input="onChange"
    >
    <base-button
      v-if="props.useReset"
      class="text-input-clear-button button-icon button-link"
      title="초기화"
      @click="onReset"
    >
      <svg-icon class="svg-icon" name="close"></svg-icon>
    </base-button>
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  text: {
    type: String,
    default: ""
  },
  useReset: {
    type: Boolean,
    default: true
  }
});


const emit = defineEmits<{
  (e: "update:text", text: string): void;
  (e: "reset"): void;
}>();

function onChange($event) {
  emit("update:text", $event.target.value)
}

function onReset() {
  console.log("reset");
  emit("reset")
}
</script>
