<template>
  <div class="inner">
    <div class="board-wrap">
      <div class="mu-heading">
        <h4>신청내용</h4>
        <div class="mu-item-group">
          <p class="txt-info">(필수<strong class="required">필수</strong>)</p>
        </div>
      </div>
      <div class="board-body">
        <!-- mu-formbox -->
        <table class="mu-formbox">
          <caption>
            데이터 요청 게시판
          </caption>
          <colgroup>
            <col style="width: 200px" />
            <col />
          </colgroup>
          <tbody>
            <tr>
              <th scope="row">분류<strong class="required">필수</strong></th>
              <td>
                <div class="mu-checkbox" v-for="(item, idx) in checkboxList">
                  <input
                    type="checkbox"
                    :id="idx"
                    title="행선택"
                    :value=item
                    @change="selected($event, item)"
                    :checked="isChecked(item)"
                  />
                  <label :for="idx"><span>{{ item }}</span></label>
                </div>
                <span v-if="!selectedCheckBoxListValid" class="complete-msg error">분류를 선택해 주세요.</span>
              </td>
            </tr>
            <tr>
              <th scope="row">제목<strong class="required">필수</strong></th>
              <td>
                <input
                  type="text"
                  placeholder="제목은 50자 이내로 입력 가능합니다."
                  v-bind:class="[!titleValid ? 'error' : '', 'mu-input']"
                  title="제목 입력 필수"
                  v-model="dataRequest.title"
                  maxlength="50"
                />
                <p v-if="!titleValid" class="complete-msg error">내용을 입력해주세요.</p>
              </td>
            </tr>
            <tr>
              <th scope="row">신청데이터명<strong class="required">필수</strong></th>
              <td>
                <input
                  type="text"
                  placeholder=""
                  v-bind:class="[!dataNameValid ? 'error' : '', 'mu-input']"
                  title="신청데이터명 입력"
                  v-model="dataRequest.data_name"
                  maxlength="100"
                  :disabled="dataNameDisabled"
                />
                <p v-if="!dataNameValid" class="complete-msg error">
                  내용을 입력해주세요.
                </p>
              </td>
            </tr>
            <tr>
              <th scope="row">신청 내용<strong class="required">필수</strong></th>
              <td>
                <textarea
                  rows="5"
                  v-bind:class="[!dataRequestContentValid ? 'error' : '', 'mu-area']"
                  placeholder="내용을 입력하세요"
                  title="신청 내용 입력 필수"
                  v-model="dataRequest.contents"
                ></textarea>
                <p v-if="!dataRequestContentValid" class="complete-msg error">
                  내용을 입력해주세요.
                </p>
              </td>
            </tr>
            <tr>
              <th scope="row">요청기간</th>
              <td>
                <!-- period-set : 기간 -->
                <div class="period-set">
                  <date-picker
                    v-model:date="request_start"
                    :disabledMaxDate="request_end"
                    type="datetime"
                    :clearable="false"
                    :confirm="false"
                    placeholder="시작일" :lang="lang"
                  ></date-picker>
                  <span>~</span>
                  <date-picker
                    v-model:date="request_end"
                    :disabledMinDate="request_start"
                    type="datetime"
                    :clearable="false"
                    :confirm="false"
                    placeholder="종료일" :time-picker-options="timePickerOptions" :lang="lang"
                  ></date-picker>
                </div>
                <!-- //period-set -->
              </td>
            </tr>
            <tr>
              <th scope="row">활용 목적 및 기대효과<strong class="required">필수</strong></th>
              <td>
                <textarea
                  rows="10"
                  v-bind:class="[!benefitContentValid ? 'error' : '', 'mu-area h80']"
                  placeholder="내용을 입력하세요"
                  title="활용 목적 및 기대효과 입력 필수"
                  v-model="dataRequest.benefit_content"
                ></textarea>
                <p v-if="!benefitContentValid" class="complete-msg error">
                  내용을 입력해주세요.
                </p>
              </td>
            </tr>
            <tr>
              <th scope="row">첨부파일</th>
              <td>
                <slot name="file"></slot>
              </td>
            </tr>
          </tbody>
        </table>
        <!-- //mu-formbox -->
        <!-- tip-box -->
        <div class="tip-box">
          <p class="bul-refer">신청 내용이 명확하지 않은 경우 데이터 제공에 제한이 있을 수 있습니다.</p>
        </div>
        <!-- //tip-box -->
      </div>
    </div>
    <!-- mu-button-wrap -->
    <div class="mu-button-wrap">
      <button type="button" class="mu-btn mu-btn-lg" @click="onClickCancelButton">
        <span>취소</span>
      </button>
      <button
        type="button"
        class="mu-btn mu-btn-lg mu-btn-primary"
        @click="onClickSaveButton"
      >
        <span>등록</span>
      </button>
    </div>
    <!-- //mu-button-wrap -->
  </div>
</template>

<script setup lang="ts">
import DatePicker from "@functional/datepicker/date-picker";
import { useRoute } from "nuxt/app";
import { ref, onMounted, computed, ComputedRef } from "vue";

const emit = defineEmits(["cancelMethod", "saveMethod", "update:dataRequest"]);
const props = defineProps(["dataRequest", "isUpdate"]);

const route = useRoute();

const lang = {
            yearFormat: "YYYY",
            monthFormat: ".MM",
            days: ["sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
            pickers: ["next 7 days", "next 30 days", "previous 7 days", "previous 30 days"],
            placeholder: {
              date: "Select Date",
              dateRange: "Select Date Range"
            }
          }

const timePickerOptions = {
            start: "00:00",
            step: "00:30",
            end: "23:30",
            format: "A hh:mm"
          }

interface DataRequest {
  id: number
	writer: string
	request_type: string
	title: string
	data_name: string
	contents: string
	request_start?: string
	request_end?: string
	benefit_content: string
	create_date: string
	update_date: string
	process_status: number
	finish_date: string
}

const selectedCheckBoxList = ref<Array<Object>>([])
const checkboxList = ref<Array<string>>(["수집", "권한"]);
const required_flag = ref<boolean>(false);
const request_start = ref<string>(null);
const request_end = ref<string>(null);
const dataNameDisabled = ref<boolean>(false);

watch(() => props.dataRequest.request_start, (newVal, oldVal) => {
  request_start.value = props.dataRequest.request_start
})

watch(() => props.dataRequest.request_end, (newVal, oldVal) => {
  request_end.value = props.dataRequest.request_end
})

// 체크박스 클릭 이벤트
const selected = (event, option) => {
  if (event.target.checked) {
    // 체크된 상태면 배열에 추가
    selectedCheckBoxList.value.push(option);
  } else {
    // 해제된 상태면 배열에서 해당 값 제거
    selectedCheckBoxList.value = selectedCheckBoxList.value.filter(item => item !== option);
  }
};

// 체크박스 체크상태 확인
const isChecked = (item) => {
  for (let i=0; i<selectedCheckBoxList.value.length; i++) {
    if (item == selectedCheckBoxList.value[i]) {
      return true
    }
  }
}

const dataRequest: ComputedRef<DataRequest> = computed(() => {
  if (props.dataRequest && props.dataRequest != null && props.dataRequest != undefined) {
    if (props.dataRequest.request_type == 0) selectedCheckBoxList.value = ["수집"]
    if (props.dataRequest.request_type == 1) selectedCheckBoxList.value = ["권한"]
    if (props.dataRequest.request_type == 2) {
      selectedCheckBoxList.value = ["수집", "권한"]
    }
  }
  return props.dataRequest
});

const selectedCheckBoxListValid: ComputedRef<boolean> = computed(() => {
  //필수체크
  if (!required_flag.value) return true;
  else return selectedCheckBoxList.value.length > 0 ? true : false;
});

const titleValid: ComputedRef<boolean> = computed(() => {
  //필수체크
  if (!required_flag.value) return true;
  else return props.dataRequest.title.length > 0 ? true : false;
});

const dataNameValid: ComputedRef<boolean> = computed(() => {
  //필수체크
  if (!required_flag.value) return true;
  else return props.dataRequest.data_name.length > 0 ? true : false;
});

const dataRequestContentValid: ComputedRef<boolean> = computed(() => {
  //필수체크
  if (!required_flag.value) return true;
  else return props.dataRequest.contents.length > 0 ? true : false;
});

const benefitContentValid: ComputedRef<boolean> = computed(() => {
  //필수체크
  if (!required_flag.value) return true;
  else return props.dataRequest.benefit_content.length > 0 ? true : false;
});

onMounted(() => {
});

const onClickCancelButton = () => {
  emit("cancelMethod");
};

const onClickSaveButton = () => {
  required_flag.value = true;
  if (
    !selectedCheckBoxListValid.value ||
    !titleValid.value ||
    !dataNameValid.value ||
    !dataRequestContentValid.value ||
    !benefitContentValid.value
  )
    return;

  dataRequest.value.request_start = request_start.value;
  dataRequest.value.request_end = request_end.value;

  if (selectedCheckBoxList.value.length == 2){ // 수집, 권한 2개
    dataRequest.value.request_type = 2
  } else if (selectedCheckBoxList.value.length == 1) { // 수집 또는 권한
    if (selectedCheckBoxList.value[0] == '수집') dataRequest.value.request_type = 0
    if (selectedCheckBoxList.value[0] == '권한') dataRequest.value.request_type = 1
  }

  emit("update:dataRequest", dataRequest.value);
  emit("saveMethod");
};
</script>
