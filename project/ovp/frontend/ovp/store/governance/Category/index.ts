import { ref } from "vue";
import _ from "lodash";
import type { TreeViewItem } from "@extends/tree/TreeProps";
import { usePagingStore } from "~/store/common/paging";
import { useQueryHelpers } from "~/composables/queryHelpers";
import { FILTER_KEYS } from "~/store/search/common";

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
  const { setQueryFilterByDepth, getTrinoQuery } = useQueryHelpers();

  const selectedNode = ref({});
  const categories: Ref<TreeViewItem[]> = ref<TreeViewItem[]>([]);
  const defaultCategoriesParentId = ref("");
  const categoriesParentId = ref("");
  const categoriesId = ref("");
  const isCategoriesNoData = ref(false);
  const modelList: Ref<any[]> = ref([]);
  const modelIdList = ref([]);
  const isBoxSelectedStyle: Ref<boolean> = ref<boolean>(false);
  const categoryAddName = ref<string>("");
  const categoryAddDesc = ref<string>("");
  const showAddNameNoti = ref<boolean>(false);
  const showAddDescNoti = ref<boolean>(false);
  const selectedModelList = ref([]);
  const addSearchInputValue = ref("");
  const checkReachedCount = ref<boolean>(false);
  const previewData: Ref<any> = ref({
    modelInfo: {
      model: {
        name: "",
      },
    },
  });
  const selectedCategoryId = ref("");
  const selectedCategoryTagId = ref("");
  const selectedNodeCategory: Ref<TreeViewItem> = ref<TreeViewItem>({
    id: "",
    name: "",
    desc: "",
    order: 0,
    parentId: "",
    tagId: "",
    expanded: false,
    selected: false,
    disabled: false,
    children: [],
  });
  const selectedTitleNodeValue = ref(selectedNodeCategory.value.name || "");
  const dupliSelectedTitleNodeValue = ref("");
  const lastChildIdList: Ref<string[]> = ref<string[]>([]);
  const childlessList: Ref<string[]> = ref<string[]>([]);

  // MAIN - TREE
  const getCategories = async () => {
    const { data } = await $api(`/api/category/list`);

    /**
     * data 는 root node 기준 으로 조회 됨.
     * root node 는 화면에 표시하지 않기 때문에 rootNode.children 만 categories 에 저장.
     */
    categories.value = data.children;
    isCategoriesNoData.value = categories.value.length === 0;
    defaultCategoriesParentId.value = data.parentId;
    categoriesParentId.value = data.parentId;

    for (const item1 of categories.value) {
      if (item1.children.length === 0) {
        childlessList.value.push(item1.id);
      }
      for (const item2 of item1.children) {
        if (item2.children.length === 0) {
          childlessList.value.push(item2.id);
        }
        for (const item3 of item2.children) {
          if (item3.children.length === 0) {
            childlessList.value.push(item3.id);
          }
        }
      }
    }

    lastChildIdList.value = categories.value
      .flatMap((item1) => item1.children)
      .flatMap((item2) => item2.children)
      .map((item3) => item3.id);
  };

  const addCategory = (node: TreeViewItem) => {
    insertOrEditAPI("PUT", node);
  };
  const editCategory = (node: TreeViewItem) => {
    insertOrEditAPI("PATCH", node);
  };
  const insertOrEditAPI = async (method: string, node: TreeViewItem) => {
    const res: any = await $api("/api/category", {
      method: method,
      body: node,
    });

    if (res.data === "HAS_SAME_NAME") {
      if (categoryAddName.value !== "") {
        alert(`${categoryAddName.value} 이미 존재합니다.`);
        categoryAddName.value = "";
        return;
      } else if (selectedTitleNodeValue.value !== "") {
        alert(`${selectedTitleNodeValue.value} 이미 존재합니다.`);
        selectedTitleNodeValue.value = dupliSelectedTitleNodeValue.value;
        selectedNodeCategory.value.name = dupliSelectedTitleNodeValue.value;
      }
    }
    // 모달 창이 뜨기 전에 확인을 해야돼서, 이 시점에는 확인하지 않는다. (추후 사용할 수 있어서 남김)
    // if (res.data === "OVER_DEPTH") {
    //   alert("카테고리는 최대 3depth 까지만 추가할 수 있습니다.");
    //   return;
    // }

    await getCategories();
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

  // MAIN - MODEL LIST
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
      { showLoader: false },
    );

    return data;
  };

  const setFromReset = () => {
    from.value = 0;
  };

  const addModelList = async () => {
    const data = await getModelByCategoryIdAPI(selectedNode.value);
    modelList.value = modelList.value.concat(data);
  };
  const getModelList = async (value?: string) => {
    if (_.isNull(selectedNode.value)) {
      return;
    }
    const data = await getModelByCategoryIdAPI(selectedNode.value, value);
    modelList.value = data === null ? [] : data;
  };
  const setModelIdList = () => {
    modelIdList.value = [];
    for (const element of modelList.value) {
      modelIdList.value.push(element.id);
    }
  };
  const setSelectedNode = (node: any) => {
    selectedNode.value = node;
  };

  // PREVIEW
  const getPreviewData = async (fqn: string) => {
    const data: any = await $api(`/api/search/preview/${fqn}`);
    previewData.value = data.data;
  };

  const getContainerPreviewData = async (id: string) => {
    const data: any = await $api(`/api/containers/${id}`);
    previewData.value = data.data;
  };

  // MODAL
  const resetAddModalStatus = () => {
    showAddNameNoti.value = false;
    showAddDescNoti.value = false;
    categoryAddName.value = "";
    categoryAddDesc.value = "";
  };

  // MODAL - FILTERS
  const createDefaultFilters = (): Filters => {
    return {
      [FILTER_KEYS.OWNER]: { text: "소유자", data: [] },
      [FILTER_KEYS.TAGS]: { text: "태그", data: [] },
      [FILTER_KEYS.SERVICE]: { text: "서비스", data: [] },
      [FILTER_KEYS.SERVICE_TYPE]: { text: "서비스타입", data: [] },
    };
  };

  const filters = ref<Filters>(createDefaultFilters());
  const selectedFilters: Ref<SelectedFilters> = ref({} as SelectedFilters);
  const selectedFilterItems: Ref<any> = ref([]);
  const setEmptyFilter = () => {
    selectedFilterItems.value = [];
    selectedFilters.value = {};
  };

  const getFilters = async () => {
    const { data } = await $api(`/api/search/filters`);

    const defaultFilters = createDefaultFilters();
    const useFilters = Object.keys(defaultFilters);

    useFilters.forEach((key: string) => {
      (filters.value as Filters)[key].data = data[key];
    });
  };

  // MODAL - MODEL LIST
  const searchResult: Ref<any[]> = ref([]);
  const searchResultLength: Ref<SearchResultLength> = ref<SearchResultLength>({
    model: 0,
    table: 0,
    storage: 0,
  });
  const selectedDataModelList = ref([]);
  const dataModelIdList = ref([]);
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

  const getSearchListAPI = async () => {
    const { data } = await $api(`/api/search/list?${getSearchListQuery()}`, {
      showLoader: false,
    });
    return data === null ? [] : data;
  };

  // 데이터 모델 데이터 누적
  const addSearchList = async () => {
    const { data } = await getSearchListAPI();
    searchResult.value = searchResult.value.concat(data[currentTab.value]);

    const currentTabCumulativeCount =
      searchResultLength.value[currentTab.value];

    if (
      searchResult.value.length < currentTabCumulativeCount ||
      (searchResult.value.length === currentTabCumulativeCount &&
        !checkReachedCount.value)
    ) {
      setDataModelIdList();
    }

    if (searchResult.value.length === currentTabCumulativeCount) {
      checkReachedCount.value = true;
    }
  };

  // 데이터 모델 목록 최초 갱신
  const getSearchList = async () => {
    const { data, totalCount } = await getSearchListAPI();
    searchResult.value = data[currentTab.value];
    searchResultLength.value = totalCount;
    isSearchResultNoData.value = searchResult.value.length === 0;
    setDataModelIdList();
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

  // 데이터 모델 목록 Reset
  const resetReloadList = async () => {
    setFrom(0);
    await getSearchList();
    updateIntersectionHandler(0);
  };

  const setSearchKeyword = (keyword: string) => {
    searchKeyword = keyword;
  };

  const setDataModelIdList = () => {
    selectedDataModelList.value = [];
    dataModelIdList.value = [];

    for (const element of searchResult.value) {
      dataModelIdList.value.push(element.id);
    }
  };

  // MODAL - TAB
  const initTab: Ref<string> = ref("table");
  const currentTab: Ref<string> = ref("table");
  const changeTab = async (item: string) => {
    checkReachedCount.value = false;
    initTab.value = item;
    currentTab.value = item;
    await resetReloadList();
  };

  const patchCategoryTagAPI = async (
    tagId: string,
    type: string,
    list: object[],
  ) => {
    const body: any = list;

    return $api(`/api/category/${tagId}/tag?type=${type}`, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json-patch+json",
      },
      body: body,
    });
  };
  const patchModelAddItemAPI = async () => {
    return patchCategoryTagAPI(
      selectedCategoryTagId.value,
      currentTab.value,
      selectedDataModelList.value,
    );
  };

  return {
    selectedNode,
    categories,
    defaultCategoriesParentId,
    categoriesParentId,
    categoriesId,
    modelList,
    modelIdList,
    isCategoriesNoData,
    previewData,
    isBoxSelectedStyle,
    categoryAddName,
    categoryAddDesc,
    showAddNameNoti,
    showAddDescNoti,
    selectedModelList,
    currentTab,
    initTab,
    filters,
    searchResult,
    selectedFilters,
    selectedFilterItems,
    searchResultLength,
    isSearchResultNoData,
    dataModelIdList,
    selectedDataModelList,
    addSearchInputValue,
    checkReachedCount,
    selectedCategoryId,
    selectedCategoryTagId,
    selectedNodeCategory,
    selectedTitleNodeValue,
    dupliSelectedTitleNodeValue,
    lastChildIdList,
    childlessList,
    resetAddModalStatus,
    patchModelAddItemAPI,
    patchCategoryTagAPI,
    getCategories,
    addModelList,
    getModelList,
    setSelectedNode,
    addCategory,
    editCategory,
    moveCategory,
    deleteCategory,
    getPreviewData,
    getContainerPreviewData,
    addNewCategory,
    addSearchList,
    getSearchList,
    getFilters,
    setSearchKeyword,
    resetReloadList,
    changeTab,
    updateIntersectionHandler,
    setEmptyFilter,
    setModelIdList,
    setFromReset,
  };
});
