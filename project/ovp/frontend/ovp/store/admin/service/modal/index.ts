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

  const checkServiceNameDuplicate = async () => {
    // TODO : [개발] 서비스명 기준 중복 조회 API 구현
    // $api("");
    await $api("");
    return [];
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
