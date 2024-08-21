import { defineStore } from "pinia";
import type { Ref } from "vue";
import { ref } from "vue";

interface JsonPatchOperation {
  op: string;
  path: string;
  value: any;
}

interface ServiceData {
  description: string;
}

export const useServiceCollectionLogStore = defineStore(
  "service_collection_log",
  () => {
    const { $api } = useNuxtApp();

    const collectionLogData: Ref<object> = ref({});
    const serviceData: Ref<ServiceData> = ref({ description: "" });
    let serviceId = "";
    const serviceFullID: string = "e2e0484e-6985-4083-a244-698700c6b189";
    let serviceFullNAME: string = "";

    const setServiceId = (id: string) => {
      serviceId = id;
    };

    const getCollectionLogData = async () => {
      const data: any = await $api(
        `/api/service-manage/collection/log/${serviceId}`,
      );

      collectionLogData.value = data.data;
    };

    const setServiceName = (name: string) => {
      serviceFullNAME = name;
    };

    const getRepositoryDescriptionAPI = async () => {
      if (serviceFullNAME.length === 0) {
        // 처음 로드시 , 첫번째 서비스목록 항목의 name값이 들어가도록 설정.
        serviceFullNAME = "test_df3"; // 기본값 주입
      }
      // TODO : 서버 만들고 주입할 예정
      let data: any = await $api(
        `/api/v1/services/databaseServices/name/${serviceFullNAME}`,
      );
      // TODO : 예시 데이터 설정 (실제 API 호출 결과로 제거 예정)
      data = {
        description: "기존 서버 description값",
      };

      serviceData.value = { description: data.description };
    };

    const updateRepositoryDescriptionAPI = async (
      patchData: JsonPatchOperation[],
    ) => {
      let result = await $api(
        `/api/v1/services/databaseServices/${serviceFullID}`,
        {
          method: "PATCH",
          headers: {
            "Content-Type": "application/json-patch+json",
          },
          body: JSON.stringify(patchData),
        },
      );
      // TODO : 예시 데이터 설정 (서버 개발후, 제거 예정)
      result = {
        result: 1,
        description: "수정된 description 값",
      };

      // 수정 후 설명 데이터 재조회
      await getRepositoryDescriptionAPI();

      return result;
    };

    return {
      serviceData,
      setServiceId,
      setServiceName,
      collectionLogData,
      getCollectionLogData,
      getRepositoryDescriptionAPI,
      updateRepositoryDescriptionAPI,
    };
  },
);
