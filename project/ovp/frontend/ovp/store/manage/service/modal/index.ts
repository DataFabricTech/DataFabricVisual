import type {
  IService,
  IServiceObj,
} from "~/components/manage/service/modal/modal-service/ModalServiceProps";
import {
  ConnectionStatus,
  ServiceIds,
} from "~/components/manage/service/modal/modal-service/ModalServiceComposition";
import _ from "lodash";
import type { Ref } from "vue";

import { storeToRefs } from "pinia";
import { useUserStore } from "~/store/user/userStore";

export const useServiceStore = defineStore("serviceStore", () => {
  const { $api } = useNuxtApp();

  const userStore = useUserStore();
  const { user } = storeToRefs(userStore);

  // 리셋하기 위해 초기상태 저장
  const initialServiceObj: IServiceObj = {
    serviceId: "",
    defaultInfo: {
      serviceNm: "",
      serviceDesc: "",
    },
    detailInfo: {},
  };

  // 비밀번호 표시/미표시 처리 변수. 페이지 전환시에 리셋해주기 위해 store 에서 처리한다.
  const openEyeValues = ref<string[]>([]);

  const serviceObj = ref<IServiceObj>(_.cloneDeep(initialServiceObj));

  const selectedServiceObj = ref<IService>({
    id: "",
    label: "",
    img: "",
    imgUrl: "",
    isDisabled: false,
  });

  const currentStep = ref<number>(1);
  const inValidMsg = ref<string>("");
  const isValid = ref<boolean>(true);

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

  const setValue = (serviceObjPath: string, value: any) => {
    _.set(serviceObj.value, serviceObjPath, value);
  };

  const isDoneTestConnection = ref<boolean | null>(null);
  const testConnectionStatus: Ref<ConnectionStatus> = ref(
    ConnectionStatus.NONE,
  );

  const checkServiceNameDuplicate = async () => {
    const { data } = await $api(
      `/api/service-manage/isDuplicatedNm?${getQueryString()}`,
    );
    return data;
  };

  const setInitServiceId = (serviceId: string) => {
    initialServiceObj.serviceId = serviceId;
  };

  const addIfExists = (obj: Record<string, any>, key: string, value: any) => {
    if (value) {
      obj[key] = value;
    }
  };
  const getConfig = (serviceId: string) => {
    const serviceObjData = serviceObj.value.detailInfo;

    const connectionArgumentsParam = _.has(
      serviceObj.value.detailInfo,
      "connectionArguments",
    )
      ? {
          connectionArguments: serviceObjData.connectionArguments.reduce(
            (
              acc: { [key: string]: string },
              [key, value]: [string, string],
            ) => {
              acc[key] = value;
              return acc;
            },
            {} as { [key: string]: string },
          ),
        }
      : {};

    const connectionOptionsParam = _.has(
      serviceObj.value.detailInfo,
      "connectionOptions",
    )
      ? {
          connectionOptions: serviceObjData.connectionOptions.reduce(
            (
              acc: { [key: string]: string },
              [key, value]: [string, string],
            ) => {
              acc[key] = value;
              return acc;
            },
            {} as { [key: string]: string },
          ),
        }
      : {};

    let specificConfig: Record<string, any> = {};

    switch (serviceId) {
      case ServiceIds.MINIO: {
        const minioConfig: Record<string, any> = {};
        addIfExists(minioConfig, "accessKeyId", serviceObjData.accessKeyId);
        addIfExists(minioConfig, "secretKey", serviceObjData.secretKey);
        addIfExists(minioConfig, "sessionToken", serviceObjData.sessionToken);
        addIfExists(minioConfig, "region", serviceObjData.region);
        addIfExists(minioConfig, "endPointURL", serviceObjData.endPointURL);

        specificConfig = {
          bucketNames: (serviceObjData.bucketNames as Array<string[]>).reduce<
            string[]
          >((acc, item) => {
            if (Array.isArray(item) && item.length > 0 && item[0] !== "") {
              acc.push(item[0]);
            }
            return acc;
          }, []),
          minioConfig,
          supportsStorageProfiler: true,
        };

        return {
          ...specificConfig,
          ...connectionArgumentsParam,
          ...connectionOptionsParam,
        };
      }

      case ServiceIds.MARIA_DB:
        addIfExists(specificConfig, "username", serviceObjData.username);
        addIfExists(specificConfig, "password", serviceObjData.password);
        addIfExists(specificConfig, "hostPort", serviceObjData.hostAndPort);
        addIfExists(
          specificConfig,
          "databaseName",
          serviceObjData.databaseName,
        );
        addIfExists(
          specificConfig,
          "databaseSchema",
          serviceObjData.databaseSchema,
        );
        break;

      case ServiceIds.MYSQL: {
        addIfExists(specificConfig, "hostPort", serviceObjData.hostAndPort);
        addIfExists(specificConfig, "username", serviceObjData.username);
        addIfExists(
          specificConfig,
          "databaseName",
          serviceObjData.databaseName,
        );
        addIfExists(
          specificConfig,
          "databaseSchema",
          serviceObjData.databaseSchema,
        );

        if (serviceObjData.password) {
          specificConfig.authType = { password: serviceObjData.password };
        }
        break;
      }

      case ServiceIds.POSTGRESQL: {
        specificConfig = {
          classificationName: "postgresPolicyTags",
          ingestAllDatabases: true,
          sslMode: "disable",
        };

        if (serviceObjData.password) {
          specificConfig.authType = { password: serviceObjData.password };
        }

        addIfExists(specificConfig, "database", serviceObjData.database);
        addIfExists(specificConfig, "hostPort", serviceObjData.hostAndPort);
        addIfExists(specificConfig, "username", serviceObjData.username);
        break;
      }

      case ServiceIds.ORACLE: {
        const oracleConnectionType: Record<string, any> = {};
        if (serviceObjData.oracleConnectionType) {
          oracleConnectionType[serviceObjData.oracleConnectionType] =
            serviceObjData[serviceObjData.oracleConnectionType];
        }

        specificConfig = {
          oracleConnectionType,
          instantClientDirectory: "/instantclient",
        };
        addIfExists(specificConfig, "username", serviceObjData.username);
        addIfExists(specificConfig, "password", serviceObjData.password);
        addIfExists(specificConfig, "hostPort", serviceObjData.hostAndPort);
        break;
      }

      default:
        // 예상치 못한 serviceId에 대한 처리
        console.warn(`Unknown serviceId: ${serviceId}`);
        break;
    }

    return {
      ...specificConfig,
      ...connectionArgumentsParam,
      ...connectionOptionsParam,
    };
  };

  const getDefaultParam = () => {
    const serviceId = serviceObj.value.serviceId;
    return {
      connection: {
        config: getConfig(serviceId),
      },
    };
  };
  const getContentType = () => {
    return serviceObj.value.serviceId === ServiceIds.MINIO
      ? "Storage"
      : "Database";
  };
  const getParams = () => {
    const serviceId = serviceObj.value.serviceId;
    return {
      ...getDefaultParam(),
      ...{
        serviceType: getContentType(),
      },
      ...{ connectionType: serviceId },
    };
  };
  const getSubmitParams = () => {
    return {
      ...getDefaultParam(),
      ...{
        serviceType: serviceObj.value.serviceId,
        name: serviceObj.value.defaultInfo.serviceNm,
        description: serviceObj.value.defaultInfo.serviceDesc,
      },
      ...{
        owner: {
          id: user.value.id,
          type: "user",
        },
      },
    };
  };
  const connectionTest = async () => {
    const { result, errorMessage, data } = await $api(
      "/api/service-manage/connectionTest",
      {
        showLoader: false,
        method: "POST",
        body: getParams(),
      },
    );
    let workflowId = "";

    let errMsg = null;
    if (result === 0 && errorMessage) {
      errMsg = getErrorMessage(errorMessage);
    } else {
      workflowId = data.workflowId;
      if (data.responseStatus.status === "Failed") {
        errMsg = data.responseStatus.steps
          .filter(
            (step: any) => step.mandatory === true && step.passed === false,
          )
          .map((step: any) => `[${step.name}] ${step.message}`)[0];
      } else {
        errMsg = "";
      }
    }

    isDoneTestConnection.value = _.isEmpty(errMsg);
    return {
      result: _.isEmpty(errMsg),
      errorMessage: errMsg,
      workflowId: workflowId,
    };
  };
  const getErrorMessage = (str: string): string | undefined => {
    return str.split("]: ").pop();
  };

  const submit = async (): Promise<boolean> => {
    const { result } = await $api(
      `/api/service-manage/save/${getContentType()}`,
      {
        method: "POST",
        body: getSubmitParams(),
      },
    );
    if (result < 1) {
      // TODO : [개발] notification 개발 완료 되면 아래 코드 변경
      alert("저장 실패했습니다. 잠시 후 다시 시도해주세요.");
    }

    return result > 0;
  };

  return {
    currentStep,
    inValidMsg,
    isValid,
    serviceObj,
    selectedServiceObj,
    isDoneTestConnection,
    testConnectionStatus,
    openEyeValues,
    setValue,
    setInitServiceId,
    resetServiceObj,
    checkServiceNameDuplicate,
    connectionTest,
    submit,
  };
});
