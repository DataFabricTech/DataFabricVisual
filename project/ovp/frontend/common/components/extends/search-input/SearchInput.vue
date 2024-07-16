<template>
  <div class="search-input">
    <svg-icon class="text-input-icon svg-icon" name="search" v-if="!isSearchInputDefaultType"></svg-icon>
    <label class="hidden-text" for="searchInp">label</label>
    <input
      id="searchInp"
      class="text-input"
      v-model="inputValue"
      @keyup.enter="setSearchValue(inputValue)"
      @change="setSearchValue(inputValue)"
      @input="setSearchValue(inputValue)"
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
      @click="onClickSearch(inputValue)"
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
  (e: "search", value: string): void;
  (e: "onClickSearch", value: string): void;
}>();

const setSearchValue = (value: string) => {
  emit("search", value);
};

const onClickSearch = (value: string) => {
  emit("onClickSearch", value);
};

const { inputValue, clearSearchValue } = SearchInputComposition(props);
</script>

<style></style>
