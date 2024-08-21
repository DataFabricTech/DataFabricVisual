import type {
  IService,
  IServiceObj,
} from "~/components/admin/service/modal/service-add-modal/ServiceAddModalProps";
import { ServiceIds } from "~/components/admin/service/modal/service-add-modal/ServiceAddModalComposition";
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

  const connectionTestStatus = ref<boolean | null>(null);

  const checkServiceNameDuplicate = async () => {
    const { data } = await $api(
      `/api/service/isDuplicatedNm?${getQueryString()}`,
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

    const defaultConfig = {
      supportsDBTExtraction: true,
      supportsMetadataExtraction: true,
      supportsProfiler: true,
      supportsQueryComment: true,
      type: serviceId,
    };

    const connectionArgumentsParam = _.has(
      serviceObj.value,
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

    const connectionOptionsParam = _.has(serviceObj.value, "connectionOptions")
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
          supportsMetadataExtraction: true,
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
        specificConfig.scheme = "mysql+pymysql";
        break;

      case ServiceIds.MYSQL: {
        specificConfig.scheme = "mysql+pymysql";

        addIfExists(specificConfig, "hostPort", serviceObjData.hostAndPort);
        addIfExists(specificConfig, "username", serviceObjData.username);

        if (serviceObjData.password) {
          specificConfig.authType = { password: serviceObjData.password };
        }
        break;
      }

      case ServiceIds.POSTGRESQL: {
        specificConfig = {
          scheme: "postgresql+psycopg2",
          classificationName: "postgresPolicyTags",
          ingestAllDatabases: true,
          sslMode: "disable",
          supportsDatabase: true,
          supportsLineageExtraction: true,
          supportsUsageExtraction: true,
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
          scheme: "oracle+cx_oracle",
          supportsLineageExtraction: true,
          supportsUsageExtraction: true,
        };
        addIfExists(specificConfig, "username", serviceObjData.username);
        addIfExists(specificConfig, "password", serviceObjData.password);
        break;
      }

      default:
        // 예상치 못한 serviceId에 대한 처리
        console.warn(`Unknown serviceId: ${serviceId}`);
        break;
    }

    return {
      ...defaultConfig,
      ...specificConfig,
      ...connectionArgumentsParam,
      ...connectionOptionsParam,
    };
  };

  const getParams = () => {
    const serviceId = serviceObj.value.serviceId;
    const params: any = {
      connectionType: serviceId,
      serviceType: serviceId === ServiceIds.MINIO ? "Storage" : "Database",
      connection: {
        config: getConfig(serviceId),
      },
    };
    return params;
  };
  const connectionTest = async () => {
    const response = await $api("/api/service/connectionTest", {
      method: "POST",
      body: getParams(),
    });
    console.log(response);

    connectionTestStatus.value = true;

    const connectionErrorMsg = "연결 실패 메시지";
    return connectionErrorMsg;
  };

  const submit = async () => {
    console.log(serviceObj);
    console.log("submit");
    return false;
  };

  return {
    currentStep,
    inValidMsg,
    isValid,
    serviceObj,
    selectedServiceObj,
    connectionTestStatus,
    setValue,
    setInitServiceId,
    resetServiceObj,
    checkServiceNameDuplicate,
    connectionTest,
    submit,
  };
});
