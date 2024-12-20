import { usePagingStore } from "@/store/common/paging";
import { useQueryHelpers } from "@/composables/queryHelpers";
import type {
  Filters,
  QueryFilter,
  SelectedFilters,
} from "@/store/governance/Category";
import { ref } from "vue";
import { FILTER_KEYS } from "@/store/search/common";
import $constants from "@/utils/constant";
import _ from "lodash";
import { useGlossaryStore } from "~/store/glossary";
import { useGovernCategoryStore } from "@/store/governance/Category";

interface Tab {
  label: string;
  value: string;
  type: string;
  labelWithCount?: string;
}

interface DataGroupByTab {
  table: string[];
  storage: string[];
  model: string[];
}

export const useDataModelTag = defineStore("DataModelTag", () => {
  const { $api } = useNuxtApp();
  const pagingStore = usePagingStore();
  const { setFrom, updateIntersectionHandler } = pagingStore;
  const { from, size } = storeToRefs(pagingStore);
  const { setQueryFilterByDepth } = useQueryHelpers();

  const categoryStore = useGovernCategoryStore();
  const { selectedCategoryTagId, modelList, selectedModelList } =
    storeToRefs(categoryStore);

  const glossaryStore = useGlossaryStore();
  const { term } = storeToRefs(glossaryStore);

  const createDefaultFilters = (): Filters => {
    return {
      [FILTER_KEYS.OWNER]: { text: "소유자", data: [] },
      [FILTER_KEYS.TAGS]: { text: "태그", data: [] },
      [FILTER_KEYS.SERVICE]: { text: "서비스", data: [] },
      [FILTER_KEYS.SERVICE_TYPE]: { text: "서비스타입", data: [] },
    };
  };

  const createDefaultTabList = (): Tab[] => {
    return $constants.COMMON.DATA_TYPE;
  };

  const createDefaultDataModelListByTab = (): DataGroupByTab => {
    return {
      table: [],
      storage: [],
      model: [],
    };
  };

  const tagId = ref<string>();

  const searchKeyword = ref<string>("");

  const filters = ref<Filters>(createDefaultFilters());
  const selectedFilters = ref<SelectedFilters>({});
  const selectedFilterItems = ref<any>([]);

  const tabList = ref<Tab[]>(createDefaultTabList());
  const currentTab = ref<string>("");

  const dataModelTotalCountByTab = ref<{ [key: string]: number }>({});
  const dataModelList = ref<{ [key: string]: any }[]>([]);
  const loadedDataModelIdListByTab = ref<DataGroupByTab>(
    createDefaultDataModelListByTab(),
  );
  const selectedDataModelListByTab = ref<DataGroupByTab>(
    createDefaultDataModelListByTab(),
  );

  const clearDataModelModalSettings = () => {
    setSearchKeyword("");
    setEmptyFilter();
    initCurrentTab();
    resetDataModelIdListByTab();
    resetSelectedDataModelListByTab();
  };

  const setSearchKeyword = (keyword: string) => {
    searchKeyword.value = keyword;
  };

  /**
   * 필터 조회
   */
  const getFilters = async () => {
    const { data } = await $api(`/api/search/filters`);

    const defaultFilters = createDefaultFilters();
    const useFilters = Object.keys(defaultFilters);

    useFilters.forEach((key: string) => {
      (filters.value as Filters)[key].data = data[key];
    });
  };

  const setEmptyFilter = () => {
    selectedFilterItems.value = [];
    selectedFilters.value = {};
  };

  const setTabList = () => {
    tabList.value.forEach((item: Tab) => {
      item.labelWithCount = `${item.label} (${dataModelTotalCountByTab.value[item.value]})`;
    });
  };

  const initCurrentTab = () => {
    currentTab.value = _.head($constants.COMMON.DATA_TYPE)?.value || "";
  };

  const getQueryFilter = (): QueryFilter => {
    const queryFilter: QueryFilter = {
      query: { bool: { must: [] } },
    };

    Object.entries(selectedFilters.value).forEach(([key, value]) => {
      queryFilter.query.bool.must.push(setQueryFilterByDepth(key, value));
    });

    return queryFilter;
  };

  const getDataModelListQuery = () => {
    const queryFilter = getQueryFilter();
    const params: any = {
      q: searchKeyword.value,
      index: currentTab.value, // table or storage or model -> tab
      from: from.value,
      size: size.value,
      deleted: false,
      query_filter: JSON.stringify(queryFilter),
      sort_field: "totalVotes",
    };
    return new URLSearchParams(params);
  };

  const getDataModelListAPI = async () => {
    const { data } = await $api(`/api/search/list?${getDataModelListQuery()}`, {
      showLoader: false,
    });
    return data || [];
  };

  const setDataModelIdListByTab = (data: {
    [key: string]: { [key: string]: any }[];
  }) => {
    const currentTabKey =
      currentTab.value as keyof typeof loadedDataModelIdListByTab.value;
    const dataModelIdList = data[currentTabKey].map(({ id }) => id);

    const existingDataModelIds =
      loadedDataModelIdListByTab.value[currentTabKey] || [];

    if (_.size(existingDataModelIds) < _.size(dataModelList.value)) {
      loadedDataModelIdListByTab.value[currentTabKey] = [
        ...existingDataModelIds,
        ...dataModelIdList,
      ];
    }
  };

  const resetDataModelIdListByTab = () => {
    loadedDataModelIdListByTab.value = createDefaultDataModelListByTab();
  };

  const resetSelectedDataModelListByTab = () => {
    selectedDataModelListByTab.value = createDefaultDataModelListByTab();
  };

  /**
   * 데이터모델 조회 -> 갱신
   */
  const getDataModelList = async () => {
    setFrom(0);

    const { data, totalCount } = await getDataModelListAPI();
    dataModelList.value = data[currentTab.value];
    dataModelTotalCountByTab.value = totalCount;

    setTabList();
    setDataModelIdListByTab(data);

    updateIntersectionHandler(0);
  };

  /**
   * 데이터모델 조회 -> 누적
   */
  const addDataModelList = async () => {
    // 현재 탭에서 불러오지 않은 데이터가 있을 경우에 추가 조회
    if (
      dataModelTotalCountByTab.value[currentTab.value] >
      dataModelList.value.length
    ) {
      const { data } = await getDataModelListAPI();
      dataModelList.value = dataModelList.value.concat(data[currentTab.value]);

      setDataModelIdListByTab(data);
    }
  };

  /**
   * 데이터모델 추가
   */
  const addDataModels = async ({
    currentPageType,
  }: {
    currentPageType: string;
  }) => {
    // category or glossary
    return currentPageType === "category"
      ? await addDataModelsCategory()
      : await addDataModelsTerm();
  };

  /**
   * 데이터모델 추가 및 변경시 공통으로 사용하는 API - 카테고리
   */
  const updateDataModelsCategory = async () => {
    const { data } = await $api(`/api/category/${tagId.value}/data-models`, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json-patch+json",
      },
      body: selectedDataModelListByTab.value,
    });
    return data;
  };

  /**
   * 데이터모델 추가 - 카테고리
   */
  const addDataModelsCategory = async () => {
    // 선택한 데이터모델 목록은 데이터모델추가 모달에서 자동으로 값 셋팅되므로 tagId 만 설정해줌
    tagId.value = selectedCategoryTagId.value;
    return await updateDataModelsCategory();
  };

  /**
   * 데이터모델 변경 - 카테고리
   */
  const changeDataModelsCategory = async () => {
    // tagId 는 카테고리변경 모달에서 자동으로 값 셋팅되므로 선택한 데이터모델 목록만 설정해줌
    selectedDataModelListByTab.value = modelList.value.reduce(
      (acc, { id, type }) => {
        if (_.includes(selectedModelList.value, id)) {
          if (!acc[type]) {
            acc[type] = [];
          }
          acc[type].push(id);
        }
        return acc;
      },
      {},
    );
    return await updateDataModelsCategory();
  };

  /**
   * 데이터모델 추가 - 용어
   */
  const addDataModelsTerm = async () => {
    const id = term.value.id;
    const { data } = await $api(`/api/glossary/terms/${id}/data-models`, {
      method: "PUT",
      body: selectedDataModelListByTab.value,
    });

    return data;
  };

  return {
    tagId,
    searchKeyword,
    filters,
    selectedFilters,
    selectedFilterItems,
    tabList,
    currentTab,
    dataModelTotalCountByTab,
    dataModelList,
    loadedDataModelIdListByTab,
    selectedDataModelListByTab,
    clearDataModelModalSettings,
    setSearchKeyword,
    getFilters,
    setEmptyFilter,
    initCurrentTab,
    getDataModelList,
    addDataModelList,
    resetDataModelIdListByTab,
    resetSelectedDataModelListByTab,
    addDataModels,
    changeDataModelsCategory,
  };
});
