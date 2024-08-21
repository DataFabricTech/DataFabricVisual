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
export enum ServiceIds {
  MINIO = "MinIO",
  MYSQL = "Mysql",
  MARIA_DB = "MariaDB",
  POSTGRESQL = "Postgres",
  ORACLE = "Oracle",
}

export interface ServiceAddModalComposition extends ServiceAddModalProps {
  services: Ref<IService[]>;
  serviceDetailFormObj: any; // 반응성 변수로 지정할 필요 없는 항목.
  serviceObj: Ref<IServiceObj>;
  selectedServiceObj: Ref<IService>;
  connectionTestStatus: Ref<boolean | null>;

  setValue(serviceObjPath: string, value: any): void;
  resetInput(serviceObjPath: string): void;
  resetServiceObj(): void;

  serviceImgClick(service: IService): void;
  setDefaultServiceId(serviceId: string): void;

  checkServiceNameDuplicate(): Promise<boolean>;
  checkRequiredValue(): boolean;
  connectionTest(): Promise<string>;

  submit(): void;
}

export function ServiceAddModalComposition(
  props: ServiceAddModalProps,
): ServiceAddModalComposition {
  const serviceStore = useServiceStore();
  const { serviceObj, selectedServiceObj, connectionTestStatus } =
    storeToRefs(serviceStore);
  const {
    setValue,
    setInitServiceId,
    resetServiceObj,
    checkServiceNameDuplicate,
    connectionTest,
    submit,
  } = serviceStore;

  const services = ref<IService[]>([
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
  ]);
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
            {
              id: "authConfigurationType",
              label: "Auth Configuration Type",
              type: PanelTypes.SELECT,
              options: [
                {
                  label: "Basic Auth",
                  value: "basicAuth",
                },
              ],
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
            {
              id: "authConfigurationType",
              label: "Auth Configuration Type",
              type: PanelTypes.SELECT,
              options: [
                {
                  value: "basicAuth",
                  label: "Basic Auth",
                },
              ],
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
            {
              id: "classificationName",
              label: "Classification Name",
              type: PanelTypes.INPUT,
              defaultValue: "PostgresPolicyTags",
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
            {
              id: "instantClientDirectory",
              label: "Oracle Instant Client Directory",
              defaultValue: "/instantclient",
              type: PanelTypes.INPUT,
              required: true,
            },
          ],
        },
      ],
    },
  };

  // 이미지 URL 동적 셋팅
  services.value.forEach(async (service: any) => {
    const imgUrl: any = await import(`@assetsPublic/images/${service.img}.png`);
    service.imgUrl = imgUrl.default;
  });

  // coomon
  const resetInput = (serviceObjPath: string) => {
    setValue(serviceObjPath, "");
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

  // step3
  const getRequiredIds = (key: string): string[] => {
    const service = serviceDetailFormObj[key];
    if (!service) {
      return [];
    }
    const defaultIds = service.defaultItems.flatMap((group: any) =>
      group.items
        .filter((item: any) => item.required)
        .filter(
          (item: any) =>
            // condition 처리한 항목의 required 값을 별도로 처리한다.
            _.has(item, "condition") &&
            item.condition.id !==
              serviceObj.value.detailInfo[item.condition.id],
        )
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

  const checkRequiredValue = (): boolean => {
    const requiredItems = getRequiredIds(serviceObj.value.serviceId);
    for (const itemId of requiredItems) {
      const value = serviceObj.value.detailInfo[itemId];
      if (Array.isArray(value)) {
        for (const innerItem of value) {
          if (_.isEmpty(innerItem)) {
            return false;
          }
        }
      } else {
        if (_.isEmpty(value)) {
          return false;
        }
      }
    }
    return true;
  };

  return {
    ...props,
    services,
    serviceObj,
    selectedServiceObj,
    serviceDetailFormObj,
    connectionTestStatus,
    setValue,
    resetInput,
    resetServiceObj,
    serviceImgClick,
    setDefaultServiceId,
    checkServiceNameDuplicate,
    checkRequiredValue,
    connectionTest,
    submit,
  };
}
