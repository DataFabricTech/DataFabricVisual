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

export const useServiceCollectionLogStore = defineStore(
  "service_collection_log",
  () => {
    const { $api } = useNuxtApp();

    const collectionLogData: Ref<object> = ref({});
    const serviceData: Ref<ServiceData> = ref({ description: "" });

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

    let serviceId = "";
    const serviceFullID: string = "e2e0484e-6985-4083-a244-698700c6b189";

    const setServiceId = (id: string) => {
      serviceId = id;
    };

    const getCollectionLogData = async () => {
      const data: any = await $api(
        `/api/service-manage/collection/log/${serviceId}`,
      );

      collectionLogData.value = data.data;
    };

    // getRepositoryDescriptionAPI의 params 생성함수
    const getQueryData = () => {
      const params = {
        fields: "owner,tags,dataProducts,domain",
        include: "all",
      };
      return new URLSearchParams(params);
    };

    // 저장소 탭 > 설명 조회 API호출 함수
    const getRepositoryDescriptionAPI = async (fqn: string) => {
      const { data }: any = await $api(
        `/api/service-manage/repository/description/${fqn}?${getQueryData()}`,
      );
      serviceData.value = { description: data.description };
    };

    // 저장소 탭 > 설명 수정 API호출 함수
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

    return {
      serviceData,
      DBServiceListData,
      setServiceId,
      collectionLogData,
      getCollectionLogData,
      getRepositoryDescriptionAPI,
      updateRepositoryDescriptionAPI,
      getDBServiceList,
    };
  },
);
