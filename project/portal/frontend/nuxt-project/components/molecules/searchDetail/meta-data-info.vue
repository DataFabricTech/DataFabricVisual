<template>
  <div class="detail-box">
    <div class="mu-heading">
      <h5 style="display:">메타속성</h5>
      <div class="btn-switch" style="display: flex; justify-content: flex-end; width: 95%;">
        <div class="mu-radio">
          <input type="radio" id="radio111" name="switch111" @click="toggleShowField(true)" :checked="showEmptyField">
          <label for="radio111"><span>표기</span></label>
        </div>
        <div class="mu-radio">
          <input type="radio" id="radio222" name="switch111" @click="toggleShowField(false)" :checked="!showEmptyField">
          <label for="radio222"><span>미표기</span></label>
        </div>
      </div>
    </div>
    <div class="max-h600">
      <table class="mu-grid mu-grid-data">
        <colgroup>
          <col style="width: 20%" />
          <col style="width: 80%" />
        </colgroup>
        <thead>
          <tr>
            <th scope="col">메타명</th>
            <th scope="col">메타값</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(value, key) in metaDataInfo" :key="key">
            <td style="vertical-align: middle;">{{ key }} <br> ({{ getKrKey(key) }})</td>
            <td>
              <!-- 데이터 타입이 object이고 key가 columns일 경우 -->
              <div v-if="getTypeByValue(key, value) === 'DIV'">
                <div class="table-scroll max-h550">
                  <table class="mu-grid mu-grid-data mu-grid-narrow">
                    <colgroup>
                      <col style="width:10%">
                      <col style="width:25%">
                      <col>
                      <col style="width:15%">
                    </colgroup>
                    <thead>
                      <tr>
                        <th scope="col">순번</th>
                        <th scope="col">컬럼명</th>
                        <th scope="col">컬럼 설명</th>
                        <th scope="col">타입</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="(item2, idx) in value">
                        <td>{{ idx + 1 }}</td>
                        <!-- <td><a @click="onClickDataName(key, item2)">{{ value[idx].name }}</a></td> -->
                        <td :title="value[idx].meta_name">{{ value[idx].meta_name }}</td>
                        <td :title="value[idx].description">{{ value[idx].description ? value[idx].description : "-" }}</td>
                        <td :title="value[idx].column_type">{{ value[idx].column_type }}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
              <!-- 데이터 타입이 object이고 key가 columns가 아닐 경우 -->
              <textarea
                v-if="getTypeByValue(key, value) === 'TEXT_AREA'"
                class="mu-area h100 tl"
                readonly="readonly"
                v-html="arrayToTextArea(value)"
                style="white-space: pre-line" />

              <!-- 데이터 타입이 object가 아니고 key가 table일 경우 -->
              <span v-if="getTypeByValue(key, value) === 'SPAN_LINK'">
                <a v-on:click="onClickDataName(key, value)">{{ value }}</a>
              </span>

              <!-- 데이터 타입이 object가 아니고 key가 table이 아닐 경우 -->
              <span
                v-if="
                  getTypeByValue(key, value) === '' &&
                  key.toLowerCase().indexOf('time') === -1 &&
                  key !== 'parameters'
                "
              >
                {{ value ? value : '-' }}
              </span>
              <span
                v-else-if="
                  key === 'parameters' && key.toLowerCase().indexOf('time') === -1
                "
              >
                {{ value.last_modified_time }}
              </span>
              <span
                v-if="
                  getTypeByValue(key, value) === '' &&
                  key.toLowerCase().indexOf('time') !== -1
                "
              >
                {{ value ? $yyyyMMddHHmmSS(value) : '-' }}
              </span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import ObjectUtils from "@/utils/ObjectUtils";
import { useUserStore } from "~/store/user";

const router = useRouter();

const userStore = useUserStore();

const emit = defineEmits(["onClickDataName"]);
const props = defineProps(["entity", "referredEntitiesAttributes"]);

// 메타속성 항목 순서 목록
const desiredOrder = ref([
  "meta_name",
  "description",
  "columns",
  "qualified_name",
  "create_time",
  "last_access_time",
  "system_name",
  "system_type",
  "table_type",
  "source_hash",
  "database_schema"
])
// 메타속성 제외 목록
const noEntries = ref([])

const showEmptyField = ref(false) // 메타속성 데이터 없는 필드 보여주기 여부
const fixEntries = ref([
  "meta_name",
  "description",
  "columns",
  "qualified_name",
  "create_time",
  "last_access_time",
  "system_name",
  "system_type",
  "table_type",
  "database_schema"
]) // 메타속성 데이터 없어도 보여줄 항목 여부

const metaDataInfo = computed(() => {
  const entity = props.entity
  if (entity != null && entity != undefined) {
    const obj = ObjectUtils.getPropertyValue(props.entity, 'attributes', {})

    // 화면 렌더에 필요한 형태로 데이터 가공
    const metaDataInfoResult = obj

    const columns = obj.columns;
    if (columns) {
      const uniqueColumns = columns.filter((el, idx) => {
        return columns.indexOf(el) === idx;
      });

      metaDataInfoResult.columns = uniqueColumns;
    }

    const sortedEntries = desiredOrder.value
      .map((key) => [key, metaDataInfoResult[key]])
      .concat(
        Object.entries(metaDataInfoResult).filter(
          ([key]) => !desiredOrder.value.includes(key) && !noEntries.value.includes(key)
        )
      );

    const sortedMetaData = Object.fromEntries(sortedEntries);

    sortedMetaData.create_time = entity.create_time
    sortedMetaData.last_access_time = entity.last_access_time
    sortedMetaData.qualified_name = entity.qualified_name
    sortedMetaData.system_name = entity.system_name

    // 값이 없는 데이터 필드 보여주기 여부
    if (!showEmptyField.value) removeEmptyFields(sortedMetaData)

    return sortedMetaData;
  }

  return {};
});

// 객체 키값이 없는 데이터 표기/미표기
const toggleShowField = (bool) => {
  showEmptyField.value = bool
}


// 객체 키값에 값이 없는 데이터 제거
const removeEmptyFields = (obj) => {
  for (let key in obj) {
    // 지우면 안되는 필드는 지우지 않도록 조건 추가
    if (fixEntries.value && fixEntries.value.length > 0) {
      let list = fixEntries.value.filter((el) => el == key)
      if (list && list.length > 0) continue
    }

    if (obj[key] === undefined || obj[key] === null || obj[key] === '') {
        delete obj[key];
    }
  }
}

// 한글명으로 수정
const getKrKey = (key) => {
  if (props.entity && props.entity != undefined) {
    if (key == "meta_name") return "메타데이터 명";
    if (key == "system_name") return "시스템 명";
    if (key == "description") return "메타데이터 description";
    if (key == "user_description") return "메타데이터 description";
    if (key == "columns") return "컬럼 목록";
    if (key == "column_type") return "컬럼 데이터 타입";
    if (key == "display_name") return "화면 표출 메타명";
    if (key == "qualified_name") return "객체 고유명"
    if (key == "create_time") return "메타 데이터 생성일자";
    if (key == "last_access_time") return "최종 접근 시간";
    if (key == "system_type") return "시스템 타입";
    if (key == "database_schema") return "스키마";
    if (key == "source_hash") return "소스 해시값";
    if (key == "tags") return "사용자 정의 태그";
    if (key == "owner") return "테이블 소유자 계정";
    if (key == "life_cycle") return "보관 주기";
    if (key == "retention") return "보존 기간";
    if (key == "table_type") return "테이블 형식";
    if (key == "table_constraints") return "테이블 제약 조건";
    if (key == "table_partition") return "테이블 파티션 키";
    if (key == "data_from") return "이 테이블의 원본소스";
    if (key == "data_to") return "이 테이블을 기반으로 만들어진 테이블";
    if (key == "view_definition") return "데이터 추출 SQL 원본"
    if (key == "file_format") return "파일 형식";
    if (key == "source_hash") return "Source hash of the entity";
    return key;
  }
  return key;
};

// 데이터 유형에 따른 컴포넌트 유형 계산
const getTypeByValue = (key, value) => {
  let type = "";

  if (typeof value === "object") {
    if (value && key === "columns") {
      type = "DIV";
    } else if (value && key === "columns") {
      type = "TEXT_AREA";
    }
  } else {
    if (key === "table") {
      type = "SPAN_LINK";
    }
  }

  return type;
};

// text area에 출력할 수 있는 형태로 데이터 가공
const arrayToTextArea = (textVal) => {
  let textHtml = "";

  if (typeof textVal === "object") {
    if (Array.isArray(textVal)) {
      textVal.forEach((text) => (textHtml += textHtml ? `\n${text}` : text));
    } else {
      textHtml += textVal.guid
        ? textVal.displayText
        : JSON.stringify(textVal, null, "\t");
    }
  }

  return textHtml;
};

// 선택된 컬럼 또는 테이블 엔티티 상세 조회 화면으로 이동
const onClickDataName = (item) => {
  console.log('go To Meta Page')
  return
  // emit("onClickData", item.guid);
  router.push(`/search/detail?guid=${item.guid}`);
  userStore.setGuid(item.guid);
  window.scrollTo(0, 0);
};

</script>
