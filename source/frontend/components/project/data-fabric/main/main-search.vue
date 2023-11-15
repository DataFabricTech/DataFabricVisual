<template>
  <div class="main-search">
    <SearchField
      class="search-field-lg"
      @search="mainKeyword"
      ></SearchField>
    <div class="search-word">
      <strong class="search-word-title">최근 검색어</strong>
      <div class="h-group gap-5" v-for="(tag, index) in recentTagList" :key="index" >
        <tag-removable
          :indexValue="index"
          @close="closeTag">{{ tag }}</tag-removable>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { fabricMainStore } from "/store/data-fabric/main/main";
import { storeToRefs } from "pinia";
import { onMounted } from "vue";
import TagRemovable from "/components/base/tag-removable.vue";

const store = fabricMainStore();
const { searchKeyword, insertRecentSearch } = store;
let {
  keyword,
} = storeToRefs(store);

// 최근 검색어 res값 받는 객체선언
let recentTagList = (['CCTV위치', '불법 주정차', '서초구', '보호구역']);

// 최근검색어 닫기를 눌렀을 때 발생하는 함수
// TODO : 최근검색어 삭제 API가 없는 경우로, 본 개발에 수정될 예정
function closeTag(indexValue) {
  recentTagList.value.splice(indexValue,1);
  // console.log("recentTagList 상태확인 : ", recentTagList);
}

onMounted(() => {
  // 최근 검색어 API함수 호출
  insertRecentSearch()
    .then((res: any) => {
      recentTagList.value = res.recentSearches.slice(0,5); // 5개만 보이도록 slice
    })
})

// 간편 검색
async function mainKeyword(key) {
  // 입력받은 keyword값 스토어에 저장
  keyword.value = key;
  // 검색 API 호출
  await searchKeyword()
    .then((data: any) => {
    console.log("data : ", data);
    // TODO : 검색이 완료되었으니, 또다시 최근 검색어 API 를 호출한다.
    })
  // await insertRecentSearch()
  //   .then((data: any) => {
  //     console.log("데이터 : ", data);
  //
  //   })

  // TODO : 수정 예정, 시연용으로, 임시 설정
  // var link = "/data-fabric/development-search";
  var link = "/data-fabric/browse/search";
  const router = useRouter();
  router.push({
    path: link,
    query:
      { keyword : key}
  });
};
</script>
