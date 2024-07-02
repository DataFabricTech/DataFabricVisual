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
  // query: [];
  // lineage: {};
  // recommendModel: [];
}

export interface searchResult {
  data: any[];
  totalCount: number;
}

import previewJson from "./preview.json";
import listResult from "./listResult.json";
import detailsJson from "./details.json";

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
  return {
    filters,
    searchResult,
    details,
    previewData,
    getSearchCommonData,
    getSearchDetails,
    getPreviewData,
  };
});
