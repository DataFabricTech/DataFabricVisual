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

  // TODO : 처리방법 고려 (별도로 조회할껀지 확인 필요)
  // query: [];
  // lineage: {};
  // recommendModel: [];
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

import detailsJson from "./samples/details.json";

export const useSearchCommonStore = defineStore("searchCommon", () => {
  const { $api } = useNuxtApp();

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
  const queryFilter: Ref<QueryFilter> = ref({
    query: { bool: { must: [] } },
  });

  // DATA
  const viewType: Ref<string> = ref<string>("listView");
  const isShowPreview: Ref<boolean> = ref<boolean>(false);
  const isBoxSelectedStyle: Ref<boolean> = ref<boolean>(false);
  const searchResultLength: Ref<number> = ref<number>(0);

  // List Query data
  const from: Ref<number> = ref<number>(0);
  const size: Ref<number> = ref<number>(100);
  const sortKey: Ref<string> = ref<string>("totalVotes");
  const sortKeyOpt: Ref<string> = ref<string>("desc");

  const getSearchListQuery = () => {
    const params: any = {
      // open-meta 에서 사용 하는 key 이기 때문에 그대로 사용.
      // eslint 예외 제외 코드 추가.
      // eslint-disable-next-line id-length
      q: "", // TODO : header 에서 검색어 입력한거 처리
      index: "table_search_index,container_search_index",
      from: from.value,
      size: size.value,
      deleted: false,
      query_filter: JSON.stringify(queryFilter.value),
      sort_field: sortKey.value,
      sort_order: sortKeyOpt.value,
    };
    return new URLSearchParams(params);
  };
  // METHODS
  const getSearchList = async () => {
    const { totalCount, data } = await $api(
      `/api/search/list?${getSearchListQuery()}`,
    );

    // 조회한 데이터 누적
    searchResult.value = searchResult.value.concat(data);
    searchResultLength.value = totalCount;
  };
  const getFilters = async () => {
    const data = await $api(`/api/search/filters`);

    // 사용할 필터를 정리
    const useFilters: string[] = [
      "domains",
      "owner.displayName.keyword",
      "tags.tagFQN",
      "service.displayName.keyword",
      "serviceType",
      "database.displayName.keyword",
      "databaseSchema.displayName.keyword",
      "columns.name.keyword",
      "tableType",
    ];
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
    const fqn: string = "df2.test_db.test_db.Active_Employees"; // 실제 fqn 값을 여기에 설정합니다.
    const data: any = await $api(`/api/search/preview/${fqn}`);
    previewData.value = data;
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

  const setQueryFilter = () => {
    queryFilter.value = {
      query: { bool: { must: [] } },
    };
    const setBoolObj: Ref<object> = ref<object>({});

    for (const key in selectedFilters.value) {
      const value = selectedFilters.value[key];
      setBoolObj.value = setQueryFilterByDepth(key, value);
      queryFilter.value.query.bool.must.push(setBoolObj.value);
    }

    const isEmptyData = searchResult.value.length < 10;

    // filter 값이 변경 되면, 조건을 리셋한다.
    resetReloadList();

    // TODO : intersection-observer 가 catch 하지 못할때의 데이터 로딩 처리 필요
    // 확인한 내용으로는 filter 를 선택하여 목록의 갯수가 N개일때, filter 를 재조정 했을때 intersection-observer가 callback 함수를 실행하지 않아 목록을 더이상 갱신할 수 없는 부분이 있음.
    // 그래서 N개 목록인 경우, 데이터 조회를 여기서 해준다.
    // 기준은 10으로 잡음. 정확한 수치 알수 없음. 갯수 기준은 화면에 표시되는 갯수 기준 인것 같음. 확대/축소에 따라 다름.
    if (isEmptyData) {
      getSearchList();
    }
  };
  const resetReloadList = () => {
    // 조건이 변경되었기 때문에, 목록의 start 값을 0으로, 기 조회하여 누적되어있는 데이터 목록도 리셋한다.
    // 다만 데이터 조회는 index.vue 페이지에서 intersection observer 에서 하기 때문에 여기서는 조회하지 않는다.
    from.value = 0;
    searchResult.value = [];
  };
  const setSortInfo = (item: string) => {
    const items = item.split("_");
    sortKey.value = items.shift() ?? ""; // undefined 오류 예외처리
    sortKeyOpt.value = items.pop() ?? "";
    resetReloadList();
  };
  const setScrollFrom = (count: number) => {
    from.value = count;
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
    getSearchList,
    getFilters,
    getSearchDetails,
    getPreviewData,
    setQueryFilter,
    setSortInfo,
    setScrollFrom,
  };
});
