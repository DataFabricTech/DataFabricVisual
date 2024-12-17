import type { Filters, SelectedFilters } from "@/store/search/common";
import { FILTER_KEYS, useSearchCommonStore } from "@/store/search/common";
import { ref } from "vue";
import _ from "lodash";
import type { PreviewData } from "@/type/common";

export interface lineageData {
  nodes: any[];
  edges: any[];
}

export const useLineageStore = defineStore("lineage", () => {
  const { $api } = useNuxtApp();
  const searchCommonStore = useSearchCommonStore();
  const {
    getUseFilters,
    getQueryFilter,
    createDefaultPreview,
    getPreviewAPI,
    getContainerPreviewAPI,
  } = searchCommonStore;

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
  const lineageFilterRef = ref(null);
  const previewData: Ref<PreviewData> = ref(createDefaultPreview());

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

  const getPreviewData = async (dataModelType: string, { fqn, id }: any) => {
    const data =
      dataModelType === "storage"
        ? await getContainerPreviewAPI(id)
        : await getPreviewAPI(fqn);
    previewData.value = data.data;
  };

  return {
    filters,
    selectedFilters,
    selectedFilterItems,
    lineageData,
    previewData,
    isShowPreview,
    lineageRef,
    lineageFilterRef,
    getFilters,
    setEmptyFilter,
    getLineageData,
    getPreviewData,
  };
});
