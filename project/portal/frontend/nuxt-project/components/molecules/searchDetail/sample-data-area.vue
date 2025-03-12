<template>
  <div :class="props.isAdmin ? 'data-box-wrap' : ''"
    :style="props.isAdmin ? 'padding-top: 5px; padding-bottom: 35px' : ''">
    <div class="detail-section" :class="showData ? 'open' : 'close'">
      <div class="mu-heading" :style="props.isModal ? 'margin-bottom: 0px;' : ''">
        <h4>샘플 데이터</h4>
        <div class="mu-item-group">
          <button type="button" class="mu-btn mu-btn-bg-non btn-section-toggle" title="접기" @click="toggleShowData()"
            style="margin-left: 10px">
            <span>{{ toggleShowDataText }}</span>
          </button>
        </div>
      </div>
      <div class="mu-heading" v-if="props.isModal">
        <h5>
          {{ props.entity.meta_name }} (
          {{ props.entity.description }} )
        </h5>
      </div>
      <div class="detail-box" v-if="props.list && props.list[0]">
        <!-- 샘플 데이터 최대 10개 출력 전체, 데이터 수 10개 미만인 경우 모든 데이터 출력/샘플 데이터 스크롤바, 컬럼 내용이 긴 경우 가로 스크롤을 이용하여 노출되지 않은 값까지 확인 가능 -->
        <div class="table-scroll max-h630 h400" v-if="props.userDataAuth == 1 || props.userDataAuth == 2">
          <table class="mu-grid mu-grid-data">
            <colgroup>
              <col style="width: 200px" :span="Object.keys(props.list[0]).length" />
            </colgroup>
            <thead>
              <tr>
                <th scope="col" v-for="(item, index) in props.list[0]" :key="index">
                  <span>{{ index }}</span><br>
                  <span style="
                        color: #9b9b9b;
                        margin-top: 2px;
                        overflow: hidden;
                        text-overflow: ellipsis;
                        display: -webkit-box;
                        -webkit-line-clamp: 2;
                        -webkit-box-orient: vertical;
                      " :title="descriptionEllipsis(index, props.entity)">
                    {{ descriptionEllipsis(index, props.entity) }}
                  </span>
                  <span :title="typeEllipsis(index, props.entity)">({{ typeEllipsis(index, props.entity) }})</span>
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
        <div class="data-result h300" v-else>
          <p class="nodata">데이터 접근 권한이 없습니다.</p>
        </div>
      </div>
      <div class="data-result" v-else>
        <p v-if="props.userDataAuth == 0" class="nodata">데이터 접근 권한이 없습니다.</p>
        <p v-else class="nodata">샘플 데이터가 존재하지 않습니다.</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps(["list", "entity", "isAdmin", "isModal", "userDataAuth"]);

const showData = ref(true);
const toggleShowDataText = ref("접기");

const toggleShowData = () => {
  showData.value = !showData.value;
  if (showData.value) {
    toggleShowDataText.value = "접기";
  } else {
    toggleShowDataText.value = "펼치기";
  }
};

const descriptionEllipsis = (val, entity) => {
  const attributes = entity.attributes

  let description;
  if (attributes) {
    const columns = attributes.columns
    if (columns) {
      columns.filter((item) => {
        if (item.meta_name === val) {
          if (item.description != null && item.description != "") {
            description = item.description;
          } else {
            description = "-"
          }
        }
      });
    }
  }

  if (description !== undefined) {
    if (description !== null) {
      return description;
    }
  } else {
    return "";
  }
};

const typeEllipsis = (val, entity) => {
  const attributes = entity.attributes

  let dataType;
  if (attributes) {
    const columns = attributes.columns
    if (columns) {
      columns.filter((item) => {
        if (item.meta_name === val) {
          if (item.column_type !== "") {
            dataType = item.column_type;
          }
        }
      });
    }
  }

  if (dataType !== undefined) {
    if (dataType !== null) {
      return dataType;
    }
  } else {
    return "";
  }
};

const nullToNa = (val) => {
  if (val === null) return "-";
  else {
    return val;
  }
};
</script>
