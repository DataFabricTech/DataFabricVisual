<template>
  <div class="text-input-group">
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
      class="text-input-action-button button button-neutral-ghost button-xs"
      type="button"
      @click="clearSearchValue"
    >
      <span class="hidden-text">지우기</span>
      <svg-icon class="button-icon" name="close"></svg-icon>
    </button>
    <button
      class="text-input-search-button button button-neutral-ghost"
      @click="setSearchValue(inputValue)"
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

const emit = defineEmits<{ (e: "search", value: string): void }>();

const setSearchValue = (value: string) => {
  value !== "" ? emit("search", value) : window.location.reload();
};

const { inputValue, clearSearchValue } = SearchInputComposition(props);
</script>

<style></style>
