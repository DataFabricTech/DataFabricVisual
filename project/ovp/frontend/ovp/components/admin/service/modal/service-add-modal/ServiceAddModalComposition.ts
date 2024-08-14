import { useServiceStore } from "~/store/admin/service/modal";
import _ from "lodash";

import type {
  IService,
  IServiceObj,
  ServiceAddModalProps,
} from "./ServiceAddModalProps";

export interface ServiceAddModalComposition extends ServiceAddModalProps {
  serviceObj: Ref<IServiceObj>;
  selectedServiceObj: Ref<IService>;
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

  enum PanelTypes {
    INPUT = "INPUT",
    INPUT_PWD = "INPUT_PWD",
    SELECT = "SELECT",
  }

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
    PanelTypes,
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
