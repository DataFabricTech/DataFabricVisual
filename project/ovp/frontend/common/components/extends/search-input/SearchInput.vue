<template>
  <div class="search-input">
    <svg-icon class="text-input-icon svg-icon" name="search" v-if="!isSearchInputDefaultType"></svg-icon>
    <!-- TODO: [개발] 접근성 개선 - label/input의 for/id값 사용되는 페이지마다 다르게 적용 필요,
         label안 텍스트도 페이지마다 적절히 사용되어야 함 -->
    <label class="hidden-text" for="searchInp">label</label>
    <input
      id="searchInp"
      class="text-input"
      v-model="inputValue"
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
import { SearchInputProps } from "./SearchInputProps";
import { SearchInputComposition } from "./SearchInputComposition";

const props = withDefaults(defineProps<SearchInputProps>(), {
  isSearchInputDefaultType: true,
  placeholder: "Placeholder",
  disabled: false
});

const emit = defineEmits<{
  (e: "onClickSearch", value: string): void;
  (e: "onChange", value: string): void;
  (e: "onInput", value: string): void;
}>();

const onClickSearch = () => {
  emit("onClickSearch", inputValue.value);
};
const onChange = () => {
  emit("onChange", inputValue.value);
};
const onInput = () => {
  emit("onInput", inputValue.value);
};

const { inputValue, clearSearchValue } = SearchInputComposition(props);
</script>

<style></style>
