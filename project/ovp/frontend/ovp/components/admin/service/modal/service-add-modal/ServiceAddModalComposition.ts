import { storeToRefs } from "pinia";
import { useServiceStore } from "~/store/admin/service/modal";
import _ from "lodash";

import type {
  IService,
  IServiceObj,
  ServiceAddModalProps,
} from "./ServiceAddModalProps";

export enum PanelTypes {
  INPUT = "INPUT",
  INPUT_PWD = "INPUT_PWD",
  SELECT = "SELECT",
}

export interface ServiceAddModalComposition extends ServiceAddModalProps {
  serviceObj: Ref<IServiceObj>;
  selectedServiceObj: Ref<IService>;

  setValue(serviceObjPath: string, value: any): void;
  resetInput(serviceObjPath: string): void;
  resetData(): void;

  serviceImgClick(service: IService): void;
  setDefaultServiceId(serviceId: string): void;

  isServiceNameDuplicate(): Promise<boolean>;
}

export function ServiceAddModalComposition(
  props: ServiceAddModalProps,
): ServiceAddModalComposition {
  const serviceStore = useServiceStore();
  const { serviceObj, selectedServiceObj } = storeToRefs(serviceStore);
  const { setInitServiceId, resetServiceObj, checkServiceNameDuplicate } =
    serviceStore;

  // coomon
  const resetInput = (serviceObjPath: string) => {
    setValue(serviceObjPath, "");
  };
  const setValue = (serviceObjPath: string, value: any) => {
    _.set(serviceObj.value, serviceObjPath, value);
  };

  const resetData = () => {
    resetServiceObj();
  };

  // step1
  const serviceImgClick = (service: IService) => {
    // serviceObj에 저장된 값들 다 날려야함. (service 1 선택해서 값 입력하고, step1 로 돌아와서 다시 작업한 경우
    resetServiceObj();

    serviceObj.value.serviceId = service.id;
    selectedServiceObj.value = service;
  };
  const setDefaultServiceId = (serviceId: string) => {
    // 첫번째 serviceId 값을 저장해둔다.
    setInitServiceId(serviceId);
  };

  // step2
  const isServiceNameDuplicate = async () => {
    const result = await checkServiceNameDuplicate();
    console.log(result);
    // return result.length > 0;
    return false;
  };

  return {
    ...props,
    serviceObj,
    selectedServiceObj,
    setValue,
    resetInput,
    resetData,
    serviceImgClick,
    setDefaultServiceId,
    isServiceNameDuplicate,
  };
}
