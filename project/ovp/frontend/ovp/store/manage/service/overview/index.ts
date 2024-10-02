import { ref } from "vue";

export const useOverviewStore = defineStore("overview", () => {
  const { $api } = useNuxtApp();

  const serviceTypeData: Ref<any[]> = ref([]);
  const serviceStatusData: Ref<any[]> = ref([]);
  const serviceResponseData: Ref<any[]> = ref([]);
  const allCurrentSituationData: Ref<any[]> = ref([]);
  const slicedCurrentSituationData: Ref<any[]> = ref([]);
  const currentSituationData: Ref<any[]> = ref([]);
  const statusDetailData: Ref<any[]> = ref([]);
  const historyData: Ref<any[]> = ref([]);
  const isOpenAgHeaderTooltip: Ref<boolean> = ref(false);
  const agHeaderTooltipContents: Ref<string> = ref("event");
  const agHeaderCoordinates = ref({ x: 0, y: 0 });
  const responsePageSize: Ref<number> = ref(20);
  const responseTotalSize: Ref<number> = ref(0);
  const collectedDateTime: Ref<string> = ref("");
  const PAGE_SIZE = 20;

  const getServiceTypeData = async () => {
    const { data } = await $api(
      `http://localhost:8080/api/service/overview/summary-info`,
    );

    serviceTypeData.value = data.typeSummary;
  };
  const getServiceStatusData = async () => {
    const { data } = await $api(
      `http://localhost:8080/api/service/overview/summary-info`,
    );

    serviceStatusData.value = data.statusList;
  };

  const getServiceResponseAPI = async () => {
    const { data } = await $api(
      `http://localhost:8080/api/service/overview/response-time?pageNumber=0&pageSize=${responsePageSize.value}`,
      {
        showLoader: false,
      },
    );

    responseTotalSize.value = data.totalSize;

    return data;
  };

  const getServiceResponseData = async () => {
    responsePageSize.value = 20;
    const { data } = await getServiceResponseAPI();
    serviceResponseData.value = data;
  };

  const addServiceResponseData = async () => {
    if (responseTotalSize.value - responsePageSize.value < PAGE_SIZE) {
      responsePageSize.value = responseTotalSize.value;
    } else {
      responsePageSize.value += PAGE_SIZE;
    }

    const { data } = await getServiceResponseAPI();
    serviceResponseData.value = data;
  };

  const getDataCurrentSituationData = async (
    startStandard: number = 0,
    count: number = 5,
  ) => {
    const { data } = await $api(
      `http://localhost:8080/api/service/overview/service-models?pageNumber=0`,
    );

    for (const element of data.data) {
      const serviceItem = [];
      serviceItem.push(element.serviceName);
      // 전데 데이터 개수
      serviceItem.push(element.modelCount);
      // 등록된 데이터 모델 개수
      serviceItem.push(element.omModelCount);
      allCurrentSituationData.value.push(serviceItem);
    }

    slicedCurrentSituationData.value = allCurrentSituationData.value.slice(
      startStandard,
      startStandard + count,
    );

    currentSituationData.value = slicedCurrentSituationData.value;
  };

  const convertDateTime = (date: string) => {
    const originDate = new Date(date);

    const setNumberZero = (num) => (num < 10 ? "0" + num : num);

    return `${originDate.getFullYear()}-${setNumberZero(originDate.getMonth() + 1)}-${setNumberZero(originDate.getDate())} ${setNumberZero(originDate.getHours())}:${setNumberZero(originDate.getMinutes())}:${setNumberZero(originDate.getSeconds())}`;
  };

  const getStatusDetailData = async () => {
    const { data } = await $api(
      `http://localhost:8080/api/service/overview/connection-history?pageNumber=0&pageSize=30`,
    );

    statusDetailData.value = data.data;
    collectedDateTime.value = convertDateTime(data.recentCollectedTime);
  };
  const getHistoryData = async () => {
    const { data } = await $api(
      `http://localhost:8080/api/service/overview/ingestion-history?pageNumber=0&pageSize=30`,
    );

    historyData.value = data.data;
  };

  const openDynamicTooltip = (event: MouseEvent, item: string): void => {
    isOpenAgHeaderTooltip.value = true;
    const rect = event.target.getBoundingClientRect();
    agHeaderTooltipContents.value = item;
    agHeaderCoordinates.value =
      agHeaderTooltipContents.value === "event"
        ? {
            x: rect.left - 130,
            y: rect.top - 105,
          }
        : {
            x: rect.left - 130,
            y: rect.top - 125,
          };
  };

  const closeDynamicTooltip = () => {
    isOpenAgHeaderTooltip.value = false;
  };

  return {
    serviceTypeData,
    serviceStatusData,
    serviceResponseData,
    currentSituationData,
    statusDetailData,
    historyData,
    isOpenAgHeaderTooltip,
    agHeaderCoordinates,
    agHeaderTooltipContents,
    responsePageSize,
    collectedDateTime,
    slicedCurrentSituationData,
    getServiceTypeData,
    openDynamicTooltip,
    closeDynamicTooltip,
    getServiceStatusData,
    getServiceResponseData,
    getDataCurrentSituationData,
    getStatusDetailData,
    getHistoryData,
    addServiceResponseData,
    convertDateTime,
  };
});
