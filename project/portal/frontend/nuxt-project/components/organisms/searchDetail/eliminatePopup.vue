<template>
  <!-- 모달팝업 -->
  <div class="mu-modal" v-if="props.eliminateModal.toggle">
    <div class="mu-dialog-background"></div>
    <div class="mu-dialog w1100">
      <div class="mu-dialog-head">
        <p class="title">데이터 추출</p>
      </div>
      <div class="mu-dialog-body">
        <!-- detail-section : 원본 샘플 데이터 -->
        <sample-data-area
          :userDataAuth="props.eliminateModal.userDataAuth"
          :isAdmin="false"
          :isModal="true"
          :list="props.eliminateModal.sampleData"
          :entity="props.eliminateModal.entity"
          :referredEntitiesAttributes="props.eliminateModal.referredEntitiesAttributes"
        />
        <!-- //detail-section -->
        <!-- detail-section : 추출조건 -->
        <select-tab
          :tabs="tabs"
          :activeTab="currentExtractTab"
          @onSelectTab="onSelectCurrentTab"
        />
        <div class="detail-section" v-if="currentExtractTab != 2">
          <div class="mu-heading">
            <h4>추출 조건</h4>
            <div class="mu-item-group" v-if="currentExtractTab == 0">
              <p class="bul-ast">데이터 용량이 너무 클 경우 대용량 데이터 추출 요청이 필요합니다.</p>
            </div>
          </div>
          <div class="detail-box">
            <!-- mu-formbox -->
            <table class="mu-formbox">
              <caption>
                추출 조건 선택 폼
              </caption>
              <colgroup>
                <col style="width: 190px" />
                <col />
              </colgroup>
              <tbody>
                <tr>
                  <th scope="row">전체 건수</th>
                  <td>
                    <div class="mu-item-group">
                      {{ numberComma(props.eliminateModal.num_rows) }} 건
                    </div>
                  </td>
                </tr>
                <tr v-if="currentExtractTab == 0">
                  <th scope="row">추출 건수</th>
                  <td>
                    <span>10만건</span>
                  </td>
                </tr>
                <tr v-if="currentExtractTab == 1">
                  <th scope="row">추출 건수</th>
                  <td>
                    <div class="mu-item-group">
                      <div class="mu-radio" v-for="(elType, idx) in extractLimitTypeList">
                        <input type="radio" :id="elType + '_' + idx" name="extractLimitTypeSelect" @click="onSelectExtractLimitType(elType)" :checked="extractLimitType && extractLimitType == elType"/>
                        <label :for="elType + '_' + idx"><span>{{ getKrName(elType) }}</span></label>
                      </div>
                      <div class="w200" v-if="extractLimitType == 'custom'">
                        <input
                          type="number"
                          min="0"
                          class="mu-input"
                          placeholder="추출 건수를 입력하세요."
                          title="추출 건수 입력"
                          @input="validateInput"
                          v-model="extractLimit" />
                      </div>
                    </div>
                  </td>
                </tr>
                <tr>
                  <th scope="row">추출 조건</th>
                  <td>
                    <div class="mu-item-group">
                      <div class="mu-selectbox w160">
                        <select title="추출조건 선택" v-model="filterLogic">
                          <option selected value="">선택없음</option>
                          <option value="AND">AND</option>
                          <option value="OR">OR</option>
                        </select>
                      </div>
                    </div>
                    <div v-if="filterLogic != ''" v-for="(value, idx) in filter">
                      <div class="mu-item-group">
                        <div class="mu-selectbox w160">
                          <select title="검색조건 선택" v-model="value.filterHeader">
                            <option value="" selected>선택 없음</option>
                            <option v-for="(item, index) in sampleDataHeader" :value="index">
                              {{ index }}
                            </option>
                          </select>
                        </div>
                        <div class="mu-selectbox w160">
                          <select title="조건부호 선택" v-model="value.filterOperator">
                            <option value="" selected>부호 선택</option>
                            <option value="=">=</option>
                            <option value="!=">!=</option>
                            <option value=">">></option>
                            <option value="<">&lt;</option>
                            <option value=">=">&gt;=</option>
                            <option value="<=">&lt;=</option>
                          </select>
                        </div>
                        <div
                          class="mu-selectbox"
                          v-if="
                            typeEllipsis(
                              filter[idx].filterHeader,
                              props.eliminateModal.entity
                            ) == 'timestamp' || typeEllipsis(
                              filter[idx].filterHeader,
                              props.eliminateModal.entity
                            ) == 'date'
                          "
                        >
                          <div class="period-set">
                            <date-picker
                              @change="onSelectDate(value, $event)"
                              v-model:date="value.filterInput"
                              :type="datetimeType"
                              :time-picker-options="timePickerOptions"
                              :lang="lang"
                              :clearable="false"
                              placeholder="시작일"
                            ></date-picker>
                          </div>
                        </div>
                        <div v-else>
                          <input type="text" title="??? 입력하세요." id="" name="" class="mu-input" v-model="value.filterInput">
                        </div>
                        <button type="button" title="조건 삭제" class="mu-btn mu-btn-icon-only" @click="deleteFilter(idx)">
                          <i class="mu-icon-svg minus"></i>
                        </button>
                      </div>
                    </div>
                    <p v-if="queryError" class="complete-msg error">{{ queryErrorMessage }}</p>
                  </td>
                </tr>
              </tbody>
            </table>
            <!-- //mu-formbox -->
          </div>
        </div>
        <!-- mu-button-wrap -->
        <div class="mu-button-wrap" v-if="currentExtractTab != 2">
          <button v-if="currentExtractTab == 0" type="button" class="mu-btn" @click="onClickFileEliminateButton">
            <i class="mu-icon-svg download"></i><span>File 추출</span>
          </button><!-- file 추출 버튼 클릭 시, 연결된 브라우저에서 파일 다운로드 -->
          <button v-if="currentExtractTab == 1" type="button" class="mu-btn" @click="onClickFileEliminateButton">
            <i class="mu-icon-svg view"></i><span>추출 요청</span>
          </button><!-- file 추출 버튼 클릭 시, 연결된 브라우저에서 파일 다운로드 -->
        </div>
        <!-- //mu-button-wrap -->
        <api-extract-area
          :visibleApiArea="visibleApiArea"
          :currentTab="currentTab"
          :eliminateModal="eliminateModal"
          @onSelectTab="onSelectApiServiceTab"
        />
      </div>
      <div class="mu-dialog-foot">
        <button type="button" class="mu-btn" @click="close"><span>닫기</span></button>
      </div>
      <!-- 접근성 : 내용을 읽고 나서 닫기 나중으로 빼준다  -->
      <div class="mu-dialog-close-btn">
        <button
          type="button"
          class="mu-btn mu-btn-close"
          title="해당 팝업 닫기"
          @click="close"
        >
          <i class="mu-icon-svg close"></i><span>팝업닫기</span>
        </button>
      </div>
    </div>
  </div>
  <!-- // mu-modal -->
</template>

<script setup lang="ts">
import { confirmAlert, defaultAlert, successAlert } from "@/components/functional/alert/alert-default";
import ObjectUtils from "@/utils/ObjectUtils";
import { errorAlert } from "@functional/alert/alert-default";
import SampleDataArea from "@molecules/searchDetail/sample-data-area";
import DatePicker from "@functional/datepicker/date-picker";
import selectTab from "@/components/functional/selectTab/select-tab.vue";
import ApiExtractArea from "@molecules/searchDetail/api-extract-area";
import { onUpdated } from "vue";

const { $ofetch, $yyyyMMdd } = useNuxtApp();
const emit = defineEmits(["onCloseModal"]);
const props = defineProps(["eliminateModal"]);

const tabs = ref([
  {idx: 0, key: "일반 데이터 추출"},
  {idx: 1, key: "대용량 데이터 추출"},
  {idx: 2, key: "API 추출"}
])
const currentExtractTab = ref(0)

const currentHostname = ref(window.location.hostname); // 현재 Domain, 동일한 주소인 경우
const iframePath = ref(
  "https://" +
    window.location.hostname +
    ":13200/docs?uuid=" +
    Math.floor(Math.random() * 10000000000000000).toString()
);
const type = ref(""); // column인지 table인지
const referredEntities = ref({}); // 파라미터로 받을 내용 : 컬럼 파라미터 정보
const columnEntities = ref({}); // 파라미터로 받을 내용 : 컬럼 파라미터 정보
const querySelect = ref(""); // 조회 쿼리
const queryWhere = ref(""); // 쿼리 조건
const queryError = ref(false); // 쿼리 오류 여부
const queryErrorMessage = ref(""); // 쿼리 오류 메세지
const visibleApiArea = ref(false);
const eliminateType = ref("description");
const partitionKeys = ref([]);
const columnPositionData = ref([]); // 컬럼정보 (Position 정보 포함)
const columnUpdateIndex = ref([]);
const columnData = ref([]); // 컬럼 정보
const sampleTestData = ref([]); // 파라미터로 받을 내용(샘플데이터)
const currentTab = ref(0); // API 현재 탭
const dataCount = ref(null); // 조회된 데이터 Row 개수

const extractLimitTypeList = ref(["all", "custom"])
const extractLimitType = ref("all")
const extractLimit = ref(100000);

const filterLogic = ref("");
const filter = ref([
  {
    filterHeader: "",
    filterOperator: "",
    filterInput: "",
    filterInputValue: "",
  },
  {
    filterHeader: "",
    filterOperator: "",
    filterInput: "",
    filterInputValue: "",
  },
  {
    filterHeader: "",
    filterOperator: "",
    filterInput: "",
    filterInputValue: "",
  },
]);
const addUserdescription = ref(true);
const addComment = ref(true);

const selectDate = ref(0)
const selectDateText = ref("")
const datetimeType = ref<string>("datetime");
  interface TimeOptions {
  start: string;
  step: string;
  end: string;
  format: string;
  timePickerIncrement?: number;
}

const timePickerOptions = ref<TimeOptions>({
  start: "00:00",
  step: "00:30",
  end: "23:30",
  format: "hh:mm",
  timePickerIncrement: 30,
});

interface Lang {
  yearFormat: string;
  days: Array<string>;
  months: Array<string>;
  pickers: Array<string>;
  placeholder: {
    date: string;
    dateRange: string;
  }
}

const lang = ref<Lang>({
  yearFormat: "YYYY년",
  days: ["일", "월", "화", "수", "목", "금", "토"],
  months: [
    "1월",
    "2월",
    "3월",
    "4월",
    "5월",
    "6월",
    "7월",
    "8월",
    "9월",
    "10월",
    "11월",
    "12월",
  ],
  pickers: ["next 7 days", "next 30 days", "previous 7 days", "previous 30 days"],
  placeholder: {
    date: "Select Date",
    dateRange: "Select Date Range",
  },
});

const typeName = computed(() =>
  ObjectUtils.getPropertyValue(props.eliminateModal.entity, "entity/typeName", "")
);

const relationshipAttributes = computed(() =>
  ObjectUtils.getPropertyValue(
    props.eliminateModal.entity,
    "entity/relationshipAttributes",
    ""
  )
);
const sampleDataHeader = computed(() =>
  props.eliminateModal.sampleData ? props.eliminateModal.sampleData[0] : []
);

watch(
  () => filterLogic.value,
  (newVal, oldVal) => {
    if (newVal == "") resetFilter();
  }
); // 선택 없음을 클릭하는 경우

// 데이터 추출 Where 조건 필터 초기화
const resetFilter = () => {
  filterLogic.value = "";
  filter.value = [
    {
      filterHeader: "",
      filterOperator: "",
      filterInput: "",
      filterInputValue: "",
    },
    {
      filterHeader: "",
      filterOperator: "",
      filterInput: "",
      filterInputValue: "",
    },
    {
      filterHeader: "",
      filterOperator: "",
      filterInput: "",
      filterInputValue: "",
    },
  ];
  querySelect.value = "";
  queryWhere.value = "";
  queryError.value = false;
  queryErrorMessage.value = "";
};

const typeEllipsis = (val, entity) => {
  const attributes = entity.attributes

  let dataType;
  if (attributes) {
    const columns = attributes.columns
    if (columns) {
      columns.filter((item) => {
        if (item.name === val) {
          if (item.dataType !== "") {
            dataType = item.dataType;
          }
        }
      });
    }
  }

  if (dataType !== undefined) {
    if (dataType !== null) {
      return dataType.toLowerCase();
    }
  } else {
    return "";
  }
};

const onSelectDate = (item, event) => {
  selectDate.value = new Date(event).getTime()
  selectDateText.value = $yyyyMMdd(event)
  item.filterInput = selectDate.value
  item.filterInputValue = selectDateText.value
}

// 컬럼 순서 설정, 파티션키 맨 마지막에 추가
const getReferredEntities = () => {
  let sortedColumnEntities = []

  // 1. 컬럼 순서 설정
  columnEntities.value = ObjectUtils.getPropertyValue(props.eliminateModal.entity, "attributes/columns", [])
  const tempPartitionKey = ObjectUtils.getPropertyValue(props.eliminateModal.entity, "attributes/table_partition", "")
  // partitionKeys.value.push(tempPartitionKey)
  sortedColumnEntities = columnEntities.value.sort((a, b) => a.ordinalPosition - b.ordinalPosition)

  columnPositionData.value = sortedColumnEntities.map((item) => item.meta_name)
  if (tempPartitionKey)
    columnPositionData.value.push(tempPartitionKey)

  return sortedColumnEntities
};

const fnSelectQueryMake = () => {
  // select 쿼리 작성, 날짜타입 trino 조회시 date타입에 timestamp로 들어가있는 데이터를 조회시 오류 발생으로 모두 string으로 타입변환
  for (var i = 0; i < columnPositionData.value.length; i++) {
    if (columnUpdateIndex.value.findIndex((item) => item === i) < 0)
      columnPositionData.value[i] = `CAST ("${columnPositionData.value[i]}" AS VARCHAR) as "${columnPositionData.value[i]}"`;
    if (i < columnPositionData.value.length - 1)
      querySelect.value += columnPositionData.value[i] + " , ";
    else querySelect.value += columnPositionData.value[i];
  }
}

const fnWhereQueryMake = () => {
  queryWhere.value = "WHERE ";
  let firstElementProcessed = false;

  for (let i = 0; i < filter.value.length; i++) {
    const element = filter.value[i];
    let inputQuery = "";

    if (element.filterHeader !== "") {
      if (
        typeEllipsis(element.filterHeader, props.eliminateModal.entity).includes(
          "timestamp"
        ) ||
        typeEllipsis(element.filterHeader, props.eliminateModal.entity).includes("date")
      ) {
        const filterInputResult = `CAST ('${element.filterInput}' AS TIMESTAMP)`;
        if (!firstElementProcessed) {
          inputQuery =
            element.filterHeader +
            " " +
            element.filterOperator +
            " " +
            filterInputResult +
            " ";
          firstElementProcessed = true;
        } else {
          inputQuery =
            filterLogic.value +
            " " +
            element.filterHeader +
            " " +
            element.filterOperator +
            " " +
            filterInputResult +
            " ";
        }
        queryWhere.value += inputQuery;
      } else if (
        typeEllipsis(element.filterHeader, props.eliminateModal.entity).includes(
          "string"
        ) ||
        typeEllipsis(element.filterHeader, props.eliminateModal.entity).includes(
          "char"
        ) ||
        typeEllipsis(element.filterHeader, props.eliminateModal.entity).includes(
          "varchar"
        )
      ) {
        if (!firstElementProcessed) {
          inputQuery =
            element.filterHeader +
            " " +
            element.filterOperator +
            " '" +
            element.filterInput +
            "' ";
          firstElementProcessed = true;
        } else {
          inputQuery =
            filterLogic.value +
            " " +
            element.filterHeader +
            " " +
            element.filterOperator +
            " '" +
            element.filterInput +
            "' ";
        }
        queryWhere.value += inputQuery;
      } else if (
        typeEllipsis(element.filterHeader, props.eliminateModal.entity).includes("int") ||
        typeEllipsis(element.filterHeader, props.eliminateModal.entity).includes(
          "tinyint"
        ) ||
        typeEllipsis(element.filterHeader, props.eliminateModal.entity).includes("bigint")
      ) {
        if (!firstElementProcessed) {
          inputQuery =
            element.filterHeader +
            " " +
            element.filterOperator +
            " " +
            element.filterInput +
            " ";
          firstElementProcessed = true;
        } else {
          inputQuery =
            filterLogic.value +
            " " +
            element.filterHeader +
            " " +
            element.filterOperator +
            " " +
            element.filterInput +
            " ";
        }
        queryWhere.value += inputQuery;
      } else if (
        typeEllipsis(element.filterHeader, props.eliminateModal.entity).includes(
          "float"
        ) ||
        typeEllipsis(element.filterHeader, props.eliminateModal.entity).includes(
          "double"
        ) ||
        typeEllipsis(element.filterHeader, props.eliminateModal.entity).includes(
          "decimal"
        )
      ) {
        if (!firstElementProcessed) {
          inputQuery =
            element.filterHeader +
            " " +
            element.filterOperator +
            " " +
            element.filterInput +
            " ";
          firstElementProcessed = true;
        } else {
          inputQuery =
            filterLogic.value +
            " " +
            element.filterHeader +
            " " +
            element.filterOperator +
            " " +
            element.filterInput +
            " ";
        }
        queryWhere.value += inputQuery;
      } else {
        // 그외 타입
        if (!firstElementProcessed) {
          inputQuery =
            element.filterHeader +
            " " +
            element.filterOperator +
            " " +
            element.filterInput +
            " ";
          firstElementProcessed = true;
        } else {
          inputQuery =
            filterLogic.value +
            " " +
            element.filterHeader +
            " " +
            element.filterOperator +
            " " +
            element.filterInput +
            " ";
        }
        queryWhere.value += inputQuery;
      }
    }
  }

  // 추출조건을 유효성 체크
  let createSuccess = false
  let noFilterCnt = 0
  for (let a=0; a<filter.value.length; a++) {
    // 두번째 쿼리부터는 입력값이 없으면 성공, 일부만 입력한 경우 오류 처리
    if (filter.value[a].filterHeader == "" && filter.value[a].filterInput == "" && filter.value[a].filterOperator == "") {
      noFilterCnt += 1
      createSuccess = true
    } else {
      if (!filter.value[a].filterHeader || !filter.value[a].filterInput || !filter.value[a].filterOperator) {
        createSuccess = false
        break;
      }
    }

    if (noFilterCnt == 3) {
      createSuccess = false
      break;
    }
  }

  return createSuccess
};

// 일반 데이터 추출, 대용량 데이터 추출 요청 버튼 클릭
const onClickFileEliminateButton = async () => {
  if (props.eliminateModal.userDataAuth != 2) {
    errorAlert("데이터 접근 권한이 없습니다. 관리자에게 문의해 주시기 바랍니다.")
    return
  }

  // 파일 추출
  if (!sampleDataHeader.value && sampleDataHeader.value == undefined) {
    errorAlert("추출할 데이터가 없습니다.");
    return;
  }

  let downloadMessage = "파일을 다운로드하시겠습니까?";
  if (currentExtractTab.value == 1) downloadMessage = "다운로드를 요청하시겠습니까?"
  if (!await confirmAlert(downloadMessage)) {
    return
  }

  queryError.value = false;
  queryErrorMessage.value = "";
  querySelect.value = " ";
  const sortedColumnEntities = getReferredEntities();

  fnSelectQueryMake();

  let createWhereQuerySuccess = false
  if (filterLogic.value != "") {
    createWhereQuerySuccess = fnWhereQueryMake();
  } else {
    createWhereQuerySuccess = true
  }

  if (!createWhereQuerySuccess) {
    errorAlert("추출조건을 '선택없음' 으로 변경하거나 조건을 하나 이상 입력해 주세요.")
    return;
  }

  if (filterLogic.value != "") {
    fnWhereQueryMake();
  }

  let typeName = ObjectUtils.getPropertyValue(
    props.eliminateModal.entity,
    "meta_type",
    "table"
  );
  let qualified_name = ObjectUtils.getPropertyValue(
    props.eliminateModal.entity,
    "qualified_name",
    ""
  );
  let meta_id = ObjectUtils.getPropertyValue(props.eliminateModal.entity, "meta_id", "");
  let meta_name = ObjectUtils.getPropertyValue(
    props.eliminateModal.entity,
    "meta_name",
    ""
  );
  let description = ObjectUtils.getPropertyValue(
    props.eliminateModal.entity,
    "description",
    ""
  );

  let userDescriptionList = [];
  let descriptionList = [];

  for (let i=0; i<sortedColumnEntities.length; i++) {
    // description
    if (sortedColumnEntities[i].description)
      descriptionList.push(sortedColumnEntities[i].description)
    else
      descriptionList.push("")

    // userDescription
    if (sortedColumnEntities[i].user_description)
      userDescriptionList.push(sortedColumnEntities[i].user_description)
    else
      userDescriptionList.push("")
  }

  let paramQueryWhere = "";
  if (queryWhere.value.trim() == "") paramQueryWhere = "";
  else paramQueryWhere = " " + queryWhere.value;
  paramQueryWhere = paramQueryWhere; // 모든 문자 인코딩

  // const userDescriptionListParam = userDescriptionList.join(",");
  // const commentListParam = commentList.join(",");

  const params = {
    typeName: typeName,
    qualified_name: qualified_name,
    querySelect: querySelect.value,
    queryWhere: paramQueryWhere,
    fileName: meta_name,
    // userDescriptionList: userDescriptionList,
    descriptionList: descriptionList,
  }

  if (currentExtractTab.value == 0) { // 일반 데이터 추출
    params.limit = 100000

    const res = await $ofetch(`/api/search/detail/eliminate/file/download`, {
      method: 'POST',
      body: params
    })

    // 성공시 {}
    if (res && res.errorMessage && res.errorMessage != "") {
      queryError.value = true;
      queryErrorMessage.value = res.errorMessage.replace("[MOBIGEN] ", "");
      errorAlert(queryErrorMessage.value)
      return;
    }

    if (res.size && res.size > 0) {
      await new Response(res).blob().then((data) => {
        const url = URL.createObjectURL(data);
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", `${meta_name}` + ".csv");
        link.style.cssText = "display:none";
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      })

      let historyParams = {
        meta_id: meta_id,
        meta_name: meta_name,
        meta_type: "table",
        qualified_name: qualified_name,
        queryWhere: queryWhere.value,
        download_type: 0, // 다운로드 방식 (0:file 1:API)';
        description: description,
      };
      historyParams.http_rqst_val = JSON.stringify(historyParams);
      $ofetch("/api/search/detail/eliminate/history/add", {
        method: "POST",
        body: historyParams,
      }).then((res) => {
        if (res && res.data.hasOwnProperty("sampleData")) {
          props.eliminateModal.sampleData = ObjectUtils.getPropertyValue(
            res.data,
            "sampleData",
            []
          );
        }
      });
      queryWhere.value = "";
    }
  } else if (currentExtractTab.value == 1) { // 대용량 데이터 추출, 직접입력 선택한 경우만 limit 필드 추가
    if (extractLimitType.value == 'custom') {
      params.limit = extractLimit.value
    }

    // download_query 제작
    const system_id = qualified_name.split(".")[0]
    const db_name = qualified_name.split(".")[1]
    const schema = qualified_name.split(".")[2]
    const name = qualified_name.split(".")[3] ? qualified_name.split(".")[3] : meta_name

    let big_params = {
      meta_id,
      meta_name,
      status: 0,
      qualified_name,
      download_query: `SELECT ${querySelect.value} FROM "${system_id}"."${schema}"."${name}" ${queryWhere.value}${params.limit ? 'limit ' + params.limit : ''}`,
      data_delimiter: ",",
      data_lineterm: "\n"
    }

    await $ofetch("/api/search/detail/eliminate/file/downloadRequest", {
      method: "POST",
      body: big_params,
    }).then((res) => {
      console.log(res)
      if (res && res.result) {
        successAlert("요청이 완료되었습니다. <br> My Catalog > 데이터 추출 내역에서 진행상태를 확인할 수 있습니다.")
      } else {
        errorAlert("대용량 데이터 추출 요청에 실패하였습니다. 관리자에게 문의해 주시기 바랍니다.")
      }
    }).catch((err) => {
      errorAlert("대용량 데이터 추출 요청에 실패하였습니다. 관리자에게 문의해 주시기 바랍니다.")
    });
  }
};

// 추출건수 유효성 검증
const validateInput = () => {
  if (Number(extractLimit.value) < 0 || !extractLimit.value) {
    extractLimit.value = 0
  }
}

// 모달 닫기
const close = () => {
  columnUpdateIndex.value = [];
  emit("onCloseModal");
};

// 추출 조건 제거버튼
const deleteFilter = (idx) => {
  filter.value[idx].filterHeader = "";
  filter.value[idx].filterInput = "";
  filter.value[idx].filterInputValue = "";
  filter.value[idx].filterLogic = "";
  filter.value[idx].filterOperator = "";
};

// 데이터 추출 방법 탭
const onSelectCurrentTab = (curTabObj) => {
  if (curTabObj) {
    if (curTabObj.idx == 2) {
      visibleApiArea.value = true
      currentTab.value = 0
    } else {
      visibleApiArea.value = false
      currentTab.value = 0
    }
    currentExtractTab.value = curTabObj.idx
  } else {
    currentExtractTab.value = 0
  }
}

// API 추출 > API서비스 내부 탭
const onSelectApiServiceTab = (curTab) => {
  if (curTab) {
    currentTab.value = curTab
  } else {
    currentTab.value = 0
  }
}

// 추출 건수 라디오버튼 클릭 이벤트
const onSelectExtractLimitType = (key) => {
  if (key) {
    extractLimitType.value = key
  } else {
    extractLimitType.value = 'all'
  }
}

// 추출 건수 라디오버튼 한글명
const getKrName = (key) => {
  if (key == 'all') return '전체'
  if (key == 'custom') return '직접입력'
}

// 숫자 콤마
const numberComma = (val) => {
  if (!!Number(val)) {
    if (Number(val) > 1) {
      let textArr = String(val).split(".");
      if (Number(textArr[0]) > 1) {
        if (textArr.length > 1) {
          return (
            String(textArr[0]).replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "." + textArr[1]
          );
        } else {
          return String(textArr[0]).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        }
      } else {
        return val;
      }
    } else {
      return val;
    }
  } else {
    if (Number(val) === 0) {
      return val;
    }
  }
};
</script>
