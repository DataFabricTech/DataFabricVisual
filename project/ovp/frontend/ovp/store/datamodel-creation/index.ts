import { defineStore } from "pinia";
import DataModelSample from "~/components/datamodel-creation/datamodel-sample.json";
import { useSearchCommonStore } from "~/store/search/common";

export const useCreationStore = defineStore("creation", () => {
  const { $api } = useNuxtApp();

  // NOTE: 탐색 페이지에서 사용되는 API 활용
  const searchCommonStore = useSearchCommonStore();
  const { filters } = storeToRefs(searchCommonStore);
  const { getFilters } = searchCommonStore;

  const query = ref("");

  const querySuccess = ref(false);
  const excuteResult = ref([]);

  const isFirstExcute = ref(false);
  const isExcuteQuery = ref(false);

  const excuteResultErrMsg = ref("");

  const modelList = ref([]);
  const modelListCnt = ref(0);
  const selectedModelList = ref([]);
  const dataModelFilter = ref({});

  /**
   * 데이터 모델 생성 > 데이터 모델 리스트
   *
   * TODO: API 연동
   */
  const setDataModelList = async () => {
    modelList.value = DataModelSample.sampleList;
    modelListCnt.value = modelList.value.length;
    console.log('modelListCnt: ', modelListCnt.value);
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

  const deleteDataModel = (value: string) => {
    modelList.value = modelList.value.filter(item => item.id !== value);
    modelListCnt.value = modelList.value.length;
  }

  // TODO: 서버 연동 처리 필요
  async function getExcuteResult(value: string) {
    isFirstExcute.value = true;
    isExcuteQuery.value = true;

    // TODO: 임시 데이터 적용 -> 서버 연동 후 제거 예정
    querySuccess.value = true;

    // TODO: 임시 데이터 적용 -> 서버 연동 후 제거 예정
    excuteResult.value = excuteResultJson.data;

    // TODO: 임시 데이터 적용 -> 서버 연동 후 제거 예정
    excuteResultErrMsg.value =
      "Line 1 ~ 6 : Unknown error. ( TableNotExistsError() [/*+ LOCATION (\n" +
      "        PARTITION >= '20240605131200' AND PARTITION <= '20240605131500' ) */\n" +
      "        SELECT CATEGORY, BOUNDARY,SIDO_ENG, SIDO_KOR G_CO FROM ROOT.DTST limit\n" +
      "        5000;] )";
  }

  return {
    modelList,
    modelListCnt,
    query,
    querySuccess,
    isExcuteQuery,
    isFirstExcute,
    excuteResult,
    excuteResultErrMsg,
    getExcuteResult,
    dataModelFilter,
    setDataModelFilter,
    setDataModelList,
    deleteDataModel
  };
});
