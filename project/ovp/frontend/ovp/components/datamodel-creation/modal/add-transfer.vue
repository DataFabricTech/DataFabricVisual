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
        @change="onChangeTab"
      >
        <template #all>
          <!-- 전체 탭 시작  -->
          <data-model-api-list
            class="h-full"
            :filter="filters"
            :data="searchResult"
            :selectedFilters="selectedFilters"
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
            @item-click="onClickData"
            @bookmark-change="onClickBookmark"
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
                :current-item="currTypeTab"
                @change="onChangeTypeTab"
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
            @item-check="onSelectAccordData"
            @item-click="onClickAccordData"
            @bookmark-change="onClickBookmark"
            @search-change="onClickAccordSearchChange"
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
        @item-click="onClickData"
        @bookmark-change="onClickBookmark"
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
const { nSelectedListData } = storeToRefs(creationStore);

// 탐색 > 데이터 모델 조회 Store
const dataModelSearchStore = useDataModelSearchStore();
const {
  addSearchList,
  resetReloadList,
  changeTypeTab,
  changeTab,
  setSortInfo,
  setSelectedFilter,
  setSearchKeyword,
  onClickData,
  onClickAccordData,
  onClickBookmark,
} = dataModelSearchStore;
const {
  filters,
  selectedFilters,
  searchResult,
  currTab,
  currTypeTab,
  mySearchResult,
} = storeToRefs(dataModelSearchStore);

const selectedListLength = computed(() => {
  return nSelectedListData.value ? nSelectedListData.value.length : 0;
});

const onChangeTab = (value: string) => {
  // Tab 변경 시 데이터가 변경되므로 API 리스트의 temp 데이터 초기화
  tempSelectedListData.value = [];
  tempAccordSelectedListData.value = [];
  changeTab(value);
};
const onChangeTypeTab = (value: string) => {
  // Tab 변경 시 데이터가 변경되므로 API 리스트의 temp 데이터 초기화
  tempSelectedListData.value = [];
  changeTypeTab(value);
};

////////////// 목록 이동 //////////////
/**
 * 선택 데이터 추가 > 오른쪽으로 이동
 */
const onSaveSelectedData = () => {
  // 임시로 저장되어 있던 값을 선택 리스트에 저장
  nSelectedListData.value = nSelectedListData.value
    .concat(tempSelectedListData.value)
    .concat(tempAccordSelectedListData.value);

  tempSelectedListData.value = [];
  tempAccordSelectedListData.value = [];

  searchResult.value = searchResult.value.map((item: any) => {
    // 선택되지 않은 항목 중에 SelectedList에 데이터가 존재하면 값 변경
    if (!item.isSelected && isSelectedData(item.id)) {
      item.isSelected = true;
    }
    return item;
  });
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
  searchResult.value = searchResult.value.map((item: any) => {
    // 선택된 항목 중에 SelectedList에 데이터가 존재하면 값 변경
    if (item.isSelected && !isSelectedData(item.id)) {
      item.isSelected = false;
    }
    return item;
  });
};

const isSelectedData: (item: any) => boolean = (itemId) => {
  const findItem = $_find(nSelectedListData.value, { id: itemId });
  return !!findItem;
};

////////////// 선택 목록 //////////////
const onDeleteListData = (value: any[]) => {
  // temp 필요없이 바로 nselectedListData, searchResult에 바로 적용
  nSelectedListData.value = value;

  searchResult.value.map((item: any) => {
    // 선택된 항목 중에 SelectedList에 데이터가 존재하면 값 변경
    if (item.isSelected && !isSelectedData(item.id)) {
      item.isSelected = false;
    }
    return item;
  });
};

const tempDeleteListData = ref([]);
const onSelectListData = (value: any[]) => {
  tempDeleteListData.value = value;
};

////////////// API - 전체 목록 //////////////
const tempSelectedListData = ref([]);
const onSelectApiData = (value: any[]) => {
  tempSelectedListData.value = value;
};
const onClickApiFilterChange = async (value: []) => {
  setSelectedFilter(value);
  await resetReloadList(nSelectedListData.value);
};
const onClickApiSortChange = async (value: string) => {
  setSortInfo(value);
  await resetReloadList(nSelectedListData.value);
};
const onClickApiSearchChange = async (value: string) => {
  setSearchKeyword(value);
  await resetReloadList(nSelectedListData.value);
};

////////////// API - MY 목록 //////////////
const tempAccordSelectedListData = ref([]);
const onSelectAccordData = (value: any[]) => {
  tempAccordSelectedListData.value = value;
};
const onClickAccordSearchChange = async (value: string) => {
  setSearchKeyword(value);
  await resetReloadList(nSelectedListData.value);
};
</script>
<style lang="scss" scoped></style>
