<template>
  <div class="search-input">
    <svg-icon class="text-input-icon svg-icon" name="search" v-if="!isSearchInputDefaultType"></svg-icon>
    <label class="hidden-text" :for="inpId">{{ labelText }}</label>
    <input
      :id="inpId"
      class="text-input"
      :value="inpValue"
      @keyup.enter="onClickSearch"
      @change="onChange"
      @input="onInput"
      :placeholder="placeholder"
      :disabled="disabled"
      :isSearchInputDefaultType="isSearchInputDefaultType"
    />
    <button
      class="search-input-action-button button button-neutral-ghost button-xs"
      type="button"
      @click="clearSearchValue"
      v-if="isShowResetButton"
    >
      <span class="hidden-text">지우기</span>
      <svg-icon class="button-icon" name="close"></svg-icon>
    </button>
    <button
      class="search-input-button button button-neutral-ghost"
      @click="onClickSearch"
      v-if="isSearchInputDefaultType"
    >
      <span class="hidden-text">검색</span>
      <svg-icon class="button-icon" name="search"></svg-icon>
    </button>
  </div>
</template>

<script setup lang="ts">
import { defineProps } from "vue";
import { SearchInputProps } from "./SearchInputProps";

const props = withDefaults(defineProps<SearchInputProps & { inpValue: string; labelText: string; inpId: string }>(), {
  isSearchInputDefaultType: true,
  placeholder: "Placeholder",
  disabled: false,
  inpValue: "",
  labelText: "검색",
  inpId: "input01"
});

const emit = defineEmits<{
  (e: "update:value", value: string): void;
  (e: "onClickSearch", value: string): void;
  (e: "onChange", value: string): void;
  (e: "onInput", value: string): void;
  (e: "reset"): void;
}>();

const isShowResetButton = ref(true);

watch(
  () => props.isSearchInputDefaultType,
  (newVal) => {
    if (newVal) {
      isShowResetButton.value = false;
    }
  },
  { immediate: true }
);

const onClickSearch = () => {
  emit("onClickSearch", props.inpValue);
};

const onChange = (event: Event) => {
  const input = event.target as HTMLInputElement;
  emit("update:value", input.value);
  emit("onChange", input.value);
};

const onInput = (event: Event) => {
  const input = event.target as HTMLInputElement;
  if (props.isSearchInputDefaultType) {
    isShowResetButton.value = true;
  }
  emit("update:value", input.value);
  emit("onInput", input.value);
};

const clearSearchValue = () => {
  if (props.isSearchInputDefaultType) {
    isShowResetButton.value = false;
  }
  emit("update:value", "");
  emit("reset");
};
</script>

<style scoped></style>
