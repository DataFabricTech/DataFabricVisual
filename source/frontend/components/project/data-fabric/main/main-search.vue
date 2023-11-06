<template>
  <div class="main-search">
    <SearchField
      class="search-field-lg"
      @search="mainKeyword"
      ></SearchField>
    <div class="search-word">
      <strong class="search-word-title">최근 검색어</strong>
      <div class="h-group gap-5">
        <baseTag># CCTV위치</baseTag>
        <baseTag># 주정차댠속</baseTag>
        <baseTag># 기후변화</baseTag>
        <baseTag># 안심지역</baseTag>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { fabricMainStore } from "../../../../store/data-fabric/main/main";
import { storeToRefs } from "pinia";

const store = fabricMainStore();
const { searchKeyword } = store;
let {
  keyword,
} = storeToRefs(store);

// 간편 검색
async function mainKeyword(key) {
  // 입력받은 keyword값 스토어에 저장
  keyword.value = key;
  // 검색 API 호출
  await searchKeyword()
    .then((data: any) => {
    console.log("data : ", data);
  })
  // TODO : 추후 router로 페이지 전환 처리로 변경될 예정
  // "검색결과 페이지"로 이동
  var link = "/data-fabric/development-search";
  location.href=link;
  window.open(link);
};


</script>
