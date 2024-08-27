import { defineStore } from "pinia";
import _ from "lodash";
import type { IngestionPipeline } from "./IngestionPipeline";

export const useServiceCollectionAddStore = defineStore(
  "service_collection_add",
  () => {
    const { $api } = useNuxtApp();

    const serviceId = ref("");
    const pipelineType = ref("");
    const serviceType = ref("");

    const modalTitle = ref("");

    const loggerLevel = computed(() =>
      isEnableDebug.value ? "DEBUG" : "INFO",
    );

    const ingestionPipeLine = ref<IngestionPipeline>({
      airflowConfig: {
        scheduleInterval: "", // 두 JSON 모두 크론 스케줄을 가짐 (단, 시간이 다를 수 있음)
        startDate: "", // 스케줄러 (생성할때는 특정시간때 우선 반영 - 서버에서 넣어줘야함)
      },
      loggerLevel: loggerLevel.value, // ex) INFO or DEBUG
      name: "", // 서버에서 넣어줘야함 ex) UUID 값
      displayName: "",
      owner: {
        id: "b7b3f5cb-4e71-4197-8ff0-34e0ced231cc", // 소유자 ID
        type: "user", // 고정?
      },
      pipelineType: "", // ex) StorageProfiler, Profiler, metadata
      service: {
        id: serviceId.value, // 공통된 서비스 ID
        type: serviceType.value, // 서비스 타입이 공통됨
      },
      sourceConfig: {
        config: {
          type: "", // ex) DatabaseMetadata, StorageMetadata, StorageProfiler, Profiler
        },
      },
    });

    const name = ref("");
    const profileSample = ref(50);
    const sampleDataRowsCount = ref(50);

    const CFP_includes = ref([]);
    const CFP_excludes = ref([]);

    const BFP_includes = ref([]);
    const BFP_excludes = ref([]);

    const DFP_includes = ref([]);
    const DFP_excludes = ref([]);

    const SFP_includes = ref([]);
    const SFP_excludes = ref([]);

    const TFP_includes = ref([]);
    const TFP_excludes = ref([]);

    const isEnableDebug = ref(false);

    const isMarkDeletedTables = ref(true);
    const isIncludeTables = ref(true);
    const isIncludeViews = ref(true);
    const isIncludeOwners = ref(true);

    const isGenerateSampleData = ref(true);
    const isComputeMetric = ref(true);

    const inValidMsg = ref("");
    const isValid = ref(true);
    const isValidCronMessage = ref(true);
    const reties = ref(0);

    interface Pattern {
      name: string;
    }

    const setModalTitle = (value: any) => {
      modalTitle.value = value;
    };

    const setId = (value: any) => {
      console.log("아이디확인: ", value);
      serviceId.value = value;
    };

    const setPipelineType = (value: any) => {
      console.log("pipelineType값 확인: ", value);
      pipelineType.value = value;
    };

    const setServiceType = (value: any) => {
      console.log("serviceType확인: ", value);
      serviceType.value = value;
    };

    // validation setters
    const setName = (value: any) => {
      name.value = value;
    };
    const setProfileSample = (value: any) => {
      profileSample.value = value;
    };
    const setSampleDataRowsCount = (value: any) => {
      sampleDataRowsCount.value = value;
    };

    // setters
    const set_CFP_includes = (value: any) => {
      CFP_includes.value = value.map((item) => item.name);
    };
    const set_CFP_excludes = (value: any) => {
      CFP_excludes.value = value.map((item) => item.name);
    };
    const set_BFP_includes = (value: any) => {
      BFP_includes.value = value.map((item) => item.name);
    };
    const set_BFP_excludes = (value: any) => {
      BFP_excludes.value = value.map((item) => item.name);
    };

    const set_DFP_includes = (value: any) => {
      DFP_includes.value = value.map((item) => item.name);
    };
    const set_DFP_excludes = (value: any) => {
      DFP_excludes.value = value.map((item) => item.name);
    };
    const set_SFP_includes = (value: any) => {
      SFP_includes.value = value.map((item) => item.name);
    };
    const set_SFP_excludes = (value: any) => {
      SFP_excludes.value = value.map((item) => item.name);
    };

    const set_TFP_includes = (value: any) => {
      TFP_includes.value = value.map((item) => item.name);
    };
    const set_TFP_excludes = (value: any) => {
      TFP_excludes.value = value.map((item) => item.name);
    };

    const setIsEnableDebug = (value: any) => {
      isEnableDebug.value = value;
    };

    const setIsMarkDeletedTables = (value: any) => {
      isMarkDeletedTables.value = value;
    };

    const setIsIncludeTables = (value: any) => {
      isIncludeTables.value = value;
    };

    const setIsIncludeViews = (value: any) => {
      isIncludeViews.value = value;
    };

    const setIsIncludeOwners = (value: any) => {
      isIncludeViews.value = value;
    };

    const setIsGenerateSampleData = (value: any) => {
      isGenerateSampleData.value = value;
    };

    const setIsComputeMetric = (value: any) => {
      isComputeMetric.value = value;
    };

    const setCronParedMessage = (value: any) => {
      isValidCronMessage.value = value;
    };

    const setRetries = (value: any) => {
      reties.value = value;
    };

    const resetData = () => {
      isValid.value = true;
      name.value = "";
      profileSample.value = 50;
      sampleDataRowsCount.value = 50;

      CFP_includes.value = [];
      CFP_excludes.value = [];
      BFP_includes.value = [];
      BFP_excludes.value = [];
      DFP_includes.value = [];
      DFP_excludes.value = [];
      SFP_includes.value = [];
      SFP_excludes.value = [];
      TFP_includes.value = [];
      TFP_excludes.value = [];

      isEnableDebug.value = false;
      isMarkDeletedTables.value = true;
      isIncludeTables.value = true;
      isIncludeViews.value = true;
      isIncludeOwners.value = true;

      isGenerateSampleData.value = true;
      isComputeMetric.value = true;

      isValidCronMessage.value = true;
    };

    const checkValidation = () => {
      // 이름이 체크
      if (_.isEmpty(name.value)) {
        isValid.value = false;
        inValidMsg.value = "필수 값을 입력해주세요.";
        return;
      }

      console.log("값좀보자: ", profileSample.value);

      if (profileSample.value === "") {
        isValid.value = false;
        inValidMsg.value = "필수 값을 입력해주세요.";
        return;
      }

      if (sampleDataRowsCount.value === "") {
        isValid.value = false;
        inValidMsg.value = "필수 값을 입력해주세요.";
        return;
      }

      // crontab 유효성 체크
      if (!isValidCronMessage.value) {
        isValid.value = false;
        inValidMsg.value = "유효하지 않은 크론 표현식 입니다.";
        return;
      }
      isValid.value = true;
      inValidMsg.value = "";

      isValidCronMessage.value = true;
    };

    const createIngestion = () => {};

    return {
      pipelineType,
      serviceType,
      modalTitle,
      ingestionPipeLine,
      isValid,
      inValidMsg,
      setModalTitle,
      setId,
      setPipelineType,
      setServiceType,
      setName,
      setSampleDataRowsCount,
      set_CFP_includes,
      set_CFP_excludes,
      set_BFP_includes,
      set_BFP_excludes,
      set_DFP_includes,
      set_DFP_excludes,
      set_SFP_includes,
      set_SFP_excludes,
      set_TFP_includes,
      set_TFP_excludes,
      setIsEnableDebug,
      setIsMarkDeletedTables,
      setIsIncludeTables,
      setIsIncludeViews,
      setIsIncludeOwners,
      setIsGenerateSampleData,
      setIsComputeMetric,
      setProfileSample,
      setCronParedMessage,
      setRetries,
      resetData,
      checkValidation,
      createIngestion,
    };
  },
);
