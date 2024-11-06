<template>
  <div class="detail-section" :class="showData ? 'open' : 'close'">
    <div class="mu-heading">
      <h4>샘플 데이터</h4>
      <div class="mu-item-group">
        <button
          type="button"
          class="mu-btn mu-btn-bg-non btn-section-toggle"
          title="접기"
          @click="toggleShowData()"
          style="margin-left: 10px"
        >
          <span>{{ toggleShowDataText }}</span>
        </button>
      </div>
    </div>
    <div class="detail-box" v-if="props.list.length > 0 && !props.sampleDataDenied">
      <!-- 샘플 데이터 최대 10개 출력 전체, 데이터 수 10개 미만인 경우 모든 데이터 출력/샘플 데이터 스크롤바, 컬럼 내용이 긴 경우 가로 스크롤을 이용하여 노출되지 않은 값까지 확인 가능 -->
      <div class="table-scroll">
        <table class="mu-grid mu-grid-data">
          <colgroup>
            <col style="width: 200px" :span="Object.keys(props.list[0]).length" />
          </colgroup>
          <thead>
            <tr>
              <th scope="col" v-for="(item, index) in props.list[0]" :key="index">
                <span>{{ index }}</span><br>
                <span class="color-light">
                  {{ commentEllipsis(index, props.entity) }}
                </span><br>
                <span style="border-bottom: 1px solid #666">{{
                  userDescriptionEllipsis(index, props.entity)
                }}</span><br>
                <span>({{ typeEllipsis(index, props.entity) }})</span>
              </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in props.list" :key="index">
              <td v-for="(ii, idx) in item">
                <span class="mu-ellipsis" :title="ii">
                  {{ nullToNa(ii) }}
                </span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="data-result" v-else>
      <p v-if="props.sampleDataDenied" class="nodata">데이터 접근 권한이 없습니다.</p>
      <p v-else class="nodata">샘플데이터가 존재하지 않습니다.</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import ObjectUtils from "@/utils/ObjectUtils";

const props = defineProps(["sampleDataDenied", "list", "entity"]);

const showData = ref(false);
const toggleShowDataText = ref("펼치기");

const toggleShowData = () => {
  showData.value = !showData.value;
  if (showData.value) {
    toggleShowDataText.value = "접기";
  } else {
    toggleShowDataText.value = "펼치기";
  }
};

const commentEllipsis = (val, entity) => {
  // 컬럼인 경우
  let typeName = ObjectUtils.getPropertyValue(entity, "entity/typeName", "")
  if (typeName == 'hive_column') {
    return ObjectUtils.getPropertyValue(entity, "entity/attributes/comment", "")
  }

  const referredEntities = Object.values(entity.referredEntities);
  let comment;
  referredEntities.filter((item) => {
    if (item.attributes.name === val) {
      if (item.attributes.comment !== "") {
        comment = item.attributes.comment;
      }
    }
  });
  if (comment !== undefined) {
    if (comment !== null) {
      return comment;
    }
  } else {
    return "";
  }
};

const typeEllipsis = (val, entity) => {
  let referredEntities = Object.values(entity.referredEntities);
  if (referredEntities.length === 0) {
    referredEntities = Object.values(entity);
  }
  let type;
  referredEntities.filter((item) => {
    if (item.attributes) {
      if (item.attributes.name === val) {
        if (item.attributes.type !== "") {
          type = item.attributes.type;
        }
      }
    }
  });
  if (type !== undefined) {
    if (type !== null) {
      return type;
    }
  } else {
    return "";
  }
};

const userDescriptionEllipsis = (val, entity) => {
  // 컬럼인 경우
  let typeName = ObjectUtils.getPropertyValue(entity, "entity/typeName", "")
  if (typeName == 'hive_column') {
    return ObjectUtils.getPropertyValue(entity, "entity/attributes/userDescription", "")
  }

  const referredEntities = Object.values(entity.referredEntities);

  let userDescription;
  referredEntities.filter((item) => {
    if (item.attributes.name === val) {
      if (item.attributes.userDescription !== "") {
        userDescription = item.attributes.userDescription;
      }
    }
  });
  if (userDescription !== undefined) {
    if (userDescription !== null) {
      return userDescription;
    }
  } else {
    return "";
  }
};

const nullToNa = (val) => {
  if (val === null) return "N/A";
  else {
    return val;
  }
};
</script>
