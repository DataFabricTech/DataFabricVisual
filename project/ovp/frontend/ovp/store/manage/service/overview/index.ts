import { ref } from "vue";

export const useOverviewStore = defineStore("overview", () => {
  // const { $api } = useNuxtApp();

  const typeData: Ref<any[]> = ref([]);
  const statusData: Ref<any[]> = ref([]);
  const recommendData: Ref<any[]> = ref([]);
  const responseData: Ref<any[]> = ref([]);
  const registerData: Ref<any[]> = ref([]);
  const serviceData: Ref<any[]> = ref([]);
  const historyData: Ref<any[]> = ref([]);
  const getTypeApi = async () => {
    // const {data} = await $api (``);
    const tempData = [
      { value: 1048, name: "Search Engine" },
      { value: 735, name: "Direct" },
      { value: 580, name: "Email" },
      { value: 484, name: "Union Ads" },
      { value: 300, name: "Video Ads" },
    ];

    typeData.value = tempData;
  };
  const getStatusApi = async () => {
    // const {data} = await $api (``);
    const tempData = [
      { value: 1048, name: "Search Engine" },
      { value: 735, name: "Direct" },
      { value: 580, name: "Email" },
    ];

    statusData.value = tempData;
  };
  const getRecommendApi = async () => {
    // const {data} = await $api (``);
    const tempData = [
      120, 200,
      // {
      //   value: 200,
      //   itemStyle: {
      //     color: "#a90000",
      //   },
      // },
      150, 80, 70, 110, 130,
    ];

    recommendData.value = tempData;
  };

  const getResponseApi = async () => {
    // const {data} = await $api (``);
    const tempData = [
      { value: 24.1, name: "서비스 C" },
      { value: 22.54, name: "서비스 A" },
      { value: 22.01, name: "서비스 F" },
      { value: 19.85, name: "서비스 E" },
      { value: 15.98, name: "서비스 B" },
    ];

    responseData.value = tempData;
  };
  const getRegisterApi = async () => {
    // const {data} = await $api (``);
    const tempData = [
      { value: 1048, name: "Search Engine" },
      { value: 735, name: "Direct" },
      { value: 580, name: "Email" },
      { value: 484, name: "Union Ads" },
      { value: 300, name: "Video Ads" },
    ];

    registerData.value = tempData;
  };
  const getServiceApi = async () => {
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

    serviceData.value = tempData;
  };
  const getHistoryApi = async () => {
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
    typeData,
    statusData,
    recommendData,
    responseData,
    registerData,
    serviceData,
    historyData,
    getTypeApi,
    getStatusApi,
    getRecommendApi,
    getResponseApi,
    getRegisterApi,
    getServiceApi,
    getHistoryApi,
  };
});
