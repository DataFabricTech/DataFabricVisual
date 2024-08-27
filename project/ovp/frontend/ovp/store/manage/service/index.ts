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
  serviceType: string;
  name: string;
  description: string | undefined;
  owner: {
    name: string | undefined;
  };
  id: string;
  fqn: string;
  type: string;
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
  const currentServiceManageID: string = "";

  const DBServiceListData: Ref<DBServiceListData[]> = ref([
    {
      serviceType: "serviceType1",
      name: "name1",
      description: "description1",
      owner: {
        name: "ownerName1",
      },
      type: "type1",
      id: "id1",
      fqn: "fqn1",
    },
  ]);

  // getRepositoryDescriptionAPI의 params 생성함수
  const getQueryData = () => {
    const params = {
      fields: "owner,tags,dataProducts,domain",
      include: "all",
    };
    return new URLSearchParams(params);
  };

  // 저장소 탭 > 설명 조회 API호출 함수
  const getRepositoryDescriptionAPI = async (fqn: string, id: string) => {
    // 선택하면 store의 serviceFullID값도 주입(수정 API에 쓰이기 위해서_)
    currentServiceManageID = id;
    const { data }: any = await $api(
      `/api/service-manage/repository/description/${fqn}?${getQueryData()}`,
    );
    serviceData.value = { description: data.description };
  };

  // 저장소 탭 > 설명 수정 API호출 함수
  const updateRepositoryDescriptionAPI = async (
    patchData: JsonPatchOperation[],
  ) => {
    console.log(
      "serviceFullID 수정할 때 필요한 아이디 값 ====> : ",
      currentServiceManageID,
    );
    const result = await $api(
      `/api/service-manage/repository/description/${currentServiceManageID}`,
      {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json-patch+json",
        },
        body: JSON.stringify(patchData),
      },
    );
    console.log("수정시 보내느 API ", result);
    // TODO : 수정 후 설명 데이터 재조회 >>> param 추가예정
    // await getRepositoryDescriptionAPI();

    return result;
  };

  const getDBServiceList = () => {
    // TODO : 가상 데이터 지정 > API생성 후 대체 예정
    // let result: any = await $api("api/v1/databases/");
    const result = {
      result: 1,
      data: [
        {
          serviceType: "serviceType1",
          name: "11이름",
          description: "설명1",
          owner: { name: "소유자1" },
          type: "table",
          id: "6ef96a3a-837b-40a5-9afe-a286e50e6ae4",
          fqn: "df2.test_db.test_db.Employee",
        },
        {
          serviceType: "serviceType2",
          name: "22이름",
          description: "설명2",
          owner: { name: "소유자2" },
          type: "table",
          id: "02ab816f-88e1-47df-bb87-926dcbcc20f0",
          fqn: "vdap2.sample.sample.sampledata_chart_02",
        },
      ],
    };
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
      getRepositoryDescriptionAPI(service.fullyQualifiedName, service.id);
    }
    // TODO : 아래 DB 테이블 조회도 호출필요 !!!---------------
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
    getRepositoryDescriptionAPI,
    updateRepositoryDescriptionAPI,
    getDBServiceList,
  };
});
