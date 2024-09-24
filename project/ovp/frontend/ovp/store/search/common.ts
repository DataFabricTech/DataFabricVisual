import { usePagingStore } from "@/store/common/paging";
import { useQueryHelpers } from "@/composables/queryHelpers";
import _ from "lodash";
import { ref } from "vue";
import $constants from "@/utils/constant";

export const FILTER_KEYS = {
  CATEGORY: "category",
  OWNER: "owner.displayName.keyword",
  TAGS: "tags.tagFQN",
  SERVICE: "service.displayName.keyword",
  SERVICE_TYPE: "serviceType",
  DATABASE: "database.displayName.keyword",
  DATABASE_SCHEMA: "databaseSchema.displayName.keyword",
  COLUMNS: "columns.name.keyword",
} as const;

export interface Filter {
  text: string;
  data: any[] | object;
}

export interface Filters {
  [FILTER_KEYS.CATEGORY]: Filter;
  [FILTER_KEYS.OWNER]: Filter;
  [FILTER_KEYS.TAGS]: Filter;
  [FILTER_KEYS.SERVICE]: Filter;
  [FILTER_KEYS.SERVICE_TYPE]: Filter;
  [FILTER_KEYS.DATABASE]: Filter;
  [FILTER_KEYS.DATABASE_SCHEMA]: Filter;
  [FILTER_KEYS.COLUMNS]: Filter;

  [key: string]: Filter; // 인덱스 시그니처 추가
}

export interface SelectedFilters {
  [key: string]: string[];
}

export interface QueryFilter {
  query: {
    bool: {
      must: any[];
    };
  };
}

export interface SearchResultLength {
  table: number;
  storage: number;
  model: number;
}

export const useSearchCommonStore = defineStore("searchCommon", () => {
  const { $api } = useNuxtApp();
  const pagingStore = usePagingStore();
  const { setFrom, updateIntersectionHandler } = pagingStore;
  const { from, size } = storeToRefs(pagingStore);
  const { setQueryFilterByDepth } = useQueryHelpers();

  // filters 초기값 부여 (text 처리)
  const createDefaultFilters = (): Filters => {
    return {
      [FILTER_KEYS.CATEGORY]: { text: "카테고리", data: {} },
      [FILTER_KEYS.OWNER]: { text: "소유자", data: [] },
      [FILTER_KEYS.TAGS]: { text: "태그", data: [] },
      [FILTER_KEYS.SERVICE]: { text: "서비스", data: [] },
      [FILTER_KEYS.SERVICE_TYPE]: { text: "서비스타입", data: [] },
      [FILTER_KEYS.DATABASE]: { text: "데이터베이스", data: [] },
      [FILTER_KEYS.DATABASE_SCHEMA]: { text: "스키마", data: [] },
      [FILTER_KEYS.COLUMNS]: { text: "컬럼", data: [] },
    };
  };
  // filter 정보
  const filters = ref<Filters>(createDefaultFilters());
  const currentTab: Ref<string> = ref("table");
  const searchResult: Ref<any[]> = ref([]);
  const previewData: Ref<any> = ref({
    modelInfo: {
      model: {
        name: "",
      },
    },
  });
  const selectedFilterItems: Ref<any> = ref([]);
  const selectedFilters: Ref<SelectedFilters> = ref({} as SelectedFilters);
  const currentPreviewId: Ref<string | number> = ref("");
  let UNDEFINED_TAG_ID: string = null;

  // DATA
  const viewType: Ref<string> = ref<string>("listView");
  const isShowPreview: Ref<boolean> = ref<boolean>(false);
  const isBoxSelectedStyle: Ref<boolean> = ref<boolean>(false);
  const searchResultLength: Ref<SearchResultLength> = ref<SearchResultLength>({
    model: 0,
    table: 0,
    storage: 0,
  });
  // List Query data
  let searchKeyword: string = "";
  const sortKey: Ref<string> = ref<string>("totalVotes");
  const sortKeyOpt: Ref<string> = ref<string>("desc");
  const isSearchResultNoData: Ref<boolean> = ref<boolean>(false);

  const getSearchListQuery = () => {
    const queryFilter = getQueryFilter();
    const params: any = {
      // open-meta 에서 사용 하는 key 이기 때문에 그대로 사용.
      // eslint 예외 제외 코드 추가.
      // eslint-disable-next-line id-length
      q: searchKeyword,
      index: currentTab.value, // table or storage or model -> tab
      from: from.value,
      size: size.value,
      deleted: false,
      query_filter: JSON.stringify(queryFilter),
      sort_field: sortKey.value,
      sort_order: sortKeyOpt.value,
    };
    return new URLSearchParams(params);
  };

  // METHODS
  const getSearchListAPI = async () => {
    const { data } = await $api(`/api/search/list?${getSearchListQuery()}`, {
      showLoader: false,
    });
    // 특수문자 이슈 때문에  backend 에서 1차로 처리(일부 특수문자에 한해서) 했으나,
    // 그외의 특수문자의 경우, backend 에서 에러를 뱉음 -> 공백으로 처리한다.
    return (
      data ?? {
        data: {
          model: [],
          table: [],
          storage: [],
        },
        totalCount: {
          model: 0,
          table: 0,
          storage: 0,
        },
      }
    );
  };
  /**
   * 데이터 조회 -> 누적
   */
  const addSearchList = async () => {
    const { data, totalCount } = await getSearchListAPI();
    searchResult.value = searchResult.value.concat(data[currentTab.value]);
    searchResultLength.value = totalCount;
  };

  /**
   * 데이터 조회 -> 갱신
   */
  const getSearchList = async () => {
    const { data, totalCount } = await getSearchListAPI();
    searchResult.value = data[currentTab.value];
    searchResultLength.value = totalCount;
    isSearchResultNoData.value = searchResult.value.length === 0;
  };

  const setEmptyFilter = () => {
    selectedFilterItems.value = [];
    selectedFilters.value = {};
  };

  const getFilters = async () => {
    const { data } = await $api(`/api/search/filters`);

    // 기본값 기준 사용할 필터 key 를 정리
    const defaultFilters = createDefaultFilters();
    const useFilters = Object.keys(defaultFilters);

    useFilters.forEach((key: string) => {
      (filters.value as Filters)[key].data = data[key];
    });

    // 미분류 카테고리 ID 저장
    UNDEFINED_TAG_ID = filters.value[FILTER_KEYS.CATEGORY].data.children[0].id;
  };
  const getFilter = async (filterKey: string) => {
    // TODO : 서버 연동 후 json 가라 데이터 삭제, 실 데이터로 변경 처리 필요.
    const data = await $api(`/api/search/filter?field=${filterKey}`);
    (filters.value as Filters)[filterKey].data = data.data[filterKey];
  };

  const getPreviewData = async (fqn: string) => {
    const data: any = await $api(`/api/search/preview/${fqn}`);
    previewData.value = data.data;
  };

  const getContainerPreviewData = async (id: string) => {
    const data: any = await $api(`/api/containers/${id}`);
    previewData.value = data.data;
  };

  const getCtgIds = () => {
    return !_.has(selectedFilters.value, FILTER_KEYS.CATEGORY)
      ? []
      : selectedFilters.value[FILTER_KEYS.CATEGORY].map(
          (filter: any) =>
            `ovp_category.${
              filter.id === UNDEFINED_TAG_ID
                ? $constants.SERVICE.CATEGORY_UNDEFINED_NAME
                : filter.id
            }`,
        );
  };

  const getQueryFilter = (): QueryFilter => {
    const queryFilter: QueryFilter = {
      query: { bool: { must: [] } },
    };

    for (const key in selectedFilters.value) {
      const value =
        key === "category" ? getCtgIds() : selectedFilters.value[key];
      const keyValue = key === "category" ? "tags.tagFQN" : key;

      queryFilter.query.bool.must.push(setQueryFilterByDepth(keyValue, value));
    }

    return queryFilter;
  };

  /**
   * 목록 reset
   * 목록을 '갱신'하는 경우, from 값을 항상 0으로 주어야 하기 때문에 fn 하나로 묶어서 처리.
   */
  const resetReloadList = async () => {
    setFrom(0);
    await getSearchList();
    updateIntersectionHandler(0);
  };
  const setSortInfo = (item: string) => {
    const items = item.split("_");
    sortKey.value = items.shift() ?? ""; // undefined 오류 예외처리
    sortKeyOpt.value = items.pop() ?? "";
  };

  const setSortFilter = (item: string | number = "totalVotes_desc") => {
    if (!_.isUndefined(item) && typeof item === "string") {
      isShowPreview.value = false;
      currentPreviewId.value = "";
      setSortInfo(item);
      resetReloadList();
    }
  };
  const setSearchKeyword = (keyword: string) => {
    searchKeyword = keyword;
  };

  const changeTab = (item: string, loadList: boolean = true) => {
    isShowPreview.value = false;
    currentTab.value = item;

    if (loadList) {
      resetReloadList();
    }
  };

  return {
    sortKey,
    sortKeyOpt,
    currentTab,
    filters,
    searchResult,
    previewData,
    selectedFilterItems,
    selectedFilters,
    viewType,
    isShowPreview,
    isBoxSelectedStyle,
    searchResultLength,
    isSearchResultNoData,
    currentPreviewId,
    addSearchList,
    getSearchList,
    getFilter,
    getFilters,
    getPreviewData,
    getContainerPreviewData,
    setSortInfo,
    setSortFilter,
    setSearchKeyword,
    resetReloadList,
    changeTab,
    updateIntersectionHandler,
    getQueryFilter,
    setEmptyFilter,
  };
});
