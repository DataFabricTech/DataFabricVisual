import { defineStore } from "pinia";
import { storeToRefs } from "../../.nuxt/imports";
import _ from "lodash";
import { useDataModelSearchStore } from "~/store/datamodel-creation/search";

export const useCreationStore = defineStore("creation", () => {
  const { $api, $alert } = useNuxtApp();

  // NOTE: 탐색 페이지에서 사용되는 API 활용
  const dataModelSearchStore = useDataModelSearchStore();
  const { selectedModelList } = storeToRefs(dataModelSearchStore);
  const { getSampleData, getProfileData } = dataModelSearchStore;

  const query = ref("");
  const referenceModels = ref([]);

  // NOTE: 쿼리 성공여부
  const querySuccess = ref(false);
  // NOTE: 쿼리 최초실행여부
  const isFirstExecute = ref(false);
  // NOTE: 쿼리 실행여부
  const isExecuteQuery = ref(false);
  // NOTE: 쿼리 비어있음여부
  const isQueryEmpty = ref(true);

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
      selectedModel.type,
    );
    if (sampleDataList.value) {
      sampleDataList.value.columnDefs = sampleDataList.value.columnDefs.map(
        (column) => {
          return {
            ...column, // 기존 속성들 복사
            minWidth: 150, // minWidth 추가
          };
        },
      );
      isItemClicked.value = true;
    }

    const result = await getProfileData(selectedModel.fqn, selectedModel.type);
    dataProfileList.value = result.rowData;
    columnOptions.value = result.rowData
      .filter((item: any) => item.name)
      .map((item: any) => ({ id: item.name, name: item.name }));

    columnOptions.value.unshift({ id: "choose", name: "선택하세요" });
  };

  const showProfile = () => {
    isColumnSelected.value = true;
  };

  /**
   * 데이터 모델 생성 > 쿼리 실행
   * */
  async function runQuery() {

    if (_.isEmpty(query.value)) {
      querySuccess.value = false;
      isFirstExecute.value = false;
      isExecuteQuery.value = false;
      $alert("쿼리를 입력해주세요", "info");
      return;
    }

    referenceModels.value = selectedModelList.value.map((item) => ({
      id: item.id,
      name: item.modelNm,
      fullyQualifiedName: item.fqn,
    }));

    const param = {
      query: query.value,
      referenceModels: referenceModels.value,
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
    referenceModels.value = [];
    isFirstExecute.value = false;
    querySuccess.value = false;
    isExecuteQuery.value = false;
    executeResultErrMsg.value = "";
  };

  return {
    selectedModelList,
    query,
    referenceModels,
    querySuccess,
    isQueryEmpty,
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
    deleteDataModel,
    onClickDataModelItem,
    showProfile,
  };
});
