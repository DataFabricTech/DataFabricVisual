<template>
  <div class="mu-modal" v-if="!props.showDomainUpdateModal">
    <div class="mu-dialog-background"></div>

    <div class="mu-dialog w1200">
      <div class="mu-dialog-head">
        <p class="title">도메인 수정</p>
      </div>
      <div class="mu-dialog-body">
        <div class="contents">
          <div class="inner">
            <div class="board-wrap">
              <div class="mu-heading">
                <h4>도메인 생성</h4>
                <div class="mu-item-group">
                  <p class="txt-info">(필수<strong class="required">필수</strong>)</p>
                </div>
              </div>
              <div class="board-body">
                <!-- mu-formbox -->
                <table class="mu-formbox">
                  <caption>
                    도메인 생성
                  </caption>
                  <colgroup>
                    <col style="width: 200px" />
                    <col />
                  </colgroup>
                  <tbody>
                    <tr>
                      <th scope="row">도메인 분류명<strong class="required">필수</strong></th>
                      <td>
                        <input type="text" placeholder="" class="mu-input w600"
                          :title="domainClassifiedName" v-model="domainClassifiedName" maxlength="100" readonly/>
                      </td>
                    </tr>
                    <tr>
                      <th scope="row">데이터타입<strong class="required">필수</strong></th>
                      <td>
                        <div class="mu-selectbox w160">
                          <select v-model="dataType" @change="onSelectChange(dataType)">
                            <option v-for="item in dataTypeList" :value="item">{{ item.name }}</option>
                          </select>
                        </div>
                      </td>
                    </tr>
                    <tr v-if="dataType.sizeRequired">
                      <th scope="row">데이터길이</th>
                      <td>
                        <input type="number" placeholder="" class="mu-input w600"
                          :title="dataSize" v-model="dataSize" min="0" maxlength="100" @input="validateDataSize"/>
                      </td>
                    </tr>
                    <tr v-if="dataType.value === 'NUMERIC'">
                      <th scope="row">소수점길이</th>
                      <td>
                        <input type="number" placeholder="데이터타입이 NUMERIC일 경우 작성" class="mu-input w600"
                          :title="decimalPointSize" v-model="decimalPointSize" min="0" maxlength="100" @input="validateDecimalPointSize"/>
                      </td>
                    </tr>
                    <tr>
                      <th scope="row">도메인명<strong class="required">필수</strong></th>
                      <td>
                        <input type="text" placeholder="" class="mu-input w600" style="margin-right: 5px;"
                          :title="domain" v-model="domain" maxlength="100" readonly/>
                        <button type="button" class="mu-btn mu-btn-primary" @click="createDomain()"><i
                          class="mu-icon-svg add"></i><span>도메인 명 생성</span>
                        </button>
                      </td>
                    </tr>
                  </tbody>
                </table>
                <!-- //mu-formbox -->
              </div>
            </div>
          </div>
        </div>
      </div>
        <div class="mu-dialog-foot">
        <button type="button" class="mu-btn mu-btn-lg" @click="onClickCancelButton">
          <span>취소</span>
        </button>
        <button type="button" class="mu-btn mu-btn-lg mu-btn-primary" @click="onClickSaveButton">
          <span>수정</span>
        </button>
      </div>
      <!-- 접근성 : 내용을 읽고 나서 닫기 나중으로 빼준다  -->
      <div class="mu-dialog-close-btn">
        <button type="button" class="mu-btn mu-btn-close" title="해당 팝업 닫기" @click="onClickCancelButton">
          <i class="mu-icon-svg close"></i><span>팝업닫기</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { confirmAlert, defaultAlert, errorAlert, successAlert } from "@/components/functional/alert/alert-default";
import { useUserStore } from "@/store/user";
import _ from 'lodash';
import { useNuxtApp, useRoute, useRouter } from "nuxt/app";

const { $ofetch } = useNuxtApp();
const userStore = useUserStore();
const router = useRouter();
const route = useRoute();

const emit = defineEmits(["onCloseModal", "reload"]);
const props = defineProps(["showDomainUpdateModal", "formData"])

const searchType = ref("kor")
const searchList = ref([
  {
    value: "kor",
    text: "한글명"
  },
  {
    value: "eng",
    text: "영문명"
  },
  {
    value: "eng_sign",
    text: "영문약어명"
  }
])

const searchObj = ref({
  searchType: "all",
  searchWord: "",
});
const pagingObj = ref({
  total: 0,
  limit: 10,
  block: 10,
  currentPage: 1,
});
// 용어 생성
const searchWordList = ref([]); // 검색된 표준단어 목록
const wordList = ref([]); // 검색 단어 사용클릭하여 선택 항목들
const formData = ref({
  id: props.formData.id, // UUID
  name: '', // 한글명
  english_sign: '', // 영문약어명
  english_name: '', // 영문명
  description: '', // 설명(공통표준용어 설명)
  domain: '', // 도메인명
  permission_value: '', // 허용값
  save_format: '', // 저장형식
  expression_format: '', // 표현형식
  admin_standard_code_name: '', // 행정표준코드명
  competent_authority: '', //소관기관명
  synonym: '', // 이음동의어
  final_version_date: '', // 최종버전일
  used: 'Y', // 사용/미사용
  creator: '', // 생성자
  modifier: '', // 수정자
})

// 도메인 생성
const domainClassifiedName = ref("") // 도메인 분류명
const dataTypeList = ref([
  {
    value: null,
    name: '선택',
    sign: 'NO',
    sizeRequired: false,
  },
  {
    value: 'CHAR',
    name: 'CHAR',
    sign: 'C',
    sizeRequired: true,
  },
  {
    value: 'VARCHAR',
    name: 'VARCHAR',
    sign: 'V',
    sizeRequired: true,
  },
  {
    value: 'TEXT',
    name: 'TEXT',
    sign: 'T',
    sizeRequired: false,
  },
  {
    value: 'NUMERIC',
    name: 'NUMERIC',
    sign: 'N',
    sizeRequired: true,
  },
  {
    value: 'INTEGER',
    name: 'INTEGER',
    sign: 'I',
    sizeRequired: false,
  },
  {
    value: 'DATETIME',
    name: 'DATETIME',
    sign: 'D',
    sizeRequired: false,
  },
])
// 데이터 타입
const dataType = ref({
  value: null,
  name: '선택',
  sign: 'NO',
  sizeRequired: false,
})
const dataSize = ref("") // 데이터 크기
const decimalPointSize = ref("") // DECIMAL 타입 소수점 크기
const domain = ref("") // 도메인명
const originDomain = ref("")

// 입력 유효성 체크
const domainDataTypeCheck = ref(true)
const domainDataSizeCheck = ref(true)
const domainDecimalPointSizeCheck = ref(true)

onMounted(() => {
  domainClassifiedName.value = props.formData.domain_classified_name
  dataType.value = dataTypeList.value.find((el) => el.value == props.formData.data_type)
  dataSize.value = props.formData.data_size
  decimalPointSize.value = props.formData.decimal_point_size
  domain.value = props.formData.domain
  originDomain.value = props.formData.domain
})

const validateDataSize = (event) => {
  const value = event.target.value

  // 숫자가 아니거나 음수면 0으로 보정
  if (isNaN(value) || Number(value) < 0) {
    dataSize.value = 0
  }

  // 값 변경 시 도메인 명 초기화
  domain.value = ''
}

const validateDecimalPointSize = (event) => {
  const value = event.target.value

  // 숫자가 아니거나 음수면 0으로 보정
  if (isNaN(value) || Number(value) < 0) {
    decimalPointSize.value = 0
  }

  // 값 변경 시 도메인 명 초기화
  domain.value = ''
}

const createDomain = async () => {
  const list = dataTypeList.value;
  const domainSign = list.find((item) => item.name === dataType.value.name);
  if (_.isEmpty(domainSign) || (domainSign && domainSign.name == '선택')) {
    await errorAlert('도메인 분류명과 데이터타입을 확인하세요.');
    domain.value = '';
    return;
  }

  domain.value = domainClassifiedName.value + domainSign.sign + dataSize.value;
  if (dataType.value.name === 'NUMERIC' && decimalPointSize.value !== '') {
    domain.value = domain.value + ',' + decimalPointSize.value;
  } else {
    domainDataSizeCheck.value = false
  }
}

// 취소 버튼 클릭
const onClickCancelButton = async () => {
  if (await confirmAlert("수정을 취소하시겠습니까?")) {
    emit("onCloseModal")
  }
}

// 수정 버튼 클릭
const onClickSaveButton = async () => {
  const valid = validateDomain()
  if (valid) {
    if (await confirmAlert("수정하시겠습니까?")) {

      let params = {
        id: formData.value.id,
        domain_classified_name: domainClassifiedName.value,
        data_type: dataType.value.name,
        data_size: dataSize.value,
        decimal_point_size: decimalPointSize.value,
        domain: domain.value,
        origin_domain: originDomain.value
      }

      await $ofetch(`/api/metadata/standard/updateTermDomain`, {
        method: 'POST',
        body: params
      }).then((res) => {
        if (res && res.data) {
          if (res.data.result) {
            successAlert('저장되었습니다.');
            emit("onCloseModal")
            emit("reload")
          } else {
            if (res.data.message) {
              defaultAlert(res.data.message)
              emit("onCloseModal")
              emit("reload")
            } else {
              errorAlert('수정에 실패하였습니다.')
            }
          }
        } else {
          errorAlert('수정에 실패하였습니다.')
        }
      }).catch((err) => {
        errorAlert('수정에 실패하였습니다.')
      });
    }
  }
}

// 데이터 타입 변경 시 길이 값 및 도메인 명 초기화
const onSelectChange = (item) => {
  dataSize.value = ''
  decimalPointSize.value = ''
  domain.value = ''
}

// 도메인 유효성 체크
const validateDomain = () => {
  const item = dataType.value
  if (item) {
    // 데이터 타입 value가 없는 경우(선택 항목 클릭) 유효성 실패 처리
    if (item.value) domainDataTypeCheck.value = true
    else {
      domainDataTypeCheck.value = false
      errorAlert("데이터타입을 확인해주세요.")
      return false
    }

    // 데이터 길이 유효성 처리
    if (item.sizeRequired) {
      if (dataSize.value && dataSize.value > 0) {
        domainDataSizeCheck.value = true
      } else {
        domainDataSizeCheck.value = false
        errorAlert("데이터길이를 확인해주세요.")
        return false
      }

      // NUMERIC은 소수점 길이도 확인
      if (item.value == 'NUMERIC') {
        if (decimalPointSize.value && decimalPointSize.value >= 0) {
          domainDecimalPointSizeCheck.value = true
        } else {
          domainDecimalPointSizeCheck.value = false
          errorAlert("소수점길이를 확인해주세요.")
          return false
        }
      }

    } else {
      domainDataSizeCheck.value = true
      domainDecimalPointSizeCheck.value = true
    }
  } else {
    domainDataTypeCheck.value = false
    domainDataSizeCheck.value = false
    domainDecimalPointSizeCheck.value = false
    errorAlert("데이터타입을 확인해주세요.")
    return false;
  }

  // 도메인명 생성 여부 확인
  if (domain.value.length == 0) {
    errorAlert("도메인 명을 확인해 주세요.")
    return false
  }

  return true
}

</script>
