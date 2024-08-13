import { useServiceStore } from "~/store/service";
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
  isServiceNameDuplicate(): Promise<void>;
}

export function ServiceAddModalComposition(
  props: ServiceAddModalProps,
): ServiceAddModalComposition {
  const serviceStore = useServiceStore();
  const { serviceObj, selectedServiceObj } = storeToRefs(serviceStore);
  const { resetServiceObj, checkServiceNameDuplicate } = serviceStore;

  // coomon
  const resetInput = (serviceObjPath: string) => {
    _.set(serviceObj.value, serviceObjPath, "");
  };
  const resetData = () => {
    resetServiceObj();
  };

  // step1
  const serviceImgClick = (service: IService) => {
    serviceObj.value.serviceId = service.id;
    selectedServiceObj.value = service;
  };

  // step2
  const isServiceNameDuplicate = async () => {
    return await checkServiceNameDuplicate();
  };

  return {
    ...props,
    serviceObj,
    selectedServiceObj,
    resetInput,
    resetData,
    serviceImgClick,
    isServiceNameDuplicate,
  };
}
