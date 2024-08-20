import type {
  IService,
  IServiceObj,
} from "~/components/admin/service/modal/service-add-modal/ServiceAddModalProps";
import _ from "lodash";

export const useServiceStore = defineStore("serviceStore", () => {
  const { $api } = useNuxtApp();

  // 리셋하기 위해 초기상태 저장
  const initialServiceObj: IServiceObj = {
    serviceId: "",
    defaultInfo: {
      serviceNm: "",
      serviceDesc: "",
    },
    detailInfo: {},
    addedList: [],
  };
  const serviceObj = ref<IServiceObj>(_.cloneDeep(initialServiceObj));

  const selectedServiceObj = ref<IService>({
    id: "",
    label: "",
    img: "",
    imgUrl: "",
    isDisabled: false,
  });

  const resetServiceObj = () => {
    // serviceId 는 리셋하지 않음.
    serviceObj.value = _.cloneDeep(initialServiceObj);
  };

  const getQueryString = () => {
    return new URLSearchParams({
      index: selectedServiceObj.value.id,
      // eslint-disable-next-line id-length
      q: serviceObj.value.defaultInfo.serviceNm,
      include_source_fields: "displayName",
      from: "0",
      size: "1",
      query_filter: JSON.stringify({
        query: {
          bool: {
            filter: [
              {
                term: {
                  "displayName.keyword": serviceObj.value.defaultInfo.serviceNm,
                },
              },
            ],
          },
        },
      }),
    });
  };

  const checkServiceNameDuplicate = async () => {
    const { data } = await $api(
      `/api/service/isDuplicatedNm?${getQueryString()}`,
    );
    return data;
  };

  const setInitServiceId = (serviceId: string) => {
    initialServiceObj.serviceId = serviceId;
  };

  return {
    serviceObj,
    selectedServiceObj,
    setInitServiceId,
    resetServiceObj,
    checkServiceNameDuplicate,
  };
});
