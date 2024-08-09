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
            :selected-items="nSelectedListData"
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
            @item-check="onSelectApiData"
            @item-click="onClickApiData"
            @bookmark-change="onClickApiBookmark"
            @filter-change="onClickApiFilterChange"
            @sort-change="onClickApiSortChange"
            @search-change="onClickApiSearchChange"
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
            :selected-items="nSelectedListData"
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
        <span>선택된 데이터 모델({{ selectedListLength }})</span>
      </div>
      <data-model-list
        :filter="filters"
        :data="nSelectedListData"
        label-key="modelNm"
        value-key="id"
        :use-item-delete-btn="true"
        :is-multi="true"
        :use-sort="false"
        :use-infinite="false"
        :use-live-search="true"
        list-type="selected"
        no-data-msg="선택된 데이터 모델이 없습니다."
        @delete="onDeleteListData"
        @item-check="onSelectListData"
        @item-click="onClickListData"
        @bookmark-change="onClickApiBookmark"
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
import { useDataModelSearchStore } from "~/store/datamodel-creation/search";
import { storeToRefs } from "pinia";
import { useCreationStore } from "~/store/datamodel-creation";

// 데이터 생성 > 선택
const creationStore = useCreationStore();
const { selectedModelList } = storeToRefs(creationStore);

// 탐색 > 데이터 모델 조회 Store
const dataModelSearchStore = useDataModelSearchStore();
const { addSearchList, resetReloadList, changeDetailTab, changeTab } =
  dataModelSearchStore;
const { filters, searchResult, currTab, currDetailTab, mySearchResult } =
  storeToRefs(dataModelSearchStore);

// 전체/MY 데이터 초기화
await resetReloadList();

// 내부적으로 사용되는 데이터(모달 닫기 시 초기화됨)
const nSelectedListData = ref([]);
const tempSelectedListData = ref([]);
const setSelectedListData = () => {
  nSelectedListData.value = $_cloneDeep(selectedModelList.value);
  tempSelectedListData.value = $_cloneDeep(selectedModelList.value);
};
const selectedListLength = computed(() => {
  return nSelectedListData.value ? nSelectedListData.value.length : 0;
});

watchEffect(() => {
  setSelectedListData();
});

const isSelectedData: (item: any) => boolean = (item) => {
  const itemId = item.id;
  const findItem = $_find(nSelectedListData.value, { id: itemId });
  return !!findItem;
};

/**
 * 선택 데이터 추가 > 오른쪽으로 이동
 */
const onSaveSelectedData = () => {
  // 임시로 저장되어 있던 값을 선택 리스트에 저장
  nSelectedListData.value = tempSelectedListData.value;
  tempSelectedListData.value = [];
  searchResult.value.map((item: any) => {
    // 선택되지 않은 항목 중에 SelectedList에 데이터가 존재하면 값 변경
    if (!item.isSelected && isSelectedData(item)) {
      item.isSelected = true;
    }
    return item;
  });
  console.log("nSelectedListData", nSelectedListData.value);
};

/**
 * 선택 데이터 삭제 > 왼쪽으로 이동
 */
const onDeleteSelectedData = () => {
  nSelectedListData.value = $_differenceBy(
    nSelectedListData.value,
    tempDeleteListData.value,
    "id",
  );
  tempDeleteListData.value = [];
  console.log("onDeleteSelectedData", nSelectedListData.value);
  searchResult.value.map((item: any) => {
    // 선택된 항목 중에 SelectedList에 데이터가 존재하면 값 변경
    if (item.isSelected && !isSelectedData(item)) {
      item.isSelected = false;
    }
    return item;
  });
};

////////////// 선택 목록 //////////////
/**
 * 선택 목록 > 아이템 선택
 * @param value
 */
const onDeleteListData = (value: string) => {
  // temp 필요없이 바로 nselectedListData, searchResult에 바로 적용
  nSelectedListData.value = $_reject(nSelectedListData.value, { id: value });

  searchResult.value.map((item: any) => {
    // 선택된 항목 중에 SelectedList에 데이터가 존재하면 값 변경
    if (item.isSelected && item.id === value) {
      item.isSelected = false;
    }
    return item;
  });
  console.log("onDeleteListData", value);
};

const tempDeleteListData = ref([]);
const onSelectListData = (value: string) => {
  let findItem = $_find(searchResult.value, { id: value });
  if (findItem) {
    tempDeleteListData.value.push(findItem);
  }
};

const onClickListData = (value: string) => {
  console.log("onClickListData", value);
};

////////////// API 목록 //////////////
/**
 * API 통신 탐색 목록 > 아이템 선택
 * @param value
 */
const onSelectApiData = (value: string) => {
  let findItem = $_find(searchResult.value, { id: value });
  if (findItem) {
    tempSelectedListData.value.push(findItem);
  }
};

const onClickApiData = () => {};
const onClickApiBookmark = () => {};
const onClickApiFilterChange = () => {};
const onClickApiSortChange = () => {};
const onClickApiSearchChange = () => {};

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
</script>
<style lang="scss" scoped></style>
