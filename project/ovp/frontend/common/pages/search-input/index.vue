<template>
  <div class="w-[541px] flex gap-[20px] flex-col">
    <SearchInput
      @on-click-search="onClickSearch"
      :placeholder="'검색어를 입력하세요.'"
      :inp-value="searchInputValue"
      :inp-id="'headerInp1'"
      :label-text="'데이터 모델 검색'"
      @update:value="updateSearchInputValue"
    ></SearchInput>
    <search-input
      class="search-input-lg"
      :is-search-input-default-type="false"
      :placeholder="'검색어를 입력하세요.'"
      :inp-value="searchInputValue2"
      :inp-id="'modelAddInp1'"
      :label-text="'데이터 모델 검색'"
      @update:value="updateSearchInputValue2"
      @on-input="onInput2"
      @on-click-search="onClickSearch2"
    ></search-input>
  </div>
</template>
<script setup lang="ts">
import SearchInput from "~/components/extends/search-input/SearchInput.vue";
// search input 1
const searchInputValue = ref("");

const updateSearchInputValue = (newValue: string) => {
  searchInputValue.value = newValue;
};

const onClickSearch = (value: string) => {
  console.log("검색 실행 - ", value);
};

// search input 2
const searchInputValue2 = ref("");
const updateSearchInputValue2 = (newValue: string) => {
  searchInputValue2.value = newValue;
};

const onInput2 = (value: string) => {
  console.log("실시간 검색 실행 - ", value);
};

const onClickSearch2 = (value: string) => {
  console.log("검색 실행 - ", value);
};
</script>
