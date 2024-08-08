import type { TreeViewItem } from "@extends/tree/TreeProps";
import { usePagingStore } from "~/store/common/paging";
import { useIntersectionObserver } from "~/composables/intersectionObserverHelper";
import _ from "lodash";
import { ref } from "vue";

export const useGovernCategoryStore = defineStore("GovernCategory", () => {
  const { $api } = useNuxtApp();
  const pagingStore = usePagingStore();
  const { from, size } = storeToRefs(pagingStore);

  const categories: Ref<TreeViewItem[]> = ref<TreeViewItem[]>([]);
  const categoriesParentId = ref("");
  const isCategoriesNoData = ref(false);
  const modelList: Ref<any[]> = ref([]);
  const modelIdList = ref([]);
  const isBoxSelectedStyle: Ref<boolean> = ref<boolean>(false);
  let selectedNodeItem: any = null;
  const previewData: Ref<any> = ref({
    modelInfo: {
      model: {
        name: "",
      },
    },
  });

  // METHODS
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
    const data = await getModelByCategoryIdAPI(selectedNodeItem);
    modelList.value = modelList.value.concat(data);
  };

  const getModelList = async (value?: string) => {
    if (_.isNull(selectedNodeItem)) {
      return;
    }
    const data = await getModelByCategoryIdAPI(selectedNodeItem, value);
    modelList.value = data === null ? [] : data;
  };
  const setSelectedNode = (node: any) => {
    selectedNodeItem = node;
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

  const setModelIdList = () => {
    modelIdList.value = [];
    for (const element of modelList.value) {
      modelIdList.value.push(element.id);
    }
  };

  // preview
  const getPreviewData = async (fqn: string) => {
    const data: any = await $api(`/api/search/preview/${fqn}`);
    previewData.value = data.data;
  };

  // Tree
  const { setScrollOptions } = useIntersectionObserver(addModelList);
  const nodeMoved: Ref<boolean> = ref(false);
  const dropMsg: Ref<any> = ref(null);

  watch(
    () => nodeMoved.value,
    (newVal) => {
      if (newVal) {
        if (dropMsg.value !== null) {
          alert(dropMsg.value);
        }
        getCategories();
      }
      nodeMoved.value = false;
      dropMsg.value = null;
    },
  );

  const selectedNode: Ref<TreeViewItem> = ref<TreeViewItem>({
    id: "",
    name: "",
    desc: "",
    order: 0,
    parentId: "",
    expanded: false,
    selected: false,
    disabled: false,
    children: [],
  });

  const onNodeClicked = (node: TreeViewItem) => {
    selectedNode.value = node;

    // 선택한 노드정보 저장
    setSelectedNode(node);

    // 선택한 노드 기준 모델 목록을 조회한다.
    setScrollOptions(0);
    getModelList();
  };

  const dropValidator = async (
    dropNode: TreeViewItem,
    targetNode: TreeViewItem,
  ): Promise<boolean> => {
    // 조건 처리 backend 에서 진행
    const resultMsg = await moveCategory(dropNode.id, targetNode.id);

    dropMsg.value = resultMsg ? null : "이동이 불가 합니다.";
    nodeMoved.value = true;

    // tree lib가 async-await 처리를 지원하지 않기 때문에 여기서는 true 로 던지고,
    // backend 동작이 끝나면 그때 결과에 따라 watch 항목에서 alert 처리, 목록을 갱신 or 유지 한다
    return true;
  };

  const addSibling = (newNode: TreeViewItem) => {
    // 형제 노드 추가
    // TODO : modal 창 띄워서 노드 추가 API  호출 (newNode 에 id(uuid), parentId 포함되어있음)
    console.log(`형제노드 추가 ${JSON.stringify(newNode)}`);
    addNewCategory(newNode);
  };

  const addChild = (newNode: TreeViewItem) => {
    // 자식 노드 추가
    // TODO : modal 창 띄워서 노드 추가 API  호출
    console.log(`자식노드 추가 ${JSON.stringify(newNode)}`);
    addNewCategory(newNode);
  };

  // TODO : [개발] 카테고리 등록 예 (등록, 수정 같은 코드 사용합니다.)
  const addNewCategory = (newNode: TreeViewItem) => {
    addCategory(newNode);
  };

  return {
    categories,
    categoriesParentId,
    modelList,
    selectedNode,
    onNodeClicked,
    isCategoriesNoData,
    modelIdList,
    previewData,
    isBoxSelectedStyle,
    getCategories,
    addModelList,
    getModelList,
    setSelectedNode,
    addCategory,
    editCategory,
    moveCategory,
    deleteCategory,
    setModelIdList,
    getPreviewData,
    dropValidator,
    addSibling,
    addChild,
    addNewCategory
  };
});
