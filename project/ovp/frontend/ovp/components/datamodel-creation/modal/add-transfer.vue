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
            :use-filter="true"
            :use-sort="true"
            :use-infinite="true"
            :use-live-search="false"
            :addSearchList="addSearchList"
            :isDoneFirModelListLoad="isDoneFirModelListLoad"
            list-type="non-selected"
            no-data-msg="데이터 모델이 없습니다."
            @item-check="onSelectApiData"
            @item-click="onClickData"
            @bookmark-change="onClickBookmark"
            @filter-change="onClickApiFilterChange"
            @sort-change="onClickApiSortChange"
            @search-change="onClickApiSearchChange"
            @filter-reset="onClickApiReset"
          >
            <template v-slot:tab>
              <Tab
                class="tab-line"
                :data="searchListDetailTab"
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
          <data-model-api-list
            class="h-full"
            :data="mySearchResult"
            :filter="[]"
            :selected-filters="[]"
            :selected-items="nSelectedListData"
            label-key="modelNm"
            value-key="id"
            :use-item-delete-btn="false"
            :is-multi="true"
            :use-filter="false"
            :use-live-search="false"
            :use-sort="false"
            :use-infinite="true"
            :addSearchList="addMySearchList"
            :isDoneFirModelListLoad="isDoneFirModelListLoad"
            list-type="non-selected"
            no-data-msg="데이터 모델이 없습니다."
            @item-check="onSelectMyListData"
            @item-click="onClickData"
            @bookmark-change="onClickBookmark"
            @search-change="onClickMyListSearchChange"
          >
            <template v-slot:tab>
              <Tab
                class="tab-line"
                :data="myListDetailTab"
                label-key="label"
                value-key="value"
                current-item-type="value"
                :current-item="currTypeMyTab"
                @change="onChangeTypeMyTab"
              ></Tab>
            </template>
          </data-model-api-list>
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
        @item-click="onClickSelectedData"
        @bookmark-change="updateSelectedModelBookmark"
      ></data-model-list>
    </div>
  </div>
</template>
<script setup lang="ts">
import Tab from "@extends/tab/Tab.vue";
import $constants from "~/utils/constant";
import DataModelApiList from "~/components/datamodel-creation/list/api/data-model-api-list.vue";
import DataModelList from "~/components/datamodel-creation/list/base/data-model-list.vue";
import { ref } from "vue";
import { useDataModelSearchStore } from "~/store/datamodel-creation/search";
import { storeToRefs } from "pinia";

// 탐색 > 데이터 모델 조회 Store
const dataModelSearchStore = useDataModelSearchStore();
const {
  addSearchList,
  addMySearchList,
  resetReloadList,
  changeTypeTab,
  changeTypeMyTab,
  changeTab,
  setSortInfo,
  setSelectedFilter,
  setSearchKeyword,
  onClickData,
  onClickSelectedData,
  setSearchMyKeyword,
  onClickBookmark,
  updateSelectedModelBookmark,
} = dataModelSearchStore;
const {
  filters,
  selectedFilters,
  searchResult,
  searchResultLength,
  currTab,
  currTypeMyTab,
  currTypeTab,
  mySearchResult,
  mySearchResultLength,
  nSelectedListData,
  isDoneFirModelListLoad,
} = storeToRefs(dataModelSearchStore);

const selectedListLength = computed(() => {
  return nSelectedListData.value ? nSelectedListData.value.length : 0;
});
const myListDetailTab = computed(() => {
  const defaultTab = $_cloneDeep($constants.DATAMODEL_CREATION.ADD.MY_DATA_TAB);
  defaultTab.map((item) => {
    const lengthValue = mySearchResultLength.value[item.value];
    item.label = !!lengthValue ? `${item.label}(${lengthValue})` : item.label;
  });
  return defaultTab;
});
const searchListDetailTab = computed(() => {
  const defaultTab = $_cloneDeep($constants.COMMON.DATA_TYPE);
  defaultTab.map((item) => {
    const lengthValue = searchResultLength.value[item.value];
    item.label = !!lengthValue ? `${item.label}(${lengthValue})` : item.label;
  });
  return defaultTab;
});

const onChangeTab = (value: string) => {
  // Tab 변경 시 데이터가 변경되므로 API 리스트의 temp 데이터 초기화
  tempSelectedListData.value = [];
  tempMyListSelectedListData.value = [];
  changeTab(value);
};
const onChangeTypeTab = (value: string) => {
  // Tab 변경 시 데이터가 변경되므로 API 리스트의 temp 데이터 초기화
  tempSelectedListData.value = [];
  changeTypeTab(value);
};
const onChangeTypeMyTab = (value: string) => {
  // Tab 변경 시 데이터가 변경되므로 API 리스트의 temp 데이터 초기화
  tempMyListSelectedListData.value = [];
  changeTypeMyTab(value);
};

////////////// 목록 이동 //////////////
/**
 * 선택 데이터 추가 > 오른쪽으로 이동
 */
const onSaveSelectedData = () => {
  // 임시로 저장되어 있던 값을 선택 리스트에 저장
  nSelectedListData.value = nSelectedListData.value
    .concat(tempSelectedListData.value)
    .concat(tempMyListSelectedListData.value);

  tempSelectedListData.value = [];
  tempMyListSelectedListData.value = [];
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
  searchResult.value = searchResult.value.map((item: any) => {
    // 선택된 항목 중에 SelectedList에 데이터가 존재하면 값 변경
    if (item.isSelected && !isSelectedData(item.id)) {
      item.isSelected = false;
      item.isChecked = false;
    }
    return item;
  });
  mySearchResult.value = mySearchResult.value.map((item: any) => {
    // 선택된 항목 중에 SelectedList에 데이터가 존재하면 값 변경
    if (item.isSelected && !isSelectedData(item.id)) {
      item.isSelected = false;
      item.isChecked = false;
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
  tempDeleteListData.value = value.map((item) => {
    return {
      ...item,
      isChecked: false,
    };
  });
};

////////////// API - 전체 목록 //////////////
const tempSelectedListData = ref([]);
const onSelectApiData = (value: any[]) => {
  tempSelectedListData.value = value;
  searchResult.value = searchResult.value.map((item: any) => {
    let findItem = $_find(value, { id: item.id });
    return {
      ...item,
      isChecked: !!findItem,
    };
  });
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

const onClickApiReset = async (value: string) => {
  setSelectedFilter(value.selectedFilter);
  setSortInfo(value.selectedSort);
  setSearchKeyword(value.searchLabel);
  await resetReloadList(nSelectedListData.value);
};

////////////// API - MY 목록 //////////////
const tempMyListSelectedListData = ref([]);
const onSelectMyListData = (value: any[]) => {
  tempMyListSelectedListData.value = value;
  mySearchResult.value = mySearchResult.value.map((item: any) => {
    let findItem = $_find(value, { id: item.id });
    return {
      ...item,
      isChecked: !!findItem,
    };
  });
};
const onClickMyListSearchChange = async (value: string) => {
  setSearchMyKeyword(value);
  await resetReloadList(nSelectedListData.value);
};

watchEffect(() => {
  const updateSelection = (resultList: any[]) => {
    return resultList.map((item: any) => {
      if (!item.isSelected && isSelectedData(item.id)) {
        item.isSelected = true;
      }
      return item;
    });
  };

  if (currTab.value === "all") {
    searchResult.value = updateSelection(searchResult.value);
  } else {
    mySearchResult.value = updateSelection(mySearchResult.value);
  }
});
</script>
<style lang="scss" scoped></style>
