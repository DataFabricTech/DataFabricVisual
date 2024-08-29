import { defineStore } from "pinia";
import _ from "lodash";
import type { IngestionPipeline } from "./IngestionPipeline";
import cronstrue from "cronstrue";
import { useUserStore } from "~/store/user/userStore";

export const useServiceCollectionAddStore = defineStore(
  "service_collection_add",
  () => {
    const { $api } = useNuxtApp();

    const userStore = useUserStore();
    // NOTE: 로그인 사용자 정보
    const { user } = storeToRefs(userStore);

    const isEditModalStatus = ref(false);
    const pipelineId = ref("");
    const cloneIngestionPipline = ref({});

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

    const cronExpression = ref("0 0 * * *");
    const isNone = ref(true);
    const isShowHour = ref(false);
    const isShowDay = ref(false);
    const isShowWeek = ref(false);
    const isShowCustom = ref(false);

    const cronParedMessage = ref("");

    const scheduleTypeOptions = ref([
      {
        label: "None",
        value: "none",
      },
      {
        label: "Hour",
        value: "hour",
      },
      {
        label: "Day",
        value: "day",
      },
      {
        label: "Week",
        value: "week",
      },
      {
        label: "Custom",
        value: "custom",
      },
    ]);

    const selectedScheduleTypeItem = ref(scheduleTypeOptions.value[0].value);
    const minute = ref("00");
    const time = ref("00:00");

    const daysOfWeek = ref([
      { label: "SUN", value: "Sunday" },
      { label: "MON", value: "Monday" },
      { label: "TUE", value: "Tuesday" },
      { label: "WED", value: "Wednesday" },
      { label: "THU", value: "Thursday" },
      { label: "FRI", value: "Friday" },
      { label: "SAT", value: "Saturday" },
    ]);

    const selectedDay = ref(daysOfWeek.value[0].value);

    const isValidCronMessage = ref(true);
    const reties = ref(0);

    watch(
      [isShowCustom, cronExpression],
      ([newIsShowCustom, newCronExpression]) => {
        if (newIsShowCustom) {
          try {
            cronParedMessage.value = cronstrue.toString(newCronExpression);
            isValidCronMessage.value = true; // 유효한 크론 표현식임을 설정
          } catch (err: any) {
            cronParedMessage.value = "유효하지 않은 크론 표현식 입니다."; // 유효하지 않은 크론 표현식임을 설정
            isValidCronMessage.value = false;
          }
        } else {
          cronParedMessage.value = ""; // isShowCustom이 false일 때 빈 메시지 설정
        }
      },
    );

    // computed
    const airflowConfig = computed(() => {
      const airflowObj = {};
      const generateCronExpression = () => {
        if (isShowHour.value) {
          // ex) "매시간 4분에" 작업을 실행
          return `${parseInt(minute.value)} * * * *`;
        } else if (isShowDay.value) {
          // 매일 특정 시간에 작업을 실행
          const [hour, min] = time.value ? time.value.split(":") : ["00", "00"];
          return `${parseInt(min)} ${parseInt(hour)} * * *`;
        } else if (isShowWeek.value) {
          // 매주 특정 요일, 특정 시간에 작업을 실행
          const [hour, min] = time.value ? time.value.split(":") : ["00", "00"];
          const dayIndex = daysOfWeek.value.findIndex(
            (day) => day.value === selectedDay.value,
          );
          return `${parseInt(min)} ${parseInt(hour)} * * ${dayIndex}`;
        } else if (isShowCustom.value) {
          // 사용자가 직접 입력한 크론 표현식을 사용
          return cronExpression.value;
        }
        return ""; // 기본 값으로 빈 문자열 반환
      };

      const cron = generateCronExpression();
      if (cron) {
        airflowObj.scheduleInterval = cron;
      }

      airflowObj.startDate = ""; // 서버에서 바인딩 처리
      airflowObj.retries = reties.value;

      return airflowObj;
    });

    const loggerLevel = computed(() =>
      isEnableDebug.value ? "DEBUG" : "INFO",
    );

    const bindPipelineType = computed(() => {
      if (pipelineType.value === "metadata") {
        return "metadata";
      }
      if (pipelineType.value === "profiler") {
        return serviceType.value === "databaseService"
          ? "profiler"
          : "storageProfiler";
      }
      return "";
    });

    const config = computed(() => {
      const configObject: any = {};

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
        configObject.useFqnForFiltering = false;
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

      function addFilterPattern(configObject, patternKey, includes, excludes) {
        if (includes.length > 0 || excludes.length > 0) {
          configObject[patternKey] = {
            includes: includes.map((item) => item.name),
            excludes: excludes.map((item) => item.name),
          };
        }
      }

      addFilterPattern(
        configObject,
        "containerFilterPattern",
        CFP_includes,
        CFP_excludes,
      );
      addFilterPattern(
        configObject,
        "bucketFilterPattern",
        BFP_includes,
        BFP_excludes,
      );
      addFilterPattern(
        configObject,
        "databaseFilterPattern",
        DFP_includes,
        DFP_excludes,
      );
      addFilterPattern(
        configObject,
        "schemaFilterPattern",
        SFP_includes,
        SFP_excludes,
      );
      addFilterPattern(
        configObject,
        "tableFilterPattern",
        TFP_includes,
        TFP_excludes,
      );

      return configObject;
    });

    const ingestionPipeLine = ref<IngestionPipeline>({
      airflowConfig: {},
      loggerLevel: "",
      name: "",
      displayName: "",
      owner: {
        id: "",
        type: "user",
      },
      pipelineType: "", // ex) storageProfiler, profiler, metadata
      service: {
        id: "",
        type: "",
      },
      sourceConfig: {
        config: {},
      },
    });

    // setters
    const setModalTitle = (value: any) => {
      modalTitle.value = value;
    };

    const setId = (value: any) => {
      serviceId.value = value;
    };

    const setPipelineType = (value: any) => {
      pipelineType.value = value;
    };

    const setServiceType = (value: any) => {
      serviceType.value = value;
    };

    const setInvalidMessage = (value: any) => {
      inValidMsg.value = value;
    };

    const setIsValid = (value: any) => {
      isValid.value = value;
    };

    const setCronExpression = (value: any) => {
      cronExpression.value = value;
    };

    const setIsValidCronParsedMessage = (value: any) => {
      isValidCronMessage.value = value;
    };

    const setRetries = (value: any) => {
      reties.value = value;
    };

    const setFilterPattern = (pattern, includes, excludes) => {
      if (pattern) {
        includes.splice(
          0,
          includes.length,
          ...(pattern.includes.map((name) => ({ name })) || []),
        );
        excludes.splice(
          0,
          excludes.length,
          ...(pattern.excludes.map((name) => ({ name })) || []),
        );
      }
    };

    const setScheduleBasedOnInterval = (scheduleInterval) => {
      // 초기화
      isShowHour.value = false;
      isShowDay.value = false;
      isShowWeek.value = false;
      hideTypeView();
      resetScheduleData();

      // 매 시간 실행하는 경우
      if (/^\d{1,2} \* \* \* \*$/.test(scheduleInterval)) {
        isShowHour.value = true;
        const [min] = scheduleInterval.split(" ");
        minute.value = min.padStart(2, "0");
        selectedScheduleTypeItem.value = scheduleTypeOptions.value[1].value;
      }
      // 매일 특정 시간에 실행하는 경우
      else if (/^\d{1,2} \d{1,2} \* \* \*$/.test(scheduleInterval)) {
        isShowDay.value = true;
        const [min, hour] = scheduleInterval.split(" ");
        time.value = `${hour.padStart(2, "0")}:${min.padStart(2, "0")}`;
        selectedScheduleTypeItem.value = scheduleTypeOptions.value[2].value;
      }
      // 매주 특정 요일, 특정 시간에 실행하는 경우
      else if (/^\d{1,2} \d{1,2} \* \* \d$/.test(scheduleInterval)) {
        isShowWeek.value = true;
        const [min, hour, , , dayOfWeek] = scheduleInterval.split(" ");
        time.value = `${hour.padStart(2, "0")}:${min.padStart(2, "0")}`;
        selectedDay.value = daysOfWeek.value[dayOfWeek].value;
        selectedScheduleTypeItem.value = scheduleTypeOptions.value[3].value;
      }
      // 사용자 정의 크론 표현식인 경우
      else {
        isShowCustom.value = true;
        cronExpression.value = scheduleInterval;
        selectedScheduleTypeItem.value = scheduleTypeOptions.value[4].value;
      }
    };

    // 수정시 화면에 값 로드
    const setEditModalData = (data: any) => {
      let loadConfig = data.sourceConfig.config;
      isEnableDebug.value = data.loggerLevel === "INFO" ? false : true;
      if (
        pipelineType.value == "metadata" &&
        serviceType.value === "databaseService"
      ) {
        isMarkDeletedTables.value = loadConfig.markDeletedTables;
        isIncludeTables.value = loadConfig.includeTables;
        isIncludeViews.value = loadConfig.includeViews;
        isIncludeOwners.value = loadConfig.includeOwners;
      }

      // TODO: 추후 필요한 값이 있으면 추가
      // if (
      //   pipelineType.value == "metadata" &&
      //   serviceType.value === "storageService"
      // ) {
      // }

      if (
        pipelineType.value == "profiler" &&
        serviceType.value === "databaseService"
      ) {
        isIncludeViews.value = loadConfig.includeViews;
        isGenerateSampleData.value = loadConfig.isGenerateSampleData;
        isComputeMetric.value = loadConfig.computeMetrics;
        selectedSampleTypeItem.value = loadConfig.profileSampleType;
        profileSample.value = loadConfig.profileSample;
        sampleDataRowsCount.value = loadConfig.sampleDataCount;
      }

      if (
        pipelineType.value == "profiler" &&
        serviceType.value === "storageService"
      ) {
        isGenerateSampleData.value = loadConfig.generateSampleData;
        isComputeMetric.value = loadConfig.computeMetrics;
        selectedSampleTypeItem.value = loadConfig.profileSampleType;
        profileSample.value = loadConfig.profileSample;
        sampleDataRowsCount.value = loadConfig.sampleDataCount;
      }

      setFilterPattern(
        loadConfig.containerFilterPattern,
        CFP_includes,
        CFP_excludes,
      );
      setFilterPattern(
        loadConfig.bucketFilterPattern,
        BFP_includes,
        BFP_excludes,
      );
      setFilterPattern(
        loadConfig.databaseFilterPattern,
        DFP_includes,
        DFP_excludes,
      );
      setFilterPattern(
        loadConfig.schemaFilterPattern,
        SFP_includes,
        SFP_excludes,
      );
      setFilterPattern(
        loadConfig.tableFilterPattern,
        TFP_includes,
        TFP_excludes,
      );
      if (data.airflowConfig.scheduleInterval) {
        setScheduleBasedOnInterval(data.airflowConfig.scheduleInterval);
        ingestionPipeLine.value.airflowConfig.scheduleInterval =
          data.airflowConfig.scheduleInterval;
      }

      reties.value = data.airflowConfig.retries;

      ingestionPipeLine.value.name = data.name;
      ingestionPipeLine.value.displayName = data.displayName;
      ingestionPipeLine.value.loggerLevel = data.loggerLevel;
      ingestionPipeLine.value.pipelineType = data.pipelineType;
      ingestionPipeLine.value.sourceConfig.config = config.value;
      ingestionPipeLine.value.airflowConfig.startDate =
        data.airflowConfig.startDate;
      ingestionPipeLine.value.airflowConfig.retries =
        data.airflowConfig.retries;

      cloneIngestionPipline.value = _.cloneDeep(ingestionPipeLine.value);
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

    const patternMap: { [key: string]: { name: string }[] } = {
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
    };

    const addInput = (patternAlias: string) => {
      const targetArray = patternMap[patternAlias];
      if (targetArray) {
        targetArray.push({ name: "" });
      }
    };

    const clearInput = (patternAlias: string, index: number) => {
      const targetArray = patternMap[patternAlias];
      if (targetArray && targetArray[index]) {
        targetArray[index].name = "";
      }
    };

    const removeInput = (patternAlias: string, index: number) => {
      const targetArray = patternMap[patternAlias];
      if (targetArray && targetArray[index]) {
        targetArray.splice(index, 1);
      }
    };

    const selectScheduleType = (value: any) => {
      if (value === selectedScheduleTypeItem.value) {
        return;
      }

      hideTypeView();
      resetScheduleData();

      if (value === "none") {
        selectedScheduleTypeItem.value = scheduleTypeOptions.value[0].value;
        isNone.value = true;
      }
      if (value === "hour") {
        selectedScheduleTypeItem.value = scheduleTypeOptions.value[1].value;
        isShowHour.value = true;
      }
      if (value === "day") {
        selectedScheduleTypeItem.value = scheduleTypeOptions.value[2].value;
        isShowDay.value = true;
      }
      if (value === "week") {
        selectedScheduleTypeItem.value = scheduleTypeOptions.value[3].value;
        isShowWeek.value = true;
      }
      if (value === "custom") {
        selectedScheduleTypeItem.value = scheduleTypeOptions.value[4].value;
        isShowCustom.value = true;
      }
    };

    const hideTypeView = () => {
      isNone.value = false;
      isShowHour.value = false;
      isShowDay.value = false;
      isShowWeek.value = false;
      isShowCustom.value = false;
    };

    const resetScheduleData = () => {
      minute.value = "00";
      time.value = "00:00";
      selectedDay.value = daysOfWeek.value[0].value;
    };

    const selectDay = (value: string) => {
      selectedDay.value = value;
    };

    const changeRetries = (e) => {
      reties.value = Number(e.target.value);
    };

    const resetData = () => {
      isValid.value = true;

      //TODO: ingestionPipeLine이 기본값으로 초기화 (수정용 처리도 해줘야함)
      ingestionPipeLine.value = {
        airflowConfig: {},
        loggerLevel: "",
        name: "", // 서버에서 넣어줘야함 ex) UUID 값
        displayName: "",
        owner: {
          id: "",
          type: "user",
        },
        pipelineType: "", // ex) StorageProfiler, Profiler, metadata
        service: {
          id: "", // 공통된 서비스 ID
          type: "", // 서비스 타입이 공통됨
        },
        sourceConfig: {
          config: {},
        },
      };

      profileSample.value = 50;
      sampleDataRowsCount.value = 50;

      Object.values(patternMap).forEach((array) => array.splice(0));

      isEnableDebug.value = false;
      isMarkDeletedTables.value = true;
      isIncludeTables.value = true;
      isIncludeViews.value = true;
      isIncludeOwners.value = true;

      selectedSampleTypeItem.value = sampleTypeOptions.value[0].value;

      isGenerateSampleData.value = true;
      isComputeMetric.value = true;

      isValidCronMessage.value = true;
      resetScheduleData();
      hideTypeView();
      isNone.value = true;
      selectedScheduleTypeItem.value = scheduleTypeOptions.value[0].value;
      cronExpression.value = "0 0 * * *";

      reties.value = 0;

      isEditModalStatus.value = false;
      pipelineId.value = "";
      cloneIngestionPipline.value = {};
    };

    const checkValidation = () => {
      if (isShowCustom.value) {
        try {
          cronParedMessage.value = cronstrue.toString(cronExpression.value);
          isValidCronMessage.value = true; // 유효한 크론 표현식임을 설정
        } catch (err: any) {
          cronParedMessage.value = "유효하지 않은 크론 표현식 입니다."; // 유효하지 않은 크론 표현식임을 설정
          isValidCronMessage.value = false;
        }
      } else {
        cronParedMessage.value = ""; // isShowCustom이 false일 때 빈 메시지 설정
      }

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

    const createIngestion = async () => {
      ingestionPipeLine.value.pipelineType = bindPipelineType.value;
      ingestionPipeLine.value.airflowConfig = airflowConfig.value;
      ingestionPipeLine.value.sourceConfig.config = config.value;
      ingestionPipeLine.value.loggerLevel = loggerLevel.value;
      ingestionPipeLine.value.service.id = serviceId.value;
      ingestionPipeLine.value.service.type = serviceType.value;
      ingestionPipeLine.value.owner.id = user.value.id;

      await $api(`/api/service-manage/pipelines`, {
        method: "POST",
        body: ingestionPipeLine.value,
      })
        .then((res: any) => {
          if (!res.data) {
            isValid.value = false;
            inValidMsg.value = "생성 실패했습니다. 잠시 후 다시 시도해주세요";
          }
        })
        .catch((err: any) => {
          console.log("err: ", err);
        });
    };

    const getPipeLineData = async (value: any) => {
      isEditModalStatus.value = true;
      await $api(`/api/service-manage/pipelines/${value}`)
        .then((res: any) => {
          if (res.result === 1) {
            pipelineId.value = res.data.id;
            if (res.data.pipelineType === "storageProfiler") {
              pipelineType.value = "profiler";
            } else {
              pipelineType.value = res.data.pipelineType;
            }

            if (pipelineType.value === "metadata") {
              modalTitle.value = "메타데이터 수집 수정";
            } else {
              modalTitle.value = "프로파일러 수집 수정";
            }
            serviceType.value = res.data.service.type;
            setEditModalData(res.data);
          } else {
            console.log("에러 발생");
          }
        })
        .catch((err: any) => {
          console.log("err: ", err);
        });
    };

    function generatePatchOperations(original, updated, basePath = "") {
      let operations = [];
      const ignorePaths = ["/airflowConfig/startDate"];

      // 원본에서 존재하지 않는 항목을 제거하는 경우
      for (const key in original) {
        const originalValue = original[key];
        const updatedValue = updated[key];
        const currentPath = `${basePath}/${key}`;

        if (ignorePaths.includes(currentPath)) {
          continue;
        }

        if (!(key in updated)) {
          operations.push({
            op: "remove",
            path: currentPath,
          });
        } else if (
          typeof originalValue === "object" &&
          typeof updatedValue === "object"
        ) {
          operations.push(
            ...generatePatchOperations(
              originalValue,
              updatedValue,
              currentPath,
            ),
          );
        } else if (originalValue !== updatedValue) {
          operations.push({
            op: "replace",
            path: currentPath,
            value: updatedValue,
          });
        }
      }

      // 업데이트된 항목 중 원본에 없는 항목을 추가하는 경우
      for (const key in updated) {
        if (!(key in original)) {
          const currentPath = `${basePath}/${key}`;
          if (!ignorePaths.includes(currentPath)) {
            operations.push({
              op: "add",
              path: currentPath,
              value: updated[key],
            });
          }
        }
      }

      // 'remove' 작업을 내림차순으로 정렬필요
      operations = operations.sort((a, b) => {
        if (a.op === "remove" && b.op === "remove") {
          return b.path.localeCompare(a.path);
        }
        return 0;
      });

      return operations;
    }

    const editIngestion = async (value: any) => {
      ingestionPipeLine.value.pipelineType = bindPipelineType.value;
      ingestionPipeLine.value.airflowConfig = airflowConfig.value;
      ingestionPipeLine.value.sourceConfig.config = config.value;
      ingestionPipeLine.value.loggerLevel = loggerLevel.value;

      let editPatchObj = generatePatchOperations(
        cloneIngestionPipline.value,
        ingestionPipeLine.value,
      );

      await $api(`/api/service-manage/pipelines/${pipelineId.value}`, {
        method: "PATCH",
        body: editPatchObj,
      });
    };

    return {
      isEditModalStatus,
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

      cronExpression,
      cronParedMessage,
      isNone,
      isShowHour,
      isShowDay,
      isShowWeek,
      isShowCustom,
      scheduleTypeOptions,
      selectedScheduleTypeItem,
      minute,
      time,
      daysOfWeek,
      selectedDay,

      reties,
      setModalTitle,
      setId,
      setPipelineType,
      setServiceType,
      setInvalidMessage,
      setIsValidCronParsedMessage,
      setIsValid,
      setCronExpression,
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

      selectScheduleType,
      selectDay,
      changeRetries,

      resetData,
      checkValidation,
      createIngestion,

      getPipeLineData,
      editIngestion,
    };
  },
);
