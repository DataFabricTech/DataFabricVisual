<template>
  <div class="detail-box">
    <div class="mu-heading">
      <h5>메타속성</h5>
    </div>
    <div class="table-wrap">
      <table class="mu-formbox">
        <colgroup>
          <col style="width: 200px" />
          <col style="width: auto" />
        </colgroup>
        <tbody>
          <tr v-for="(value, key) in metaDataInfo" :key="key">
            <th scope="row">{{ getKrKey(key) }}</th>
            <td>
              <!-- 데이터 타입이 object이고 key가 columns일 경우 -->
              <div
                v-if="getTypeByValue(key, value) === 'DIV'"
              >
                <span v-for="(item2, idx) in value" :key="idx">
                  <a v-on:click="arrayToLinkGuid(key, item2)">
                    {{ value[idx] }}({{ refComment[idx] }})</a
                  >
                  &emsp;
                  <!-- <br v-if="(idx + 1) % 10 == 0" /> -->
                </span>
              </div>
              <!-- 데이터 타입이 object이고 key가 columns가 아닐 경우 -->
              <textarea
                v-if="getTypeByValue(key, value) === 'TEXT_AREA'"
                class="mu-area h100 tl"
                readonly="readonly"
                v-html="arrayToTextArea(value)"
              />

              <!-- 데이터 타입이 object가 아니고 key가 table일 경우 -->
              <span v-if="getTypeByValue(key, value) === 'SPAN_LINK'">
                <a v-on:click="arrayToLinkGuid(key, value)">{{ value }}</a>
              </span>

              <!-- 데이터 타입이 object가 아니고 key가 table이 아닐 경우 -->

              <span
                v-if="
                  getTypeByValue(key, value) === '' &&
                  key.toLowerCase().indexOf('time') === -1 &&
                  key !== 'parameters'
                "
              >
                {{ value }}
              </span>
              <!-- <span
                v-else-if="
                  key === 'parameters' && key.toLowerCase().indexOf('time') === -1 && ( value !== undefined && value.last_modified_time !== undefined )
                "
              >
                {{ getUNIXDate(value.last_modified_time) }}
              </span> -->
              <span
                v-else-if="
                  key === 'parameters'
                "
              >
                {{ value && value.modified_date ? getModifiedDate(value.modified_date) : '-' }}
              </span>
              <span
                v-if="
                  getTypeByValue(key, value) === '' &&
                  key.toLowerCase().indexOf('time') !== -1
                "
              >
                {{ value }}
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
const props = defineProps(["entity", "referredEntitiesAttributes", "metaDataInfoResult", "refComment", "allowColumnInfo"]);

const refComment = ref([]);
const relationshipAttributes = computed(() =>
  ObjectUtils.getPropertyValue(props.entity, "entity/relationshipAttributes", "")
);
const metaDataInfo = computed(() => {
  const obj = ObjectUtils.getPropertyValue(props.entity, "entity/attributes", {});
  const relationshipAttributes_l = relationshipAttributes.value;

  const metaDataInfoResult = props.metaDataInfoResult;
  refComment.value = props.refComment

  // 화면 렌더에 필요한 형태로 데이터 가공
  /*
  const metaDataInfoResult = {};
  Object.keys(obj).forEach((key) => {
    if (typeof obj[key] !== "object") {
      metaDataInfoResult[key] = obj[key];
    } else {
      if (Array.isArray(obj[key])) {
        const guidByDisplayText = [];
        if (key === "columns") {
          props.referredEntitiesAttributes.filter((ref_item) => {
            obj[key].forEach((value) => {
              if (value.guid) {
                relationshipAttributes_l[key].forEach((attr) => {
                  if (attr.guid && attr.guid === value.guid) {
                    if (ref_item.name === attr.displayText) {
                      refComment.value.push(ref_item.comment);
                      value.displayText = attr.displayText;
                      guidByDisplayText.push(attr.displayText);
                    }
                  }
                });
              }
            });
          });
        } else {
          obj[key].forEach((value) => {
            if (value.guid) {
              relationshipAttributes_l[key].forEach((attr) => {
                if (attr.guid && attr.guid === value.guid) {
                  value.displayText = attr.displayText;
                  guidByDisplayText.push(attr.displayText);
                }
              });
            }
          });
        }
        metaDataInfoResult[key] = guidByDisplayText;
      } else {
        if (obj[key] != null) {
          metaDataInfoResult[key] =
            obj[key].guid != null ? relationshipAttributes_l[key].displayText : obj[key];
        }
      }
    }
  });
  */

  const columns = metaDataInfoResult.columns;
  if (columns) {
    const uniqueColumns = columns.filter((el, idx) => {
      return columns.indexOf(el) === idx;
    });

    metaDataInfoResult.columns = uniqueColumns;
  }

  let desiredOrder = []; // 순서
  let noEntries = []; // 미사용 항목

  // 속성에 따라 순서 및 미표시 항목 설정
  if (props.entity && props.entity != undefined) {
    if (props.entity.entity && props.entity.entity != undefined) {
      let type = props.entity.entity.typeName;
      if (type == "hive_table") {
        desiredOrder = [
          "name",
          "comment",
          "userDescription",
          "columns",
          "parameters",
          "db",
        ];
        noEntries = [
          "owner",
          "temporary",
          "qualifiedName",
          "partitionKeys",
          "parameters",
          "displayName",
          "description",
          "tableType",
          "createTime",
          "lastAccessTime",
          "aliases",
          "replicatedTo",
          "replicatedFrom",
          "sd",
          "retention"
        ];

        // View 테이블 미사용 항목
        noEntries.push("viewExpandedText");
        noEntries.push("viewOriginalText");
      } else if (type == "hive_column") {
        desiredOrder = ["table", "name", "comment", "userDescription"];
        noEntries = ["owner", "qualifiedName", "position", "type", "description", "replicatedTo", "replicatedFrom", "displayName"];
      }
    }
  }

  const sortedEntries = desiredOrder
    .map((key) => [key, metaDataInfoResult[key]])
    .concat(
      Object.entries(metaDataInfoResult).filter(
        ([key]) => !desiredOrder.includes(key) && !noEntries.includes(key)
      )
    );

  const sortedMetaData = Object.fromEntries(sortedEntries);

  // 사용하지 않는 속성 제거
  if (props.entity && props.entity != undefined) {
    if (props.entity.entity && props.entity.entity != undefined) {
      let type = props.entity.entity.typeName;
      if (type == "hive_table") {
      } else if (type == "hive_column") {
      }
    }
  }

  if(!props.allowColumnInfo){
    delete sortedMetaData.columns;
  }

  return sortedMetaData;
});

// 한글명
const getUNIXDate = (dt) => {
  // Unix 타임스탬프 (초 단위)
  const unixTimestamp = dt;

  // 초를 밀리초로 변환
  const date = new Date(unixTimestamp * 1000);

  // 각 요소 추출
  const year = date.getUTCFullYear();
  const month = String(date.getUTCMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
  const day = String(date.getUTCDate()).padStart(2, '0');
  const hours = String(date.getUTCHours()).padStart(2, '0');
  const minutes = String(date.getUTCMinutes()).padStart(2, '0');
  const seconds = String(date.getUTCSeconds()).padStart(2, '0');

  // yyyy-MM-dd HH:mm:ss 형식으로 포맷
  const formattedDate = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;

  return formattedDate
}

// 한글명으로 수정
const getKrKey = (key) => {
  if (props.entity && props.entity != undefined) {
    if (props.entity.entity && props.entity.entity != undefined) {
      let type = props.entity.entity.typeName;
      if (type == "hive_table") {
        if (key == "name") return "데이터 정보(테이블)";
        if (key == "comment") return "데이터 설명";
        if (key == "userDescription") return "사용자 데이터 설명";
        if (key == "columns") return "테이블 컬럼 정보";
        if (key == "lastAccessTime") return "최종 업데이트 날짜";
        if (key == "parameters") return "최종 업데이트 날짜"; // parameters의 lastAccessTime으로 이용
        if (key == "db") return "데이터베이스";
        return key;
      } else if (type == "hive_column") {
        if (key == "table") return "대상 테이블";
        if (key == "name") return "데이터 정보(컬럼)";
        if (key == "comment") return "데이터 설명";
        if (key == "userDescription") return "사용자 데이터 설명";
        return key;
      } else {
        return key;
      }
    } else {
      return key;
    }
  } else {
    return key;
  }
};

// 최종 업데이트 날짜 포맷 수정
const getModifiedDate = (strDate) => {
  // 연도, 월, 일 부분을 추출하고 포맷 변경
  if (strDate) {
    return `${strDate.slice(0, 4)}-${strDate.slice(4, 6)}-${strDate.slice(6, 8)} ${strDate.slice(9)}`;
  } else {
    return '-'
  }
}

// 데이터 유형에 따른 컴포넌트 유형 계산
const getTypeByValue = (key, value) => {
  let type = "";

  if (typeof value === "object") {
    // if (value && key === 'columns') {
    //     type = 'DIV';
    // } else if (value && key !== 'columns') {
    //     type = 'TEXT_AREA';
    // }
    if (value && (key === "테이블 컬럼 정보" || key === "columns")) {
      type = "DIV";
    } else if (value && (key === "테이블 컬럼 정보" || key === "columns")) {
      type = "TEXT_AREA";
    }
  } else {
    if (key === "table") {
      type = "SPAN_LINK";
    }
  }

  return type;
};

// 컬럼 또는 테이블 링크 클릭 이벤트 핸들러
const arrayToLinkGuid = (key, value) => {
  if (key === "columns") {
    if (metaDataInfo.value.columns) {
      const matched = relationshipAttributes.value.columns.find(
        (el) => el.displayText === value
      );
      if (matched) {
        onClickDataName(matched);
      }
    }
  } else {
    if (metaDataInfo.value.table) {
      const table = ObjectUtils.getPropertyValue(
        relationshipAttributes.value,
        "table",
        null
      );
      if (table && table.displayText && table.displayText === value) {
        onClickDataName(table);
      }
    }
  }
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
  // emit("onClickData", item.guid);
  router.push(`/search/detail?guid=${item.guid}`);
  userStore.setGuid(item.guid);
  window.scrollTo(0, 0);
};
</script>
