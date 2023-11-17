<template>
  <div class="search-filter" v-if="props.toggle">
    <div class="search-filter-body">
      <form action="" class="form flex-row items-center">
        <div class="form-list">
          <div class="form-item">
            <div class="form-label">
              <strong class="form-title">검색어</strong>
            </div>
            <div class="form-content">
              <BaseSelect
                class="select-lg"
                :data="props.keywordData"
                :default-value="defaultValue.keyword"
                @select="setKeywordData"
              ></BaseSelect>
              <label for="01" class="hidden-text">검색어</label>
              <BaseTextInput id="01" class="max-w-[522px] text-input-lg" v-model="keyword"></BaseTextInput>
            </div>
          </div>
          <div class="form-item">
            <div class="form-label">
              <strong class="form-title">기간 선택</strong>
            </div>
            <div class="form-content">
              <BaseSelect
                class="select-lg"
                :data="props.dateData"
                :default-value="defaultValue.date"
                @select="setDateData"
              ></BaseSelect>
              <DatePicker
                class="date-picker date-picker-lg"
                v-model="date.range"
                :type="'date'"
                :disabled="date.disabled"
                @update:modelValue="updateDate"
              >
              </DatePicker>
              <div class="toggle toggle-lg">
                <BaseRadio
                  v-for="dateRadio in dateRadioList"
                  name="toggleDate"
                  :id="dateRadio.id"
                  :value="dateRadio.value"
                  :checked="date.default === dateRadio.value"
                  @change="changeDateRange"
                >
                  {{ dateRadio.name }}
                </BaseRadio>
              </div>
            </div>
          </div>
          <div class="form-item">
            <div class="form-label">
              <strong class="form-title">연결정보</strong>
            </div>
            <div class="form-content">
              <BaseSelect
                class="select-lg"
                :is-check="true"
                :data="props.connectionData"
                :default-value="defaultValue.connection"
                :default-title="'연결정보 선택'"
                @select="selectConnection"
                @getName="setConnectionTags"
              ></BaseSelect>
              <BaseSelect
                class="select-lg"
                :is-check="true"
                :data="props.connectionTypeData"
                :default-value="defaultValue.connectionType"
                :default-title="'유형선택'"
                @select="selectConnectionType"
              ></BaseSelect>
            </div>
          </div>
          <div class="form-item">
            <div class="form-label">
              <strong class="form-title">도메인</strong>
            </div>
            <div class="form-content">
              <BaseSelect
                class="select-lg"
                :is-check="true"
                :data="props.domainData"
                :default-value="defaultValue.domain"
                @select="selectDomain"
                @getName="setDomainTags"
              ></BaseSelect>
            </div>
          </div>
          <div class="form-item">
            <div class="form-label">
              <strong class="form-title">데이터모델</strong>
            </div>
            <div class="form-content">
              <BaseSelect
                class="select-lg"
                :is-check="true"
                :data="props.modelTypeData"
                :default-title="'유형선택'"
                :default-value="defaultValue.modelType"
                @select="selectModelTypeData"
                @getName="setModelTypeTags"
              ></BaseSelect>
              <BaseSelect
                class="select-lg"
                :is-check="true"
                :data="props.modelFormData"
                :default-title="'형식선택'"
                :default-value="defaultValue.modelForm"
                @select="selectModelFormData"
                @getName="setModelFormTags"
              ></BaseSelect>
              <BaseSelect
                class="select-lg"
                :is-check="true"
                :data="props.modelFormatData"
                :default-title="'포맷선택'"
                :default-value="defaultValue.modelFormat"
                @select="selectModelFormatData"
                @getName="setModelFormatTags"
              ></BaseSelect>
            </div>
          </div>
        </div>
        <div class="form-action">
          <BaseButton class="button-lg button-primary" @click="search">
            <span class="button-text">검색</span>
          </BaseButton>
          <BaseButton class="button-lg button-negative" @click="reset">
            <span class="button-text">초기화</span>
          </BaseButton>
        </div>
      </form>
    </div>
    <div class="search-filter-footer">
      <div class="h-group gap-[16px]">
        <BaseBadge class="bg-marker-cyan" v-for="tag in tags.connection">{{ tag }}</BaseBadge>
        <BaseBadge class="bg-marker-purple" v-for="tag in tags.domain">{{ tag }}</BaseBadge>
        <BaseBadge class="bg-marker-cyan" v-for="tag in tags.modelType">{{ tag }}</BaseBadge>
        <BaseBadge class="bg-marker-purple" v-for="tag in tags.modelForm">{{ tag }}</BaseBadge>
        <BaseBadge class="bg-marker-cyan" v-for="tag in tags.modelFormat">{{ tag }}</BaseBadge>
      </div>
      <BaseButton class="button-ghost" @click="closeToggle">
        <span class="button-text">상세닫기</span>
      </BaseButton>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, defineEmits, defineProps } from "vue";
import type { Select } from "~/components/base/select";
import dayjs, { Dayjs } from "dayjs";
const FORMAT = "YYYY-MM-DD";
const TODAY = "TODAY";
const ALL = "ALL";
const WEEK = "WEEK";
const ONE_MONTH = "1MONTH";
const THREE_MONTH = "3MONTH";
const YEAR = "YEAR";

const props = defineProps({
  toggle: {
    type: Boolean,
    default: true
  },
  /**
   * 검색어 ALL
   */
  keywordData: {
    type: Array as () => Array<Select>,
    default: [
      {
        key: "전체",
        value: "ALL"
      },
      {
        key: "데이터모델명",
        value: "ALL"
      },
      {
        key: "태그",
        value: "ALL"
      },
      {
        key: "소유자",
        value: "ALL"
      }
    ]
  },
  /**
   * 기간 선택 select
   */
  dateData: {
    type: Array as () => Array<Select>,
    default: [
      {
        key: "전체",
        value: "ALL"
      },
      {
        key: "최근수정일자",
        value: "ALL"
      },
      {
        key: "최근등록일자",
        value: "ALL"
      },
      {
        key: "최근동기화일자",
        value: "ALL"
      }
    ]
  },
  /**
   * 연결 정보 select
   */
  connectionData: {
    type: Array as () => Array<Select>,
    default: null
  },
  /**
   * 연결 정보 유형 선택 select
   */
  connectionTypeData: {
    type: Array as () => Array<Select>,
    default: null
  },
  /**
   * 도메인 select
   */
  domainData: {
    type: Array as () => Array<Select>,
    default: null
  },
  /**
   * 데이터 모델 유형 select
   */
  modelTypeData: {
    type: Array as () => Array<Select>,
    default: null
  },
  /**
   * 데이터 모델 형식 select
   */
  modelFormData: {
    type: Array as () => Array<Select>,
    default: null
  },
  /**
   * 데이터 모델 포맷 select
   */
  modelFormatData: {
    type: Array as () => Array<Select>,
    default: null
  }
});
const emit = defineEmits(["search", "reset", "close"]);

const keyword = ref(null);
const date: {
  range: string[];
  disabled: boolean;
  default: string;
} = reactive({
  range: [],
  disabled: false,
  default: THREE_MONTH
});
const range: ref<Array<string>> = ref([]);
const detailSearch: {
  KEYWORD_TYPE: string;
  DATE_TYPE: string;
  CONNECTOR_NAME: string;
  STORAGE_TYPE: string;
  DATA_TYPE: string;
  DATA_FORMAT: string;
  START_DATE: string | null;
  END_DATE: string | null;
} = reactive({
  KEYWORD_TYPE: "",
  DATE_TYPE: "",
  CONNECTOR_NAME: "",
  STORAGE_TYPE: "",
  DATA_TYPE: "",
  DATA_FORMAT: "",
  START_DATE: "",
  END_DATE: ""
});
const tags: {
  connection: string[];
  domain: string[];
  modelType: string[];
  modelForm: string[];
  modelFormat: string[];
} = reactive({
  connection: [],
  domain: [],
  modelType: [],
  modelForm: [],
  modelFormat: []
});
const defaultValue: {
  keyword: any | null;
  date: any | null;
  connection: any | null;
  connectionType: any | null;
  domain: any | null;
  modelType: any | null;
  modelForm: any | null;
  modelFormat: any | null;
} = reactive({
  keyword: null,
  date: null,
  connection: null,
  connectionType: null,
  domain: null,
  modelType: null,
  modelForm: null,
  modelFormat: null
});
const dateRadioList = [
  { id: "toggle-today", value: TODAY, name: "오늘" },
  { id: "toggle-week", value: WEEK, name: "7일" },
  { id: "toggle-month-1", value: ONE_MONTH, name: "1개월" },
  {
    id: "toggle-month-3",
    value: THREE_MONTH,
    name: "3개월"
  },
  {
    id: "toggle-year",
    value: YEAR,
    name: "1년"
  },
  {
    id: "toggle-all",
    value: ALL,
    name: "전체"
  }
];
/**
 * 검색어 select
 * @param data
 */
function setKeywordData(data: string) {
  detailSearch.KEYWORD_TYPE = data;
}

/**
 * 기간 선택
 * @param data
 */
function setDateData(data: string) {
  detailSearch.DATA_TYPE = data;
}

/**
 * DatePicker
 * @param data
 */
function updateDate(data: any) {
  date.range = data;
  detailSearch.START_DATE = data[0];
  detailSearch.END_DATE = data[1];
}

/**
 * DatePicker btn click
 * @param dateRange
 */
function changeDateRange(dateRange: string) {
  date.disabled = false;
  date.default = dateRange;
  let today = new Date();
  date.range[1] = formatDate(today);
  switch (dateRange) {
    case "TODAY":
      date.range[0] = formatDate(dayjs(today));
      break;
    case "WEEK":
      date.range[0] = formatDate(dayjs(today).subtract(1, "weeks"));
      break;
    case "1MONTH":
      date.range[0] = formatDate(dayjs(today).subtract(1, "months"));
      break;
    case "3MONTH":
      date.range[0] = formatDate(dayjs(today).subtract(3, "months"));
      break;
    case "YEAR":
      date.range[0] = formatDate(dayjs(today).subtract(1, "years"));
      break;
    case "ALL":
      date.range = [];
      date.disabled = true;
      break;
  }

  if (date.range.length === 0) {
    detailSearch.START_DATE = null;
    detailSearch.END_DATE = null;
  } else {
    detailSearch.START_DATE = date.range[0];
    detailSearch.END_DATE = date.range[1];
  }
}

/**
 * 연결 정보 선택
 * @param data
 */
function selectConnection(data: string) {
  detailSearch.CONNECTOR_NAME = data;
}

/**
 * 연결 정보 유형 선택
 * @param data
 */
function selectConnectionType(data: string) {
  detailSearch.STORAGE_TYPE = data;
}
/**
 * 도메인 선택
 * @param data
 */
function selectDomain(data: string) {
  console.log(detailSearch);
}

/**
 * 데이터 모델 유형 선택
 * @param value
 */
function selectModelTypeData(value: any) {
  console.log(value);
}

/**
 * 데이터 모델 형식 선택
 * @param value
 */
function selectModelFormData(value: any) {
  detailSearch.DATA_TYPE = value;
}

/**
 * 데이터 모델 포맷 선택
 * @param value
 */
function selectModelFormatData(value: any) {
  detailSearch.DATA_FORMAT = value;
}

/**
 * 검색
 */
function search() {
  let search = { keyword: keyword.value, detailSearch };
  emit("search", search);
}

/**
 * 초기화
 */
async function reset() {
  await resetDefaultValue(defaultValue);
  keyword.value = null;
  emit("reset", detailSearch);
  changeDateRange(THREE_MONTH);
  defaultValue.keyword = props.keywordData[0].value;
  defaultValue.date = props.dateData[0].value;
}

function resetDefaultValue(defaultValue: any | null) {
  for (const key in defaultValue) {
    if (key === "connection" || "connectionType" || "domain" || "modelType" || "modelForm" || "modelFormat") {
      defaultValue[key] = [];
    } else {
      defaultValue[key] = null;
    }
  }
}
/**
 * 토글 관련
 */
function closeToggle() {
  emit("close", false);
}

/**
 * 태그 관련
 * @param data
 */
function setConnectionTags(data: []) {
  tags.connection = data;
}
function setDomainTags(data: []) {
  tags.domain = data;
}
function setModelTypeTags(data: []) {
  tags.modelType = data;
}
function setModelFormTags(data: []) {
  tags.modelForm = data;
}
function setModelFormatTags(data: []) {
  tags.modelFormat = data;
}

/**
 * 데이트 포맷
 * @param date
 */
const formatDate = (date: Date | Dayjs): string => {
  return dayjs(date).format(FORMAT);
};
onMounted(async () => {
  let url = "/portal/v1/searchItems";
  let searchItems = await $fetch(url);

  let today = new Date();
  let threeMonthsAgo = dayjs(today).subtract(3, "months");
  date.range.push(formatDate(threeMonthsAgo));
  date.range.push(formatDate(new Date()));
});
</script>
