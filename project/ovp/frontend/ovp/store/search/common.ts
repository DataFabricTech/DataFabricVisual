export interface filters {
  domains: { text: string; data: any[] };
  owners: { text: string; data: any[] };
  tags: { text: string; data: any[] };
  service: { text: string; data: any[] };
  database: { text: string; data: any[] };
  schema: { text: string; data: any[] };
  column: { text: string; data: any[] };
  tableType: { text: string; data: any[] };
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

export interface searchResult {
  data: any[];
  totalCount: number;
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
import listResult from "./samples/listResult.json";
import detailsJson from "./samples/details.json";

export const useSearchCommonStore = defineStore("searchCommon", () => {
  // filter 정보
  const filters: Ref<filters> = ref({} as filters);
  const searchResult: Ref<searchResult> = ref({} as searchResult);
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

  // METHODS
  const getSearchCommonData = async () => {
    // TODO: 서버 연동 후 json 가라 데이터 삭제, 실 데이터로 변경 처리 필요.
    filters.value = listResult.filters;
    searchResult.value = listResult.data;
  };

  const getSearchDetails = async () => {
    // TODO: 서버 연동 후 json 가라 데이터 삭제, 실 데이터로 변경 처리 필요.
    details.value = detailsJson;
  };

  const getPreviewData = async () => {
    // TODO: 서버 연동 후 json 가라 데이터 삭제, 실 데이터로 변경 처리 필요.
    previewData.value = previewJson;
  };

  const setQueryFilterByDepth = (key: string, value: any, suffix: string) => {
    const setTermObj: Ref<any[]> = ref<any[]>([]);
    const setBoolObj: Ref<BoolObj> = ref<BoolObj>({
      bool: { should: [] },
    });

    for (const item of value) {
      const setKeyObj: Ref<KeyObj> = ref<KeyObj>({
        term: { [`${key}${suffix}`]: ref(item) },
      });

      setTermObj.value.push(setKeyObj.value);
    }
    setBoolObj.value.bool.should = setTermObj.value;
    return setBoolObj.value;
  };

  const setQueryFilter = () => {
    const queryFilter: Ref<QueryFilter> = ref({
      query: { bool: { must: [] } },
    });
    const setBoolObj: Ref<object> = ref<object>({});

    for (const key in selectedFilters.value) {
      const value = selectedFilters.value[key];

      switch (key) {
        case "owners":
        case "service":
          setBoolObj.value = setQueryFilterByDepth(
            key,
            value,
            ".displayName.keyword",
          );
          queryFilter.value.query.bool.must.push(setBoolObj.value);
          break;
        case "database":
        case "databaseSchema":
        case "columns":
          setBoolObj.value = setQueryFilterByDepth(key, value, ".name.keyword");
          queryFilter.value.query.bool.must.push(setBoolObj.value);
          break;
        case "tags":
          setBoolObj.value = setQueryFilterByDepth(key, value, ".tagFQN");
          queryFilter.value.query.bool.must.push(setBoolObj.value);
          break;
        default:
          setBoolObj.value = setQueryFilterByDepth(key, value, "");
          queryFilter.value.query.bool.must.push(setBoolObj.value);
      }
    }

    // TODO: 서버 연동 후 queryFilter Request 필요
    console.log("queryFilter: ", queryFilter.value);
  };

  return {
    filters,
    searchResult,
    details,
    previewData,
    selectedFilters,
    viewType,
    isShowPreview,
    isBoxSelectedStyle,
    searchResultLength,
    getSearchCommonData,
    getSearchDetails,
    getPreviewData,
    setQueryFilter,
  };
});
