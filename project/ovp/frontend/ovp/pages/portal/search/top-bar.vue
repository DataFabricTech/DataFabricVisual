<template>
  <!--  상단 검색 결과 & 우측 필터-->
  <div class="l-top-bar">
    <strong
      >총 <em class="primary">{{ searchResultLength[currentTab] }}개</em>의 검색
      결과가 있습니다.</strong
    >
    <div class="h-group gap-1">
      <select-box
        class="select-clean"
        :data="options"
        label-key="label"
        value-key="value"
        :selectedItem="`${sortKey}_${sortKeyOpt}`"
        :isFirstSelectedEvent="isFirstCheckedEvent"
        @select="setSortFilter"
      ></select-box>
      <div class="button-group">
        <input
          type="radio"
          id="listView"
          class="button-group-input"
          name="button-group3"
          value="listView"
          v-model="viewType"
          checked
        />
        <label for="listView" class="button-group-label">
          <svg-icon class="svg-icon" name="list"></svg-icon>
          <span class="hidden-text">리스트보기</span>
        </label>
        <input
          type="radio"
          id="graphView"
          class="button-group-input"
          name="button-group3"
          value="graphView"
          v-model="viewType"
        />
        <label for="graphView" class="button-group-label">
          <svg-icon class="svg-icon" name="knowleage-graph"></svg-icon>
          <span class="hidden-text">시각화 보기</span>
        </label>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useSearchCommonStore } from "@/store/search/common";
import { storeToRefs } from "pinia";
import SelectBox from "@extends/select-box/SelectBox.vue";
import $constants from "~/utils/constant";

const searchCommonStore = useSearchCommonStore();
const { setSortFilter } = searchCommonStore;
const { viewType, searchResultLength, sortKey, sortKeyOpt, currentTab } =
  storeToRefs(searchCommonStore);

const options: { [key: string]: string | number }[] =
  $constants.COMMON.SORT_FILTER;
const isFirstCheckedEvent: boolean = true;
</script>

<style lang="scss" scoped></style>
