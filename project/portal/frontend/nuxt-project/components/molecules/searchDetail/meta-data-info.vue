<template>
  <div class="detail-box">
    <div class="mu-heading">
      <h5>데이터 정보</h5>
      <div v-if="(props.typeName && props.typeName.toUpperCase() !== 'FILE') || props.isAdmin" class="btn-switch" style="display: flex; justify-content: flex-end; width: 94%;">
        <input type="checkbox" id ="toggle-slider" @click="toggleShowField(props.typeName)" :title="showEmptyField ? 'Show Empty Field' : 'Hide Empty Field'">
        <label class="show-label" for="toggle-slider" :title="showEmptyField ? 'Show Empty Field' : 'Hide Empty Field'">표기/미표기</label>
      </div>
    </div>
    <div v-if="!props.isAdmin && props.typeName && props.typeName.toUpperCase() === 'FILE'" class="max-h600">
      <table class="mu-formbox">
        <caption>
          데이터 정보
        </caption>
        <colgroup>
          <col style="width: 10%"/>
          <col style="width: 30%"/>
          <col style="width: 10%"/>
          <col style="width: 15%"/>
          <col style="width: 10%"/>
          <col style="width: 15%"/>
        </colgroup>
        <tbody>
          <tr>
            <th scope="row">데이터 ID</th>
            <td>{{ entity.meta_id ? entity.meta_id : '-' }}</td>
            <th scope="row">데이터 출처</th>
            <td colspan="3">{{ entity.attributes.data_from ? entity.attributes.data_from : '-' }}</td>
          </tr>
          <tr>
            <th scope="row">파일 명</th>
            <td>
              {{ entity.meta_name ? entity.meta_name : '-' }}
            </td>
            <th scope="row" rowspan="2">데이터 설명</th>
            <td colspan="3" rowspan="2">{{ entity.user_description ? entity.user_description : '-' }}</td>
          </tr>
          <tr>
            <th scope="row">데이터 명</th>
            <td>{{ entity.description ? entity.description : '-' }}</td>
          </tr>
          <tr>
            <th scope="row">데이터 고유명</th>
            <td>{{ entity.qualified_name ? entity.qualified_name : '-' }}</td>
            <th scope="row">파일 형식</th>
            <td>{{ entity.attributes.file_format ? entity.attributes.file_format : '-' }}</td>
            <th scope="row">파일 크기(Byte)</th>
            <td>{{ $numberComma(Math.floor(entity.attributes.size)) || $numberComma(Math.floor(entity.attributes.size)) == 0 ? $numberComma(Math.floor(entity.attributes.size)) : '-' }}</td>
          </tr>
          <tr>
            <th scope="row">데이터 위치</th>
            <td>{{ entity.attributes.full_path ? entity.attributes.full_path : '-' }}</td>
            <th scope="row">최종 수집일</th>
            <td>{{ $yyyyMMddHHmmSS(entity.meta_sync_time) ? $yyyyMMddHHmmSS(entity.meta_sync_time) : '-' }}</td>
            <th scope="row">공개 여부</th>
            <td>{{ entity.release ? getKrRelease(entity.release) : '-' }}</td>
          </tr>
        </tbody>
      </table>
      <div class="mu-heading">
        <h5></h5>
      </div>
      <table class="mu-formbox">
        <caption>
          시스템 정보
        </caption>
        <colgroup>
          <col />
        </colgroup>
        <tbody>
          <tr>
            <th scope="row">시스템 타입</th>
            <td>{{ entity.system_type ? entity.system_type : '-' }}</td>
            <th scope="row">시스템 명</th>
            <td>{{ entity.system_name ? entity.system_name : '-' }}</td>
            <th scope="row">등록자</th>
            <td>{{ entity.create_user ? entity.create_user : entity.create_by ? entity.create_by : '-' }}</td>
          </tr>
          <tr>
            <th scope="row">데이터 타입</th>
            <td>{{ entity.meta_type ? entity.meta_type : '-' }}</td>
            <th scope="row">파일 위치</th>
            <td>{{ entity.qualified_name ? entity.qualified_name.split(".")[1] : '-' }}</td>
            <th scope="row">등록일</th>
            <td>{{ $yyyyMMddHHmmSS(entity.create_time) ? $yyyyMMddHHmmSS(entity.create_time) : '-' }}</td>
          </tr>
        </tbody>
      </table>
      <div class="mu-heading" v-if="checkShowFileColumns()">
        <h5></h5>
      </div>
      <table class="mu-formbox" v-if="checkShowFileColumns()">
        <caption>
          스키마 정보
        </caption>
        <colgroup>
          <col style="width: 10%"/>
          <col style="width: 30%"/>
          <col style="width: 10%"/>
          <col style="width: 15%"/>
          <col style="width: 10%"/>
          <col style="width: 15%"/>
        </colgroup>
        <tbody>
          <tr style="border-top: 100px;">
            <th scope="row">컬럼 목록</th>
            <td colspan="5">
              <div>
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
                      <tr v-for="(item2, idx) in entity.attributes.columns">
                        <td>{{ idx + 1 }}</td>
                        <td :title="entity.attributes.columns[idx] && entity.attributes.columns[idx].meta_name ? entity.attributes.columns[idx].meta_name : '-'">{{ entity.attributes.columns[idx] && entity.attributes.columns[idx].meta_name ? entity.attributes.columns[idx].meta_name : '-'}}</td>
                        <td :title="entity.attributes.columns[idx] && entity.attributes.columns[idx].description ? entity.attributes.columns[idx].description : '-'">{{ entity.attributes.columns[idx] && entity.attributes.columns[idx].description ? entity.attributes.columns[idx].description : '-' }}</td>
                        <td :title="entity.attributes.columns[idx] && entity.attributes.columns[idx].column_type ? entity.attributes.columns[idx].column_type : '-'">{{ entity.attributes.columns[idx] && entity.attributes.columns[idx].column_type ? entity.attributes.columns[idx].column_type : '-' }}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      <div class="mu-heading" v-if="checkShowFileRdfs()">
        <h5></h5>
      </div>
      <table class="mu-formbox" v-if="checkShowFileRdfs()">
        <caption>
          상세 속성
        </caption>
        <colgroup>
          <col style="width: 10%"/>
          <col style="width: 30%"/>
          <col style="width: 10%"/>
          <col style="width: 15%"/>
          <col style="width: 10%"/>
          <col style="width: 15%"/>
        </colgroup>
        <tbody>
          <tr>
            <th scope="row">상세 속성</th>
            <td colspan="5">
              <div>
                <div class="table-scroll max-h550">
                  <table class="mu-grid mu-grid-data mu-grid-narrow">
                    <colgroup>
                      <col style="width:20%">
                      <col style="width:80%">
                    </colgroup>
                    <thead>
                      <tr>
                        <th scope="col">메타 명</th>
                        <th scope="col">메타 값</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="(item2, idx) in entity.attributes.rdfs">
                        <td :title="idx">{{ idx }}</td>
                        <td :title="entity.attributes.rdfs[idx]">{{ checkNumber(entity.attributes.rdfs[idx]) ? $numberComma(entity.attributes.rdfs[idx]) : entity.attributes.rdfs[idx] }}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-else class="max-h600">
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
                        <td :title="value[idx] && value[idx].meta_name ? value[idx].meta_name : '-'">{{ value[idx] && value[idx].meta_name ? value[idx].meta_name : '-'}}</td>
                        <td :title="value[idx] && value[idx].description ? value[idx].description : '-'">{{ value[idx] && value[idx].description ? value[idx].description : '-' }}</td>
                        <td :title="value[idx] && value[idx].type ? value[idx].type : '-'">{{ value[idx] && value[idx].type ? value[idx].type : '-' }}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
              <!-- 데이터 타입이 object가 아니고 key가 table일 경우 -->
              <span v-if="getTypeByValue(key, value) === 'SPAN_LINK'">
                <a v-on:click="onClickDataName(key, value)">{{ value }}</a>
              </span>

              <!-- 데이터 타입이 object가 아니고 key가 table이 아니고 값이 객체가 아닌 경우 -->
              <span
                v-if="
                  getTypeByValue(key, value) === '' &&
                  key.toLowerCase().indexOf('time') === -1 &&
                  key !== 'parameters' &&
                  !checkObject(value)
                "
                :title="value == 0 || value ? value : '-'"
                style="white-space: normal;"
              >
                {{ value == 0 || value ? value : '-' }}
              </span>
              <!-- 데이터 타입이 object가 아니고 key가 table이 아니고 값이 객체인 경우 -->
              <span v-else-if="
                getTypeByValue(key, value) === '' &&
                key.toLowerCase().indexOf('time') === -1 &&
                key !== 'parameters' &&
                checkObject(value)">
                <pre style="display: inline-block; text-align: left; white-space: pre-wrap;">{{ value }}</pre>
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

const { $numberComma } = useNuxtApp();

const router = useRouter();

const userStore = useUserStore();

const emit = defineEmits(["onClickDataName"]);
const props = defineProps(["entity", "referredEntitiesAttributes", "isAdmin", "typeName"]);

// 메타속성 항목 순서 목록
const desiredOrder = ref([
  "meta_id",
  "meta_name",
  "display_name",
  "description",
  "columns",
  "qualified_name",
  "create_time",
  "create_by",
  "update_time",
  "update_by",
  "last_access_time",
  "meta_sync_time",
  "system_id",
  "system_name",
  "system_type",
  "meta_type",
  "version",
  "status",
  "release",
])
// 메타속성 제외 목록
const noEntries = ref([])

const showEmptyField = ref(false) // 데이터 정보 데이터 없는 필드 보여주기 여부
const showFileEmptyField = ref(false) // 데이터 정보 컬럼 목록/ 상세 속성 보여주기 여부

const metaDataInfo = computed(() => {
  const entity = props.entity
  if (entity != null && entity != undefined) {
    const obj = ObjectUtils.getPropertyValue(entity, 'attributes', {})
    const typeName = ObjectUtils.getPropertyValue(entity, 'meta_type', "")

    // 화면 렌더에 필요한 형태로 데이터 가공
    const metaDataInfoResult = obj

    const key =  entity.system_type=='ElasticSearch' ? 'fields' : 'columns'
    const typeKey = entity.system_type=='ElasticSearch' ? 'field_type' : 'column_type'
    
    // 컬럼(필드) 메타 데이터
    const columns =  obj[key]
    if (columns) {
      if (Array.isArray(columns)) {
        const uniqueColumns = columns.filter((el, idx) => {
          return columns.indexOf(el) === idx;
        });

        // column_type, field_type 모두 "type" 으로 사용
        uniqueColumns.forEach((el)=>el.type = el[typeKey])
        metaDataInfoResult[key] = uniqueColumns;
      } else {
        metaDataInfoResult[key] = []
      }
    } else {
      metaDataInfoResult[key] = []
    }

    // desiredOrder의 'columns'를 system_type=='ElasticSearch' 인 경우 'fields'로 수정
    desiredOrder.value = desiredOrder.value.map(d=>{ 
      return d == 'columns' ? key : d
    })

    const sortedEntries = desiredOrder.value
      .map((key) => [key, metaDataInfoResult[key]])
      .concat(
        Object.entries(metaDataInfoResult).filter(
          ([key]) => !desiredOrder.value.includes(key) && !noEntries.value.includes(key)
        )
      );

    const sortedMetaData = Object.fromEntries(sortedEntries);

    // attributes 속성에 없는 데이터 추가
    sortedMetaData.meta_id = entity.meta_id
    sortedMetaData.meta_name = entity.meta_name
    sortedMetaData.display_name = entity.display_name
    sortedMetaData.description = entity.description
    sortedMetaData.status = entity.status
    sortedMetaData.life_cycle = entity.life_cycle
    sortedMetaData.create_time = entity.create_time
    sortedMetaData.create_by = entity.create_by
    sortedMetaData.update_time = entity.update_time
    sortedMetaData.update_by = entity.update_by
    sortedMetaData.last_access_time = entity.last_access_time
    sortedMetaData.version = entity.version
    sortedMetaData.release = entity.release
    sortedMetaData.meta_sync_time = entity.meta_sync_time
    sortedMetaData.qualified_name = entity.qualified_name
    sortedMetaData.system_id = entity.system_id
    sortedMetaData.system_name = entity.system_name
    sortedMetaData.system_type = entity.system_type
    sortedMetaData.meta_type = entity.meta_type
    if (typeName && typeName.toUpperCase() != 'FILE' && entity.system_type && entity.system_type.toUpperCase() != 'MYSQL') {
      sortedMetaData.database = getDbName(entity.qualified_name)
    }
    if (typeName && typeName.toUpperCase() != 'FILE' && entity.system_type && entity.system_type.toUpperCase() != 'HIVE') {
      sortedMetaData.schema = getSchemaName(entity.qualified_name)
    }

    // File에서 사이즈 단위 표기 조정
    if (typeName && typeName.toUpperCase() == 'FILE') {
      const byteSize = Math.floor(sortedMetaData.size)
      sortedMetaData.size = $numberComma(byteSize)
    }

    // 값이 없는 데이터 필드 보여주기 여부
    if (!showEmptyField.value) removeEmptyFields(sortedMetaData)

    return sortedMetaData;
  }

  return {};
});

// 객체 키값이 없는 데이터 표기/미표기
const toggleShowField = (typeName) => {
  showEmptyField.value = !showEmptyField.value
  showFileEmptyField.value = !showFileEmptyField.value
}

// 객체 키값에 값이 없는 데이터 제거
const removeEmptyFields = (obj) => {
  for (let key in obj) {
    const val = obj[key]

    const isEmptyObject = val && val.constructor === Object && Object.keys(val).length === 0
    const isEmptyArray = Array.isArray(val) && val.length === 0

    if (val === undefined || val === null || val === '' || isEmptyObject || isEmptyArray) {
      delete obj[key]
    }
  }

}

// 한글명으로 수정
const getKrKey = (key) => {
  if (props.entity && props.entity != undefined) {
    if (key == "meta_id") return "메타데이터 ID";
    if (key == "meta_name") return "메타데이터 명";
    if (key == "meta_type") return "메타데이터 타입";
    if (key == "system_id") return "시스템 ID";
    if (key == "system_name") return "시스템 명";
    if (key == "system_type") return "시스템 타입";
    if (key == "description") return "메타데이터 description";
    if (key == "user_description") return "메타데이터 description";
    if (key == "columns") return "컬럼 목록";
    if (key == "fields") return "필드 목록";
    if (key == "column_type") return "컬럼 데이터 타입";
    if (key == "display_name") return "화면 표출 메타명";
    if (key == "qualified_name") return "객체 고유명"
    if (key == "create_by") return "메타 데이터 생성자";
    if (key == "create_time") return "메타 데이터 생성일자";
    if (key == "update_by") return "메타 수정자";
    if (key == "update_time") return "메타 수정일";
    if (key == "last_access_time") return "최종 접근 시간";
    if (key == "meta_sync_time") return "최종 수집일";
    if (key == "database") return "데이터베이스";
    if (key == "database_schema") return "스키마";
    if (key == "schema") return "스키마";
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
    if (key == "rdfs") return "JSON타입의 값";
    if (key == "prefix") return "파일 경로 (bucket)"
    if (key == "size") return "파일 크기(byte)";
    if (key == "full_path") return "파일 전체 경로 (local path)";
    if (key == "source_URL") return "파일 소스 URL";
    if (key == "db") return "db";
    if (key == "version") return "버전";
    if (key == "status") return "상태값";
    if (key == "release") return "메타 공개 여부";
    return key;
  }
  return key;
};

// 데이터 유형에 따른 컴포넌트 유형 계산
const getTypeByValue = (key, value) => {
  let type = "";

  if (typeof value === "object") {
    if (value && ["columns","fields"].includes(key) ) {
      type = "DIV";
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
};

// DB명
const getDbName = (qualified_name) => {
  if (qualified_name) {
    return qualified_name.split(".")[1]
  }
  return "-"
}

// 스키마명
const getSchemaName = (qualified_name) => {
  if (qualified_name) {
    return qualified_name.split(".")[2]
  }
  return "-"
}

const checkObject = (value) => {
  return typeof value === "object" && value !== null
}

const checkNumber = (str) => {
  return str.trim() !== "" && !isNaN(str);
}

const checkShowFileColumns = () => {
  const entity = props.entity

  // 1. columns 필드 존재
  const isColumns = entity
        && entity.attributes
        && entity.attributes.columns
        && entity.attributes.columns.length > 0

  // 2. file_format 조건 ( csv, xls, xlsx, json, xml )
  const isFormat = entity
        && entity.attributes
        && entity.attributes.file_format
        && (entity.attributes.file_format.toUpperCase() === 'CSV'
          || entity.attributes.file_format.toUpperCase() === 'XLS'
          || entity.attributes.file_format.toUpperCase() === 'XLSX'
          || entity.attributes.file_format.toUpperCase() === 'JSON'
          || entity.attributes.file_format.toUpperCase() === 'XML')

  // 3. 없는 값 표시 여부 +
  return isColumns && isFormat
}

const checkShowFileRdfs = () => {
  const entity = props.entity

  // 1. rdfs 필드 및 값이 존재
  const isRdfs = entity
        && entity.attributes
        && entity.attributes.rdfs
        && Object.keys(entity.attributes.rdfs)
        && Object.keys(entity.attributes.rdfs).length > 0

  // 2. 없는 값 표시 여부 +
  return isRdfs

}

const getKrRelease = (release) => {
  if (release == 0) return 'secret'
  if (release == 1) return 'public'
  if (release == 2) return 'private'
  return release
}

</script>
