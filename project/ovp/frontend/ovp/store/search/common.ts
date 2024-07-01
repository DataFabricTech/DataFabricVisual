export interface filters {
  domains: any[];
  owners: any[];
  tags: any[];
  service: any[];
  database: any[];
  schema: any[];
  column: any[];
  tableType: any[];
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
    columnCnt: number;
    rowsCnt: number;
    tags: any[];
    glossary: any[];
  };
  schema: [];
  sample: {
    columns: string[];
    rows: any[];
  };
  profiling: any[];
  // query: [];
  // lineage: {};
  // recommendModel: [];
}

import resJson from "./res.json";
import previewJson from "./preview.json";

export const useSearchCommonStore = defineStore("searchCommon", () => {
  // filter 정보
  const filters: Ref<any> = ref({});
  const details: Ref<any> = ref({});
  const previewData: Ref<any> = ref({
    modelInfo: {
      model: {
        name: "",
      },
    },
  });

  const getSearchDefaults = async () => {
    // TODO: 서버 연동 후 json 가라 데이터 삭제, 실 데이터로 변경 처리 필요.
    filters.value = resJson;
  };

  const getPreviewData = async () => {
    // TODO: 서버 연동 후 json 가라 데이터 삭제, 실 데이터로 변경 처리 필요.
    previewData.value = previewJson;
  };
  return {
    filters,
    details,
    previewData,
    getSearchDefaults,
    getPreviewData,
  };
});
