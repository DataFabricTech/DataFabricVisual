<template>
  <div style="display: flex; align-items: center; justify-content: center; width: 100%; margin-top: 100px">
    <div style="width: 400px; display: flex; gap: 20px; flex-direction: column">
      <SearchInput @search="getSearchValue" :placeholder="'default 유형 - 검색어를 입력하세요.'"></SearchInput>
      <SearchInput
        @search="getSearchValue"
        :placeholder="'default 아닌 유형 - 검색어를 입력하세요.'"
        :is-search-input-default-type="false"
      ></SearchInput>
    </div>
  </div>
</template>

<script setup lang="ts">
import SearchInput from "~/components/extends/search-input/SearchInput.vue";

const getSearchValue = (value: string) => {
  console.log("Emit Change Value: ", value);
};
</script>

<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
