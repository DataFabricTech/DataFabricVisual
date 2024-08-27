import { defineStore } from "pinia";
import _ from "lodash";
import type { IngestionPipeline } from "./IngestionPipeline";
import { computed } from "../../../../.nuxt/imports";

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
        value: "PERCENTAGE",
      },
      {
        label: "Rows",
        value: "ROWS",
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

    const bindPipelineType = computed(() => {
      if (
        pipelineType.value === "metadata" &&
        serviceType.value === "databaseService"
      ) {
        return "metadata";
      }

      if (
        pipelineType.value === "metadata" &&
        serviceType.value === "storageService"
      ) {
        return "metadata";
      }

      if (
        pipelineType.value === "profiler" &&
        serviceType.value === "databaseService"
      ) {
        return "profiler";
      }

      if (
        pipelineType.value === "profiler" &&
        serviceType.value === "storageService"
      ) {
        return "storageProfiler";
      }
    });

    const config = computed(() => {
      const configObject = {};

      if (
        pipelineType.value === "metadata" &&
        serviceType.value === "databaseService"
      ) {
        configObject.type = "DatabaseMetadata";
        configObject.markDeletedTables = isMarkDeletedTables.value;
        configObject.markDeletedStoredProcedures = false;
        configObject.includeTables = isIncludeTables.value;
        configObject.includeViews = isIncludeViews.value;
        configObject.includeTags = false;
        configObject.includeOwners = isIncludeOwners.value;
        configObject.includeStoredProcedures = false;
        configObject.includeDDL = false;
        configObject.queryLogDuration = 1;
        configObject.queryParsingTimeoutLimit = 300;
        configObject.useFqnForFiltering = false;
        configObject.threads = 1;
        configObject.incremental = {
          enabled: false,
          lookbackDays: 7, // TODO: 파람값으로 필요 판단 여부
          safetyMarginDays: 1, // TODO: 파람값으로 필요 판단 여부
        };
      }

      if (
        pipelineType.value === "metadata" &&
        serviceType.value === "storageService"
      ) {
        configObject.type = "StorageMetadata";
        // TODO: 기획서랑 openmeta 화면이랑 filterpattern 포함 내용이 다름..bucket 패턴?
        // 확인 후 처리
      }

      if (
        pipelineType.value === "profiler" &&
        serviceType.value === "databaseService"
      ) {
        configObject.type = "Profiler";
        configObject.includeViews = isIncludeViews.value;
        configObject.useFqnForFiltering = false;
        configObject.generateSampleData = isGenerateSampleData.value;
        configObject.computeMetrics = isComputeMetric.value;
        configObject.processPiiSensitive = false;
        configObject.profileSampleType = selectedSampleTypeItem.value;
        configObject.profileSample = Number(profileSample.value);
        configObject.sampleDataCount = Number(sampleDataRowsCount.value);
        configObject.threadCount = 5;
        configObject.timeoutSeconds = 43200;
      }

      if (
        pipelineType.value === "profiler" &&
        serviceType.value === "storageService"
      ) {
        configObject.type = "StorageProfiler";
        configObject.useFqnForFiltering = false;
        configObject.generateSampleData = isGenerateSampleData.value;
        configObject.computeMetrics = isComputeMetric.value;
        configObject.processPiiSensitive = false;
        configObject.profileSampleType = selectedSampleTypeItem.value;
        configObject.profileSample = Number(profileSample.value);
        configObject.sampleDataCount = Number(sampleDataRowsCount.value);
        configObject.threadCount = 5;
        configObject.timeoutSeconds = 43200;
      }

      // CFP_includes나 CFP_excludes가 비어 있지 않으면 containerFilterPattern 추가
      if (CFP_includes.length > 0 || CFP_excludes.length > 0) {
        configObject.containerFilterPattern = {
          includes: CFP_includes.map((item) => item.name),
          excludes: CFP_excludes.map((item) => item.name),
        };
      }

      // BFP_includes나 BFP_excludes가 비어 있지 않으면 bucketFilterPattern 추가
      if (BFP_includes.length > 0 || BFP_excludes.length > 0) {
        configObject.bucketFilterPattern = {
          includes: BFP_includes.map((item) => item.name),
          excludes: BFP_excludes.map((item) => item.name),
        };
      }

      // DFP_includes나 DFP_excludes가 비어 있지 않으면 databaseFilterPattern 추가
      if (DFP_includes.length > 0 || DFP_excludes.length > 0) {
        configObject.databaseFilterPattern = {
          includes: DFP_includes.map((item) => item.name),
          excludes: DFP_excludes.map((item) => item.name),
        };
      }

      // SFP_includes나 SFP_excludes가 비어 있지 않으면 schemaFilterPattern 추가
      if (SFP_includes.length > 0 || SFP_excludes.length > 0) {
        configObject.schemaFilterPattern = {
          includes: SFP_includes.map((item) => item.name),
          excludes: SFP_excludes.map((item) => item.name),
        };
      }

      // TFP_includes나 TFP_excludes가 비어 있지 않으면 tableFilterPattern 추가
      if (TFP_includes.length > 0 || TFP_excludes.length > 0) {
        configObject.tableFilterPattern = {
          includes: TFP_includes.map((item) => item.name),
          excludes: TFP_excludes.map((item) => item.name),
        };
      }

      return configObject;
    });

    const ingestionPipeLine = ref<IngestionPipeline>({
      airflowConfig: {
        scheduleInterval: "", // 두 JSON 모두 크론 스케줄을 가짐 (단, 시간이 다를 수 있음)
        startDate: "", // 스케줄러 (생성할때는 특정시간때 우선 반영 - 서버에서 넣어줘야함)
      },
      loggerLevel: loggerLevel.value, // ex) INFO or DEBUG
      name: "", // 서버에서 넣어줘야함 ex) UUID 값
      displayName: "",
      owner: {
        id: "b7b3f5cb-4e71-4197-8ff0-34e0ced231cc", // TODO: 소유자 ID 바인딩
        type: "user", // TODO: 이정책임님과 논의
      },
      pipelineType: bindPipelineType.value, // ex) storageProfiler, profiler, metadata
      service: {
        id: serviceId.value,
        type: serviceType.value,
      },
      sourceConfig: {
        config: config.value,
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

    const createIngestion = () => {
      console.log("config값확인: ", config.value);
    };

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
