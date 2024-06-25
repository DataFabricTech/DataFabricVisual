<template>
  <!--  리소스 박스 1개-->
  <resource-box />
  <Tab
    :data="tabOptions2"
    :label-key="'label'"
    :value-key="'value'"
    :current-item="currentTab2"
    :current-item-type="'index'"
    @change="changeTab2"
  ></Tab>
  <!--   탭 별 화면-->
  <default-info />
  <schema />
  <sample />
  <profiling />
  <query />
  <lineage />
  <knowledge-graph />
  <recommend-model />
</template>

<script setup lang="ts">
import Tab from "@extends/tab/Tab.vue";
import { ref } from "vue";

const tabOptions2 = [
  { label: "기본정보", value: 1 },
  { label: "스키마", value: 2 },
  { label: "샘플데이터", value: 3 },
  { label: "프로파일링", value: 4 },
  { label: "쿼리", value: 5 },
  { label: "데이터 리니지", value: 6 },
  { label: "Knowledge graph", value: 7 },
  { label: "추천 데이터 모델", value: 8 },
];
const currentTab2 = ref();
currentTab2.value = 1;

function changeTab2(item: number | string) {
  currentTab2.value = item;
}
</script>

<style lang="scss" scoped></style>
