import { defineStore } from "pinia";
import type { Ref } from "vue";

export const useServiceCollectionLogStore = defineStore(
  "service_collection_log",
  () => {
    const { $api } = useNuxtApp();

    const collectionLogData: Ref<object> = ref({});
    let serviceId = "";

    const setServiceId = (id: string) => {
      serviceId = id;
    };
    const getCollectionLogData = async () => {
      const data: any = await $api(`/api/services/collection/log/${serviceId}`);
      collectionLogData.value = data.data;
    };

    return { setServiceId, collectionLogData, getCollectionLogData };
  },
);
