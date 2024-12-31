<template>
  <div class="detail-box">
    <div class="mu-heading">
      <h5>메타속성</h5>
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
                        <td :title="value[idx].name">{{ value[idx].name }}</td>
                        <td :title="value[idx].description">{{ value[idx].description ? value[idx].description : "-" }}</td>
                        <td :title="value[idx].dataType">{{ value[idx].dataType }}</td>
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

const desiredOrder = ref([
  "name", "description", "columns", "createtime", "lastaccesstime", "systemType", "tableType", "sourceHash", "databaseSchema"
]) // 메타속성 항목 순서 목록
const noEntries = ref([
  "viewDefinition"
]) // 메타속성 제외 목록

const metaDataInfo = computed(() => {
  const entity = props.entity
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

  sortedMetaData.createtime = entity.createtime
  sortedMetaData.lastaccesstime = entity.lastaccesstime
  return sortedMetaData;
});

// 한글명으로 수정
const getKrKey = (key) => {
  if (props.entity && props.entity != undefined) {
    if (key == "name") return "시스템명";
    if (key == "description") return "메타데이터 description";
    if (key == "columns") return "컬럼 목록";
    if (key == "createtime") return "메타 데이터 생성일자";
    if (key == "lastaccesstime") return "최종 접근 시간";
    if (key == "systemType") return "시스템 타입";
    if (key == "tableType") return "테이블 형식";
    if (key == "sourceHash") return "Source hash of the entity";
    if (key == "databaseSchema") return "스키마";
    if (key == "sourceHash") return "소스 해시값";
    if (key == "tags") return "사용자 정의 태그";
    if (key == "owner") return "테이블 소유자 계정";
    // if (key == "domain") return "도메인";
    // if (key == "extension") return "소스 해시값";
    if (key == "lifeCycle") return "보관 주기";
    // if (key == "sourceUrl") return "소스 URL";
    if (key == "fileFormat") return "파일 형식";
    if (key == "displayName") return "화면 표출 메타명";
    // if (key == "dataProducts") return "데이터 제품";
    if (key == "tablePartition") return "테이블 파티션 키";
    if (key == "retentionPeriod") return "보존 기간";
    if (key == "tableConstraints") return "테이블 제약 조건";
    if (key == "viewDefinition") return "데이터 추출 SQL 원본"
    // if (key == "tableProfilerConfig") return "테이블 프로파일러 설정";
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
