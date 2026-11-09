<template>
  <div class="contents">
    <div class="inner">
      <div class="board-wrap">
        <div class="mu-heading" v-if="showDomainUpdateModal">
          <h4>도메인 분류어 검색</h4>
          <div class="mu-item-group">
            <button v-if="showDomainUpdateModal" type="button" class="mu-btn mu-btn" @click="onToggleDomainUpdateModal">
              <span>도메인 수정</span>
            </button>
            <p class="txt-info">(필수<strong class="required">필수</strong>)</p>
          </div>
        </div>
        <div class="board-body" v-if="showDomainUpdateModal">
          <!-- mu-formbox -->
          <table class="mu-formbox">
            <caption>
              도메인 분류어 검색
            </caption>
            <colgroup>
              <col style="width: 200px" />
              <col />
            </colgroup>
            <tbody>
              <tr>
                <th scope="row">검색<strong class="required">필수</strong></th>
                <td>
                  <div class="board-list-search" style="display: flex; align-items: left; gap: 5px;">
                    <form action="">
                      <fieldset>
                        <legend>게시물검색</legend>
                        <div class="inner-search">
                          <div class="mu-selectbox w160">
                            <select v-model="searchType">
                              <option value="all">전체</option>
                              <option v-for="item in searchList" :value="item.value">{{ item.text }}</option>
                            </select>
                          </div>
                          <div class="mu-input-icon">
                            <input type="text" class="mu-input w440" placeholder="표준 단어 검색 (*= LIKE 검색)" title="표준 단어 검색 (*= LIKE 검색)" v-model="searchObj.searchWord"
                              @keypress.enter.prevent='getMetaDataStandardList()' />
                            <button type="button" class="mu-btn mu-btn-sch" @click="getMetaDataStandardList()">
                              <i class="mu-icon-svg search"></i>
                              <span>검색</span>
                            </button>
                          </div>
                        </div>
                      </fieldset>
                    </form>
                  </div>
                </td>
              </tr>
              <tr>
                <th scope="row">선택</th>
                <td>
                  <div class="board-list">
                    <table class="mu-grid">
                      <colgroup>
                        <col style="width: 10%" />
                        <col style="width: 10%" />
                        <col style="width: 15%" />
                        <col style="width: 15%" />
                        <col style="width: 15%" />
                        <col style="width: 45%" />
                      </colgroup>
                      <thead>
                        <tr>
                          <th scope="col">사용</th>
                          <th scope="col">분류어여부</th>
                          <th scope="col">단어명 (한글)</th>
                          <th scope="col">단어명 (영문)</th>
                          <th scope="col">단어 의미</th>
                          <th scope="col">설명</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr v-for="(item, index) in searchWordList" :key="index">
                          <td>
                            <button type="button" class="mu-btn mu-btn-sm" @click="onClickUseWord(item)"><i
                              class="mu-icon-svg add"></i><span>사용</span>
                            </button>
                          </td>
                          <td :title="item.form_checked ? item.form_checked : '-'">{{ item.form_checked ? item.form_checked : '-' }}</td>
                          <td>{{ item.name ? item.name : '-' }}</td>
                          <td> {{ item.english_sign ? item.english_sign : '-' }} </td>
                          <td>{{ item.word_eng ? item.word_eng : '-' }}</td>
                          <td>{{ item.description }}</td>
                        </tr>
                        <tr v-if="searchWordList.length === 0">
                          <td colspan="6">
                            <p class="nodata">데이터가 존재하지 않습니다.</p>
                          </td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                  <pagination :paging="pagingObj" @onClickPage="onClickPage" />
                </td>
              </tr>
              <tr>
                <th scope="row">선택 분류어<strong class="required">필수</strong></th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600"
                    :title="selectedWord.name" v-model="selectedWord.name" maxlength="100" readonly/>
                </td>
              </tr>
            </tbody>
          </table>
          <!-- //mu-formbox -->
        </div>
        <div class="mu-heading">
          <h4>도메인 필수값 설정</h4>
          <div class="mu-item-group">
            <button v-if="!showDomainUpdateModal" type="button" class="mu-btn mu-btn-primary" @click="onToggleDomainUpdateModal">
              <span>도메인 수정</span>
            </button>
            <p class="txt-info">(필수<strong class="required">필수</strong>)</p>
          </div>
        </div>
        <div class="board-body">
          <!-- mu-formbox -->
          <table class="mu-formbox">
            <caption>
              도메인 필수값 설정
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
                    :title="formData.domain_classified_name" v-model="formData.domain_classified_name" maxlength="100" readonly/>
                </td>
              </tr>
              <tr>
                <th scope="row">데이터타입<strong class="required">필수</strong></th>
                <td>
                  <div class="mu-selectbox w160">
                    <select v-model="dataType" @change="onDataTypeChanged">
                      <option v-for="item in dataTypeList" :value="item">{{ item.name }}</option>
                    </select>
                  </div>
                </td>
              </tr>
              <tr v-if="dataType.sizeRequired">
                <th scope="row">데이터길이<strong class="required">필수</strong></th>
                <td>
                  <input type="number" placeholder="" class="mu-input w600"
                    :title="dataSize" v-model="dataSize" min="0" maxlength="100" @input="validateDataSize"/>
                </td>
              </tr>
              <tr v-if="dataType.value === 'NUMERIC'">
                <th scope="row">소수점길이<strong class="required">필수</strong></th>
                <td>
                  <input type="number" placeholder="데이터타입이 NUMERIC일 경우 작성" class="mu-input w600"
                    :title="decimalPointSize" v-model="decimalPointSize" min="0" maxlength="100" @input="validateDecimalPointSize"/>
                </td>
              </tr>
              <tr>
                <th scope="row">도메인명<strong class="required">필수</strong></th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600" style="margin-right: 5px;"
                    :title="formData.name" v-model="formData.name" maxlength="100" readonly/>
                  <button type="button" class="mu-btn mu-btn-primary" style="margin-right: 5px;" @click="onClickSetDomain()"><i
                    class="mu-icon-svg add"></i><span>도메인 명 생성</span>
                  </button>
                  <button type="button" class="mu-btn mu-btn-secondary" @click="checkDomainName" :disabled="domainNameCheck">
                    <span>중복확인</span>
                  </button>
                </td>
              </tr>
              <tr>
                <th scope="row">도메인 설명<strong class="required">필수</strong></th>
                <td>
                  <textarea class="mu-area h100 w600" :title="formData.description" placeholder="" v-model="formData.description" />
                </td>
              </tr>
              <tr>
                <th scope="row">도메인 그룹명<strong class="required">필수</strong></th>
                <td>
                  <div class="mu-selectbox w160">
                    <select v-model="domainGroup" @change="onDomainGroupChanged">
                      <option value="">선택</option>
                      <option v-for="item in domainGroupList" :value="item.value">{{ item.name }}</option>
                    </select>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
          <!-- //mu-formbox -->
        </div>
        <div class="mu-heading">
          <h4>부가정보</h4>
        </div>
        <div class="board-body">
          <!-- mu-formbox -->
          <table class="mu-formbox">
            <caption>
              부가정보
            </caption>
            <colgroup>
              <col style="width: 200px" />
              <col />
            </colgroup>
            <tbody>
              <tr>
                <th scope="row">저장형식</th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600"
                    :title="formData.save_format" v-model="formData.save_format" maxlength="100"/>
                </td>
              </tr>
              <tr>
                <th scope="row">표현형식</th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600"
                    :title="formData.expression_format" v-model="formData.expression_format" maxlength="100"/>
                </td>
              </tr>
              <tr>
                <th scope="row">단위</th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600"
                    :title="formData.unit" v-model="formData.unit" maxlength="100"/>
                </td>
              </tr>
              <tr>
                <th scope="row">허용값</th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600"
                    :title="formData.permission_value" v-model="formData.permission_value" maxlength="100"/>
                </td>
              </tr>
              <tr>
                <th scope="row">최종버전일</th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600"
                    :title="formData.final_version_date" v-model="formData.final_version_date" maxlength="100"/>
                </td>
              </tr>
              <tr>
                <th scope="row">사용여부</th>
                <td>
                  <div class="mu-radio" v-for="(used, idx) in usedList">
                    <input type="radio" :id="'rd_' + idx" name="used" :value="used.name"
                      v-model="formData.used"/>
                    <label :for="'rd_' + idx"><span>{{ used.description }}</span></label>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
          <!-- //mu-formbox -->
        </div>
      </div>
      <!-- mu-button-wrap -->
      <div class="mu-button-wrap">
        <button type="button" class="mu-btn mu-btn-lg" @click="onClickCancelButton">
          <span>취소</span>
        </button>
        <button type="button" class="mu-btn mu-btn-lg mu-btn-primary" @click="onClickUpdateButton">
          <span>수정</span>
        </button>
      </div>
      <!-- //mu-button-wrap -->
    </div>
  </div>
</template>

<script setup lang="ts">
import { confirmAlert, errorAlert, successAlert } from "@/components/functional/alert/alert-default";
import { useUserStore } from "@/store/user";
import Pagination from "@functional/pagination/pagination";
import _ from 'lodash';
import { useNuxtApp, useRouter } from "nuxt/app";

definePageMeta({
  name: "AdminMetaDataStandardDomainDetail",
  layout: "admin",
});

const { $ofetch } = useNuxtApp();
const userStore = useUserStore();
const router = useRouter();
const route = useRoute();

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

const domainGroup = ref("")
const domainGroupList = ref([
  {
    value: '율',
    name: '율',
  },
  {
    value: '명칭',
    name: '명칭',
  },
  {
    value: '내용',
    name: '내용',
  },
  {
    value: '금액',
    name: '금액',
  },
  {
    value: '수량',
    name: '수량',
  },
  {
    value: '날짜/시간',
    name: '날짜/시간',
  },
  {
    value: '코드',
    name: '코드',
  },
  {
    value: '번호',
    name: '번호',
  },
])

const searchObj = ref({
  searchType: "all",
  searchWord: "",
  changedTab: false,
});
const pagingObj = ref({
  total: 0,
  limit: 10,
  block: 10,
  currentPage: 1,
});

const selectedWord = ref({})
const searchWordList = ref([]); // 검색된 표준단어 목록
const formData = ref({
  id: undefined, // UUID
  name: '', // 도메인명
  description: '', // 도메인 설명
  domain_classified_name: '',
  group_name: '', // 도메인 그룹명
  data_type: '', // 데이터타입
  data_size: '', // 데이터길이
  decimal_point_size: '', // 소수점길이
  save_format: '', // 저장형식
  expression_format: '', // 표현형식
  unit: '', // 단위
  permission_value: '', // 허용값
  final_version_date: '', // 최종버전일
  used: 'Y', // 사용/미사용
  creator: '', // 생성자
  modifier: '', // 수정자
})

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
const name = ref("") // 도메인명

// 중복체크
const domainNameCheck = ref(true)

// 도메인 분류명 변경 여부 체크
const domainClassifiedNameCheck = ref(true)

// 사용여부 라디오 버튼 목록
const usedList = ref([
  {
    name: "Y",
    description: "사용"
  },
  {
    name: "N",
    description: "미사용"
  }
])

const showDomainUpdateModal = ref(false) // 도메인 수정 모달 활성화 여부

onMounted(() => {
  const id = route.query.id
  if (id && id != "") getMetaDataStandardInfo(id, "domain")
})

// 표준 데이터 정보 조회
const getMetaDataStandardInfo = async (id, select_type) => {
  await $ofetch(`/api/metadata/standard/info?id=${id}&select_type=${select_type}`).then(async (res) => {
    if (res && res.data && res.data.result) {
      formData.value = res.data.info

      dataType.value = dataTypeList.value.find((el) => el.name == formData.value.data_type)
      if (dataType.value && dataType.value.sizeRequired) {
        // 데이터 길이
        if (formData.value.data_size != '')
          dataSize.value = formData.value.data_size

        // 소수점 길이
        if (dataType.value.name === 'NUMERIC') {
          if (formData.value.decimal_point_size != '')
            decimalPointSize.value = formData.value.decimal_point_size
        }
      }
      domainGroup.value = formData.value.group_name
    } else {
      await errorAlert("조회에 실패하였습니다.")
    }
  }).catch(async (err) => {
    await errorAlert("조회에 실패하였습니다.")
  });
}

// 표준단어 검색목록 조회 (50개씩 검색)
const getMetaDataStandardList = async (offset = 1) => {
  const param = {
    select_type: "word", // 표준단어
    offset,
    limit: pagingObj.value.limit,
    searchType: searchObj.value.searchType,
    searchWord: searchObj.value.searchWord,
    used: "",
    formChecked: 'Y'
  };

  await $ofetch("/api/metadata/standard/list", {
    method: 'POST',
    body: JSON.stringify(param),
    headers: {
      'Content-Type': 'application/json'
    }
  }).then(async (res) => {
    if (res && res.data && res.data.list) {
      pagingObj.value = {
        ...pagingObj.value,
        total: res.data.total,
        currentPage: offset,
      };
      searchWordList.value = res.data.list;
    } else {
      initListData()
      await errorAlert("조회에 실패하였습니다.")
    }
  }).catch(async (err) => {
    initListData()
    await errorAlert("조회에 실패하였습니다.")
  });
}

// 리스트 데이터 및 페이징 초기화
const initListData = () => {
  searchWordList.value = []
  pagingObj.value = {
    total: 0,
    limit: 10,
    block: 10,
    currentPage: 1,
  }
}

// 단어 추가
const onClickUseWord = (obj) => {
  selectedWord.value = obj
  formData.value.domain_classified_name = obj.domain_classified_name
  domainClassifiedNameCheck.value = false
}

// 페이지 번호 클릭
const onClickPage = (page) => {
  getMetaDataStandardList(page);
};

const validateDataSize = (event) => {
  const value = event.target.value

  // 숫자가 아니거나 음수면 0으로 보정
  if (isNaN(value) || Number(value) < 0) {
    dataSize.value = 0
  }
}

const validateDecimalPointSize = (event) => {
  const value = event.target.value

  // 숫자가 아니거나 음수면 0으로 보정
  if (isNaN(value) || Number(value) < 0) {
    decimalPointSize.value = 0
  }
}

// 취소 버튼 클릭
const onClickCancelButton = () => {
  goToList()
}

// 수정 버튼 클릭
const onClickUpdateButton = async () => {
  if (!validateFormData(formData.value)) return;

  if (await confirmAlert("수정하시겠습니까?")) {
    await $ofetch(`/api/metadata/standard/updateDomain`, {
      method: 'POST',
      body: formData.value
    }).then(async (res) => {
      if (res && res.data) {
        if (res.data.result) {
          await successAlert('저장되었습니다.');
          getMetaDataStandardInfo(route.query.id, 'domain');
        } else {
          errorAlert('수정에 실패하였습니다.')
        }
      } else {
        errorAlert('수정에 실패하였습니다.')
      }
    }).catch((err) => {
      errorAlert('수정에 실패하였습니다.')
    });
  }
}

const validateFormData = (obj) => {
  if (obj) {
    if (!domainClassifiedNameCheck.value) {
      errorAlert('도메인 명을 다시 생성해 주세요.')
      return false;
    }
    if (obj.name == '') {
      errorAlert("도메인 명을 생성해 주세요.")
      return false
    }

    if (domainNameCheck.value === false) {
      errorAlert('도메인명 중복 체크를 해주세요.');
      return;
    }

    if (obj.description == '') {
      errorAlert("도메인 설명을 입력해 주세요.")
      return false
    }
    if (obj.group_name == '') {
      errorAlert("도메인 그룹명을 선택해 주세요.")
      return false
    }
  } else {
    errorAlert("유효성 검사에 실패하였습니다.")
    return false
  }

  return true
}

const goToList = () => {
  router.push(`/admin/data/meta-data/standard`)
}

/**
 * 도메인 명 생성
 */
const onClickSetDomain = () => {
  domainNameCheck.value = false // 중복확인 버튼 활성화
  if (!validateDomainSettingParams()) return;
  formData.value.name = createDomainName();
}

// 도메인 명 생성
const createDomainName = () => {
  formData.value.data_type = dataType.value.value // 데이터 타입 설정
  formData.value.data_size = dataSize.value // 데이터 길이 설정
  formData.value.decimal_point_size = decimalPointSize.value // 소수점 길이 설정

  const list = dataTypeList.value;
  const domainSign = list.find((item) => item.name === dataType.value.name);

  let domainName =
    formData.value.domain_classified_name + domainSign?.sign + formData.value.data_size;
  if (formData.value.data_type === 'NUMERIC' && formData.value.decimal_point_size !== '') {
    domainName = domainName + ',' + formData.value.decimal_point_size;
  }

  domainClassifiedNameCheck.value = true // 도메인 분류명 유효성 성공으로 변경
  return domainName;
}

// 도메인 명 생성 validation
const validateDomainSettingParams = () => {
  if (!validateStringEmpty(formData.value.domain_classified_name, '도메인 분류명'))
    return false;

  if (!validateStringEmpty(dataType.value.value, '데이터 타입')) {
    return false;
  }

  if (dataType.value.sizeRequired) {
    // 데이터 길이
    if ((dataSize.value === '')
    ) {
      errorAlert('데이터 길이를 입력해주세요.');
      return false;
    }

    // 소수점 길이
    if (dataType.value.name === 'NUMERIC') {
      if (decimalPointSize.value === '') {
        errorAlert('소수점 길이를 입력해주세요.');
        return false;
      }
    }
  }

  return true;
}

// 문자열 validation
const validateStringEmpty = (value, message) => {
  if (value === null || value === undefined || value === '') {
    errorAlert(`${message}이/가 빈 값입니다.`);
    return false;
  }
  return true;
}

/**
 * 중복확인
 */
const checkDomainName = async () => {
  const name = formData.value.name;

  if (_.isEmpty(name)) {
    errorAlert('도메인 명을 생성해주세요.');
    return;
  }
  const params = { name: name, type: 'kor' };
  const duplicateResult = await checkDomainDuplicate(params);
  if (duplicateResult) {
    domainNameCheck.value = true;
  } else {
    domainNameCheck.value = false;
  }
}

// 도메인명 중복체크
const checkDomainDuplicate = async ({ name, type }) => {
  let dupCheckOk = false;
  await $ofetch(`/api/metadata/standard/checkDomainDuplicated?word=${encodeURI(name)}&type=${type}`).then((res) => {
    if (res) {
      if (res.duplicated) {
        errorAlert('중복된 도메인 명이 있습니다.')
      } else {
        if (!res.duplicatedResult) {
          errorAlert('중복 체크에 실패하였습니다.')
        } else {
          successAlert("사용 가능한 도메인 입니다.")
          dupCheckOk = true
        }
      }
    }
  }).catch((err) => {
    errorAlert('중복 체크에 실패하였습니다.')
  });

  return dupCheckOk
}

// 데이터 타입 변경 이벤트
const onDataTypeChanged = () => {
  formData.value.name = ''
  formData.value.data_size = ''
  formData.value.decimal_point_size = ''
  dataSize.value = ''
  decimalPointSize.value = ''
}

// 도메인 그룹명 변경 이벤트
const onDomainGroupChanged = () => {
  formData.value.group_name = domainGroup.value
}

// 도메인 수정 모달 열기
const onToggleDomainUpdateModal = () => {
  showDomainUpdateModal.value = !showDomainUpdateModal.value
}
</script>
