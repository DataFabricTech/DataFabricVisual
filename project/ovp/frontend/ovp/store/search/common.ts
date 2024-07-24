import { IntersectionObserverHandler } from "~/utils/intersection-observer";
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

export interface details {
  modelInfo: {
    id: string;
    fqn: string;
    depth: string;
    firDataNm: string;
    modelNm: string;
    modelDesc: string;
    owners: {
      id: string;
      name: string;
    };
    category: {
      description: string;
      displayName: string;
      id: string;
      name: string;
    };
    iconSrc: string;
  };
  cntInfo: {
    upVotes: number;
    downVotes: number;
    bookmarked: number;
  };

  basicInfo: {
    type: string;
    columnsCnt: number;
    rowsCnt: number;
    tags: any[];
    glossary: any[];
  };
  schema: any[];
  sample: {
    columns: string[];
    rows: any[];
  };
  profiling: any[];
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

  let intersectionHandler: IntersectionObserverHandler | null = null;

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
  const searchResult: Ref<any[]> = ref([]);
  const details: Ref<details> = ref({} as details);
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
  const from: Ref<number> = ref<number>(0);
  const size: Ref<number> = ref<number>(100);
  const sortKey: Ref<string> = ref<string>("totalVotes");
  const sortKeyOpt: Ref<string> = ref<string>("desc");

  const getSearchListQuery = () => {
    const params: any = {
      // open-meta 에서 사용 하는 key 이기 때문에 그대로 사용.
      // eslint 예외 제외 코드 추가.
      // eslint-disable-next-line id-length
      q: searchKeyword,
      index: "table_search_index,container_search_index",
      from: from.value,
      size: size.value,
      deleted: false,
      query_filter: JSON.stringify(getQueryFilter()),
      sort_field: sortKey.value,
      sort_order: sortKeyOpt.value,
    };
    return new URLSearchParams(params);
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
    searchResult.value = searchResult.value.concat(data);
    searchResultLength.value = totalCount;
  };

  /**
   * 데이터 조회 -> 갱신
   */
  const getSearchList = async () => {
    const { data, totalCount } = await getSearchListAPI();
    searchResult.value = data;
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
    updateIntersectionHandler(0);
    getSearchList();
  };
  const setSortInfo = (item: string) => {
    const items = item.split("_");
    sortKey.value = items.shift() ?? ""; // undefined 오류 예외처리
    sortKeyOpt.value = items.pop() ?? "";
  };
  const setScrollFrom = (count: number) => {
    from.value = count;
  };
  const updateIntersectionHandler = (count: number) => {
    if (count < 1) {
      if (intersectionHandler !== null) {
        intersectionHandler.updateChangingInitialCount(from.value);
        intersectionHandler.scrollToFirElement();
      }
    }
  };
  const setSearchKeyword = (keyword: string) => {
    searchKeyword = keyword;
  };
  const setIntersectionHandler = (ih: any) => {
    intersectionHandler = ih;
  };

  return {
    from,
    size,
    sortKey,
    sortKeyOpt,
    filters,
    searchResult,
    details,
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
    setIntersectionHandler,
    resetReloadList,
    updateIntersectionHandler,
  };
});
