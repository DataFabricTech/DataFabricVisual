<template>
  <div class="search-field">
    <baseTextInput placeholder="검색어를 입력하세요." v-model="keyword"></baseTextInput>
    <baseButton class="search-clear-button button-icon button-link" title="초기화" @click="resetKeyword">
      <svg-icon name="close" class="svg-icon"/>
    </baseButton>
    <baseButton class="search-button button-icon button-normal" title="검색" @click="searchKeyword">
      <svg-icon name="search" class="svg-icon"/>
    </baseButton>
  </div>
</template>

<script setup lang="ts">
import { ref, defineEmits } from "vue";

const emit = defineEmits(['search','reset']);
const keyword = ref(null)
function resetKeyword() {
  keyword.value = null;
  emit('reset', keyword.value)
}
function searchKeyword() {
  emit('search', keyword.value)
}
</script>
