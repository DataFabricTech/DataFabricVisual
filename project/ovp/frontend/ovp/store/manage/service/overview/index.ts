import { ref } from "vue";

export const useOverviewStore = defineStore("overview", () => {
  // const { $api } = useNuxtApp();

  const serviceTypeData: Ref<any[]> = ref([]);
  const serviceStatusData: Ref<any[]> = ref([]);
  const serviceResponseData: Ref<any[]> = ref([]);
  const currentSituationData: Ref<any[]> = ref([]);
  const statusDetailData: Ref<any[]> = ref([]);
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

  // TODO: [개발] API 완료 후 데이터 누적 기능 구현 필요
  const addServiceResponseData = async () => {
    console.log("인피니티 스크롤 누적");
    // const { data, totalCount } = await getSearchListAPI();
    // searchResult.value = searchResult.value.concat(data[currentTab.value]);
    // searchResultLength.value = totalCount;
  };

  // Move To Page
  const allTempData = [
    ["서비스 A", 43.3, 85.8],
    ["서비스 B", 83.1, 73.4],
    ["서비스 C", 86.4, 65.2],
    ["서비스 D", 72.4, 53.9],
    ["서비스 E", 92.4, 15.3],

    ["서비스 F", 15.3, 73.5],
    ["서비스 G", 94.1, 12.4],
    ["서비스 H", 86.4, 23.2],
    ["서비스 I", 45.4, 53.9],
    ["서비스 K", 92.4, 36.3],

    ["서비스 Q", 43.3, 85.8],
    ["서비스 W", 47.5, 73.4],
    ["서비스 E", 34.4, 6.3],
    ["서비스 R", 100, 53.9],
    ["서비스 T", 83.4, 35.5],

    ["서비스 Y", 43.3, 85.8],
    ["서비스 R", 83.1, 58.4],
    ["서비스 V", 66.4, 65.2],
    ["서비스 I", 45.4, 53.9],
    ["서비스 K", 92.4, 36.3],

    ["서비스 T", 43.3, 85.8],
    ["서비스 W", 47.5, 73.4],
    ["서비스 E", 34.4, 6.3],
    ["서비스 R", 100, 53.9],
    ["서비스 S", 83.4, 35.5],

    ["서비스 B", 43.3, 85.8],
    ["서비스 R", 83.1, 58.4],
    ["서비스 V", 66.4, 65.2],
  ];
  const currentSituationTempData: Ref<any[]> = ref([]);

  const getDataCurrentSituationData = async (
    startStandard: number = 0,
    count: number = 5,
  ) => {
    // const {data} = await $api (``);
    currentSituationTempData.value = allTempData.slice(
      startStandard,
      startStandard + count,
    );
    currentSituationData.value = currentSituationTempData.value;
  };
  const getStatusDetailData = async () => {
    // const {data} = await $api (``);
    const tempData = [
      {
        name: "서비스 B",
        type: "MySQL",
        status: "Disconnected",
      },
      {
        name: "서비스 G",
        type: "MariaDB",
        status: "Connected",
      },
      {
        name: "서비스 F",
        type: "MinIO",
        status: "Error",
      },
      {
        name: "서비스 D",
        type: "MariaDB",
        status: "Connected",
      },
      {
        name: "서비스 G",
        type: "MySQL",
        status: "Disconnected",
      },
      {
        name: "서비스 B",
        type: "MySQL",
        status: "Disconnected",
      },
      {
        name: "서비스 G",
        type: "MariaDB",
        status: "Connected",
      },
      {
        name: "서비스 F",
        type: "MinIO",
        status: "Error",
      },
      {
        name: "서비스 D",
        type: "MariaDB",
        status: "Connected",
      },
      {
        name: "서비스 G",
        type: "MySQL",
        status: "Disconnected",
      },
      {
        name: "서비스 B",
        type: "MySQL",
        status: "Disconnected",
      },
      {
        name: "서비스 G",
        type: "MariaDB",
        status: "Connected",
      },
      {
        name: "서비스 F",
        type: "MinIO",
        status: "Error",
      },
      {
        name: "서비스 D",
        type: "MariaDB",
        status: "Connected",
      },
      {
        name: "서비스 G",
        type: "MySQL",
        status: "Disconnected",
      },
    ];

    statusDetailData.value = tempData;
  };
  const getHistoryData = async () => {
    // const {data} = await $api (``);
    const tempData = [
      {
        date: "2023-01-05 12:16:46",
        collectionName: "pgv2_metadata_ingestion",
        event: "동기화 완료",
        status: "Success",
        name: "서비스 B",
        type: "MySQL",
      },
      {
        date: "2023-01-05 12:16:46",
        collectionName: "pgv2_metadata_ingestion",
        event: "연결",
        status: "PartialSuccess",
        name: "서비스 B",
        type: "MySQL",
      },
      {
        date: "2023-01-05 12:16:46",
        collectionName: "pgv2_metadata_ingestion",
        event: "연결",
        status: "Failed",
        name: "서비스 B",
        type: "MySQL",
      },
      {
        date: "2023-01-05 12:16:46",
        collectionName: "pgv2_metadata_ingestion",
        event: "동기화 완료",
        status: "Success",
        name: "서비스 B",
        type: "MySQL",
      },
      {
        date: "2023-01-05 12:16:46",
        collectionName: "pgv2_metadata_ingestion",
        event: "동기화 완료",
        status: "PartialSuccess",
        name: "서비스 B",
        type: "MySQL",
      },
      {
        date: "2023-01-05 12:16:46",
        collectionName: "pgv2_metadata_ingestion",
        event: "연",
        status: "Success",
        name: "서비스 B",
        type: "MySQL",
      },
      {
        date: "2023-01-05 12:16:46",
        collectionName: "pgv2_metadata_ingestion",
        event: "연",
        status: "Queued",
        name: "서비스 B",
        type: "MySQL",
      },
      {
        date: "2023-01-05 12:16:46",
        collectionName: "pgv2_metadata_ingestion",
        event: "연",
        status: "Success",
        name: "서비스 B",
        type: "MySQL",
      },
      {
        date: "2023-01-05 12:16:46",
        collectionName: "pgv2_metadata_ingestion",
        event: "연결",
        status: "Queued",
        name: "서비스 B",
        type: "MySQL",
      },
      {
        date: "2023-01-05 12:16:46",
        collectionName: "pgv2_metadata_ingestion",
        event: "동기화 완료",
        status: "Running",
        name: "서비스 B",
        type: "MySQL",
      },
      {
        date: "2023-01-05 12:16:46",
        collectionName: "pgv2_metadata_ingestion",
        event: "동기화 완료",
        status: "Success",
        name: "서비스 B",
        type: "MySQL",
      },
      {
        date: "2023-01-05 12:16:46",
        collectionName: "pgv2_metadata_ingestion",
        event: "연결",
        status: "PartialSuccess",
        name: "서비스 B",
        type: "MySQL",
      },
      {
        date: "2023-01-05 12:16:46",
        collectionName: "pgv2_metadata_ingestion",
        event: "동기화 완료",
        status: "Success",
        name: "서비스 B",
        type: "MySQL",
      },
      {
        date: "2023-01-05 12:16:46",
        collectionName: "pgv2_metadata_ingestion",
        event: "동기화 완료",
        status: "Success",
        name: "서비스 B",
        type: "MySQL",
      },
      {
        date: "2023-01-05 12:16:46",
        collectionName: "pgv2_metadata_ingestion",
        event: "동기화 완료",
        status: "Running",
        name: "서비스 B",
        type: "MySQL",
      },
    ];

    historyData.value = tempData;
  };

  return {
    serviceTypeData,
    serviceStatusData,
    serviceResponseData,
    currentSituationData,
    statusDetailData,
    historyData,
    getServiceTypeData,
    getServiceStatusData,
    getServiceResponseData,
    getDataCurrentSituationData,
    getStatusDetailData,
    getHistoryData,
    addServiceResponseData,
  };
});
