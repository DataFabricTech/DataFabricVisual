<template>
  <div class="search-field">
    <label for="searchField" class="hidden-text">검색어를 입력하세요.</label>
<!--    <baseTextInput placeholder="검색어를 입력하세요."-->
<!--                    :model-value="keywordValue"-->
<!--                   id="searchField"></baseTextInput>-->
    <baseTextInput placeholder="검색어를 입력하세요."
                   v-model:modelValue="keyword"
                   id="searchField"></baseTextInput>
    <baseButton class="search-clear-button button-icon button-link" title="초기화" @click="resetKeyword">
      <svg-icon name="close" class="svg-icon"/>
      <span class="hidden-text">초기화</span>
    </baseButton>
    <baseButton class="search-button button-icon button-normal" title="검색" @click="searchKeyword">
      <svg-icon name="search" class="svg-icon"/>
      <span class="hidden-text">검색</span>
    </baseButton>
  </div>
</template>

<script setup lang="ts">
import { ref, defineEmits } from "vue";

const emit = defineEmits(['search','reset']);
const keyword = ref(null);
// TODO : [시연용] 메인에 입력한 키워드 검색결과페이지에 검색창에 입력된 상태로 나오게 처리

const props = defineProps({
  keyword: {
    type: String,
    default: null
  }
})

watch(
  () => props.keyword,
  (nValue, oValue) => {
    keyword.value = props.keyword;
    console.log(keyword.value, "ek");
  },
  {immediate:true}
);

function resetKeyword() {
  keyword.value = null;
  emit('reset', keyword.value)
}
function searchKeyword() {
  emit('search', keyword.value)
}
</script>
