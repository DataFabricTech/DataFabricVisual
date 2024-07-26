import type { TreeViewItem } from "@extends/tree/TreeProps";

export const useGovernCategoryStore = defineStore("GovernCategory", () => {
  const { $api } = useNuxtApp();

  const categories = ref<TreeViewItem>();
  const modelList: Ref<any[]> = ref([]);

  // METHODS
  const getCategories = async () => {
    const { data } = await $api(`/api/category/list`);

    /**
     * data 는 root node 기준 으로 조회 됨.
     * root node 는 화면에 표시하지 않기 때문에 rootNode.children 만 categories 에 저장.
     */
    categories.value = data.children;
  };
  const getModelByCategoryId = async (categoryId: string) => {
    const { data } = await $api(`/api/category/models/${categoryId}`);
    modelList.value = data;
  };

  return {
    categories,
    modelList,
    getCategories,
    getModelByCategoryId,
  };
});
