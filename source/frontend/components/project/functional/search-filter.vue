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
              <BaseSelect class="select-lg" :data="props.keywordData" @select="setKeywordData"></BaseSelect>
              <label for="01" class="hidden-text">검색어</label>
              <BaseTextInput id="01" class="max-w-[522px] text-input-lg" v-model="keyword"></BaseTextInput>
            </div>
          </div>
          <div class="form-item">
            <div class="form-label">
              <strong class="form-title">기간 선택</strong>
            </div>
            <div class="form-content">
              <BaseSelect class="select-lg" :data="props.dateData" @select="setDateData"></BaseSelect>
              <DatePicker
                class="date-picker"
                v-model="date.range"
                :type="'date'"
                :disabled="date.disabled"
                @update:modelValue="updateDate"
              >
              </DatePicker>
              <div class="toggle">
                <BaseRadio
                  name="toggleDate"
                  id="toggle-today"
                  :value="TODAY"
                  :checked="date.default === TODAY"
                  @change="changeDateRange"
                >
                  오늘
                </BaseRadio>
                <BaseRadio
                  name="toggleDate"
                  id="toggle-week"
                  :value="WEEK"
                  :checked="date.default === WEEK"
                  @change="changeDateRange"
                  >7일</BaseRadio
                >
                <BaseRadio
                  name="toggleDate"
                  id="toggle-month-1"
                  :value="ONE_MONTH"
                  :checked="date.default === ONE_MONTH"
                  @change="changeDateRange"
                  >1개월
                </BaseRadio>
                <BaseRadio
                  name="toggleDate"
                  id="toggle-month-3"
                  :value="THREE_MONTH"
                  :checked="date.default === THREE_MONTH"
                  @change="changeDateRange"
                >
                  3개월
                </BaseRadio>
                <BaseRadio
                  name="toggleDate"
                  id="toggle-year"
                  :value="YEAR"
                  :checked="date.default === YEAR"
                  @change="changeDateRange"
                  >1년</BaseRadio
                >
                <BaseRadio
                  name="toggleDate"
                  id="toggle-all"
                  :value="ALL"
                  :checked="date.default === ALL"
                  @change="changeDateRange"
                  >전체</BaseRadio
                >
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
                :default-title="'연결정보 선택'"
                @select="selectConnection"
                @getName="setConnectionTags"
              ></BaseSelect>
              <BaseSelect
                class="select-lg"
                :is-check="true"
                :data="props.connectionTypeData"
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
                @select="selectModelTypeData"
                @getName="setModelTypeTags"
              ></BaseSelect>
              <BaseSelect
                class="select-lg"
                :is-check="true"
                :data="props.modelFormData"
                :default-title="'형식선택'"
                @select="selectModelFormData"
                @getName="setModelFormTags"
              ></BaseSelect>
              <BaseSelect
                class="select-lg"
                :is-check="true"
                :data="props.modelFormatData"
                :default-title="'포맷선택'"
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
import { ref, reactive, onMounted, defineEmits, defineProps, Ref } from "vue";
import moment from "moment";
const FORMAT = "yyyy-MM-DD";
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
    type: Array,
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
    type: Array,
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
    type: Array,
    default: null
  },
  /**
   * 연결 정보 유형 선택 select
   */
  connectionTypeData: {
    type: Array,
    default: null
  },
  /**
   * 도메인 select
   */
  domainData: {
    type: Array,
    default: null
  },
  /**
   * 데이터 모델 유형 select
   */
  modelTypeData: {
    type: Array,
    default: null
  },
  /**
   * 데이터 모델 형식 select
   */
  modelFormData: {
    type: Array,
    default: null
  },
  /**
   * 데이터 모델 포맷 select
   */
  modelFormatData: {
    type: Array,
    default: null
  }
});
const emit = defineEmits(["search", "reset", "close"]);

const keyword = ref(null);
const date: {
  range: string[] | null[];
  disabled: boolean;
  default: string;
} = reactive({
  range: [],
  disabled: false,
  default: THREE_MONTH
});
const range: Ref<Array<string>> = ref([]);
const detailSearch: {
  DATA_NAME: string;
  DATA_TYPE: string;
  DATA_FORMAT: string;
  CATEGORY: string;
  TAG: string;
  STORAGE_TYPE: string;
  CONNECTOR_NAME: string;
  CREATOR: string;
  START_DATE: string | null;
  END_DATE: string | null;
} = reactive({
  DATA_NAME: "",
  DATA_TYPE: "",
  DATA_FORMAT: "",
  CATEGORY: "",
  TAG: "",
  STORAGE_TYPE: "",
  CONNECTOR_NAME: "",
  CREATOR: "",
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

/**
 * 검색어 select
 * @param data
 */
function setKeywordData(data: string) {
  detailSearch.DATA_NAME = data;
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
      date.range[0] = formatDate(moment(today));
      break;
    case "WEEK":
      date.range[0] = formatDate(moment(today).subtract(1, "weeks"));
      break;
    case "1MONTH":
      date.range[0] = formatDate(moment(today).subtract(1, "months"));
      break;
    case "3MONTH":
      date.range[0] = formatDate(moment(today).subtract(3, "months"));
      break;
    case "YEAR":
      date.range[0] = formatDate(moment(today).subtract(1, "years"));
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
  console.log(data);
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
  console.log(value);
}

/**
 * 데이터 모델 포맷 선택
 * @param value
 */
function selectModelFormatData(value: any) {
  console.log(value);
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
function reset() {
  keyword.value = null;
  emit("reset", detailSearch);
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
const formatDate = (date: Date | object): string => {
  const momentDate = moment(date);
  return momentDate.format(FORMAT);
};

onMounted(() => {
  let today = new Date();
  let threeMonthsAgo = moment(today).subtract(3, "months");
  date.range.push(formatDate(threeMonthsAgo));
  date.range.push(formatDate(new Date()));
});
</script>
