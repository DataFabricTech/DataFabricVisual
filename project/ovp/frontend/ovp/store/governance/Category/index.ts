import type { TreeViewItem } from "@extends/tree/TreeProps";
import { usePagingStore } from "~/store/common/paging";
import _ from "lodash";
import { ref } from "vue";

export const useGovernCategoryStore = defineStore("GovernCategory", () => {
  const { $api } = useNuxtApp();
  const pagingStore = usePagingStore();
  const { from, size } = storeToRefs(pagingStore);

  const categories: Ref<TreeViewItem[]> = ref<TreeViewItem[]>([]);
  const isCategoriesNoData = ref(false);
  const modelList: Ref<any[]> = ref([]);
  const modelIdList = ref([]);
  const isBoxSelectedStyle: Ref<boolean> = ref<boolean>(false);
  let selectedNode: any = null;
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
  };
  const getModelListQuery = (tagId: string) => {
    // TODO : [개발] 검색어 조건 여기 추가.
    const params: any = {
      // eslint-disable-next-line id-length
      q: "",
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
    // data가 null 로 전달받는 경우 빈 값으로 전달 받도록 수정
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
      console.log("insertOrEditAPI의 res", res);
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
    await $api(`/api/category`, {
      method: "delete",
      body: {
        id: nodeId,
      },
    });

    alert("삭제 되었습니다.");
    getCategories();
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

  return {
    categories,
    modelList,
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
  };
});
