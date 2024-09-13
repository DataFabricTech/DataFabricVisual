import { ref } from "vue";
import _ from "lodash";
import type { TreeViewItem } from "@extends/tree/TreeProps";
import { usePagingStore } from "~/store/common/paging";
import { useQueryHelpers } from "~/composables/queryHelpers";
import { FILTER_KEYS } from "~/store/search/common";
import $constants from "@/utils/constant";

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

// 미분류 카테고리 처리
interface UndefinedTagIdManager {
  set: (value: string | null) => void;
  get: () => string | null;
}

// TODO: 데이터모델추가 모달 공통모달로 변경되면서 사용하지 않는 코드가 있으므로 관련 코드 삭제 필요. Category 관련 이슈처리가 완료된 후 정리하는것이 좋을 것 같음.
export const useGovernCategoryStore = defineStore("GovernCategory", () => {
  const { $api } = useNuxtApp();
  const pagingStore = usePagingStore();
  const { setFrom, setDataLoadDone, updateIntersectionHandler } = pagingStore;
  const { from, size } = storeToRefs(pagingStore);
  const { setQueryFilterByDepth } = useQueryHelpers();

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
  const searchInputValue = ref("");
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
  const isShowPreview = ref<boolean>(false);

  // 미분류 set/get 분리해서 page 단에는 get() 만 해서 사용하도록 처리함.
  const createUndefinedTagId = (): UndefinedTagIdManager => {
    let UNDEFINED_TAG_ID: string | null = null; // Adjust type based on your requirements

    return {
      set: (value: string | null) => {
        if (UNDEFINED_TAG_ID === null) {
          UNDEFINED_TAG_ID = value;
        }
      },
      get: () => UNDEFINED_TAG_ID,
    };
  };
  const undefinedTagIdManager = createUndefinedTagId();

  // MAIN - TREE
  const getCategories = async () => {
    const { data } = await $api(`/api/category/list`);

    /**
     * data 는 root node 기준 으로 조회 됨.
     * root node 는 화면에 표시하지 않기 때문에 rootNode.children 만 categories 에 저장.
     */

    // 미분류항목은 categories 에 첫번째 항목으로 등록할꺼기 때문에 (자동이든, 수동이든)
    // categories 의 항목이 1개이상인 경우, 첫번째 항목이 '미분류' 항목이 됨.
    // 저장해둔다.
    if (data.children.length > 0) {
      undefinedTagIdManager.set(data.children[0].id);
    }

    categories.value = data.children;
    isCategoriesNoData.value = categories.value.length === 0;
    defaultCategoriesParentId.value = data.parentId;
    categoriesParentId.value = data.parentId;

    lastChildIdList.value = categories.value
      .flatMap((item1) => item1.children)
      .flatMap((item2) => item2.children)
      .map((item3) => item3.id);
  };

  // 노드명 유효한지 체크.
  const isValidNodeName = (name: string) => {
    // node name 약어 처리 ('미분류' 항목은 사용할 수 없음)
    if (
      !_.isNull(undefinedTagIdManager.get()) &&
      name.trim() === $constants.SERVICE.CATEGORY_UNDEFINED_NAME
    ) {
      // TODO : [개발] '미분류' 카테고리 명으로 입력 못하게 Notification 표시 추가.
      alert(`${name} 은 예약어로 등록되어 사용 불가능합니다.`);
      return false;
    }
    return true;
  };

  const addCategory = (node: TreeViewItem) => {
    if (!isValidNodeName(node.name)) {
      return;
    }
    insertOrEditAPI("PUT", node);
  };
  const editCategory = (node: TreeViewItem) => {
    if (!isValidNodeName(node.name)) {
      return;
    }
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
    if (nodeId === undefinedTagIdManager.get()) {
      alert(
        `${$constants.SERVICE.CATEGORY_UNDEFINED_NAME} 카테고리는 삭제가 불가능합니다.`,
      );
      return;
    }
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
  const getModelListQuery = (tagId: string) => {
    // TODO : [개발] 검색어 조건 여기 추가.
    const params: any = {
      // eslint-disable-next-line id-length
      q: searchInputValue.value || "",
      from: from.value,
      size: size.value,
      tagId: tagId,
    };
    return new URLSearchParams(params);
  };
  const getModelByCategoryIdAPI = async (node: TreeViewItem) => {
    if (_.isNull(node) || _.isEmpty(node.tagId)) {
      return;
    }
    const { data } = await $api(
      `/api/category/models?${getModelListQuery(node.tagId)}`,
      { showLoader: false },
    );

    return data;
  };

  const addModelList = async () => {
    const data = await getModelByCategoryIdAPI(selectedNode.value);
    modelList.value = modelList.value.concat(data);
  };
  const getModelList = async () => {
    if (_.isNull(selectedNode.value)) {
      return;
    }
    const data = await getModelByCategoryIdAPI(selectedNode.value);
    modelList.value = data === null ? [] : data;

    // [데이터 갱신] 이 완료되면 호출한다. infiniteScroll 처리하기 위해 필요한 함수.
    setDataLoadDone();
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
    selectedDataModelList.value = [];
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

    try {
      const res = await $api(`/api/category/${tagId}/tag?type=${type}`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json-patch+json",
        },
        body: body,
      });

      return await res;
    } catch (error) {
      alert(`업데이트 실패했습니다.`);
    }
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
    isShowPreview,
    undefinedTagIdManager,
    searchInputValue,
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
  };
});
