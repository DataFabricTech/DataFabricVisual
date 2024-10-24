import { defineStore } from "pinia";
import _ from "lodash";
import { useCreationStore } from "~/store/datamodel-creation/index";
import { useUserStore } from "~/store/user/userStore";
import $constants from "@/utils/constant";

export const useDataModelSaveStore = defineStore("dataModelSave", () => {
  const { $api } = useNuxtApp();

  const creationStore = useCreationStore();
  const {
    query,
    referenceModels,
    querySuccess,
    isQueryEmpty,
    isFirstExecute,
    isExecuteQuery,
  } = storeToRefs(creationStore);

  const userStore = useUserStore();
  const { user } = storeToRefs(userStore);

  const dataModelName = ref("");
  const modelDescription = ref("");
  const isSaveButtonClicked = ref(false);
  const isNameEmpty = ref(false);
  const isDuplicate = ref(false);
  const isNameValid = ref(true);
  const isQueryExecuteValid = ref(true);
  const saveErrorMsg = ref("");

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
    tree_selectedItem.value = categoryList.value[0];
  };

  const getTagList = async () => {
    const data = await $api("/api/search/detail/tag/all");
    tagList.value = data.data;
  };

  const getCategoryInfo = async (value: any) => {
    return await $api(`/api/category/${value}`);
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
    isSaveButtonClicked.value = false;
    isNameEmpty.value = false;
    isDuplicate.value = false;
    isQueryExecuteValid.value = true;
    isNameValid.value = true;
    saveErrorMsg.value = "";
  };

  const saveValidation = async () => {
    resetValidation();

    isSaveButtonClicked.value = true;

    isNameEmpty.value = _.isEmpty(dataModelName.value);

    if (isNameEmpty.value) {
      saveErrorMsg.value = "필수값을 입력해주세요.";
      return;
    }

    isNameValid.value = $constants.DATAMODEL_CREATION.SAVE.REGEX.test(
      dataModelName.value,
    );

    if (!isNameValid.value && !isNameEmpty) {
      return;
    }

    if (isSaveButtonClicked.value && isQueryEmpty.value) {
      saveErrorMsg.value = "쿼리를 입력해주세요.";
      return;
    }

    if (!isExecuteQuery.value) {
      saveErrorMsg.value = "쿼리를 실행해주세요.";
      return;
    }

    if (!querySuccess.value) {
      saveErrorMsg.value = "잘못된 쿼리입니다.";
      return;
    }

    const param = {
      modelName: dataModelName.value,
    };

    const res = await $api(`/api/creation/validation-name`, {
      method: "POST",
      body: param,
    }).catch((err: any) => {
      console.log("err: ", err);
    });

    if (res.result === 1) {
      isDuplicate.value = res.data;
    } else {
      console.log("err: ", err);
    }
  };

  const saveModel = async () => {
    let cateInfo = {};
    const tags = [];

    if (!_.isEmpty(tree_selectedItem.value)) {
      cateInfo = await getCategoryInfo(tree_selectedItem.value.tagId);
      tags.push(cateInfo.data);
    }

    if (!_.isEmpty(tag_selectedItem.value)) {
      const tagMap = tag_selectedItem.value.map((tag) => tag.tagFQN);
      tags.push(...tagMap);
    }

    if (querySuccess.value && isFirstExecute.value && isExecuteQuery.value) {
      isQueryExecuteValid.value = true;
      return await createModel(tags);
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
      owner: user.value.name,
      description: modelDescription.value,
      tags: tags,
    };

    return await $api(`/api/creation/save`, {
      method: "POST",
      body: param,
    });
  };

  return {
    dataModelName,
    modelDescription,
    isSaveButtonClicked,
    isNameEmpty,
    isNameValid,
    isDuplicate,
    isQueryExecuteValid,
    saveErrorMsg,
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
