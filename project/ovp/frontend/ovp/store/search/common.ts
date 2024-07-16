import { IntersectionObserverHandler } from "~/utils/intersection-observer";

export interface Filter {
  text: string;
  data: any[];
}

export interface Filters {
  domains: Filter;
  "owner.displayName.keyword": Filter;
  "tags.tagFQN": Filter;
  "service.displayName.keyword": Filter;
  serviceType: Filter;
  "database.displayName.keyword": Filter;
  "databaseSchema.displayName.keyword": Filter;
  "columns.name.keyword": Filter;
  tableType: Filter;
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

import previewJson from "./samples/preview.json";
import detailsJson from "./samples/details.json";

export const useSearchCommonStore = defineStore("searchCommon", () => {
  const { $api } = useNuxtApp();

  let intersectionHandler: IntersectionObserverHandler | null = null;

  // filters 초기값 부여 (text 처리)
  const createDefaultFilters = (): Filters => {
    return {
      domains: { text: "카테고리", data: [] },
      "owner.displayName.keyword": { text: "소유자", data: [] },
      "tags.tagFQN": { text: "태그", data: [] },
      "service.displayName.keyword": {
        text: "서비스",
        data: [],
      },
      serviceType: {
        text: "서비스타입",
        data: [],
      },
      "database.displayName.keyword": {
        text: "데이터베이스",
        data: [],
      },
      "databaseSchema.displayName.keyword": {
        text: "스키마",
        data: [],
      },
      "columns.name.keyword": { text: "컬럼", data: [] },
      tableType: { text: "테이블타입", data: [] },
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
      (filters.value as any)[key].data = data[key];
    });
  };

  const getSearchDetails = async () => {
    // TODO: 서버 연동 후 json 가라 데이터 삭제, 실 데이터로 변경 처리 필요.
    details.value = detailsJson;
  };

  const getPreviewData = async () => {
    // TODO: 서버 연동 후 json 가라 데이터 삭제, 실 데이터로 변경 처리 필요.
    previewData.value = previewJson;
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
    getFilters,
    getSearchDetails,
    getPreviewData,
    setSortInfo,
    setScrollFrom,
    setSearchKeyword,
    setIntersectionHandler,
    resetReloadList,
    updateIntersectionHandler,
  };
});
