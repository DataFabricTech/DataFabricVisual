import { usePagingStore } from "~/store/common/paging";
import _ from "lodash";

export const FILTER_KEYS = {
  CATEGORY: "domains",
  OWNER: "owner.displayName.keyword",
  TAGS: "tags.tagFQN",
  SERVICE: "service.displayName.keyword",
  SERVICE_TYPE: "serviceType",
  DATABASE: "database.displayName.keyword",
  DATABASE_SCHEMA: "databaseSchema.displayName.keyword",
  COLUMNS: "columns.name.keyword",
  TABLE_TYPE: "tableType",
} as const;

export interface Filter {
  text: string;
  data: any[];
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
  [FILTER_KEYS.TABLE_TYPE]: Filter;

  [key: string]: { text: string; data: any[] }; // 인덱스 시그니처 추가
}

export interface SelectedFilters {
  [key: string]: string[];
}

interface SingleKeyObj {
  [key: string]: Ref<string>;
}

interface KeyObj {
  term: SingleKeyObj;
}

interface BoolObj {
  bool: {
    should: any[];
  };
}

export interface QueryFilter {
  query: {
    bool: {
      must: any[];
    };
  };
}

export const useSearchCommonStore = defineStore("searchCommon", () => {
  const { $api } = useNuxtApp();
  const pagingStore = usePagingStore();

  // filters 초기값 부여 (text 처리)
  const createDefaultFilters = (): Filters => {
    return {
      [FILTER_KEYS.CATEGORY]: { text: "카테고리", data: [] },
      [FILTER_KEYS.OWNER]: { text: "소유자", data: [] },
      [FILTER_KEYS.TAGS]: { text: "태그", data: [] },
      [FILTER_KEYS.SERVICE]: { text: "서비스", data: [] },
      [FILTER_KEYS.SERVICE_TYPE]: { text: "서비스타입", data: [] },
      [FILTER_KEYS.DATABASE]: { text: "데이터베이스", data: [] },
      [FILTER_KEYS.DATABASE_SCHEMA]: { text: "스키마", data: [] },
      [FILTER_KEYS.COLUMNS]: { text: "컬럼", data: [] },
      [FILTER_KEYS.TABLE_TYPE]: { text: "테이블타입", data: [] },
    };
  };
  // filter 정보
  const filters = ref<Filters>(createDefaultFilters());
  // TODO : [개발] 탐색 - 목록 페이지 tab 구현 완료 되면 currentTab 부분 변수 맞춰서 수정 필요.
  const currentTab: Ref<string> = ref("table");
  const searchResult: Ref<any[]> = ref([]);
  const previewData: Ref<any> = ref({
    modelInfo: {
      model: {
        name: "",
      },
    },
  });
  const selectedFilters: Ref<SelectedFilters> = ref({} as SelectedFilters);

  // DATA
  const viewType: Ref<string> = ref<string>("listView");
  const isShowPreview: Ref<boolean> = ref<boolean>(false);
  const isBoxSelectedStyle: Ref<boolean> = ref<boolean>(false);
  const searchResultLength: Ref<number> = ref<number>(0);

  // List Query data
  let searchKeyword: string = "";
  const sortKey: Ref<string> = ref<string>("totalVotes");
  const sortKeyOpt: Ref<string> = ref<string>("desc");

  const getSearchListQuery = () => {
    const queryFilter = getQueryFilter();
    const params: any = {
      // open-meta 에서 사용 하는 key 이기 때문에 그대로 사용.
      // eslint 예외 제외 코드 추가.
      // eslint-disable-next-line id-length
      q: searchKeyword,
      index: currentTab.value, // table or storage or model -> tab
      from: pagingStore.state.from,
      size: pagingStore.state.size,
      deleted: false,
      query_filter: JSON.stringify(queryFilter),
      sort_field: sortKey.value,
      sort_order: sortKeyOpt.value,
      trino_query: JSON.stringify(getTrinoQuery(queryFilter)),
    };
    return new URLSearchParams(params);
  };
  const getTrinoQuery = (queryFilter: QueryFilter) => {
    // query 구현을 backend 에서 하려니까 코드가 너무 복잡해져서 front 에 해서 넘겨서 처리.
    const trinoFilter: QueryFilter = {
      query: {
        bool: {
          must: [{ bool: { should: [{ term: { serviceType: "trino" } }] } }],
        },
      },
    };
    const trinoMustArray = trinoFilter.query.bool.must;
    queryFilter.query.bool.must = _.concat(
      queryFilter.query.bool.must,
      trinoMustArray,
    );
    return queryFilter;
  };
  // METHODS
  const getSearchListAPI = async () => {
    const { data } = await $api(`/api/search/list?${getSearchListQuery()}`);
    return data;
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
  };
  const getFilters = async () => {
    const { data } = await $api(`/api/search/filters`);

    // 기본값 기준 사용할 필터 key 를 정리
    const defaultFilters = createDefaultFilters();
    const useFilters = Object.keys(defaultFilters);

    useFilters.forEach((key: string) => {
      (filters.value as Filters)[key].data = data[key];
    });
  };
  const getFilter = async (filterKey: string) => {
    // TODO : 서버 연동 후 json 가라 데이터 삭제, 실 데이터로 변경 처리 필요.
    // const data = await $api(`/api/search/filter?filterKey=${filterKey}`);

    const data: any = {
      "owner.displayName.keyword": [
        {
          key: "N_mariadb",
        },
        {
          key: "N_bigquery",
        },
        {
          key: "N_mysql",
        },
        {
          key: "N_glue",
        },
      ],
      domains: [
        {
          key: "N_category1",
        },
        {
          key: "N_category2",
        },
        {
          key: "N_category3",
        },
        {
          key: "N_category",
        },
      ],
    };
    (filters.value as Filters)[filterKey].data = data[filterKey];
  };

  const getPreviewData = async (fqn: string) => {
    const data: any = await $api(`/api/search/preview/${fqn}`);
    previewData.value = data.data;
  };

  const setQueryFilterByDepth = (key: string, value: any) => {
    const setTermObj: Ref<any[]> = ref<any[]>([]);
    const setBoolObj: Ref<BoolObj> = ref<BoolObj>({
      bool: { should: [] },
    });

    for (const item of value) {
      const setKeyObj: Ref<KeyObj> = ref<KeyObj>({
        term: { [`${key}`]: ref(item) },
      });

      setTermObj.value.push(setKeyObj.value);
    }
    setBoolObj.value.bool.should = setTermObj.value;
    return setBoolObj.value;
  };

  const getQueryFilter = () => {
    const queryFilter: QueryFilter = {
      query: { bool: { must: [] } },
    };
    let setBoolObj: object = {};

    for (const key in selectedFilters.value) {
      const value = selectedFilters.value[key];
      setBoolObj = setQueryFilterByDepth(key, value);
      queryFilter.query.bool.must.push(setBoolObj);
    }
    return queryFilter;
  };

  /**
   * 목록 reset
   * 목록을 '갱신'하는 경우, from 값을 항상 0으로 주어야 하기 때문에 fn 하나로 묶어서 처리.
   */
  const resetReloadList = () => {
    setScrollFrom(0);
    pagingStore.updateIntersectionHandler(0);
    getSearchList();
  };
  const setSortInfo = (item: string) => {
    const items = item.split("_");
    sortKey.value = items.shift() ?? ""; // undefined 오류 예외처리
    sortKeyOpt.value = items.pop() ?? "";
  };
  const setScrollFrom = (count: number) => {
    pagingStore.state.from = count;
  };
  const setSearchKeyword = (keyword: string) => {
    searchKeyword = keyword;
  };

  return {
    sortKey,
    sortKeyOpt,
    currentTab,
    filters,
    searchResult,
    previewData,
    selectedFilters,
    viewType,
    isShowPreview,
    isBoxSelectedStyle,
    searchResultLength,
    addSearchList,
    getSearchList,
    getFilter,
    getFilters,
    getPreviewData,
    setSortInfo,
    setScrollFrom,
    setSearchKeyword,
    resetReloadList,
  };
});
