import { ref } from "vue";

export const useOverviewStore = defineStore("overview", () => {
  // const { $api } = useNuxtApp();

  const serviceTypeData: Ref<any[]> = ref([]);
  const serviceStatusData: Ref<any[]> = ref([]);
  const serviceResponseData: Ref<any[]> = ref([]);
  const currentSituationData: Ref<any[]> = ref([]);
  const recentServiceData: Ref<any[]> = ref([]);
  const historyData: Ref<any[]> = ref([]);
  const getServiceTypeData = async () => {
    // const {data} = await $api (``);
    const tempData = [
      { value: 1048, name: "Search Engine" },
      { value: 735, name: "Direct" },
      { value: 580, name: "Email" },
      { value: 484, name: "Union Ads" },
      { value: 300, name: "Video Ads" },
    ];

    serviceTypeData.value = tempData;
  };
  const getServiceStatusData = async () => {
    // const {data} = await $api (``);
    const tempData = [
      { value: 1048, name: "Search Engine" },
      { value: 735, name: "Direct" },
      { value: 580, name: "Email" },
    ];

    serviceStatusData.value = tempData;
  };
  const getServiceResponseData = async () => {
    // const {data} = await $api (``);
    const tempData = [
      { name: "서비스 C", value: 24.1 },
      { name: "서비스 A", value: 22.54 },
      { name: "서비스 F", value: 22.01 },
      { name: "서비스 E", value: 19.85 },
      { name: "서비스 B", value: 15.98 },
      { name: "서비스 C", value: 24.1 },
      { name: "서비스 A", value: 22.54 },
      { name: "서비스 F", value: 22.01 },
      { name: "서비스 E", value: 19.85 },
      { name: "서비스 B", value: 15.98 },
      { name: "서비스 C", value: 24.1 },
      { name: "서비스 A", value: 22.54 },
      { name: "서비스 F", value: 22.01 },
      { name: "서비스 E", value: 19.85 },
      { name: "서비스 B", value: 15.98 },
      { name: "서비스 C", value: 24.1 },
      { name: "서비스 A", value: 22.54 },
      { name: "서비스 F", value: 22.01 },
      { name: "서비스 E", value: 19.85 },
      { name: "서비스 B", value: 15.98 },
      { name: "서비스 C", value: 24.1 },
      { name: "서비스 A", value: 22.54 },
      { name: "서비스 F", value: 22.01 },
      { name: "서비스 E", value: 19.85 },
      { name: "서비스 B", value: 15.98 },
    ];

    serviceResponseData.value = tempData;
  };
  const addServiceResponseData = async () => {
    console.log("응답시간 누적");
    // const { data, totalCount } = await getSearchListAPI();
    // searchResult.value = searchResult.value.concat(data[currentTab.value]);
    // searchResultLength.value = totalCount;
  };
  const getDataCurrentSituationData = async () => {
    // const {data} = await $api (``);
    const tempData = [
      { value: 1048, name: "Search Engine" },
      { value: 735, name: "Direct" },
      { value: 580, name: "Email" },
      { value: 484, name: "Union Ads" },
      { value: 300, name: "Video Ads" },
    ];

    currentSituationData.value = tempData;
  };
  const getRecentServiceData = async () => {
    // const {data} = await $api (``);
    const tempData = [
      {
        name: "서비스 B",
        type: "MySQL",
        status: "Disconnected",
        register: "2023-01-02 12:13:14",
        modification: "2023-01-05 12:12:55",
        detail: "수정",
      },
      {
        name: "서비스 G",
        type: "MariaDB",
        status: "Connected",
        register: "2023-01-02 12:13:14",
        modification: "2023-01-05 12:12:55",
        detail: "등록",
      },
      {
        name: "서비스 F",
        type: "MinIO",
        status: "Error",
        register: "2023-01-02 12:13:14",
        modification: "2023-01-05 12:12:55",
        detail: "등록",
      },
      {
        name: "서비스 D",
        type: "MariaDB",
        status: "Connected",
        register: "2023-01-02 12:13:14",
        modification: "2023-01-05 12:12:55",
        detail: "등록",
      },
      {
        name: "서비스 G",
        type: "MySQL",
        status: "Disconnected",
        register: "2023-01-02 12:13:14",
        modification: "2023-01-05 12:12:55",
        detail: "수정",
      },
      {
        name: "서비스 B",
        type: "MySQL",
        status: "Disconnected",
        register: "2023-01-02 12:13:14",
        modification: "2023-01-05 12:12:55",
        detail: "수정",
      },
      {
        name: "서비스 G",
        type: "MariaDB",
        status: "Connected",
        register: "2023-01-02 12:13:14",
        modification: "2023-01-05 12:12:55",
        detail: "등록",
      },
      {
        name: "서비스 F",
        type: "MinIO",
        status: "Error",
        register: "2023-01-02 12:13:14",
        modification: "2023-01-05 12:12:55",
        detail: "등록",
      },
      {
        name: "서비스 D",
        type: "MariaDB",
        status: "Connected",
        register: "2023-01-02 12:13:14",
        modification: "2023-01-05 12:12:55",
        detail: "등록",
      },
      {
        name: "서비스 G",
        type: "MySQL",
        status: "Disconnected",
        register: "2023-01-02 12:13:14",
        modification: "2023-01-05 12:12:55",
        detail: "수정",
      },
      {
        name: "서비스 B",
        type: "MySQL",
        status: "Disconnected",
        register: "2023-01-02 12:13:14",
        modification: "2023-01-05 12:12:55",
        detail: "수정",
      },
      {
        name: "서비스 G",
        type: "MariaDB",
        status: "Connected",
        register: "2023-01-02 12:13:14",
        modification: "2023-01-05 12:12:55",
        detail: "등록",
      },
      {
        name: "서비스 F",
        type: "MinIO",
        status: "Error",
        register: "2023-01-02 12:13:14",
        modification: "2023-01-05 12:12:55",
        detail: "등록",
      },
      {
        name: "서비스 D",
        type: "MariaDB",
        status: "Connected",
        register: "2023-01-02 12:13:14",
        modification: "2023-01-05 12:12:55",
        detail: "등록",
      },
      {
        name: "서비스 G",
        type: "MySQL",
        status: "Disconnected",
        register: "2023-01-02 12:13:14",
        modification: "2023-01-05 12:12:55",
        detail: "수정",
      },
    ];

    recentServiceData.value = tempData;
  };
  const getHistoryData = async () => {
    // const {data} = await $api (``);
    const tempData = [
      {
        date: "2023-01-05 12:16:46",
        event: "동기화 완료",
        name: "서비스 B",
        type: "MySQL",
        detail: "데이터 모델 신규 등록",
      },
      {
        date: "2023-01-05 12:16:46",
        event: "연결",
        name: "서비스 B",
        type: "MySQL",
        detail: "데이터 모델 신규 등록",
      },
      {
        date: "2023-01-05 12:16:46",
        event: "연결",
        name: "서비스 B",
        type: "MySQL",
        detail: "데이터 모델 신규 등록",
      },
      {
        date: "2023-01-05 12:16:46",
        event: "동기화 완료",
        name: "서비스 B",
        type: "MySQL",
        detail: "데이터 모델 신규 등록",
      },
      {
        date: "2023-01-05 12:16:46",
        event: "동기화 완료",
        name: "서비스 B",
        type: "MySQL",
        detail: "데이터 모델 신규 등록",
      },
      {
        date: "2023-01-05 12:16:46",
        event: "연",
        name: "서비스 B",
        type: "MySQL",
        detail: "데이터 모델 신규 등록",
      },
      {
        date: "2023-01-05 12:16:46",
        event: "연",
        name: "서비스 B",
        type: "MySQL",
        detail: "데이터 모델 신규 등록",
      },
      {
        date: "2023-01-05 12:16:46",
        event: "연",
        name: "서비스 B",
        type: "MySQL",
        detail: "데이터 모델 신규 등록",
      },
      {
        date: "2023-01-05 12:16:46",
        event: "연결",
        name: "서비스 B",
        type: "MySQL",
        detail: "데이터 모델 신규 등록",
      },
      {
        date: "2023-01-05 12:16:46",
        event: "동기화 완료",
        name: "서비스 B",
        type: "MySQL",
        detail: "데이터 모델 신규 등록",
      },
      {
        date: "2023-01-05 12:16:46",
        event: "동기화 완료",
        name: "서비스 B",
        type: "MySQL",
        detail: "데이터 모델 신규 등록",
      },
      {
        date: "2023-01-05 12:16:46",
        event: "연결",
        name: "서비스 B",
        type: "MySQL",
        detail: "데이터 모델 신규 등록",
      },
      {
        date: "2023-01-05 12:16:46",
        event: "동기화 완료",
        name: "서비스 B",
        type: "MySQL",
        detail: "데이터 모델 신규 등록",
      },
      {
        date: "2023-01-05 12:16:46",
        event: "동기화 완료",
        name: "서비스 B",
        type: "MySQL",
        detail: "데이터 모델 신규 등록",
      },
      {
        date: "2023-01-05 12:16:46",
        event: "동기화 완료",
        name: "서비스 B",
        type: "MySQL",
        detail: "데이터 모델 신규 등록",
      },
    ];

    historyData.value = tempData;
  };

  return {
    serviceTypeData,
    serviceStatusData,
    serviceResponseData,
    currentSituationData,
    recentServiceData,
    historyData,
    getServiceTypeData,
    getServiceStatusData,
    getServiceResponseData,
    getDataCurrentSituationData,
    getRecentServiceData,
    getHistoryData,
    addServiceResponseData,
  };
});
