import { defineStore } from "pinia";
import { ref, reactive, type Ref } from "vue";
import type {
  Service,
  Owner,
  Ingestion,
  ServiceData,
  DBServiceListData,
} from "@/type/service";
import type { JsonPatchOperation } from "@/type/common";
import type { MenuSearchItemImpl } from "@extends/menu-seach/MenuSearchComposition";
import {
  ConnectionStatus,
  services,
  ServiceIds,
} from "@/components/manage/service/modal/modal-service/ModalServiceComposition";
import { useDataModelDetailStore } from "@/store/search/detail";
import $constants from "@/utils/constant";
import _ from "lodash";

export const useServiceStore = defineStore("service", () => {
  const { $api } = useNuxtApp();
  const dataModelDetailStore = useDataModelDetailStore();
  const { tagList, termList } = storeToRefs(dataModelDetailStore);
  const tab = ref<string>("ingestion");

  const service = reactive<Service>(<Service>{});
  const serviceList = reactive<Service[]>([]);

  // 이미지 URL 동적 셋팅
  services.value.forEach(async (service: any) => {
    const imgUrl: any = await import(`@assetsPublic/images/${service.img}.png`);
    service.imgUrl = imgUrl.default;
  });
  const servicesById = _.keyBy(services.value, "id");

  const userList = reactive<Owner[]>([]);
  const userSearchList = reactive<object[]>([]);

  const ingestionList = reactive<Ingestion[]>([]);

  const editInfo = reactive({
    owner: false,
    tag: false,
    term: false,
  });

  // 수정가능상태
  const isDescEditable = ref<boolean>(false);

  const serviceData: Ref<ServiceData> = ref({ description: "" });

  const DBServiceListData: Ref<DBServiceListData[] | null | undefined> = ref([
    {},
  ]);

  let selectedFqn: string = "";
  let selectedID: string = "";
  let selectedName: string = "";
  let selectedServiceType: string = "";

  // getRepositoryDescriptionAPI의 params 생성함수
  const getQueryData = () => {
    const params = {
      fields: "owner,tags,dataProducts,domain",
      include: "all",
    };
    return new URLSearchParams(params);
  };

  // 저장소 탭 > 설명 [데이터베이스] 조회 API호출 함수
  const getRepositoryDescriptionAPI = async () => {
    const { data }: any = await $api(
      `/api/service-manage/repository/description/${selectedFqn}?${getQueryData()}`,
    );
    serviceData.value = data;
  };

  // 저장소 탭 > [스토리지] 설명 조회 API호출 함수
  const getRepositoryStorageDescriptionAPI = async () => {
    const { data }: any = await $api(
      `/api/service-manage/repository/storage/description/${selectedFqn}?${getQueryData()}`,
    );

    serviceData.value = data;
  };

  // 저장소 탭 > 설명 수정 API호출 함수
  const updateRepositoryDescriptionAPI = (patchData: JsonPatchOperation[]) => {
    const serviceType: string = ["MINIO"].includes(
      selectedServiceType.toUpperCase(),
    )
      ? "repository/storage"
      : "repository";
    return $api(
      `/api/service-manage/${serviceType}/description/${selectedID}`,
      {
        method: "PATCH",
        body: patchData,
      },
    );
  };

  const getDBServiceList = async () => {
    const uri: string = ["MINIO"].includes(selectedServiceType.toUpperCase())
      ? "storage-services"
      : "database-services";

    const result: any = await $api(
      `/api/service-manage/${uri}/${selectedName}`,
    );
    DBServiceListData.value = result.data;

    return result;
  };

  const testConnectionStatus: Ref<ConnectionStatus> = ref(
    ConnectionStatus.NONE,
  );
  const connectionInfo: Ref<{ [key: string]: any }> = ref({});
  const viewConnectionInfo: Ref<{ [key: string]: any }> = ref({});

  async function changeTab(value: string) {
    tab.value = value;
  }

  /**
   * 서비스 리스트
   */
  async function getServiceList(): Promise<void> {
    const res = await $api(`/api/service-manage/list`);
    const serviceListData: Service[] = res.data;
    serviceList.splice(0, serviceList.length, ...serviceListData);

    const serviceData: Service =
      Object.keys(service).length === 0 ? serviceListData[0] : service;
    changeCurrentService(serviceData);
  }

  /**
   * 서비스 검색 리스트
   * @param keyword
   * @param from
   */
  async function searchServiceList(
    keyword: string,
    from: string,
  ): Promise<void> {
    const res = await $api(
      `/api/service-manage/list/search?q=*${keyword}*&from=${from}`,
    );
    if (res.data !== null) {
      serviceList.splice(0, serviceList.length, ...res.data);

      const serviceData: Service =
        Object.keys(service).length === 0 ? res.data[0] : service;
      changeCurrentService(serviceData);
    }
  }

  /**
   * 현재 서비스 엔티티 변경
   * @param source
   */
  function changeCurrentService(source: Service): void {
    isDescEditable.value = false;
    if (!source.owner) {
      source.owner = [];
    }
    Object.assign(service, source);
    disableEditInfo();
    // 서비스관리 목록 클릭시, 설명 API호출
    if (
      !_.isEmpty(service.fullyQualifiedName) &&
      !_.isUndefined(service.fullyQualifiedName)
    ) {
      selectedFqn = service.fullyQualifiedName;
      selectedID = service.id;
      selectedName = service.name;
      selectedServiceType = service.serviceType;
      if (
        ["MYSQL", "MARIADB", "ORACLE", "POSTGRES"].includes(
          selectedServiceType.toUpperCase(),
        )
      ) {
        // 서비스타입이 데이터베이스 일 경우
        getRepositoryDescriptionAPI();
      } else if (["MINIO"].includes(selectedServiceType.toUpperCase())) {
        // 서비스 타입이 스토리지일 경우
        getRepositoryStorageDescriptionAPI();
      } else {
        // 서비스 타입이 기타일 경우
      }
    }
    getDBServiceList();
  }

  /**
   * 서비스 삭제
   * @param id
   */
  async function deleteService(id: string): Promise<void> {
    await $api(
      `/api/service-manage/${id}?type=${service.type}&hardDelete=true&recursive=true`,
      {
        method: "DELETE",
      },
    );
    emptyService();
  }

  /**
   * 현재 서비스 비움 처리
   */
  function emptyService() {
    Object.keys(service).forEach((key: string) => {
      delete service[key];
    });
  }

  async function changeTag(target: string, data: any) {
    let body: any[] = [];

    if (_.isEmpty(data)) {
      body = [];
    } else if (target === "Classification") {
      body = _.filter(tagList.value, (tag) => {
        return data.includes(tag.tagFQN);
      });
    } else if (target === "Glossary") {
      body = _.filter(termList.value, (tag) => {
        return data.includes(tag.tagFQN);
      });
    }

    const res = await $api(
      `/api/service-manage/${service.id}/tag?name=${service.name}&type=${service.type}&target=${target}`,
      {
        method: "patch",
        body: body,
      },
    );
    if (res.data !== null) {
      changeCurrentService(res.data);
    } else {
      alert(res.errorMessage);
    }
  }

  /**
   * 서비스 수정
   * @param id
   * @param body
   */
  async function updateService(
    id: string,
    body: JsonPatchOperation[],
  ): Promise<void> {
    const res = await $api(`/api/service-manage/${id}?type=${service.type}`, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json-patch+json",
      },
      body: body,
    });
    if (res.data !== null) {
      changeCurrentService(res.data);
    } else {
      alert(res.errorMessage);
    }
  }

  /**
   * 유저 리스트
   */
  async function getUserList(): Promise<void> {
    const res = await $api(`/api/user/all`);
    if (res.data !== null) {
      // eslint-disable-next-line id-length
      res.data.forEach((v: Owner) => {
        userList.push({
          id: v.id,
          name: v.name,
          type: v.isAdmin === false ? "user" : "admin",
          displayName: v.displayName,
          fullyQualifiedName: v.fullyQualifiedName,
        });
        userSearchList.push({ id: v.id, name: v.name });
      });
    }
  }

  /**
   * operation 생성
   * @param item
   */
  function createOwnerOperation(
    item: MenuSearchItemImpl,
  ): JsonPatchOperation[] {
    const operations: JsonPatchOperation[] = [];
    const foundUser: any = userList.find((user: Owner) => user.id === item.id);

    const isEmpty = item.id === undefined;
    if (service.owner.id === undefined) {
      if (item && foundUser) {
        operations.push({
          op: "add",
          path: "/owner",
          value: foundUser,
        });
      }
    } else {
      if (!isEmpty) {
        operations.push({ op: "remove", path: "/owner/href" });
        operations.push({ op: "remove", path: "/owner/deleted" });

        if (foundUser) {
          $constants.PATCH_OPERATION.PATH_LIST.OWNER.forEach((path: string) => {
            if (foundUser[path] !== undefined) {
              operations.push({
                op: "replace",
                path: `/owner/${path}`,
                value: foundUser[path],
              });
            }
          });
        }
      } else {
        operations.push({
          op: "remove",
          path: "/owner",
        });
      }
    }
    return operations;
  }

  /**
   * 수집 탭
   */
  async function getIngestionList(service: Service) {
    const serviceType =
      service.serviceType.toUpperCase() === "MINIO"
        ? "storageService"
        : "databaseService";
    const res = await $api(
      `/api/service-manage/ingestion/list?service=${service.name}&serviceType=${serviceType}`,
    );

    if (res.data !== null) {
      ingestionList.splice(0, ingestionList.length, ...res.data);
    }
  }

  async function getIngestionStatus(
    name: string,
    startTs: number,
    endTs: number,
  ): Promise<void> {
    const res = await $api(
      `/api/service-manage/ingestion/status/${name}?startTs=${startTs}&endTs=${endTs}`,
      {
        showLoader: false,
      },
    );
    setIngestionStatus(name, res.data.pipelineState);
  }

  function setIngestionStatus(name: string, pipelineState: string) {
    // eslint-disable-next-line id-length
    const index = ingestionList.findIndex((v: Ingestion) => v.name === name);
    if (index !== -1) {
      ingestionList[index].pipelineState = pipelineState;
    }
  }

  async function runIngestion(id: string): Promise<void> {
    const res = await $api(`/api/service-manage/ingestion/trigger/${id}`, {
      method: "POST",
      showLoader: false,
    });
    if (res.data === null) {
      throw new Error(res.errorMessage);
    }
  }

  async function deployIngestion(id: string): Promise<void> {
    const res = await $api(`/api/service-manage/ingestion/deploy/${id}`, {
      method: "POST",
      showLoader: false,
    });
    if (res.data === null) {
      throw new Error(res.errorMessage);
    }
  }

  async function deleteIngestion(id: string): Promise<void> {
    const res = await $api(`/api/service-manage/ingestion/${id}`, {
      method: "DELETE",
      showLoader: false,
    });
    if (res.data === null) {
      throw new Error(res.errorMessage);
    }
  }

  async function killIngestion(id: string): Promise<void> {
    const res = await $api(`/api/service-manage/ingestion/kill/${id}`, {
      method: "POST",
      showLoader: false,
    });
    if (res.data === null) {
      throw new Error(res.errorMessage);
    }
  }

  /**
   * input show-hide
   * @param property
   */
  function changeEditInfo(property: keyof typeof editInfo): void {
    editInfo[property] = !editInfo[property];
  }

  function disableEditInfo(): void {
    editInfo.owner = false;
    editInfo.tag = false;
    editInfo.term = false;
  }

  /**
   * 연결정보 조회
   */
  const getConnectionInfo = async () => {
    // TODO: API 구현 완료 후 수정
    const queryParams = {
      fields: "owner,tags,dataProducts,domain",
      include: "all",
    };
    const connectionQuery = new URLSearchParams(queryParams);
    const { type, name } = service;
    const { data } = await $api(
      `/api/service-manage/${type}/${name}?${connectionQuery}`,
    );

    connectionInfo.value = data;
    setViewConnectionInfo();
  };

  const addIfExists = (obj: any, key: string, value: any) => {
    // TODO: 공통모듈화, service modal 에 같은 코드 있음
    if (value) {
      obj[key] = value;
    }
  };

  const setViewConnectionInfo = () => {
    // TODO: 상수처리, service modal 에 비슷한 코드 확인 후 공통으로 사용가능하면 수정 필요
    const connectionInfoData = connectionInfo.value;
    const { type, scheme, connectionArguments, connectionOptions } =
      connectionInfoData;

    const data: { [key: string]: any } = {};
    addIfExists(data, "scheme", scheme);
    addIfExists(data, "connectionArguments", connectionArguments);
    addIfExists(data, "connectionOptions", connectionOptions);
    switch (type) {
      case ServiceIds.MINIO: {
        const minioConfig = connectionInfoData.minioConfig;
        addIfExists(data, "accessKeyId", minioConfig.accessKeyId);
        addIfExists(data, "secretKey", minioConfig.secretKey);
        addIfExists(data, "sessionToken", minioConfig.sessionToken);
        addIfExists(data, "region", minioConfig.region);
        addIfExists(data, "endPointURL", minioConfig.endPointURL);
        addIfExists(data, "bucketNames", connectionInfoData.bucketNames);
        break;
      }
      case ServiceIds.MARIA_DB:
        addIfExists(data, "username", connectionInfoData.username);
        addIfExists(data, "password", connectionInfoData.password);
        addIfExists(data, "hostPort", connectionInfoData.hostPort);
        addIfExists(data, "databaseName", connectionInfoData.databaseName);
        addIfExists(data, "databaseSchema", connectionInfoData.databaseSchema);
        break;
      case ServiceIds.MYSQL:
        addIfExists(
          data,
          "authConfigurationType",
          connectionInfoData.authConfigurationType,
        );
        addIfExists(data, "username", connectionInfoData.username);
        addIfExists(data, "password", connectionInfoData.authType?.password);
        addIfExists(data, "hostPort", connectionInfoData.hostPort);
        addIfExists(data, "databaseName", connectionInfoData.databaseName);
        break;
      case ServiceIds.POSTGRESQL:
        addIfExists(
          data,
          "authConfigurationType",
          connectionInfoData.authConfigurationType,
        );
        addIfExists(data, "username", connectionInfoData.username);
        addIfExists(data, "password", connectionInfoData.authType?.password);
        addIfExists(data, "hostPort", connectionInfoData.hostPort);
        addIfExists(data, "database", connectionInfoData.database);
        addIfExists(
          data,
          "ingestAllDatabases",
          connectionInfoData.ingestAllDatabases,
        );
        addIfExists(data, "sslMode", connectionInfoData.sslMode);
        break;
      case ServiceIds.ORACLE:
        addIfExists(
          data,
          "oracleConnectionType",
          connectionInfoData.oracleConnectionType,
        );
        addIfExists(
          data,
          "instantClientDirectory",
          connectionInfoData.instantClientDirectory,
        );
        addIfExists(data, "username", connectionInfoData.username);
        addIfExists(data, "password", connectionInfoData.authType?.password);
        addIfExists(data, "hostPort", connectionInfoData.hostPort);
        break;
      default:
        // 예상치 못한 serviceId에 대한 처리
        console.warn(`Unknown serviceId: ${type}`);
        break;
    }

    viewConnectionInfo.value = data;
  };

  return {
    tab,
    servicesById,
    service,
    serviceList,
    userList,
    userSearchList,

    ingestionList,
    editInfo,
    changeTab,
    changeTag,

    getServiceList,
    searchServiceList,

    changeCurrentService,
    deleteService,
    updateService,
    emptyService,

    getIngestionList,
    getIngestionStatus,
    runIngestion,
    deployIngestion,
    deleteIngestion,
    killIngestion,

    changeEditInfo,
    disableEditInfo,

    getUserList,
    createOwnerOperation,

    serviceData,
    DBServiceListData,
    getRepositoryDescriptionAPI,
    updateRepositoryDescriptionAPI,
    getDBServiceList,

    testConnectionStatus,
    connectionInfo,
    viewConnectionInfo,
    getConnectionInfo,

    isDescEditable,
  };
});
