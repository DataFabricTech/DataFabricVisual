import type { TreeViewItem } from "@extends/tree/TreeProps";
import { usePagingStore } from "~/store/common/paging";
import _ from "lodash";
import { ref } from "vue";

export const FILTER_KEYS = {
  OWNER: "owner.displayName.keyword",
  TAGS: "tags.tagFQN",
  SERVICE: "service.displayName.keyword",
  SERVICE_TYPE: "serviceType",
} as const;

export interface Filter {
  text: string;
  data: any[] | object;
}

export interface Filters {
  [FILTER_KEYS.OWNER]: Filter;
  [FILTER_KEYS.TAGS]: Filter;
  [FILTER_KEYS.SERVICE]: Filter;
  [FILTER_KEYS.SERVICE_TYPE]: Filter;

  [key: string]: Filter; // 인덱스 시그니처 추가
}

export interface SearchResultLength {
  table: number;
  storage: number;
  model: number;
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

export const useGovernCategoryStore = defineStore("GovernCategory", () => {
  const { $api } = useNuxtApp();
  const pagingStore = usePagingStore();
  const { setFrom, updateIntersectionHandler } = pagingStore;
  const { from, size } = storeToRefs(pagingStore);

  let selectedNode: any = null;
  const categories: Ref<TreeViewItem[]> = ref<TreeViewItem[]>([]);
  const categoriesParentId = ref("");
  const isCategoriesNoData = ref(false);
  const modelList: Ref<any[]> = ref([]);
  const isBoxSelectedStyle: Ref<boolean> = ref<boolean>(false);
  const categoryAddName = ref<string>("");
  const categoryAddDesc = ref<string>("");
  const showAddNameNoti = ref<boolean>(false);
  const showAddDescNoti = ref<boolean>(false);
  const selectedModelList = ref([]);

  const previewData: Ref<any> = ref({
    modelInfo: {
      model: {
        name: "",
      },
    },
  });

  // METHODS
  // TREE
  const getCategories = async () => {
    const { data } = await $api(`/api/category/list`);

    /**
     * data 는 root node 기준 으로 조회 됨.
     * root node 는 화면에 표시하지 않기 때문에 rootNode.children 만 categories 에 저장.
     */
    categories.value = data.children;
    isCategoriesNoData.value = categories.value.length === 0;
    categoriesParentId.value = data.parentId;
  };
  const getModelListQuery = (tagId: string, value?: string) => {
    // TODO : [개발] 검색어 조건 여기 추가.
    const params: any = {
      // eslint-disable-next-line id-length
      q: value || "",
      from: from.value,
      size: size.value,
      tagId: tagId,
    };
    return new URLSearchParams(params);
  };
  const getModelByCategoryIdAPI = async (
    node: TreeViewItem,
    value?: string,
  ) => {
    if (_.isNull(node) || _.isEmpty(node.tagId)) {
      return;
    }
    const { data } = await $api(
      `/api/category/models?${getModelListQuery(node.tagId, value)}`,
    );
    return data;
  };
  const addModelList = async () => {
    const data = await getModelByCategoryIdAPI(selectedNode);
    modelList.value = modelList.value.concat(data);
  };
  const getModelList = async (value?: string) => {
    if (_.isNull(selectedNode)) {
      return;
    }
    const data = await getModelByCategoryIdAPI(selectedNode, value);
    modelList.value = data === null ? [] : data;
  };
  const setSelectedNode = (node: any) => {
    selectedNode = node;
  };
  const addCategory = (node: TreeViewItem) => {
    insertOrEditAPI("PUT", node);
  };
  const editCategory = (node: TreeViewItem) => {
    insertOrEditAPI("PATCH", node);
  };
  const insertOrEditAPI = (method: string, node: TreeViewItem) => {
    $api("/api/category", {
      method: method,
      body: node,
    }).then((res: any) => {
      if (res.data === "HAS_SAME_NAME") {
        alert(`${categoryAddName.value} 이 이미 존재합니다.`);
        return;
      }

      if (res.data === "OVER_DEPTH") {
        alert("카테고리는 최대 3depth 까지만 추가할 수 있습니다.");
        return;
      }
      getCategories();
    });
  };
  const moveCategory = async (dropNodeId: string, targetNodeId: string) => {
    const { data } = await $api("/api/category/move", {
      method: "PUT",
      body: {
        dropNodeId: dropNodeId,
        targetNodeId: targetNodeId,
      },
    });
    return data;
  };
  const deleteCategory = (nodeId: string) => {
    return $api(`/api/category`, {
      method: "delete",
      body: {
        id: nodeId,
      },
    });
  };

  const addNewCategory = (newNode: TreeViewItem) => {
    addCategory(newNode);
  };

  // PREVIEW
  const getPreviewData = async (fqn: string) => {
    const data: any = await $api(`/api/search/preview/${fqn}`);
    previewData.value = data.data;
  };

  // MODAL
  const resetAddModalStatus = () => {
    showAddNameNoti.value = false;
    showAddDescNoti.value = false;
    categoryAddName.value = "";
    categoryAddDesc.value = "";
  };

  // filters 초기값 부여 (text 처리)
  const createDefaultFilters = (): Filters => {
    return {
      [FILTER_KEYS.OWNER]: { text: "소유자", data: [] },
      [FILTER_KEYS.TAGS]: { text: "태그", data: [] },
      [FILTER_KEYS.SERVICE]: { text: "서비스", data: [] },
      [FILTER_KEYS.SERVICE_TYPE]: { text: "서비스타입", data: [] },
    };
  };
  // filter 정보
  const filters = ref<Filters>(createDefaultFilters());
  const currentTab: Ref<string> = ref("table");
  const searchResult: Ref<any[]> = ref([]);
  const selectedFilters: Ref<SelectedFilters> = ref({} as SelectedFilters);
  const searchResultLength: Ref<SearchResultLength> = ref<SearchResultLength>({
    model: 0,
    table: 0,
    storage: 0,
  });

  const selectedDataModelList = ref([]);
  const dataModelIdList = ref([]);

  // List Query data
  let searchKeyword: string = "";
  const isSearchResultNoData: Ref<boolean> = ref<boolean>(false);
  const getSearchListQuery = () => {
    const queryFilter = getQueryFilter();
    const params: any = {
      q: searchKeyword,
      index: currentTab.value, // table or storage or model -> tab
      from: from.value,
      size: size.value,
      deleted: false,
      query_filter: JSON.stringify(queryFilter),
      sort_field: "totalVotes",
      trino_query: JSON.stringify(getTrinoQuery(queryFilter)),
    };
    return new URLSearchParams(params);
  };

  const getTrinoQuery = (queryFilter: QueryFilter) => {
    // query 구현을 backend 에서 하려니까 코드가 너무 복잡해져서 front 에 해서 넘겨서 처리.
    const trinoFilter: QueryFilter = {
      query: {
        bool: {
          must: [{ bool: { should: [{ term: { serviceType: "trino" } }] } }],
        },
      },
    };
    const trinoMustArray = trinoFilter.query.bool.must;
    queryFilter.query.bool.must = _.concat(
      queryFilter.query.bool.must,
      trinoMustArray,
    );
    return queryFilter;
  };
  // METHODS
  const getSearchListAPI = async () => {
    const { data } = await $api(`/api/search/list?${getSearchListQuery()}`, {
      showLoader: false,
    });
    return data;
  };
  /**
   * 데이터 조회 -> 누적
   */
  const addSearchList = async () => {
    const { data, totalCount } = await getSearchListAPI();
    searchResult.value = searchResult.value.concat(data[currentTab.value]);
    searchResultLength.value = totalCount;
  };

  /**
   * 데이터 조회 -> 갱신
   */
  const getSearchList = async () => {
    const { data, totalCount } = await getSearchListAPI();
    searchResult.value = data[currentTab.value];
    searchResultLength.value = totalCount;
    isSearchResultNoData.value = searchResult.value.length === 0;
    console.log("갱신 searchResultLength", searchResultLength.value);
    setDataModelIdList();
  };
  const getFilters = async () => {
    const { data } = await $api(`/api/search/filters`);

    const defaultFilters = createDefaultFilters();
    const useFilters = Object.keys(defaultFilters);

    useFilters.forEach((key: string) => {
      (filters.value as Filters)[key].data = data[key];
    });
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

  const getQueryFilter = (): QueryFilter => {
    const queryFilter: QueryFilter = {
      query: { bool: { must: [] } },
    };

    for (const key in selectedFilters.value) {
      const value = selectedFilters.value[key];
      const keyValue = key;

      queryFilter.query.bool.must.push(setQueryFilterByDepth(keyValue, value));
    }

    return queryFilter;
  };

  /**
   * 목록 reset
   * 목록을 '갱신'하는 경우, from 값을 항상 0으로 주어야 하기 때문에 fn 하나로 묶어서 처리.
   */
  const resetReloadList = async () => {
    setFrom(0);
    await getSearchList();
    updateIntersectionHandler(0);
  };

  const setSearchKeyword = (keyword: string) => {
    searchKeyword = keyword;
  };

  const changeTab = async (item: string) => {
    currentTab.value = item;

    await resetReloadList();
  };

  const setDataModelIdList = () => {
    selectedDataModelList.value = [];
    dataModelIdList.value = [];

    for (const element of searchResult.value) {
      dataModelIdList.value.push(element.id);
    }
  };
  return {
    categories,
    categoriesParentId,
    modelList,
    isCategoriesNoData,
    previewData,
    isBoxSelectedStyle,
    categoryAddName,
    categoryAddDesc,
    showAddNameNoti,
    showAddDescNoti,
    selectedModelList,
    currentTab,
    filters,
    searchResult,
    selectedFilters,
    searchResultLength,
    isSearchResultNoData,
    dataModelIdList,
    selectedDataModelList,
    resetAddModalStatus,
    getCategories,
    addModelList,
    getModelList,
    setSelectedNode,
    addCategory,
    editCategory,
    moveCategory,
    deleteCategory,
    getPreviewData,
    addNewCategory,
    addSearchList,
    getSearchList,
    getFilters,
    setSearchKeyword,
    resetReloadList,
    changeTab,
    updateIntersectionHandler,
  };
});
