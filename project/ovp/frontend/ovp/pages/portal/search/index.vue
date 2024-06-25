<template>
  <!--  상단 필터 & 초기화-->
  <div class="filters">
    <!--  template v-for -->
    <filter-item />
    <div>초기화</div>
  </div>
  <!--  상단 검색 결과 & 우측 필터-->
  <div>검색 결과, 우측 보기타입/정렬필터/보기개수</div>
  <!--  목록형 보기-->
  <resource-box-list />
  <!--  그래프혇 보기 - resource-box-list 안에 resource-box가 존재-->
  <custom-knowledge-graph />
  <Pagination
    :totalCount="60"
    :perPage="10"
    :currentPageNumber="4"
    @change="checkCurrentPage"
  ></Pagination>
  <!--  우측 미리보기-->
  <preview />
</template>

<script setup lang="ts">
import Pagination from "@extends/pagination/Pagination.vue";

const checkCurrentPage = (item: number) => {
  console.log("check", item);
};
</script>

<style lang="scss" scoped></style>
