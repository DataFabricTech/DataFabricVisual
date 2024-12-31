<template>
  <!-- detail-section : 데이터 정보 -->
  <div :class="props.isAdmin ? 'data-box-wrap' : ''" :style="props.isAdmin ? 'padding-top: 5px; padding-bottom: 35px' : ''">
    <div class="detail-section" :class="showData ? 'open' : 'close'">
      <div class="mu-heading">
        <h4>데이터 프로파일링</h4>
        <div class="mu-item-group">
          <button type="button" class="mu-btn mu-btn-bg-non btn-section-toggle" @click="toggleShowData()">
            <span>{{ toggleShowDataText }}</span>
          </button>
        </div>
      </div>
      <!-- detail-box : DB 프로필 -->
      <div v-if="(db && db.status) || (table && table.status) || (column && column.status)">
        <div class="detail-box" v-if="showDbArea">
          <div class="mu-heading">
            <h5>DB 프로필</h5>
          </div>
          <table class="mu-formbox">
            <colgroup>
              <col style="width: 200px" />
              <col />
              <col style="width: 200px" />
              <col />
            </colgroup>
            <tbody>
              <tr>
              <th scope="col">행수</th>
              <!-- <th scope="col">테이블 크기(Byte)</th> -->
              <td>{{ comman(table.num_rows) }}</td>
              <th scope="col">테이블프로파일링 일시</th>
              <!-- <td>{{ table.total_size | numberComma }}</td> -->
              <td>{{ $yyyyMMddHHmmSS(table.update_date, " ") }}</td>
            </tr>
            </tbody>
          </table>
        </div>
        <!-- detail-box : 테이블 프로필 -->
        <div class="detail-box" v-if="showTableArea">
          <div class="mu-heading">
            <h5>테이블 프로필</h5>
          </div>
          <table class="mu-formbox">
            <colgroup>
              <col style="width: 200px" />
              <col />
              <col style="width: 200px" />
              <col />
            </colgroup>
            <tbody>
              <tr>
              <th scope="col">행수</th>
              <!-- <th scope="col">테이블 크기(Byte)</th> -->
              <td>{{ comman(table.num_rows) }}</td>
              <th scope="col">테이블프로파일링 일시</th>
              <!-- <td>{{ table.total_size | numberComma }}</td> -->
              <td>{{ $yyyyMMddHHmmSS(table.update_date, " ") }}</td>
            </tr>
            </tbody>
          </table>
        </div>

        <!-- detail-box : 컬럼 프로필 -->
        <div class="detail-box" v-if="showColumnArea">
          <div class="mu-heading">
            <h5>컬럼 프로필</h5>
          </div>
          <div class="table-scroll max-h630">
            <table class="mu-grid mu-grid-data" v-show="showData">
              <colgroup>
                <col style="width: 150px" />
              </colgroup>
              <thead>
                <tr>
                  <th scope="col">컬럼명</th>
                  <th scope="col">결측값이 있는 행수</th>
                  <th scope="col">고유값 수</th>
                  <th scope="col">최소값</th>
                  <th scope="col">최대값</th>
                  <th scope="col">평균</th>
                  <th scope="col">분산</th>
                  <th scope="col">편차</th>
                  <th scope="col">업데이트날짜</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in column" :key="index">
                  <td>
                    <span :title="item.name + '(' + item.type_name">{{ item.name ? item.name : "-" }}<br>({{ item.type_name ? item.type_name : "-" }})</span>
                  </td>
                  <td>
                    <span class="mu-ellipsis" :title="numberComma(item.null_count)">
                      {{ numberComma(item.null_count) }}
                    </span>
                  </td>
                  <td>
                    <span class="mu-ellipsis" :title="numberComma(item.distinct_count)">
                      {{ numberComma(item.distinct_count) }}
                    </span>
                  </td>

                  <template v-if="checkInvaildNumberColumn(item)">
                    <td>
                      <span class="mu-ellipsis" :title="numberComma(item.min_value)">
                        {{ numberComma(item.min_value) }}
                      </span>
                    </td>
                    <td>
                      <span class="mu-ellipsis" :title="numberComma(item.max_value)">
                        {{ numberComma(item.max_value) }}
                      </span>
                    </td>
                    <td>
                      <span class="mu-ellipsis" :title="numberComma(item.avg_value)">
                        {{ numberComma(item.avg_value) }}
                      </span>
                    </td>
                    <td>
                      <span class="mu-ellipsis" :title="numberComma(item.var_value)">
                        {{ numberComma(item.var_value) }}
                      </span>
                    </td>
                    <td>
                      <span class="mu-ellipsis" :title="numberComma(item.std_value)">
                        {{ numberComma(item.std_value) }}
                      </span>
                    </td>
                  </template>
                  <template v-else>
                    <td colspan="5">
                      <span class="mu-ellipsis">
                        해당 컬럼에는 숫자가 아닌 값들이 포함되어 있습니다.
                      </span>
                    </td>
                  </template>
                  <td v-html="$yyyyMMddHHmmSS_Sep(item.update_date, '<br>')"></td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
      <div class="data-result" v-else>
        <p class="nodata">데이터 프로파일이 존재하지 않습니다.</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import ObjectUtils from "@/utils/ObjectUtils";
const props = defineProps(["dataProfile", "columnCommentMap", "isAdmin"]);

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

// 랜더링 후 안보이도록 하기 위함
// onMounted(() => { showData.value = false})

const db = computed(() => ObjectUtils.getPropertyValue(props.dataProfile, "db", {}));

const table = computed(() =>
  ObjectUtils.getPropertyValue(props.dataProfile, "table", {})
);

const column = computed(() => {
  //console.log(ObjectUtils.getPropertyValue(props.dataProfile, "column", []))
  return ObjectUtils.getPropertyValue(props.dataProfile, "column", []);
});


const showDbArea = computed(() => Object.keys(db.value).length > 0);
const showTableArea = computed(() => Object.keys(table.value).length > 0);
const showColumnArea = computed(() => Object.keys(column.value).length > 0);

const comman = (val) => {
  if (val !== undefined) {
    return String(val).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
  } else {
    return "";
  }
};

const numberComma = (val) => {
  const num = Number(val);

  // 숫자가 유효하지 않으면 경고 메시지 반환
  if (isNaN(num)) {
    return "해당 컬럼에는 숫자가 아닌 값들이 포함되어 있습니다.";
  }

  // 정수와 소수 부분을 분리
  let [integerPart, decimalPart] = String(num).split(".");

  // 정수 부분에 쉼표 추가
  integerPart = integerPart.replace(/\B(?=(\d{3})+(?!\d))/g, ",");

  // 소수점이 있을 경우 처리
  return decimalPart ? `${integerPart}.${decimalPart}` : integerPart;
};

const getColumnComent = (column, sep) => {
  return column + sep + "(" + props.columnCommentMap[column] + ")";
}

const checkInvaildNumberColumn = (column) => {
  if( column.hasOwnProperty('min_value') &&
          column.hasOwnProperty('max_value') &&
          column.hasOwnProperty('avg_value') &&
          column.hasOwnProperty('var_value') &&
          column.hasOwnProperty('std_value') )
    return true;
  else
    return false;
}

</script>
<style>
.mu-grid tbody tr td { white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
</style>
