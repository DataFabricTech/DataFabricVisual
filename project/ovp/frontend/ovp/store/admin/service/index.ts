import { defineStore } from "pinia";
import { ref, reactive } from "vue";
import type { Service, Owner } from "~/type/service";
import type { JsonPatchOperation } from "~/type/common";
import type { MenuSearchItemImpl } from "@extends/menu-seach/MenuSearchComposition";
import $constants from "~/utils/constant";

export const useServiceStore = defineStore("service", () => {
  const { $api } = useNuxtApp();

  const tab = ref<string>("repository");

  const service = reactive<Service>(<Service>{});
  const serviceList = reactive<Service[]>([]);

  const userList = reactive<Owner[]>([
    {
      id: "1",
      type: "admin",
      name: "Alice Smith",
      fullyQualifiedName: "com.example.users.AliceSmith",
      displayName: "Alice",
    },
    {
      id: "2",
      type: "editor",
      name: "Bob Johnson",
      fullyQualifiedName: "com.example.users.BobJohnson",
      displayName: "Bob",
    },
    {
      id: "3",
      type: "viewer",
      name: "Carol White",
      fullyQualifiedName: "com.example.users.CarolWhite",
      displayName: "Carol",
    },
  ]);

  const editInfo = reactive({
    owner: false,
    tag: false,
    term: false,
  });

  async function changeTab(value: string) {
    tab.value = value;
  }

  async function getServiceList(): Promise<void> {
    const res = await $api(`/api/admin/service/list`);
    const serviceListData: Service[] = res.data;
    serviceList.splice(0, serviceList.length, ...serviceListData);

    const serviceData: Service =
      Object.keys(service).length === 0 ? serviceListData[0] : service;
    changeCurrentService(serviceData);
  }

  async function searchServiceList(keyword: string): Promise<void> {
    const res = await $api(
      `/api/admin/service/search/list?search=*${keyword}*`,
    );
    if (res.data !== null) {
      serviceList.splice(0, serviceList.length, ...res.data);
    }
  }

  function changeCurrentService(source: Service): void {
    Object.assign(service, source);
  }

  async function deleteService(id: string): Promise<void> {
    await $api(`/api/admin/service/${id}`, {
      method: "DELETE",
    });
    Object.keys(service).forEach((key: string) => {
      delete service[key];
    });
  }

  async function updateService(
    id: string,
    body: JsonPatchOperation[],
  ): Promise<void> {
    await $api(`/api/admin/service/${id}`, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json-patch+json",
      },
      body: body,
    });
  }

  async function getUserList(): Promise<void> {
    const res = await $api(`/api/admin/service/user/list`);
    if (res.data !== null) {
      userList.splice(0, userList.length, ...res.data);
    }
  }

  function createOwnerOperation(
    item: MenuSearchItemImpl,
  ): JsonPatchOperation[] {
    const operations: JsonPatchOperation[] = [];
    const foundUser = userList.find((user: Owner) => user.id === item.id);

    if (!service.owner) {
      if (item && foundUser) {
        operations.push({
          op: "add",
          path: "/owner",
          value: foundUser,
        });
      }
    } else {
      if (item) {
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

    editInfo,
    changeTab,

    getServiceList,
    searchServiceList,

    changeCurrentService,
    deleteService,
    updateService,

    changeEditInfo,
    disableEditInfo,

    getUserList,
    createOwnerOperation,
  };
});
