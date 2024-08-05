import { defineStore } from "pinia";
import DataModelSample from "~/components/datamodel-creation/datamodel-sample.json";
import { useSearchCommonStore } from "~/store/search/common";
import executeResultJson from "~/store/datamodel-creation/samples/executeResult.json";

export const useCreationStore = defineStore("creation", () => {
  const { $api } = useNuxtApp();

  // NOTE: 탐색 페이지에서 사용되는 API 활용
  const searchCommonStore = useSearchCommonStore();
  const { filters } = storeToRefs(searchCommonStore);
  const { getFilters } = searchCommonStore;

  const query = ref("");

  const querySuccess = ref(false);
  const executeResult = ref([]);

  const isFirstExecute = ref(false);
  const isExecuteQuery = ref(false);

  const executeResultErrMsg = ref("");

  const modelList = ref([]);
  const modelListCnt = ref(0);
  const selectedModelList = ref([]);
  const dataModelFilter = ref({});

  const isItemClicked = ref(false);
  const isColumnSelected = ref(false);

  const dataModelName = ref("");
  const dataModelOwner = ref("");

  const sampleDataList = ref([]);
  const columnOptions = ref([]);
  const dataProfileList = ref([]);

  /**
   * 데이터 모델 생성 > 데이터 모델 리스트
   *
   * TODO: API 연동
   */
  const setDataModelList = async () => {
    modelList.value = DataModelSample.sampleList;
    modelListCnt.value = modelList.value.length;
  };

  /**
   * 데이터 모델 생성 > 목록 필터 리스트 조회
   */
  const setDataModelFilter = async () => {
    await getFilters();
    const result = {} as { [key: string]: any };
    for (const key in filters.value) {
      if (
        filters.value[key].text === "카테고리" ||
        filters.value[key].text === "태그" ||
        filters.value[key].text === "소유자"
      ) {
        // TODO: (확인 필요) item.category === filter.domains임.
        // 검색 처리를 위해 filter의 Key값을 category로 변경
        const filterKey =
          filters.value[key].text === "카테고리" ? "category" : key;
        result[filterKey] = filters.value[key];
        result[filterKey].data =
          result[filterKey].data !== null &&
          result[filterKey].data !== undefined
            ? result[filterKey].data
            : [];
      }
    }
    dataModelFilter.value = result;
  };

  /**
   * 데이터 모델 생성 > 북마크 변경
   * */
  const changeBookmark = (value: string) => {
    //TODO: 북마크 변경
  };

  /**
   * 데이터 모델 생성 > 목록 리스트의 항목 삭제
   */
  const deleteDataModel = (value: string) => {
    modelList.value = modelList.value.filter((item) => item.id !== value);
    modelListCnt.value = modelList.value.length;
    isItemClicked.value = false;
  };

  /**
   * 데이터 모델 생성 > 샘플데이터 조회
   * */
  const onClickDataModelItem = async (value: string) => {
    isColumnSelected.value = false;
    let selectedModel = await modelList.value.filter(
      (item) => item.id === value,
    );
    let fqn = selectedModel[0].fqn;

    dataModelName.value = selectedModel[0].modelNm;
    dataModelOwner.value = selectedModel[0].owner;

    await $api(`/api/search/detail/sample-data/${value}`)
      .then((res: any) => {
        if (res.result === 1) {
          const columnDefs = res.data.columns.map((column) => ({
            field: column.name,
          }));

          sampleDataList.value = {
            columnDefs: columnDefs,
            rowData: res.data.sampleList,
          };
          isItemClicked.value = true;
        } else {
          isItemClicked.value = false;
        }
      })
      .catch((err: any) => {
        console.log("err: ", err);
      });

    await $api(`/api/search/detail/profile/${fqn}`)
      .then((res: any) => {
        if (res.result === 1) {
          columnOptions.value = res.data
            .filter((item) => item.name)
            .map((item) => ({ id: item.name, name: item.name }));
          dataProfileList.value = res.data;
        } else {
          isItemClicked.value = false;
        }
      })
      .catch((err: any) => {
        console.log("err: ", err);
      });
  };

  // TODO: 서버 연동 처리 필요
  function runQuery(value: any) {
    query.value = value;
    // quary.value 를 파람으로 axios 요청
    isFirstExecute.value = true;
    isExecuteQuery.value = true;

    // TODO: 임시 데이터 적용 -> 서버 연동 후 제거 예정
    querySuccess.value = true;

    // TODO: 임시 데이터 적용 -> 서버 연동 후 제거 예정
    executeResult.value = executeResultJson.data;

    // TODO: 임시 데이터 적용 -> 서버 연동 후 제거 예정
    executeResultErrMsg.value =
      "Line 1 ~ 6 : Unknown error. ( TableNotExistsError() [/*+ LOCATION (\n" +
      "        PARTITION >= '20240605131200' AND PARTITION <= '20240605131500' ) */\n" +
      "        SELECT CATEGORY, BOUNDARY,SIDO_ENG, SIDO_KOR G_CO FROM ROOT.DTST limit\n" +
      "        5000;] )";
  }

  const resetQuery = () => {
    query.value = "";
    isFirstExecute.value = false;
    querySuccess.value = false;
    isExecuteQuery.value = false;
  };

  const editQueryText = (value: string) => {
    query.value = value;
    isExecuteQuery.value = false;
  };

  return {
    modelList,
    modelListCnt,
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
    dataModelFilter,
    setDataModelFilter,
    setDataModelList,
    changeBookmark,
    deleteDataModel,
    onClickDataModelItem,
  };
});
