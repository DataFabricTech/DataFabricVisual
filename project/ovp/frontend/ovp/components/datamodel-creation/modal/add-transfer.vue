<template>
  <div class="transfer">
    <div class="transfer-box">
      <Tab
        class="h-full"
        :data="$constants.DATAMODEL_CREATION.ADD.TAB"
        label-key="label"
        value-key="value"
        current-item-type="value"
        :current-item="currTab"
        @change="changeTab"
      >
        <template #all>
          <!-- 전체 탭 시작  -->
          <data-model-api-list
            class="h-full"
            :filter="filters"
            :data="searchResult"
            :sort-list="$constants.COMMON.SORT_FILTER"
            :selected-items="props.selectedModelList"
            label-key="modelNm"
            value-key="id"
            :use-item-delete-btn="false"
            :is-multi="true"
            :use-sort="true"
            :use-infinite="true"
            :use-live-search="false"
            :addSearchList="addSearchList"
            list-type="non-selected"
            no-data-msg="데이터 모델이 없습니다."
            @item-check="onSelectData"
            @item-click="emitItemClick"
            @bookmark-change="emitBookmark"
            @filter-change="emitFilterChange"
            @sort-change="emitSortChange"
            @search-change="emitSearchChange"
          >
            <template v-slot:tab>
              <Tab
                class="tab-line"
                :data="$constants.COMMON.DATA_TYPE"
                label-key="label"
                value-key="value"
                current-item-type="value"
                :current-item="currDetailTab"
                @change="changeDetailTab"
              ></Tab>
            </template>
          </data-model-api-list>
        </template>
        <template #my>
          <data-model-accordian-list
            :data="mySearchResult"
            :selected-items="props.selectedModelList"
            label-key="modelNm"
            value-key="id"
            :use-item-delete-btn="false"
            :is-multi="true"
            :use-live-search="false"
            list-type="non-selected"
            no-data-msg="데이터 모델이 없습니다."
          ></data-model-accordian-list>
        </template>
      </Tab>
    </div>
    <div class="transfer-handle">
      <button class="button button-neutral-stroke" @click="onSaveSelectedData">
        <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
        <span class="hidden-text">오른쪽 이동</span>
      </button>
      <button
        class="button button-neutral-stroke"
        @click="onDeleteSelectedData"
      >
        <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
        <span class="hidden-text">왼쪽 이동</span>
      </button>
    </div>
    <div class="transfer-box">
      <div class="transfer-head">
        <span>선택된 데이터 모델({{}})</span>
      </div>
      <data-model-list
        :filter="filters"
        :data="props.selectedModelList"
        label-key="modelNm"
        value-key="id"
        :use-item-delete-btn="true"
        :is-multi="true"
        :use-sort="false"
        :use-infinite="false"
        :use-live-search="true"
        list-type="selected"
        no-data-msg="선택된 데이터 모델이 없습니다."
        @delete="onDeleteDataModel"
      ></data-model-list>
    </div>
  </div>
</template>
<script setup lang="ts">
import Tab from "@extends/tab/Tab.vue";
import $constants from "~/utils/constant";
import DataModelApiList from "~/components/datamodel-creation/list/api/data-model-api-list.vue";
import DataModelList from "~/components/datamodel-creation/list/base/data-model-list.vue";
import DataModelAccordianList from "~/components/datamodel-creation/list/api/accoridan/data-model-accordian-list.vue";
import { ref } from "vue";
import _ from "lodash";
import { useDataModelSearchStore } from "~/store/datamodel-creation/search";
import { storeToRefs } from "pinia";

const props = defineProps({
  selectedModelList: {
    type: Array,
    required: true,
  },
});
// 내부적으로 사용되는 데이터(모달 닫기 시 초기화됨)
const selectedListData = ref([]);
const setSelectedListData = () => {
  selectedListData.value = _.cloneDeep(props.selectedModelList);
};
watchEffect(() => {
  setSelectedListData();
});

const onSelectData = (item: string) => {
  let findItem = _.find(props.modelList, { id: item });
  if (findItem) {
    selectedListData.value.push(findItem);
  }
};

const selectedListLength = computed(() => {
  return props.selectedModelList ? props.selectedModelList.length : 0;
});
const onDeleteSelectedData = () => {};
const onDeleteDataModel = (value: string) => {
  console.log("onDeleteDataModel", value);
};

const dataModelSearchStore = useDataModelSearchStore();
const { addSearchList, resetReloadList, changeDetailTab, changeTab } =
  dataModelSearchStore;
const {
  filters,
  searchResult,
  currTab,
  currDetailTab,
  selectedFilters,
  mySearchResult,
} = storeToRefs(dataModelSearchStore);

// 전체/MY 데이터 초기화
await resetReloadList();

// EMIT
// const emit = defineEmits<{
//   (e: "change", option: boolean): void;
//   (e: "tab-change", value: string): void;
//   (e: "detail-tab-change", value: string): void;
//   (e: "delete", value: string): void;
//   (e: "item-click", value: string): void;
//   (e: "bookmark-change", value: string): void;
//   (e: "filter-change", value: string): void;
//   (e: "sort-change", value: string): void;
//   (e: "search-change", value: string): void;
//   (e: "select", value: []): void;
// }>();
//
// const onSaveSelectedData = () => {
//   console.log("onSaveSelectedData", selectedListData.value);
//   emit("select", selectedListData.value);
// };
// function emitTab(item: number | string) {
//   console.log("emitTab", item);
//   emit("tab-change", item);
// }
//
// function emitDetailTypeTab(item: number | string) {
//   console.log("emitDetailTypeTab", item);
//   emit("detail-tab-change", item);
// }
//
// const emitBookmark = (value: string) => {
//   console.log("emitBookmark", value);
//   emit("bookmark-change", value);
// };
//
// const emitItemClick = (value: string) => {
//   console.log("emitItemClick", value);
//   emit("item-click", value);
// };
//
// const emitDeleteItem = (value: string) => {
//   console.log("emitDeleteItem", value);
//   emit("delete", value);
// };
//
// const emitFilterChange = (value: {}) => {
//   console.log("emitFilterChange", value);
//   emit("filter-change", value);
// };
// const emitSortChange = (value: string) => {
//   console.log("emitSortChange", value);
//   emit("sort-change", value);
// };
// const emitSearchChange = (value: string) => {
//   console.log("emitSearchChange", value);
//   emit("search-change", value);
// };
</script>
<style lang="scss" scoped></style>
