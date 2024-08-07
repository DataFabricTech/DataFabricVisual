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
  const myModelList = ref({});
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
    console.log("modelListCnt: ", modelListCnt.value);
  };

  const setMyModelList = async () => {
    myModelList.value = DataModelSample.my_sampleList;
  };
  const setSelectedModelList = async () => {
    selectedModelList.value = [
      {
        owner: "",
        fqn: "vdap2.sample.sample.sampledata_chart_02",
        depth: ["vdap2", "sample", "sample", "sampledata_chart_02"],
        serviceIcon: "",
        modelNm: "sampledata_chart_02",
        modelDesc: null,
        id: "02ab816f-88e1-47df-bb87-926dcbcc20f0",
        type: "table",
        firModelNm: "sampledata_chart_02",
        category: "",
        bookmarked: true,
        tags: [],
      },
      {
        owner: "",
        fqn: "vdap.was_v2.was_v2.was_login_lock",
        depth: ["vdap", "was_v2", "was_v2", "was_login_lock"],
        serviceIcon: "",
        modelNm: "was_login_lock",
        modelDesc: null,
        id: "40be78e5-fdc4-43cf-9d43-14b7196ec4eb",
        type: "table",
        firModelNm: "was_login_lock",
        category: "",
        bookmarked: false,
        tags: [],
      },
      {
        owner: "",
        fqn: "vdap.was_v2.was_v2.was_access_rights",
        depth: ["vdap", "was_v2", "was_v2", "was_access_rights"],
        serviceIcon: "",
        modelNm: "was_access_rights",
        modelDesc: null,
        id: "425a6505-df66-4abb-a6ae-ce1d45459aee",
        type: "table",
        firModelNm: "was_access_rights",
        category: "",
        bookmarked: false,
        tags: [],
      },
      {
        owner: "",
        fqn: "vdap2.sample.sample.sampledata_map_08",
        depth: ["vdap2", "sample", "sample", "sampledata_map_08"],
        serviceIcon: "",
        modelNm: "sampledata_map_08",
        modelDesc: null,
        id: "06b740b4-20e1-4f41-9912-49346db18a0d",
        type: "table",
        firModelNm: "sampledata_map_08",
        category: "",
        bookmarked: false,
        tags: ["분류01.태그01_01"],
      },
    ];
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
    modelList.value = modelList.value.filter((item) => item.id !== value);
    modelListCnt.value = modelList.value.length;
  };

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
    myModelList,
    selectedModelList,
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
    setSelectedModelList,
    setMyModelList,
    setDataModelList,
    deleteDataModel,
  };
});
