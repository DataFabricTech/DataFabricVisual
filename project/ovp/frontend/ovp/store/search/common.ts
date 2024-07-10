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

import previewJson from "./samples/preview.json";
import detailsJson from "./samples/details.json";

import _ from "lodash";

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
  const queryFilter: Ref<QueryFilter> = ref({
    query: { bool: { must: [] } },
  });

  // DATA
  const viewType: Ref<string> = ref<string>("listView");
  const isShowPreview: Ref<boolean> = ref<boolean>(false);
  const isBoxSelectedStyle: Ref<boolean> = ref<boolean>(false);
  const searchResultLength: Ref<number> = ref<number>(0);

  // List Query data
  const searchKeyword: Ref<string> = ref<string>("");
  const SIZE_CNT = 100;
  const from: Ref<number> = ref<number>(0);
  const size: Ref<number> = ref<number>(SIZE_CNT);
  const sortKey: Ref<string> = ref<string>("totalVotes");
  const sortKeyOpt: Ref<string> = ref<string>("desc");

  const getSearchListQuery = () => {
    const params: any = {
      // open-meta 에서 사용 하는 key 이기 때문에 그대로 사용.
      // eslint 예외 제외 코드 추가.
      // eslint-disable-next-line id-length
      q: "employee",
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
  const getSearchList = async (useAdd: boolean = true) => {
    const { totalCount, data } = await $api(
      `/api/search/list?${getSearchListQuery()}`,
    );

    // 조회한 데이터 누적 or 대치
    searchResult.value = useAdd ? searchResult.value.concat(data) : data;
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

  const setQueryFilter = () => {
    queryFilter.value = {
      query: { bool: { must: [] } },
    };

    // 초기화 클릭 or filter 선택을 모두 해제해서 초기화 누른거랑 동일한 환경
    if (_.isEmpty(selectedFilters.value)) {
      /**
       * '초기화' 를 클릭했을때 searchResult 목록을 0부터 가지고 와야함.
       * from 값을 0으로 설정하고 목록을 조회하면
       *
       * 1. from 0 -> api 조회
       * 2. 1의 response 를 화면에 표시
       * 3. 2의 동작으로 observer 가 동작 ( 내부에서 from += size ) 가 됨. -> from 5
       * 4.
       * 초기화 클릭했을때 searchResult 를 0부터 가지고 와야하는데
       * from 을 0으로 놓고, 목록을 reload 하면,
       * 1. from 0 으로 해서 api 조회
       * 2. 1의 api 조회 response 값을 화면에 표시
       * 3. 2의 동작으로 intersection-observer 가 동작 ( 여기서 from += size 가 됨 )
       * 4. 3의 동작 callback으로 api 를 다시 조회 ( from 을 +SIZE_CNT 로 다시 호출 )
       *
       * 화면을 맨 처음 조회하는 경우, from 을 -SIZE_CNT 로 하면,
       * -SIZE_CNT + SIZE_CNT 가 되어 0이 됨. api 호출을 2번 하지 않고 1번만 하고 끝남.
       */
      resetReloadList(-SIZE_CNT);
    } else {
      const setBoolObj: Ref<object> = ref<object>({});

      for (const key in selectedFilters.value) {
        const value = selectedFilters.value[key];
        setBoolObj.value = setQueryFilterByDepth(key, value);
        queryFilter.value.query.bool.must.push(setBoolObj.value);
      }
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
  const resetReloadList = (count: number = 0) => {
    setScrollFrom(count);
    count < 0 ? (searchResult.value = []) : getSearchList(false);
  };
  const setSortInfo = (item: string) => {
    const items = item.split("_");
    sortKey.value = items.shift() ?? ""; // undefined 오류 예외처리
    sortKeyOpt.value = items.pop() ?? "";
    resetReloadList();
  };
  const setScrollFrom = (count: number) => {
    from.value = count;
    // count 가 0 포함 이하 일때, handler 값을 갱신해준다. ( 초기화 등이 되어서 화면이 목록이 갱신되었기 때문 )
    if (count < 1) {
      if (intersectionHandler !== null) {
        intersectionHandler.updateChangingInitialCount(from.value);
      }
    }
  };
  const setSearchKeyword = (keyword: string) => {
    searchKeyword.value = keyword;
    // 검색 조건 리셋.
    resetReloadList(0);
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
    getSearchList,
    getFilters,
    getSearchDetails,
    getPreviewData,
    setQueryFilter,
    setSortInfo,
    setScrollFrom,
    setSearchKeyword,
    setIntersectionHandler,
  };
});
