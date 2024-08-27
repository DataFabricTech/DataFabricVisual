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

    const profileSample = ref(50);
    const sampleDataRowsCount = ref(50);

    interface Pattern {
      name: string;
    }

    const CFP_includes = reactive<Pattern[]>([]);
    const CFP_excludes = reactive<Pattern[]>([]);

    const BFP_includes = reactive<Pattern[]>([]);
    const BFP_excludes = reactive<Pattern[]>([]);

    // NOTE: DFP = database filter pattern
    const DFP_includes = reactive<Pattern[]>([]);
    const DFP_excludes = reactive<Pattern[]>([]);

    // NOTE: SFP = schema filter pattern
    const SFP_includes = reactive<Pattern[]>([]);
    const SFP_excludes = reactive<Pattern[]>([]);

    // NOTE: TFP = table filter pattern
    const TFP_includes = reactive<Pattern[]>([]);
    const TFP_excludes = reactive<Pattern[]>([]);

    const isEnableDebug = ref(false);

    const isMarkDeletedTables = ref(true);
    const isIncludeTables = ref(true);
    const isIncludeViews = ref(true);
    const isIncludeOwners = ref(true);

    const sampleTypeOptions = ref([
      {
        label: "Percentage",
        value: "persentage",
      },
      {
        label: "Rows",
        value: "rows",
      },
    ]);
    const selectedSampleTypeItem = ref(sampleTypeOptions.value[0].value);

    const isGenerateSampleData = ref(true);
    const isComputeMetric = ref(true);

    const inValidMsg = ref("");
    const isValid = ref(true);
    const isValidCronMessage = ref(true);
    const reties = ref(0);

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

    // setters
    // const set_CFP_includes = (value: any) => {
    //   CFP_includes.value = value.map((item) => item.name);
    // };
    // const set_CFP_excludes = (value: any) => {
    //   CFP_excludes.value = value.map((item) => item.name);
    // };
    // const set_BFP_includes = (value: any) => {
    //   BFP_includes.value = value.map((item) => item.name);
    // };
    // const set_BFP_excludes = (value: any) => {
    //   BFP_excludes.value = value.map((item) => item.name);
    // };
    //
    // const set_DFP_includes = (value: any) => {
    //   DFP_includes.value = value.map((item) => item.name);
    // };
    // const set_DFP_excludes = (value: any) => {
    //   DFP_excludes.value = value.map((item) => item.name);
    // };
    // const set_SFP_includes = (value: any) => {
    //   SFP_includes.value = value.map((item) => item.name);
    // };
    // const set_SFP_excludes = (value: any) => {
    //   SFP_excludes.value = value.map((item) => item.name);
    // };
    //
    // const set_TFP_includes = (value: any) => {
    //   TFP_includes.value = value.map((item) => item.name);
    // };
    // const set_TFP_excludes = (value: any) => {
    //   TFP_excludes.value = value.map((item) => item.name);
    // };

    const setCronParedMessage = (value: any) => {
      isValidCronMessage.value = value;
    };

    const setRetries = (value: any) => {
      reties.value = value;
    };

    const handleNameInput = (e) => {
      ingestionPipeLine.value.displayName = e.target.value;
    };

    const clearNameInput = () => {
      ingestionPipeLine.value.displayName = "";
    };

    const changeEnableDebug = () => {
      isEnableDebug.value = !isEnableDebug.value;
    };

    const changeIsMarkDeletedTables = () => {
      isMarkDeletedTables.value = !isMarkDeletedTables.value;
    };

    const changeIsIncludeTables = () => {
      isIncludeTables.value = !isIncludeTables.value;
    };

    const changeIncludeViews = () => {
      isIncludeViews.value = !isIncludeViews.value;
    };

    const changeGenerateSampleData = () => {
      isGenerateSampleData.value = !isGenerateSampleData.value;
    };

    const changeComputeMetric = () => {
      isComputeMetric.value = !isComputeMetric.value;
    };

    const changeIsIncludeOwners = () => {
      isIncludeOwners.value = !isIncludeOwners.value;
    };

    const selectSampleType = (value: any) => {
      selectedSampleTypeItem.value = value;
    };

    const handleProfileSampleInput = (e) => {
      profileSample.value = e.target.value;
    };

    const handleRowCount = (e: any) => {
      sampleDataRowsCount.value = e.target.value;
    };

    const addInput = (patternAlias: any) => {
      if (patternAlias === "CFP_includes") {
        CFP_includes.push({ name: "" });
      }
      if (patternAlias === "CFP_excludes") {
        CFP_excludes.push({ name: "" });
      }
      if (patternAlias === "BFP_includes") {
        BFP_includes.push({ name: "" });
      }
      if (patternAlias === "BFP_excludes") {
        BFP_excludes.push({ name: "" });
      }
      if (patternAlias === "DFP_includes") {
        DFP_includes.push({ name: "" });
      }
      if (patternAlias === "DFP_excludes") {
        DFP_excludes.push({ name: "" });
      }

      if (patternAlias === "SFP_includes") {
        SFP_includes.push({ name: "" });
      }
      if (patternAlias === "SFP_excludes") {
        SFP_excludes.push({ name: "" });
      }

      if (patternAlias === "TFP_includes") {
        TFP_includes.push({ name: "" });
      }
      if (patternAlias === "TFP_excludes") {
        TFP_excludes.push({ name: "" });
      }
    };

    const clearInput = (patternAlias: any, index: any) => {
      if (patternAlias === "CFP_includes") {
        CFP_includes[index].name = "";
      }
      if (patternAlias === "CFP_excludes") {
        CFP_excludes[index].name = "";
      }
      if (patternAlias === "BFP_includes") {
        BFP_includes[index].name = "";
      }
      if (patternAlias === "BFP_excludes") {
        BFP_excludes[index].name = "";
      }
      if (patternAlias === "DFP_includes") {
        DFP_includes[index].name = "";
      }
      if (patternAlias === "DFP_excludes") {
        DFP_excludes[index].name = "";
      }

      if (patternAlias === "SFP_includes") {
        SFP_includes[index].name = "";
      }
      if (patternAlias === "SFP_excludes") {
        SFP_excludes[index].name = "";
      }

      if (patternAlias === "TFP_includes") {
        TFP_includes[index].name = "";
      }
      if (patternAlias === "TFP_excludes") {
        TFP_excludes[index].name = "";
      }
    };

    const removeInput = (patternAlias: any, index: number) => {
      if (patternAlias === "CFP_includes") {
        CFP_includes.splice(index, 1);
      }
      if (patternAlias === "CFP_excludes") {
        CFP_excludes.splice(index, 1);
      }
      if (patternAlias === "BFP_includes") {
        BFP_includes.splice(index, 1);
      }
      if (patternAlias === "BFP_excludes") {
        BFP_excludes.splice(index, 1);
      }
      if (patternAlias === "DFP_includes") {
        DFP_includes.splice(index, 1);
      }
      if (patternAlias === "DFP_excludes") {
        DFP_excludes.splice(index, 1);
      }

      if (patternAlias === "SFP_includes") {
        SFP_includes.splice(index, 1);
      }
      if (patternAlias === "SFP_excludes") {
        SFP_excludes.splice(index, 1);
      }

      if (patternAlias === "TFP_includes") {
        TFP_includes.splice(index, 1);
      }
      if (patternAlias === "TFP_excludes") {
        TFP_excludes.splice(index, 1);
      }
    };

    const resetData = () => {
      isValid.value = true;

      //TODO: ingestionPipeLine이 기본값으로 초기화 (수정용 처리도 해줘야함)
      ingestionPipeLine.value = {
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
            // config를 compute로 만들어야함
            type: "", // ex) DatabaseMetadata, StorageMetadata, StorageProfiler, Profiler
          },
        },
      };

      profileSample.value = 50;
      sampleDataRowsCount.value = 50;

      CFP_includes.splice(0);
      CFP_excludes.splice(0);
      BFP_includes.splice(0);
      BFP_excludes.splice(0);
      DFP_includes.splice(0);
      DFP_excludes.splice(0);
      SFP_includes.splice(0);
      SFP_excludes.splice(0);
      TFP_includes.splice(0);
      TFP_excludes.splice(0);

      isEnableDebug.value = false;
      isMarkDeletedTables.value = true;
      isIncludeTables.value = true;
      isIncludeViews.value = true;
      isIncludeOwners.value = true;

      selectedSampleTypeItem.value = sampleTypeOptions.value[0].value;

      isGenerateSampleData.value = true;
      isComputeMetric.value = true;

      isValidCronMessage.value = true;
    };

    const checkValidation = () => {
      // 이름이 체크
      if (_.isEmpty(ingestionPipeLine.value.displayName)) {
        isValid.value = false;
        inValidMsg.value = "필수 값을 입력해주세요.";
        return;
      }

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
      isValid,
      inValidMsg,
      ingestionPipeLine,

      isEnableDebug,
      isMarkDeletedTables,
      isIncludeTables,
      isIncludeViews,
      isGenerateSampleData,
      isComputeMetric,
      isIncludeOwners,

      sampleTypeOptions,
      selectedSampleTypeItem,

      profileSample,
      sampleDataRowsCount,

      CFP_includes,
      CFP_excludes,
      BFP_includes,
      BFP_excludes,
      DFP_includes,
      DFP_excludes,
      SFP_includes,
      SFP_excludes,
      TFP_includes,
      TFP_excludes,
      setModalTitle,
      setId,
      setPipelineType,
      setServiceType,
      setCronParedMessage,
      setRetries,
      handleNameInput,
      clearNameInput,
      addInput,
      clearInput,
      removeInput,

      changeEnableDebug,
      changeIsMarkDeletedTables,
      changeIsIncludeTables,
      changeIsIncludeOwners,
      changeIncludeViews,
      changeGenerateSampleData,
      changeComputeMetric,

      selectSampleType,
      handleProfileSampleInput,
      handleRowCount,
      resetData,
      checkValidation,
      createIngestion,
    };
  },
);
