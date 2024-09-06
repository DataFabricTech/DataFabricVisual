<template>
  <!--  상단 검색 결과 & 우측 필터-->
  <div class="l-top-bar">
    <h4 class="search-result">
      총
      <em class="search-result-num">{{ searchResultLength[currentTab] }}개</em
      >의 검색 결과가 있습니다.
    </h4>
    <div class="h-group gap-1">
      <select-box
        v-if="viewType === 'listView'"
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
          @change="onGraphView"
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

const onGraphView = () => {
  viewType.value = "graphView";
  // alert("개발중 입니다.");
};
</script>

<style lang="scss" scoped></style>
