import type { TreeViewItem } from "@extends/tree/TreeProps";
import { usePagingStore } from "~/store/common/paging";
import _ from "lodash";

export const useGovernCategoryStore = defineStore("GovernCategory", () => {
  const { $api } = useNuxtApp();
  const pagingStore = usePagingStore();
  const { from, size } = storeToRefs(pagingStore);

  const categories = ref<TreeViewItem>();
  const modelList: Ref<any[]> = ref([]);
  let selectedNode: any = null;

  // METHODS
  const getCategories = async () => {
    const { data } = await $api(`/api/category/list`);

    /**
     * data 는 root node 기준 으로 조회 됨.
     * root node 는 화면에 표시하지 않기 때문에 rootNode.children 만 categories 에 저장.
     */
    categories.value = data.children;
  };
  const getModelListQuery = (id: string) => {
    const params: any = {
      categoryId: id,
      from: from.value,
      size: size.value,
    };
    return new URLSearchParams(params);
  };

  const getModelByCategoryIdAPI = async (node: TreeViewItem) => {
    if (_.isNull(node) || _.isEmpty(node.id)) {
      return;
    }
    const { data } = await $api(
      `/api/category/models?${getModelListQuery(node.id)}`,
    );
    return data;
  };
  const addModelList = async () => {
    const data = await getModelByCategoryIdAPI(selectedNode);
    modelList.value = modelList.value.concat(data);
  };
  const getModelList = async () => {
    if (_.isNull(selectedNode)) {
      return;
    }
    const data = await getModelByCategoryIdAPI(selectedNode);
    modelList.value = data;
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
    // TODO : [개발] edit의 경우에도 id, parentId, name, desc, order 값을 모두 backend 로 보내야 함. 없는 항목을 'null'로 업데이트 됨.
    $api("/api/category", {
      method: method,
      body: node,
    }).then(() => {
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
  const deleteCategory = async (nodeId: string) => {
    const { data } = await $api(`/api/category`, {
      method: "delete",
      body: {
        id: nodeId,
      },
    });
    if (data === "HAS_MODEL_LIST") {
      alert("하위 노드에 매칭되어 있는 데이터 모델이 있습니다.");
    } else {
      alert("삭제 되었습니다.");
      getCategories();
    }
  };

  return {
    categories,
    modelList,
    getCategories,
    addModelList,
    getModelList,
    setSelectedNode,
    addCategory,
    editCategory,
    moveCategory,
    deleteCategory,
  };
});
