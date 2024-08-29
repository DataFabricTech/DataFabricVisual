import { defineStore } from "pinia";
import { ref, reactive, Ref } from "vue";
import type { Service, Owner, Ingestion } from "~/type/service";
import type { JsonPatchOperation } from "~/type/common";
import type { MenuSearchItemImpl } from "@extends/menu-seach/MenuSearchComposition";
import $constants from "~/utils/constant";
import _ from "lodash";

interface JsonPatchOperation {
  op: string;
  path: string;
  value: any;
}

interface ServiceData {
  description: string;
}

interface DBServiceListData {
  owner: string | undefined;
  fqn: string;
  name: string;
  id: string;
  type: string;
  desc: string | undefined;
}

export const useServiceStore = defineStore("service", () => {
  const { $api } = useNuxtApp();

  const tab = ref<string>("repository");

  const service = reactive<Service>(<Service>{});
  const serviceList = reactive<Service[]>([]);

  const userList = reactive<Owner[]>([]);
  const userSearchList = reactive<object[]>([]);

  const ingestionList = reactive<Ingestion[]>([]);

  const editInfo = reactive({
    owner: false,
    tag: false,
    term: false,
  });

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
      `/api/service-manage/list/search?search=*${keyword}*&from=${from}`,
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
    await $api(`/api/service-manage/${id}?hardDelete=true&recursive=true`, {
      method: "DELETE",
    });
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

  /**
   * 서비스 수정
   * @param id
   * @param body
   */
  async function updateService(
    id: string,
    body: JsonPatchOperation[],
  ): Promise<void> {
    const res = await $api(`/api/service-manage/${id}`, {
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
    const foundUser = userList.find((user: Owner) => user.id === item.id);

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
  async function getIngestionList() {
    const res = await $api(`/api/service-manage/ingestion/list`);

    if (res.data !== null) {
      ingestionList.splice(0, ingestionList.length, ...res.data);
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

  return {
    tab,
    service,
    serviceList,
    userList,
    userSearchList,

    editInfo,
    changeTab,

    getServiceList,
    searchServiceList,

    changeCurrentService,
    deleteService,
    updateService,
    emptyService,

    changeEditInfo,
    disableEditInfo,

    getUserList,
    createOwnerOperation,

    serviceData,
    DBServiceListData,
    getRepositoryDescriptionAPI,
    updateRepositoryDescriptionAPI,
    getDBServiceList,
  };
});
