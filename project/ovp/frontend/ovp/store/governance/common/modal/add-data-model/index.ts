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

export const useAddDataModel = defineStore("AddDataModel", () => {
  const { $api } = useNuxtApp();
  const pagingStore = usePagingStore();
  const { setFrom, updateIntersectionHandler } = pagingStore;
  const { from, size } = storeToRefs(pagingStore);
  const { setQueryFilterByDepth, getTrinoQuery } = useQueryHelpers();

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

  const searchKeyword = ref<string>("");

  const filters = ref<Filters>(createDefaultFilters());
  const selectedFilters = ref<SelectedFilters>({});
  const selectedFilterItems = ref<any>([]);

  const tabList = ref<Tab[]>(createDefaultTabList());
  const currentTab = ref<string>("");

  const dataModelTotalCountByTab = ref<{ [key: string]: number }>({});
  const dataModelList = ref<{ [key: string]: any }[]>([]);
  const dataModelIdList = ref<string[]>([]);
  const selectedDataModelListByTab = ref<DataGroupByTab>(
    createDefaultDataModelListByTab(),
  );

  const clearDataModelModalSettings = () => {
    setSearchKeyword("");
    setEmptyFilter();
    initCurrentTab();
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
      trino_query: JSON.stringify(getTrinoQuery(queryFilter)),
    };
    return new URLSearchParams(params);
  };

  const getDataModelListAPI = async () => {
    const { data } = await $api(`/api/search/list?${getDataModelListQuery()}`, {
      showLoader: false,
    });
    return data || [];
  };

  const setDataModelIdList = () => {
    dataModelIdList.value = dataModelList.value.map(({ id }) => id);
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
    setDataModelIdList();

    updateIntersectionHandler(0);
  };

  /**
   * 데이터모델 조회 -> 누적
   */
  const addDataModelList = async () => {
    const { data } = await getDataModelListAPI();
    dataModelList.value = dataModelList.value.concat(data[currentTab.value]);

    setDataModelIdList();
  };

  /**
   * 데이터모델 추가
   */
  const addDataModel = async ({
    currentPageType, // glossary or category
  }: {
    currentPageType: string;
  }) => {
    // TODO: pageTyp e에 따라 각각 다른 API 를 구현하여 개발
    console.log("currentPageType", currentPageType);
    console.log(selectedDataModelListByTab.value);
  };

  return {
    searchKeyword,
    filters,
    selectedFilters,
    selectedFilterItems,
    tabList,
    currentTab,
    dataModelTotalCountByTab,
    dataModelList,
    dataModelIdList,
    selectedDataModelListByTab,
    clearDataModelModalSettings,
    setSearchKeyword,
    getFilters,
    setEmptyFilter,
    initCurrentTab,
    getDataModelList,
    addDataModelList,
    resetSelectedDataModelListByTab,
    addDataModel,
  };
});
