import { defineStore } from "pinia";
import _ from "lodash";
import { useCreationStore } from "~/store/datamodel-creation/index";

export const useDataModelSaveStore = defineStore("dataModelSave", () => {
  const { $api } = useNuxtApp();

  const creationStore = useCreationStore();
  const {
    query,
    referenceModels,
    querySuccess,
    isFirstExecute,
    isExecuteQuery,
  } = storeToRefs(creationStore);

  const dataModelName = ref("");
  const modelDescription = ref("");
  const isNameEmpty = ref(false);
  const isDuplicate = ref(false);
  const isQueryExecuteValid = ref(true);

  const cateTitle = ref("");
  const categoryList: Ref<any[]> = ref([]);
  const tagList: Ref<any[]> = ref([]);

  const tree_selectedItem: Ref<any[]> = ref<any[]>([]);
  const tag_selectedItem: Ref<any> = ref<any[]>([]);

  const setCateTitle = (value: any) => {
    cateTitle.value = value;
  };

  const setDataModelName = (value: any) => {
    dataModelName.value = value;
  };

  const setModelDescription = (value: any) => {
    modelDescription.value = value;
  };

  const setTreeSelectionItem = (value: any) => {
    tree_selectedItem.value = value;
  };

  const setTagSelectionItem = (value: any) => {
    tag_selectedItem.value = value;
  };

  const getCategoryList = async () => {
    const data = await $api("/api/category/list");
    categoryList.value = data.data.children;
    cateTitle.value = categoryList.value[0].name;
    tree_selectedItem.value = categoryList.value[0];
  };

  const getTagList = async () => {
    const data = await $api("/api/search/detail/tag/all");
    tagList.value = data.data;
  };

  const getCategoryInfo = async (value: any) => {
    return await $api(`api/category/${value}`);
  };

  const editDoneForCategory = (value: any) => {
    cateTitle.value = _.isEmpty(value) ? "" : value.name;
    tree_selectedItem.value = value;
  };

  const changeTags = (tags: any) => {
    tag_selectedItem.value = tagList.value.filter((item) =>
      tags.some((tag) => tag.tagFQN === item.tagFQN),
    );
  };

  const resetValidation = () => {
    isNameEmpty.value = false;
    isDuplicate.value = false;
    isQueryExecuteValid.value = true;
  };

  const saveValidation = async () => {
    resetValidation();

    isNameEmpty.value = _.isEmpty(dataModelName.value);

    if (isNameEmpty.value) {
      return;
    }

    let param = {
      modelName: dataModelName.value,
    };

    await $api(`/api/creation/validation-name`, {
      method: "POST",
      body: param,
    })
      .then((res: any) => {
        if (res.result === 1) {
          isDuplicate.value = res.data;
        } else {
          console.log("err: ", err);
        }
      })
      .catch((err: any) => {
        console.log("err: ", err);
      });
  };

  const saveModel = async () => {
    let cateInfo = {};
    let tags = [];

    if (!_.isEmpty(tree_selectedItem.value)) {
      cateInfo = await getCategoryInfo(tree_selectedItem.value.tagId);
      tags.push(cateInfo.data);
    }

    if (!_.isEmpty(tag_selectedItem.value)) {
      let tagMap = tag_selectedItem.value.map((tag) => tag.tagFQN);
      tags.push(...tagMap);
    }

    if (querySuccess.value && isFirstExecute.value && isExecuteQuery.value) {
      isQueryExecuteValid.value = true;
      await createModel(tags);
    } else {
      isQueryExecuteValid.value = false;
    }
  };

  const createModel = async (tags: any) => {
    const param = {
      modelName: dataModelName.value,
      baseModel: {
        type: "QUERY",
        query: query.value,
        referenceModels: referenceModels.value,
      },
      description: modelDescription.value,
      tags: tags,
    };

    // TODO: 서버 연동 테스트 필요
    await $api(`/api/creation/save`, {
      method: "POST",
      body: param,
    });
  };

  return {
    dataModelName,
    modelDescription,
    isNameEmpty,
    isDuplicate,
    isQueryExecuteValid,
    cateTitle,
    categoryList,
    tagList,
    tree_selectedItem,
    tag_selectedItem,
    setCateTitle,
    setDataModelName,
    setModelDescription,
    setTreeSelectionItem,
    setTagSelectionItem,
    getCategoryList,
    getTagList,
    editDoneForCategory,
    changeTags,
    resetValidation,
    saveValidation,
    saveModel,
  };
});
