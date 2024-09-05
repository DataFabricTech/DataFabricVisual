import { defineStore } from "pinia";
import { storeToRefs } from "../../.nuxt/imports";
import _ from "lodash";
import { useDataModelSearchStore } from "~/store/datamodel-creation/search";

export const useCreationStore = defineStore("creation", () => {
  const { $api } = useNuxtApp();

  // NOTE: 탐색 페이지에서 사용되는 API 활용
  const dataModelSearchStore = useDataModelSearchStore();
  const { selectedModelList } = storeToRefs(dataModelSearchStore);
  const { getSampleData, getProfileData } = dataModelSearchStore;

  const query = ref("");

  // NOTE: 쿼리 성공여부
  const querySuccess = ref(false);
  // NOTE: 쿼리 최초실행여부
  const isFirstExecute = ref(false);
  // NOTE: 쿼리 실행여부
  const isExecuteQuery = ref(false);

  const executeResult = ref([]);
  const executeResultErrMsg = ref("");

  const isItemClicked = ref(false);
  const isColumnSelected = ref(false);

  const dataModelName = ref("");
  const dataModelOwner = ref("");

  const sampleDataList = ref([]);
  const columnOptions = ref([]);
  const dataProfileList = ref([]);

  /**
   * 데이터 모델 생성 > 목록 리스트의 항목 삭제
   */
  const deleteDataModel = (value: string) => {
    selectedModelList.value = value;
    isItemClicked.value = false;
  };

  /**
   * 데이터 모델 생성 > 샘플데이터 조회, 데이터 프로파일링 조회
   * */
  const onClickDataModelItem = async (value: string) => {
    isColumnSelected.value = false;

    const selectedModel = _.find(selectedModelList.value, ["id", value]);
    dataModelName.value = selectedModel.modelNm;
    dataModelOwner.value = selectedModel.ownerDisplayName;

    sampleDataList.value = await getSampleData(
      selectedModel.id,
      selectedModel.fqn,
    );
    if (sampleDataList.value) {
      isItemClicked.value = true;
    }

    const result = await getProfileData(selectedModel.fqn);
    dataProfileList.value = result.rowData;
    columnOptions.value = result.rowData
      .filter((item: any) => item.name)
      .map((item: any) => ({ id: item.name, name: item.name }));
  };

  const showProfile = () => {
    isColumnSelected.value = true;
  };

  /**
   * 데이터 모델 생성 > 쿼리 실행
   * */
  async function runQuery(value: any) {
    query.value = value;
    const referenceModels = selectedModelList.value.map((item) => ({
      id: item.id,
      name: item.modelNm,
      fullyQualifiedName: item.fqn,
    }));

    const param = {
      query: query.value,
      referenceModels: referenceModels,
      limit: 100,
      page: 0,
    };
    insertDataModel(param);
  }
  const insertDataModel = (param: any) => {
    return $api(`/api/creation/query/execute`, {
      method: "POST",
      body: param,
    }).then((res: any) => {
      if (res.result === 1) {
        executeResult.value = res.data;
        isFirstExecute.value = true;
        isExecuteQuery.value = true;
        querySuccess.value = true;
      } else {
        isFirstExecute.value = true;
        isExecuteQuery.value = true;
        querySuccess.value = false;
        executeResultErrMsg.value = res.errorMessage;
      }
    });
  };

  /**
   * 데이터 모델 생성 > 쿼리 실행
   * */
  const resetQuery = () => {
    query.value = "";
    isFirstExecute.value = false;
    querySuccess.value = false;
    isExecuteQuery.value = false;
  };

  /**
   * 데이터 모델 생성 > 쿼리 편집
   * */
  const editQueryText = (value: string) => {
    query.value = value;
    isExecuteQuery.value = false;
  };

  return {
    selectedModelList,
    query,
    querySuccess,
    isExecuteQuery,
    isFirstExecute,
    isItemClicked,
    isColumnSelected,
    dataModelName,
    dataModelOwner,
    sampleDataList,
    columnOptions,
    dataProfileList,
    executeResult,
    executeResultErrMsg,
    runQuery,
    resetQuery,
    editQueryText,
    deleteDataModel,
    onClickDataModelItem,
    showProfile,
  };
});
