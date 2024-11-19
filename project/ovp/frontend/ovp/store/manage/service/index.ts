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
import type { IService } from "~/components/manage/service/modal/modal-service/ModalServiceProps";

export const useServiceStore = defineStore("service", () => {
  const { $api, $alert } = useNuxtApp();
  const dataModelDetailStore = useDataModelDetailStore();
  const { tagList, termList } = storeToRefs(dataModelDetailStore);
  const tab = ref<string>("ingestion");

  const isDoneRepoAPI = ref<boolean>(false);

  const service = reactive<Service>(<Service>{});
  const serviceList = reactive<Service[]>([]);

  enum ServiceIdsWithTrino {
    MINIO = "MinIO",
    MYSQL = "Mysql",
    MARIA_DB = "MariaDB",
    POSTGRESQL = "Postgres",
    ORACLE = "Oracle",
    TRINO = "Trino",
  }

  const servicesWithTrino = ref<IService[]>([
    {
      id: ServiceIdsWithTrino.MINIO,
      label: "MinIO",
      img: "storage-type_06",
      imgUrl: "",
      isDisabled: false,
    },
    {
      id: ServiceIdsWithTrino.MYSQL,
      label: "MySQL",
      img: "storage-type_02",
      imgUrl: "",
      isDisabled: false,
    },
    {
      id: ServiceIdsWithTrino.MARIA_DB,
      label: "MariaDB",
      img: "storage-type_01",
      imgUrl: "",
      isDisabled: false,
    },
    {
      id: ServiceIdsWithTrino.POSTGRESQL,
      label: "PostgreSQL",
      img: "storage-type_03",
      imgUrl: "",
      isDisabled: false,
    },
    {
      id: ServiceIdsWithTrino.ORACLE,
      label: "Oracle",
      img: "storage-type_04",
      imgUrl: "",
    },
    {
      id: ServiceIdsWithTrino.TRINO,
      label: "Trino",
      img: "storage-type_07",
      imgUrl: "",
    },
  ]);

  servicesWithTrino.value.forEach(async (service: any) => {
    const imgUrl: any = await import(`@assetsPublic/images/${service.img}.png`);
    service.imgUrl = imgUrl.default;
  });
  const servicesWithTrinoById = _.keyBy(servicesWithTrino.value, "id");

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
    description: false,
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

  // 설명 관련 코드
  const defaultDescription = ref("");
  const newDescription = ref("");

  // 저장소 탭 > 설명 수정 API호출 함수
  const updateRepositoryDescriptionAPI = async (
    patchData: JsonPatchOperation[],
  ) => {
    const serviceType: string = ["MINIO"].includes(
      selectedServiceType.toUpperCase(),
    )
      ? "repository/storage"
      : "repository";
    const { result, data } = await $api(
      `/api/service-manage/${serviceType}/description/${selectedID}`,
      {
        method: "PATCH",
        body: patchData,
      },
    ).catch((error) => {
      console.error("API 호출 중 오류 발생: ", error);
      serviceData.value = defaultDescription;
    });

    if (result === 1) {
      $alert("수정이 완료되었습니다.", "success");
      service.description = data.description;
    } else {
      $alert("수정에 실패하였습니다.", "error");
      service.description = defaultDescription.value;
    }
  };

  const getDBServiceList = async () => {
    isDoneRepoAPI.value = true;
    const uri: string = ["MINIO"].includes(selectedServiceType.toUpperCase())
      ? "storage-services"
      : "database-services";

    const result: any = await $api(
      `/api/service-manage/${uri}/${selectedName}`,
      { showLoader: false },
    );
    DBServiceListData.value = result.data;

    isDoneRepoAPI.value = false;

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
   * 서비스 리스트 갱신
   */
  async function getServiceList(): Promise<void> {
    const { data } = await $api(`/api/service-manage/list`);
    const serviceListData: Service[] = data;
    serviceList.splice(0, serviceList.length, ...serviceListData);

    // 첫번째 항목 자동 선택
    if (Object.keys(service).length === 0) {
      await changeCurrentService(serviceListData[0]);
    }
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
      await changeCurrentService(serviceData);
    }
  }

  /**
   * 현재 서비스 엔티티 변경
   * @param source
   */
  async function changeCurrentService(source: Service): Promise<void> {
    // 서비스 조회
    const newService = await getServiceInfo(source);

    isDescEditable.value = false;
    if (!source.owner) {
      source.owner = [];
    }
    Object.assign(service, source, newService);
    disableEditInfo();
    // 서비스관리 목록 클릭시, 설명 API호출
    if (
      !_.isEmpty(newService.fullyQualifiedName) &&
      !_.isUndefined(newService.fullyQualifiedName)
    ) {
      selectedFqn = newService.fullyQualifiedName;
      selectedID = newService.id;
      selectedName = newService.name;
      selectedServiceType = newService.serviceType;
      defaultDescription.value = "";
      newDescription.value = newService.description;
    }
  }

  const getServiceInfo = async ({ type, name }) => {
    const { data } = await $api(
      `/api/service-manage/detail/${type}/${name}`,
      {},
    );
    return data;
  };

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
      $alert(res.errorMessage, "error");
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
      $alert(res.errorMessage, "error");
    }
  }

  /**
   * 유저 리스트
   */
  async function getUserList(useEmptyOption: boolean = false): Promise<void> {
    const res = await $api(`/api/user/all`);

    if (useEmptyOption) {
      res.data.unshift({
        id: "EMPTY_USER",
        name: "소유자 없음",
        type: "",
        displayName: "사용자 없음",
        fullyQualifiedName: "EMPTY_USER",
      });
    }

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

    if (foundUser !== null && foundUser !== undefined) {
      // 바꾸려는 값이 'admin' 인 경우, admin 은 type 이 'admin'으로 되어있음.
      // open meta 에 type 을 'admin' 으로 날리면 서버 오류가 남.
      foundUser.type = "user";
    }

    const isEmpty = item.id === undefined || item.id === "EMPTY_USER";
    if (service.owner === null || service.owner.id === undefined) {
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
    const latestPipelineState: string = res.data[0].pipelineState;
    setIngestionStatus(name, latestPipelineState);
  }

  function setIngestionStatus(
    fullyQualifiedName: string,
    pipelineState: string,
  ) {
    const index = ingestionList.findIndex(
      (ingestion: Ingestion) =>
        ingestion.fullyQualifiedName === fullyQualifiedName,
    );
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

  function getIngestionPipelineStatus() {
    return $api(`/api/service-manage/ingestionPipelines/status`);
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
    editInfo.description = false;
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

  watch(
    () => tab.value,
    (value) => {
      if (value === "repository") {
        // repository 저장소 탭을 누를때만 조회한다 (전체조회에 포함하지 않음)
        getDBServiceList();
      }
    },
  );

  return {
    tab,
    servicesById,
    servicesWithTrinoById,
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
    getIngestionPipelineStatus,

    changeEditInfo,
    disableEditInfo,

    getUserList,
    createOwnerOperation,

    serviceData,
    DBServiceListData,
    updateRepositoryDescriptionAPI,
    getDBServiceList,

    testConnectionStatus,
    connectionInfo,
    viewConnectionInfo,
    getConnectionInfo,

    isDescEditable,
    isDoneRepoAPI,

    defaultDescription,
    newDescription,
  };
});
