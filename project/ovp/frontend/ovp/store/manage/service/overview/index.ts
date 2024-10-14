import { ref } from "vue";
import { ModuleRegistry } from "ag-grid-community";
import { InfiniteRowModelModule } from "@ag-grid-community/infinite-row-model";

export const useOverviewStore = defineStore("overview", () => {
  const { $api } = useNuxtApp();

  const serviceTypeData: Ref<any[]> = ref([]);
  const serviceStatusData: Ref<any[]> = ref([]);
  const serviceResponseData: Ref<any[]> = ref([]);
  const allCurrentSituationData: Ref<any[]> = ref([]);
  const slicedCurrentSituationData: Ref<any[]> = ref([]);
  const currentSituationData: Ref<any[]> = ref([]);
  const isOpenAgHeaderTooltip: Ref<boolean> = ref(false);
  const agHeaderTooltipContents: Ref<string> = ref("event");
  const agHeaderCoordinates = ref({ x: 0, y: 0 });
  const responsePageSize: Ref<number> = ref(20);
  const responseTotalSize: Ref<number> = ref(0);
  const collectedDateTime: Ref<string> = ref("");
  const PAGE_SIZE = 20;

  const getServiceTypeData = async () => {
    const { data } = await $api(`/api/service/overview/summary-info`);

    serviceTypeData.value = data !== null ? data.typeSummary : [];
  };
  const getServiceStatusData = async () => {
    const { data } = await $api(`/api/service/overview/summary-info`);

    serviceStatusData.value = data !== null ? data.statusList : [];
  };

  const getServiceResponseAPI = async () => {
    const { data } = await $api(
      `/api/service/overview/response-time?pageNumber=0&pageSize=${responsePageSize.value}`,
      {
        showLoader: false,
      },
    );

    responseTotalSize.value = data !== null ? data.totalSize : [];
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
      `/api/service/overview/service-models?pageNumber=0`,
    );

    if (data.data !== null) {
      for (const element of data.data) {
        const serviceItem = [];
        serviceItem.push(element.serviceName);
        serviceItem.push(element.modelCount);
        serviceItem.push(element.omModelCount);
        allCurrentSituationData.value.push(serviceItem);
      }

      slicedCurrentSituationData.value = allCurrentSituationData.value.slice(
        startStandard,
        startStandard + count,
      );
    } else {
      slicedCurrentSituationData.value = [];
    }

    currentSituationData.value = slicedCurrentSituationData.value;
  };

  const convertDateTime = (date: string) => {
    const originDate = new Date(date);

    const setNumberZero = (num: number) => (num < 10 ? "0" + num : num);

    return `${originDate.getFullYear()}-${setNumberZero(originDate.getMonth() + 1)}-${setNumberZero(originDate.getDate())} ${setNumberZero(originDate.getHours())}:${setNumberZero(originDate.getMinutes())}:${setNumberZero(originDate.getSeconds())}`;
  };

  // AG-GRID
  // 무한 스크롤 기능을 제공하는 모듈인 InfiniteRowModelModule을 등록하는 코드 (한 번에 모든 데이터를 로드하지 않고, 필요한 부분만 서버에서 로드하여 표시)
  ModuleRegistry.registerModules([InfiniteRowModelModule]);
  // ag-grid 인스턴스에 접근할 수 있는 객체, 객체 자체의 깊은 속성을 반응형으로 만들 필요가 없어서 얕은 반응성을 가지는 참조 객체로 사용(shallowRef)
  const collectorHistoryGridApi = shallowRef(null);
  const serviceStatusGridApi = shallowRef(null);
  const showCollectorHistoryLoading = ref(false);
  const showServiceStatusLoading = ref(false);
  const isEmptyCollectorHistory = ref(false);
  const isEmptyServiceStatus = ref(false);
  const onCollectorHistoryGrid = (params: any) => {
    // collectorHistoryGridApi.value > ag-grid의 API에 접근하는 변수로, onCollectorHistoryGrid가 호출될 때, 초기화 된다.
    collectorHistoryGridApi.value = params.paramApi;

    // collectorHistoryGridApi.value가 제대로 초기화되었는지 확인 후 실행
    if (collectorHistoryGridApi.value) {
      // dataSource는 데이터를 그리드에 제공하는 역할을 한다. dataSource 객체에는 데이터 로딩에 필요한 여러가지 설정과 메서드가 포함된다.
      const dataSource = {
        // 그리드에 있는 전체 데이터 개수를 나타내는 속성. 무한 스크롤 모드에서는 undefined로 설정하면 전체 데이터의 개수를 알 수 없다고 가정하고, 동적 로딩을 가능하게 한다.
        rowCount: undefined,
        // ag-grid가 필요한 데이터만 가져오기 위해 호출하는 함수. 어떤 데이터 범위를 요청할지를 결정하며, 요청된 범위에 따라 데이터를 서버에서 가져와 제공한다.
        getRows: async (params: any) => {
          showCollectorHistoryLoading.value = true;

          try {
            const { data: result } = await $api(
              `/api/service/overview/ingestion-history?pageNumber=0`,
            );

            const data = result.data.map((item: any) => ({
              ...item,
              serviceNameFormatted: item.serviceDisplayName ?? item.serviceName,
              eventAtFormatted: convertDateTime(item.eventAt),
              eventFormatted: setEventStatus(item.event),
              statusFormatted: item.state,
            }));

            const rowsThisPage = data.slice(params.startRow, params.endRow); // 요청 범위만큼 데이터 잘라서 보여줌
            const lastRow = data.length <= params.endRow ? data.length : -1; // 마지막 페이지 여부 확인 후 lastRow 설정
            isEmptyCollectorHistory.value = data.length === 0;

            params.successCallback(rowsThisPage, lastRow); // 데이터와 lastRow 콜백 전달
          } catch (error) {
            console.error("Error fetching data:", error);
            params.failCallback();
          } finally {
            showCollectorHistoryLoading.value = false;
          }
        },
      };

      // 그리드에 데이터 소스를 설정하여, 그리드가 데이터를 어떻게 가져오고 처리할지를 알려준다.
      // 아래 코드를 통해 ag-grid는 설정된 DataSource를 사용하여 스크롤할 때 필요한 데이터 범위를 getRows함수를 통해 요청한다.
      collectorHistoryGridApi.value.setDatasource(dataSource);
    } else {
      console.error("collectorHistoryGridApi is undefined or null.");
    }
  };

  const onServiceStatusGrid = (params: any) => {
    serviceStatusGridApi.value = params.paramApi;

    if (serviceStatusGridApi.value) {
      const dataSource = {
        rowCount: undefined,
        getRows: async (params: any) => {
          showServiceStatusLoading.value = true;

          try {
            const { data: result } = await $api(
              `/api/service/overview/connection-history?pageNumber=0`,
            );

            const data = result.data.map((item: any) => ({
              ...item,
              serviceNameFormatted: item.serviceDisplayName ?? item.serviceName,
            }));
            const rowsThisPage = data.slice(params.startRow, params.endRow);
            const lastRow = data.length <= params.endRow ? data.length : -1;
            collectedDateTime.value = convertDateTime(
              result.recentCollectedTime,
            );
            isEmptyServiceStatus.value = data.length === 0;
            params.successCallback(rowsThisPage, lastRow);
          } catch (error) {
            console.error("Error fetching data:", error);
            params.failCallback();
          } finally {
            showServiceStatusLoading.value = false;
          }
        },
      };

      serviceStatusGridApi.value.setDatasource(dataSource);
    } else {
      console.error("serviceStatusGridApi is undefined or null.");
    }
  };

  const setEventStatus = (status: string) => {
    let eventTheme = "";
    switch (status) {
      case "status_change":
        eventTheme = "현황변경";
        break;
      case "created":
        eventTheme = "등록";
        break;
      case "updated":
        eventTheme = "편집";
        break;
      case "deleted":
        eventTheme = "삭제";
        break;
      default:
        eventTheme = "등록";
    }

    return eventTheme;
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
    isOpenAgHeaderTooltip,
    agHeaderCoordinates,
    agHeaderTooltipContents,
    responsePageSize,
    collectedDateTime,
    slicedCurrentSituationData,
    showCollectorHistoryLoading,
    showServiceStatusLoading,
    isEmptyCollectorHistory,
    isEmptyServiceStatus,
    getServiceTypeData,
    openDynamicTooltip,
    closeDynamicTooltip,
    getServiceStatusData,
    getServiceResponseData,
    getDataCurrentSituationData,
    addServiceResponseData,
    convertDateTime,
    onCollectorHistoryGrid,
    onServiceStatusGrid,
  };
});
