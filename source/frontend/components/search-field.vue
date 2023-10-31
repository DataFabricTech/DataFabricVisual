<template>
  <div class="search-field">
    <baseTextInput
      v-model="searchText"
      placeholder="검색어를 입력하세요."
      ></baseTextInput>
    <baseButton class="search-clear-button button-icon button-link" title="초기화">
      <svg-icon name="close" class="svg-icon"/>
    </baseButton>
    <baseButton class="search-button button-icon button-normal" title="검색" @click="searchKeyword()" >
      <svg-icon name="search" class="svg-icon"/>
    </baseButton>
  </div>
</template>

<script setup lang="ts">
const searchText = ref("");
function searchKeyword(){
  console.log("searchText : ", searchText);
}
</script>
