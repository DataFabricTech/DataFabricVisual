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

import previewJson from "./samples/preview.json";
import listResult from "./samples/listResult.json";
import detailsJson from "./samples/details.json";
import filtersJson from "./samples/filters.json";

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

  const getSearchList = async () => {
    // TODO: 서버 연동 후 json 가라 데이터 삭제, 실 데이터로 변경 처리 필요.
    searchResult.value = listResult.data;
  };

  const getFilters = async () => {
    // TODO: 서버 연동 후 json 가라 데이터 삭제, 실 데이터로 변경 처리 필요.
    filters.value = filtersJson;
  };

  const getSearchDetails = async () => {
    // TODO: 서버 연동 후 json 가라 데이터 삭제, 실 데이터로 변경 처리 필요.
    details.value = detailsJson;
  };

  const getPreviewData = async () => {
    // TODO: 서버 연동 후 json 가라 데이터 삭제, 실 데이터로 변경 처리 필요.
    previewData.value = previewJson;
  };
  return {
    filters,
    searchResult,
    details,
    previewData,
    getFilters,
    getSearchList,
    getSearchDetails,
    getPreviewData,
  };
});
