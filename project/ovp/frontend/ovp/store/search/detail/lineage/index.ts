import type { Ref } from "vue";

export interface lineageData {
  nodes: any[];
  edges: any[];
}

import ownerListJson from "./samples/ownerFilterList.json";
import tagListJson from "./samples/tagFilterList.json";

export const useLineageStore = defineStore("lineage", () => {
  const { $api } = useNuxtApp();
  const lineageData: Ref<lineageData> = ref({} as lineageData);

  // TODO: 다른곳에서 preview에 대한 store를 만들기 떄문에 삭제 예정
  const previewData: Ref<any> = ref({
    modelType: "structured",
    modelInfo: {
      model: {
        name: "",
        desc: "",
        tableType: "",
        cnt: 0,
        columns: [],
      },
      details: "",
      url: "",
    },
    tags: [],
    glossaries: [],
  });

  const categoryList = ref([]);
  const ownerList = ref([]);
  const tagList = ref([]);
  const serviceList = ref([]);

  // TODO: (fqn(외부스토어에서 호출, 필터) 파람 추가 필요
  const getLineageData = async (type: string, fqn: string) => {
    console.log();
    const queryFilter: any = {
      query: {
        bool: {
          must: [
            {
              bool: {
                should: [],
              },
            },
          ],
        },
      },
    };
    const params: any = {
      fqn: fqn,
      query_filter: encodeURIComponent(JSON.stringify(queryFilter)),
      upstreamDepth: "10",
      downstreamDepth: "10",
      includeDeleted: false,
      type: "table",
    };

    const data = await $api(`/api/search/detail/lineage/${type}`, {
      params: params,
    });

    lineageData.value.nodes = data.data.rawNodes;
    lineageData.value.edges = data.data.rawEdges;
  };

  // TODO: 다른곳에서 preview에 대한 store를 만들기 떄문에 삭제 예정
  // TODO: fqn 파람 추가 필요
  const getPreviewData = async (fqn: string) => {
    // TODO: 서버 연동 후 json 가라 데이터 삭제, 실 데이터로 변경 처리 필요.
    const data: any = await $api(`/api/search/preview/${fqn}`);
    console.log(data.data);
    previewData.value = data.data;
    // TODO: fqn값을 쿼리파람에 넣어 api 요청 후 previewData 세팅 필요
  };

  const getCateList = async () => {
    // TODO: 서버 연동 후 json 가라 데이터 삭제, 실 데이터로 변경 처리 필요.
    categoryList.value = ownerListJson.data.map((item) => ({
      id: item,
      value: item,
    }));
  };

  const getOwnerList = async () => {
    // TODO: 서버 연동 후 json 가라 데이터 삭제, 실 데이터로 변경 처리 필요.
    ownerList.value = ownerListJson.data.map((item) => ({
      id: item,
      value: item,
    }));
  };
  const getTagList = async () => {
    // TODO: 서버 연동 후 json 가라 데이터 삭제, 실 데이터로 변경 처리 필요.
    tagList.value = tagListJson.data.map((item) => ({
      id: item,
      value: item,
    }));
  };
  const getServiceList = async () => {
    // TODO: 서버 연동 후 json 가라 데이터 삭제, 실 데이터로 변경 처리 필요.
    serviceList.value = ownerListJson.data.map((item) => ({
      id: item,
      value: item,
    }));
  };
  return {
    lineageData,
    previewData,
    categoryList,
    ownerList,
    tagList,
    serviceList,
    getLineageData,
    getPreviewData,
    getCateList,
    getOwnerList,
    getTagList,
    getServiceList,
  };
});
