<template>
  <h3 class="hidden-text">데이터모델 브라우저 검색목록</h3>
  <article class="page-article items-start">
    <h4 class="hidden-text">검색</h4>
    <div class="data-search">
      <SearchField @search="searchField" @reset="resetField"></SearchField>
      <BaseButton class="button-icon button-normal" title="상세 검색" @click="state.isOpen = !state.isOpen">
        <span class="hidden-text">상세 검색</span>
        <svg-icon name="sort" class="svg-icon"></svg-icon>
      </BaseButton>
    </div>
    <SearchFilter :toggle="state.isOpen" @close="closeSearchFilter"></SearchFilter>
  </article>
  <article class="page-article">
    <div class="result-info">
      <h4 class="result-summary-title">
        "<em class="color-tertiary">{{ filter.keyword }}</em
        >"검색 결과는 <em class="color-tertiary">{{ dataModelList.length }}</em
        >건 입니다.
      </h4>
      <div class="result-sort">
        <BaseSelect class="select-lg" :data="$constants.FILTER.SORT" @select="setSort"></BaseSelect>
        <BaseSelect class="select-lg" :data="$constants.FILTER.PAGE" @select="setPageSize"></BaseSelect>
      </div>
    </div>
    <ul class="card-list">
      <li class="card-item" v-for="dataModel in dataModelList">
        <Card :model="dataModel" @download="onRequestDownload(dataModel.id)" @click="moveDetailPage"></Card>
      </li>
    </ul>
    <BasePagination
      :total-elements="pageable.totalSize"
      :page-size="pageable.size"
      :current-page="pageable.selectPage"
      @click="setPage"
    ></BasePagination>
  </article>
</template>

<script lang="ts" setup>
import { storeToRefs } from "pinia";
import { dataModelStore } from "/store/data-fabric/browse/dataModel";
import $constants from "/utils/constants";

const store = dataModelStore();
const { getDataModelList, requestDownload } = store;
const { pageable, dataModelList } = storeToRefs(store);
const state = reactive({
  isOpen: true
});
const filter: {
  keyword: string | null;
} = reactive({
  keyword: null
});
const page: {
  size: number;
  selectPage: number;
} = reactive({
  size: 10,
  selectPage: 0
});
function closeSearchFilter(close: boolean) {
  state.isOpen = close;
}
function searchField(value: string) {
  filter.keyword = value;
}
function resetField(value: null) {
  filter.keyword = value;
}
function setPageSize(pageSize: number) {
  page.size = pageSize;
}
function setPage(pageNum: number) {
  page.selectPage = pageNum;
}
function setSort(data: any) {}

function moveDetailPage(id: string) {
  console.log(id);
}
async function onRequestDownload(id: string) {
  await requestDownload(id);
  await getDataModelList();
}
</script>
