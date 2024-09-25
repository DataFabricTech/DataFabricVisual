import type { Filters, SelectedFilters } from "@/store/search/common";
import { FILTER_KEYS, useSearchCommonStore } from "@/store/search/common";
import { ref } from "vue";
import _ from "lodash";

export interface lineageData {
  nodes: any[];
  edges: any[];
}

export const useLineageStore = defineStore("lineage", () => {
  const { $api } = useNuxtApp();
  const searchCommonStore = useSearchCommonStore();
  const { getUseFilters, getQueryFilter } = searchCommonStore;

  // filters 초기값 부여 (text 처리)
  const createDefaultFilters = (): Partial<Filters> => {
    return {
      [FILTER_KEYS.CATEGORY]: { text: "카테고리", data: {} },
      [FILTER_KEYS.OWNER]: { text: "소유자", data: [] },
      [FILTER_KEYS.TAGS]: { text: "태그", data: [] },
      [FILTER_KEYS.SERVICE]: { text: "서비스", data: [] },
    };
  };

  const filters = ref<Partial<Filters>>(createDefaultFilters());
  const selectedFilterItems: Ref<any> = ref({});
  const selectedFilters: Ref<SelectedFilters> = ref({} as SelectedFilters);
  let UNDEFINED_TAG_ID: string = "";
  const lineageData = ref<lineageData>({} as lineageData);
  const isShowPreview = ref<boolean>(false);
  const lineageRef = ref(null);

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

  const getFilters = async () => {
    filters.value = (await getUseFilters(
      createDefaultFilters(),
    )) as Partial<Filters>;

    // 미분류 카테고리 ID 저장
    const categoryData = filters.value[FILTER_KEYS.CATEGORY]?.data;
    if (_.has(categoryData, "children")) {
      UNDEFINED_TAG_ID = _.get(categoryData, "children[0].id", "");
    }
  };

  const setEmptyFilter = () => {
    selectedFilterItems.value = {};
    selectedFilters.value = {};
  };

  const getLineageData = async (type: string, fqn: string) => {
    const queryFilter = getQueryFilter(selectedFilters.value, UNDEFINED_TAG_ID);
    const params: any = {
      fqn: fqn,
      query_filter: encodeURIComponent(JSON.stringify(queryFilter)),
      upstreamDepth: "10",
      downstreamDepth: "10",
      includeDeleted: false,
      type: type === "storage" ? "container" : "table",
    };

    const data = await $api(`/api/search/detail/lineage`, {
      params: params,
    });

    if (data.data === null) {
      lineageData.value.nodes = [];
      lineageData.value.edges = [];
      return;
    }

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

  return {
    filters,
    selectedFilters,
    selectedFilterItems,
    lineageData,
    previewData,
    isShowPreview,
    lineageRef,
    getFilters,
    setEmptyFilter,
    getLineageData,
    getPreviewData,
  };
});
