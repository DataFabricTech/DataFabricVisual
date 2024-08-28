import { defineStore } from "pinia";
import { ref, reactive } from "vue";
import type { Service, Owner, Ingestion } from "~/type/service";
import type { JsonPatchOperation } from "~/type/common";
import type { MenuSearchItemImpl } from "@extends/menu-seach/MenuSearchComposition";
import $constants from "~/utils/constant";

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
    if (source.owner) {
      source.owner = {
        id: source.owner.id,
        name: source.owner.name,
      };
    } else {
      source.owner = [];
    }
    Object.assign(service, source);
    disableEditInfo();
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
  async function getIngestionList(service: string) {
    const res = await $api(
      `/api/service-manage/ingestion/list?service=${service}`,
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

  return {
    tab,
    service,
    serviceList,
    userList,
    userSearchList,

    ingestionList,
    editInfo,
    changeTab,

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
  };
});
