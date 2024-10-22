import { storeToRefs } from "pinia";
import { useServiceStore } from "@/store/manage/service/modal";
import _ from "lodash";

import type {
  IService,
  IServiceObj,
  ModalServiceProps,
} from "./ModalServiceProps";
import type { Ref } from "vue";

export enum PanelTypes {
  INPUT = "INPUT",
  INPUT_PWD = "INPUT_PWD",
  SELECT = "SELECT",
}

export enum ServiceIds {
  MINIO = "MinIO",
  MYSQL = "Mysql",
  MARIA_DB = "MariaDB",
  POSTGRESQL = "Postgres",
  ORACLE = "Oracle",
  TRINO = "Trino",
}

export enum ConnectionStatus {
  LOADING = "LOADING",
  SUCCESS = "SUCCESS",
  ERROR = "ERROR",
  NONE = "NONE",
}

export interface ModalServiceComposition extends ModalServiceProps {
  isAddMode: Ref<boolean>;
  currentStep: Ref<number>;
  isValid: Ref<boolean>;
  inValidMsg: Ref<string>;

  services: Ref<IService[]>;
  serviceDetailFormObj: any; // 반응성 변수로 지정할 필요 없는 항목.
  serviceObj: Ref<IServiceObj>;
  selectedServiceObj: Ref<IService>;
  isDoneTestConnection: Ref<boolean | null>;
  testConnectionStatus: Ref<ConnectionStatus>;
  openEyeValues: Ref<string[]>;

  setValue(serviceObjPath: string, value: any): void;

  resetInput(serviceObjPath: string): void;

  resetServiceObj(): void;

  newServiceIdSelect(service: IService): void;

  setDefaultServiceId(serviceId: string): void;

  checkServiceNameDuplicate(): Promise<boolean>;

  checkRequiredValue(): boolean;

  connectionTest(): Promise<any>;

  submit(): Promise<boolean>;

  checkValidation(type: string): Promise<boolean>;
}

export const services = ref<IService[]>([
  {
    id: ServiceIds.MINIO,
    label: "MinIO",
    img: "storage-type_06",
    imgUrl: "",
    isDisabled: false,
  },
  {
    id: ServiceIds.MYSQL,
    label: "MySQL",
    img: "storage-type_02",
    imgUrl: "",
    isDisabled: false,
  },
  {
    id: ServiceIds.MARIA_DB,
    label: "MariaDB",
    img: "storage-type_01",
    imgUrl: "",
    isDisabled: false,
  },
  {
    id: ServiceIds.POSTGRESQL,
    label: "PostgreSQL",
    img: "storage-type_03",
    imgUrl: "",
    isDisabled: false,
  },
  {
    id: ServiceIds.ORACLE,
    label: "Oracle",
    img: "storage-type_04",
    imgUrl: "",
  },
  {
    id: ServiceIds.TRINO,
    label: "Trino",
    img: "storage-type_07",
    imgUrl: "",
  },
]);

export function ModalServiceComposition(
  props: ModalServiceProps,
): ModalServiceComposition {
  const serviceStore = useServiceStore();
  const {
    isAddMode,
    currentStep,
    isValid,
    inValidMsg,
    serviceObj,
    selectedServiceObj,
    isDoneTestConnection,
    testConnectionStatus,
    openEyeValues,
  } = storeToRefs(serviceStore);
  const {
    setValue,
    setInitServiceId,
    resetServiceObj,
    checkServiceNameDuplicate,
    connectionTest,
    submit,
  } = serviceStore;

  const serviceDetailFormObj: any = {
    [ServiceIds.MINIO]: {
      defaultItems: [
        {
          title: "MinIO Credentials Configuration",
          items: [
            {
              id: "accessKeyId",
              label: "Access Key ID",
              type: PanelTypes.INPUT,
              required: true,
            },
            {
              id: "secretKey",
              label: "Secret Key",
              type: PanelTypes.INPUT_PWD,
              required: true,
            },
            {
              id: "sessionToken",
              label: "Session Token",
              type: PanelTypes.INPUT,
            },
            {
              id: "region",
              label: "Region",
              type: PanelTypes.INPUT,
            },
            {
              id: "endPointURL",
              label: "Endpoint URL",
              type: PanelTypes.INPUT,
              required: true,
            },
          ],
        },
      ],
      addableItems: {
        title: "Bucket Names",
        id: "bucketNames",
        required: true,
        items: [{ id: "key", type: PanelTypes.INPUT, showItemLabel: true }],
      },
    },
    [ServiceIds.MYSQL]: {
      defaultItems: [
        {
          title: "",
          items: [
            {
              id: "username",
              label: "Username",
              type: PanelTypes.INPUT,
              required: true,
            },
          ],
        },
        {
          title: "Basic Auth",
          items: [
            {
              id: "password",
              label: "Password",
              type: PanelTypes.INPUT_PWD,
              required: true,
            },
            {
              id: "hostAndPort",
              label: "Host and Port",
              type: PanelTypes.INPUT,
              required: true,
            },
            {
              id: "databaseName",
              label: "Database Name",
              type: PanelTypes.INPUT,
            },
            {
              id: "databaseSchema",
              label: "Database Schema",
              type: PanelTypes.INPUT,
            },
          ],
        },
      ],
    },
    [ServiceIds.POSTGRESQL]: {
      defaultItems: [
        {
          title: "",
          items: [
            {
              id: "username",
              label: "Username",
              type: PanelTypes.INPUT,
              required: true,
            },
          ],
        },
        {
          title: "Basic Auth",
          items: [
            {
              id: "password",
              label: "Password",
              type: PanelTypes.INPUT_PWD,
              required: true,
            },
            {
              id: "hostAndPort",
              label: "Host and Port",
              type: PanelTypes.INPUT,
              required: true,
            },
            {
              id: "database",
              label: "Database",
              type: PanelTypes.INPUT,
              required: true,
            },
          ],
        },
      ],
    },
    [ServiceIds.MARIA_DB]: {
      defaultItems: [
        {
          title: "",
          items: [
            {
              id: "username",
              label: "Username",
              type: PanelTypes.INPUT,
              required: true,
            },
            {
              id: "password",
              label: "Password",
              type: PanelTypes.INPUT_PWD,
              required: true,
            },
            {
              id: "hostAndPort",
              label: "Host and Port",
              type: PanelTypes.INPUT,
              required: true,
            },
            {
              id: "databaseName",
              label: "Database Name",
              type: PanelTypes.INPUT,
            },
            {
              id: "databaseSchema",
              label: "Database Schema",
              type: PanelTypes.INPUT,
            },
          ],
        },
      ],
    },
    [ServiceIds.ORACLE]: {
      defaultItems: [
        {
          title: "",
          items: [
            {
              id: "username",
              label: "Username",
              type: PanelTypes.INPUT,
              required: true,
            },
            {
              id: "password",
              label: "Password",
              type: PanelTypes.INPUT_PWD,
              required: true,
            },
            {
              id: "hostAndPort",
              label: "Host and Port",
              type: PanelTypes.INPUT,
              required: true,
            },
          ],
        },
        {
          title: "Oracle Connection Type",
          items: [
            {
              id: "oracleConnectionType",
              label: "",
              type: PanelTypes.SELECT,
              // 값변경시 다른 입력 panel 옵션 처리를 하기 위해 아래와 같이 설정함.
              hasCondition: true,
              conditionIds: [
                "databaseSchema",
                "oracleServiceName",
                "oracleTNSConnection",
              ],
              options: [
                {
                  label: "Database Schema",
                  value: "databaseSchema",
                },
                {
                  label: "Oracle Service Name",
                  value: "oracleServiceName",
                },
                {
                  label: "Oracle TNS Connection",
                  value: "oracleTNSConnection",
                },
              ],
            },
          ],
        },
        {
          title: "Database Schema",
          items: [
            {
              id: "databaseSchema",
              label: "Database Schema",
              type: PanelTypes.INPUT,
              required: true,
              condition: {
                class: "oracleConnectionType",
                id: "oracleConnectionType",
              },
            },
            {
              id: "oracleServiceName",
              label: "Oracle Service Name",
              type: PanelTypes.INPUT,
              required: true,
              defaultHide: true,
              condition: {
                class: "oracleConnectionType",
                id: "oracleConnectionType",
              },
            },
            {
              id: "oracleTNSConnection",
              label: "Oracle TNS Connection String",
              type: PanelTypes.INPUT,
              required: true,
              defaultHide: true,
              condition: {
                class: "oracleConnectionType",
                id: "oracleConnectionType",
              },
            },
          ],
        },
      ],
    },
  };

  // coomon
  const resetInput = (serviceObjPath: string) => {
    setValue(serviceObjPath, "");
  };

  const checkValidation = async (type: string): Promise<boolean> => {
    if (currentStep.value === 1) {
      // modal 진입시에 무조건 첫번째 값을 선택하게 되어있기 때문에 값이 없을수가 없음.
    } else if (currentStep.value === 2) {
      if (_.isEmpty(serviceObj.value.defaultInfo.serviceNm)) {
        isValid.value = false;
        inValidMsg.value = "필수 값을 입력해주세요.";
        return false;
      } else if (await checkServiceNameDuplicate()) {
        isValid.value = false;
        inValidMsg.value = "중복된 서비스 이름입니다.";
        return false;
      }
    } else if (currentStep.value === 3) {
      if (!checkRequiredValue()) {
        isValid.value = false;
        inValidMsg.value = "필수 값을 입력해주세요.";
        return false;
      }
      if (type === "submit" && _.isNull(isDoneTestConnection.value)) {
        isValid.value = false;
        inValidMsg.value = "연결 테스트를 수행해 주세요.";
        return false;
      }
      if (type === "submit" && !isDoneTestConnection.value) {
        isValid.value = false;
        inValidMsg.value = "연결 테스트를 다시 수행해 주세요.";
        return false;
      }
    }

    isValid.value = true;
    inValidMsg.value = "";
    return true;
  };

  // step1
  const newServiceIdSelect = (service: IService) => {
    // serviceObj에 저장된 값들 다 날려야함. (service 1 선택해서 값 입력하고, step1 로 돌아와서 다시 작업한 경우
    resetServiceObj();

    serviceObj.value.serviceId = service.id;
    selectedServiceObj.value = service;

    // 선택한 서비스의 기본값 항목이 있는 경우, serviceObj 에 해당값을 미리 반영해둔다.
    setDefaultValueItems();

    // 비밀번호 표시/미표시 정보를 리셋해준다.
    openEyeValues.value = [];
  };
  const setDefaultValueItems = () => {
    return serviceDetailFormObj[
      serviceObj.value.serviceId
    ].defaultItems.forEach((group: any) => {
      group.items.forEach((item: any) => {
        if (item.defaultValue !== undefined) {
          setValue(`detailInfo.${item.id}`, item.defaultValue);
        }
      });
    });
  };
  const setDefaultServiceId = (serviceId: string) => {
    // 첫번째 serviceId 값을 저장해둔다.
    setInitServiceId(serviceId);
  };

  // step2

  // step3
  /**
   * 필수 key 조회
   * @param key
   */
  const getRequiredIds = (key: string): string[] => {
    const service = serviceDetailFormObj[key];
    if (!service) {
      return [];
    }
    const defaultIds = service.defaultItems.flatMap((group: any) =>
      group.items
        .filter((item: any) => item.required)
        .filter((item: any) => {
          // condifiton 항목이 있는 데이터 처리
          return (
            !_.has(item, "condition") ||
            item.id === serviceObj.value.detailInfo[item.condition.id]
          );
        })
        .map((item: any) => item.id),
    );

    const addableIds = service.addableItems?.required
      ? [
          service.addableItems.id,
          ...service.addableItems.items
            .filter((item: any) => item.required)
            .map((item: any) => item.id),
        ]
      : [];
    return [...defaultIds, ...addableIds];
  };

  const checkDoubleArrayRequiredValue = (jsonObj: any): boolean => {
    // 값이 아예 없으면 true 를 return 한다
    if (_.isUndefined(jsonObj)) {
      return true;
    }

    return (
      Array.isArray(jsonObj) &&
      jsonObj.every(
        (item) =>
          Array.isArray(item) &&
          item.every((subItem) => subItem !== null && subItem !== ""),
      )
    );
  };
  const checkRequiredValue = (): boolean => {
    const requiredItems = getRequiredIds(serviceObj.value.serviceId);

    for (const itemId of requiredItems) {
      const value = serviceObj.value.detailInfo[itemId];

      // bucketNames 는 inputItem 을 추가한 경우에만 필수
      if (itemId === "bucketNames") {
        return checkDoubleArrayRequiredValue(value);
      }

      if (Array.isArray(value)) {
        if (_.isEmpty(value)) {
          return false;
        } else {
          for (const innerItem of value) {
            if (_.isEmpty(innerItem)) {
              return false;
            }
          }
        }
      } else {
        if (_.isEmpty(value)) {
          return false;
        }
      }
    }

    if (
      !checkDoubleArrayRequiredValue(
        serviceObj.value.detailInfo.connectionOptions,
      )
    ) {
      return false;
    }
    if (
      !checkDoubleArrayRequiredValue(
        serviceObj.value.detailInfo.connectionArguments,
      )
    ) {
      return false;
    }
    return true;
  };

  return {
    ...props,
    isAddMode,
    currentStep,
    isValid,
    inValidMsg,
    services,
    serviceObj,
    selectedServiceObj,
    serviceDetailFormObj,
    isDoneTestConnection,
    testConnectionStatus,
    openEyeValues,
    setValue,
    resetInput,
    resetServiceObj,
    newServiceIdSelect,
    setDefaultServiceId,
    checkServiceNameDuplicate,
    checkRequiredValue,
    connectionTest,
    submit,
    checkValidation,
  };
}
