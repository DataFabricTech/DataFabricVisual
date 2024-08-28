import type {
  IService,
  IServiceObj,
} from "~/components/manage/service/modal/modal-service/ModalServiceProps";
import {
  ConnectionStatus,
  ServiceIds,
  services,
} from "~/components/manage/service/modal/modal-service/ModalServiceComposition";
import _ from "lodash";
import type { Ref } from "vue";
import { compare } from "fast-json-patch";

import { storeToRefs } from "pinia";
import { useUserStore } from "~/store/user/userStore";
import { useServiceStore as useDefaultServiceStore } from "../index";

const defaultSelectedServiceObj: IService = {
  id: "",
  label: "",
  img: "",
  imgUrl: "",
  isDisabled: false,
};

export const useServiceStore = defineStore("serviceStore", () => {
  const { $api } = useNuxtApp();

  const userStore = useUserStore();
  const { user } = storeToRefs(userStore);

  const defaultServiceStore = useDefaultServiceStore();
  const { service, connectionInfo } = storeToRefs(defaultServiceStore);

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

  const beforeServiceObj = ref<IServiceObj>(_.cloneDeep(initialServiceObj));
  const serviceObj = ref<IServiceObj>(_.cloneDeep(initialServiceObj));

  const selectedServiceObj = ref<IService>(defaultSelectedServiceObj);

  const isAddMode = ref<boolean>(true);
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
  const formatConfig = (purpose: string) => {
    const serviceObjData: Record<string, any> = {};
    const connectionData = _.cloneDeep(connectionInfo.value);
    const isForModal = purpose === "modal";

    const connectionArgumentsParam = _.has(
      connectionData,
      "connectionArguments",
    )
      ? {
          connectionArguments: Object.entries(
            connectionData.connectionArguments,
          ),
        }
      : {};

    const connectionOptionsParam = _.has(connectionData, "connectionOptions")
      ? { connectionOptions: Object.entries(connectionData.connectionOptions) }
      : {};

    let specificConfig: Record<string, any> = {};

    switch (service.value.serviceType) {
      case ServiceIds.MINIO: {
        const minioConfig: Record<string, any> = connectionData.minioConfig;
        addIfExists(serviceObjData, "accessKeyId", minioConfig.accessKeyId);
        if (isForModal) {
          serviceObjData.secretKey = "";
        } else {
          addIfExists(serviceObjData, "secretKey", minioConfig.secretKey);
        }
        addIfExists(serviceObjData, "sessionToken", minioConfig.sessionToken);
        addIfExists(serviceObjData, "region", minioConfig.region);
        addIfExists(serviceObjData, "endPointURL", minioConfig.endPointURL);

        specificConfig = {
          bucketNames: connectionData.bucketNames.map((item: any) => [item]),
          ...serviceObjData,
        };

        return {
          ...specificConfig,
          ...connectionArgumentsParam,
          ...connectionOptionsParam,
        };
      }

      case ServiceIds.MARIA_DB:
        if (isForModal) {
          specificConfig.password = "";
        } else {
          addIfExists(specificConfig, "password", connectionData.password);
        }
        addIfExists(specificConfig, "username", connectionData.username);
        addIfExists(specificConfig, "hostAndPort", connectionData.hostPort);
        addIfExists(
          specificConfig,
          "databaseName",
          connectionData.databaseName,
        );
        addIfExists(
          specificConfig,
          "databaseSchema",
          connectionData.databaseSchema,
        );
        break;

      case ServiceIds.MYSQL: {
        specificConfig.authConfigurationType = "basicAuth";
        if (isForModal) {
          specificConfig.password = "";
        } else {
          addIfExists(
            specificConfig,
            "password",
            connectionData.authType?.password,
          );
        }
        addIfExists(specificConfig, "username", connectionData.username);
        addIfExists(specificConfig, "hostAndPort", connectionData.hostPort);
        addIfExists(
          specificConfig,
          "databaseName",
          connectionData.databaseName,
        );
        break;
      }

      case ServiceIds.POSTGRESQL: {
        specificConfig.authConfigurationType = "basicAuth";
        if (isForModal) {
          specificConfig.password = "";
        } else {
          addIfExists(
            specificConfig,
            "password",
            connectionData.authType?.password,
          );
        }
        addIfExists(specificConfig, "database", connectionData.database);
        addIfExists(specificConfig, "username", connectionData.username);
        addIfExists(specificConfig, "hostAndPort", connectionData.hostPort);
        addIfExists(
          specificConfig,
          "classificationName",
          connectionData.classificationName,
        );
        break;
      }

      case ServiceIds.ORACLE: {
        if (connectionData.oracleConnectionType) {
          const [key, value] = Object.entries(
            connectionData.oracleConnectionType,
          )[0];
          specificConfig.oracleConnectionType = key;
          specificConfig[key] = value;
        }

        if (isForModal) {
          specificConfig.password = "";
        } else {
          addIfExists(
            specificConfig,
            "password",
            connectionData.authType?.password,
          );
        }
        addIfExists(specificConfig, "username", connectionData.username);
        addIfExists(specificConfig, "hostAndPort", connectionData.hostPort);
        addIfExists(
          specificConfig,
          "instantClientDirectory",
          connectionData.instantClientDirectory,
        );
        break;
      }

      default:
        // 예상치 못한 serviceId에 대한 처리
        console.warn(`Unknown serviceId: ${service.value.id}`);
        break;
    }

    return {
      ...specificConfig,
      ...connectionArgumentsParam,
      ...connectionOptionsParam,
    };
  };
  const getConfig = (
    serviceId: string,
    detailInfo?: { [key: string]: any },
  ) => {
    const serviceObjData = detailInfo || serviceObj.value.detailInfo;

    const connectionArgumentsParam = _.has(
      serviceObjData,
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

    const connectionOptionsParam = _.has(serviceObjData, "connectionOptions")
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
    // 연결테스트 시, 이미 저장된 데이터인 경우 serviceName 값도 보내줘야 정상적으로 작동
    const hasSavedData = _.isEmpty(serviceObj.value.defaultInfo.serviceNm);
    return {
      ...getDefaultParam(),
      serviceType: getContentType(),
      connectionType: serviceId,
      ...(hasSavedData && {
        serviceName: service.value.name,
      }),
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
  const getEditSubmitParams = () => {
    const serviceId = service.value.serviceType;
    const patch = compare(
      getConfig(serviceId, beforeServiceObj.value.detailInfo),
      getConfig(serviceId, serviceObj.value.detailInfo),
    );

    patch.forEach((data: { op: string; path: string }) => {
      data.path = "/connection/config" + data.path;
      if (
        _.includes(data.path, "/password") &&
        !_.isEmpty(connectionInfo.value?.password)
      ) {
        data.op = "replace";
      }
    });

    return patch;
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
    let res: { [key: string]: any } = {};
    if (isAddMode.value) {
      res = await $api(`/api/service-manage/save/${getContentType()}`, {
        method: "POST",
        body: getSubmitParams(),
      });
    } else {
      res = await $api(
        `/api/service-manage/update/${service.value.type}/${service.value.id}`,
        {
          method: "PATCH",
          headers: {
            "Content-Type": "application/json-patch+json",
          },
          body: getEditSubmitParams(),
        },
      );
    }

    if (res.result < 1) {
      // TODO : [개발] notification 개발 완료 되면 아래 코드 변경
      alert("저장 실패했습니다. 잠시 후 다시 시도해주세요.");
    }

    return res.result > 0;
  };

  const setServiceObj = ({ purpose }: { purpose: string }) => {
    const serviceIds = _.keyBy(services.value, "id");
    const serviceType = service.value.serviceType;
    if (_.has(serviceIds, serviceType)) {
      selectedServiceObj.value = serviceIds[serviceType];

      serviceObj.value.serviceId = serviceType;
      serviceObj.value.detailInfo = formatConfig(purpose);

      beforeServiceObj.value = _.cloneDeep(serviceObj.value);
      return true;
    } else {
      // MiniO, MySQL, MariaDB, PostgreSQL, Oracle 외의 다른 서비스타입의 경우 모달 오픈시 에러남.
      // 따라서 제공되는 서비스타입 이외의 서비스타입의 경우 모달을 열지못하게 처리.
      alert("제공하지 않는 서비스 정보입니다.");
      selectedServiceObj.value = defaultSelectedServiceObj;
      return false;
    }
  };

  return {
    isAddMode,
    currentStep,
    inValidMsg,
    isValid,
    beforeServiceObj,
    serviceObj,
    selectedServiceObj,
    isDoneTestConnection,
    testConnectionStatus,
    openEyeValues,
    setValue,
    setInitServiceId,
    setServiceObj,
    resetServiceObj,
    checkServiceNameDuplicate,
    connectionTest,
    submit,
  };
});
