import { defineStore } from "pinia";
import excuteResultJson from "./samples/excuteResult.json";

export const creationStore = defineStore("creation", () => {
  const { $api } = useNuxtApp();

  const modelList = ref([]);

  const query = ref("");

  const querySuccess = ref(false);
  const excuteResult = ref([]);

  const isFirstExcute = ref(false);
  const isExcuteQuery = ref(false);

  const excuteResultErrMsg = ref("");

  // TODO: 추가 모달 창에서 modelList에 값을 넣어줘야함
  function getModelList() {}

  // TODO: 임시 데이터 적용 -> 삭제 예정
  modelList.value = [
    {
      id: "1",
      label: "데이터 모델1",
      owner: "admin",
      bookmarked: true,
      category: "test",
      serviceType: "",
      tag: "1",
      fqn: "df2.test_db.test_db.Employee",
      type: "table",
    },
    {
      id: "2",
      label: "데이터 모델",
      owner: "b",
      bookmarked: false,
      category: "tel",
      serviceType: "",
      tag: "2",
      fqn: "df2.test_db.Employee2",
      type: "table",
    },
    {
      id: "3",
      label: "데이터 모델",
      owner: "b",
      bookmarked: false,
      category: "tel",
      serviceType: "",
      tag: "2",
      fqn: "df2.test_db.Employee3",
      type: "table",
    },
    {
      id: "4",
      label: "데이터 모델",
      owner: "admin",
      bookmarked: false,
      category: "tel",
      serviceType: "",
      tag: "3",
      fqn: "df2.test_db.Employee4",
      type: "container",
    },
    {
      id: "5",
      label: "데이터 모델",
      owner: "c",
      bookmarked: false,
      category: "test",
      serviceType: "",
      tag: "5",
      fqn: "df2.test_db.Employee5",
      type: "table",
    },
    {
      id: "6",
      label: "데이터 모델",
      owner: "jack",
      bookmarked: false,
      category: "test",
      serviceType: "",
      tag: "6",
      fqn: "df2.test_db.Employee6",
      type: "table",
    },
    {
      id: "7",
      label: "테스트",
      owner: "b",
      bookmarked: false,
      category: "test",
      serviceType: "",
      tag: "6",
      fqn: "df5.test_db.Employee7",
      type: "table",
    },
  ];

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
    query,
    querySuccess,
    isExcuteQuery,
    isFirstExcute,
    excuteResult,
    excuteResultErrMsg,
    getModelList,
    getExcuteResult,
  };
});
