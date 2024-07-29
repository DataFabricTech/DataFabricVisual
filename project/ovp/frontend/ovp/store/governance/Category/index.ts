import type { TreeViewItem } from "@extends/tree/TreeProps";
import { usePagingStore } from "~/store/common/paging";
import _ from "lodash";

export const useGovernCategoryStore = defineStore("GovernCategory", () => {
  const { $api } = useNuxtApp();
  const pagingStore = usePagingStore();
  const { page, size } = storeToRefs(pagingStore);

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
      page: page.value,
      size: size.value,
    };
    return new URLSearchParams(params);
  };

  const getModelByCategoryIdAPI = async () => {
    if (_.isNull(selectedNode) || _.isEmpty(selectedNode.id)) {
      return;
    }
    const { data } = await $api(
      `/api/category/models?${getModelListQuery(selectedNode.id)}`,
    );
    return data;
  };
  const addModelList = async () => {
    const data = await getModelByCategoryIdAPI();
    modelList.value = modelList.value.concat(data);
  };
  const getModelList = async () => {
    if (_.isNull(selectedNode)) {
      return;
    }
    const data = await getModelByCategoryIdAPI();
    modelList.value = data;
  };
  const setSelectedNode = (node: any) => {
    selectedNode = node;
  };

  return {
    categories,
    modelList,
    getCategories,
    addModelList,
    getModelList,
    setSelectedNode,
  };
});
