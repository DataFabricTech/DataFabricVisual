<template>
  <div class="contents">
    <div class="inner">
      <div class="board-wrap">
        <div class="mu-heading">
          <h4>용어 생성</h4>
          <div class="mu-item-group">
            <p class="txt-info">(필수<strong class="required">필수</strong>)</p>
          </div>
        </div>
        <div class="board-body">
          <!-- mu-formbox -->
          <table class="mu-formbox">
            <caption>
              용어 생성
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
              <tr v-for="(word, idx) in wordList">
                <th scope="row">단어({{ idx }})<strong class="required">필수</strong></th>
                <td>
                  <div style="display: flex; gap: 5px;">
                    <input type="text" placeholder="" class="mu-input"
                      :title="word.name" v-model="word.name" maxlength="100" readonly />
                    <input type="text" placeholder="" class="mu-input"
                      :title="word.english_sign" v-model="word.english_sign" maxlength="100" readonly />
                    <button @click="onClickDel(idx)" type="button"
                      class="mu-btn mu-btn-icon-only" title="선택단어삭제"
                      style="min-width: 35px;">
                      <i class="mu-icon-svg minus"></i><span>필터삭제</span></button>
                  </div>
                </td>
              </tr>
              <tr>
                <th scope="row">한글명<strong class="required">필수</strong></th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600"
                    :title="formData.name" v-model="formData.name" maxlength="100" readonly/>
                </td>
              </tr>
              <tr>
                <th scope="row">영문약어명<strong class="required">필수</strong></th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600"
                    :title="formData.english_sign" v-model="formData.english_sign" maxlength="100" readonly/>
                </td>
              </tr>
            </tbody>
          </table>
          <!-- //mu-formbox -->
        </div>
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
                    <select v-model="dataType">
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
                    :title="formData.domain" v-model="formData.domain" maxlength="100" readonly/>
                  <button type="button" class="mu-btn mu-btn-primary" @click="createDomain()"><i
                    class="mu-icon-svg add"></i><span>도메인 명 생성</span>
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
          <!-- //mu-formbox -->
        </div>
        <div class="mu-heading">
          <h4>용어 정보</h4>
          <div class="mu-item-group">
            <p class="txt-info">(필수<strong class="required">필수</strong>)</p>
          </div>
        </div>
        <div class="board-body">
          <!-- mu-formbox -->
          <table class="mu-formbox">
            <caption>
              용어 정보
            </caption>
            <colgroup>
              <col style="width: 200px" />
              <col />
            </colgroup>
            <tbody>
              <tr>
                <th scope="row">용어명<strong class="required">필수</strong></th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600" style="margin-right: 5px;"
                    :title="formData.name" v-model="formData.name" maxlength="100" readonly/>
                  <button type="button" class="mu-btn mu-btn-secondary" @click="checkTermName"><span>중복확인</span>
                  </button>
                </td>
              </tr>
              <tr>
                <th scope="row">용어 영문 약어명<strong class="required">필수</strong></th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600" style="margin-right: 5px;"
                    :title="formData.english_name" v-model="formData.english_name" maxlength="100" readonly/>
                  <button type="button" class="mu-btn mu-btn-secondary" @click="checkTermEnglishSign"><span>중복확인</span></button>
                </td>
              </tr>
              <tr>
                <th scope="row">도메인명<strong class="required">필수</strong></th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600"
                    :title="formData.domain" v-model="formData.domain" maxlength="100" readonly/>
                  <div class="tip-box">
                    <p class="bul-refer">"도메인 생성 > 도메인명 > 도메인 명 생성"에서 도메인을 생성할 수 있습니다.</p>
                  </div>
                </td>
              </tr>
              <tr>
                <th scope="row">공통표준용어 설명<strong class="required">필수</strong></th>
                <td>
                  <textarea class="mu-area h100 w600" :title="formData.description" placeholder="" v-model="formData.description" />
                </td>
              </tr>
            </tbody>
          </table>
          <!-- //mu-formbox -->
        </div>
        <div class="mu-heading">
          <h4>추가 정보</h4>
        </div>
        <div class="board-body">
          <!-- mu-formbox -->
          <table class="mu-formbox">
            <caption>
              추가 정보
            </caption>
            <colgroup>
              <col style="width: 200px" />
              <col />
            </colgroup>
            <tbody>
              <tr>
                <th scope="row">허용값</th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600"
                    :title="formData.permission_value" v-model="formData.permission_value" maxlength="100"/>
                </td>
              </tr>
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
                <th scope="row">행정표준코드명</th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600"
                    :title="formData.admin_standard_code_name" v-model="formData.admin_standard_code_name" maxlength="100"/>
                </td>
              </tr>
              <tr>
                <th scope="row">소관기관명</th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600"
                    :title="formData.competent_authority" v-model="formData.competent_authority" maxlength="100"/>
                </td>
              </tr>
              <tr>
                <th scope="row">이음동의어</th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600"
                    :title="formData.synonym" v-model="formData.synonym" maxlength="100"/>
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
                      v-model="formData.used" disabled/>
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
        <button type="button" class="mu-btn mu-btn-lg mu-btn-primary" @click="onClickSaveButton">
          <span>등록</span>
        </button>
      </div>
      <!-- //mu-button-wrap -->
    </div>
  </div>
</template>

<script setup lang="ts">
import { confirmAlert, defaultAlert, errorAlert, successAlert } from "@/components/functional/alert/alert-default";
import { useUserStore } from "@/store/user";
import Pagination from "@functional/pagination/pagination";
import _ from 'lodash';
import { useNuxtApp, useRouter } from "nuxt/app";

definePageMeta({
  name: "AdminMetaDataStandardTermRegister",
  layout: "admin",
});

const { $ofetch } = useNuxtApp();
const userStore = useUserStore();
const router = useRouter();

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
  changedTab: false,
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
  id: undefined, // UUID
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

// 중복체크
const termNameCheck = ref(false)
const termEnglishSignCheck = ref(false)
const termNameCheckMessage = ref(true)
const termEnglishSignCheckMessage = ref(true)

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

onMounted(() => {
  getMetaDataStandardList()
})

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

// 표준단어 검색목록 조회 (50개씩 검색)
const getMetaDataStandardList = async (offset = 1) => {
  const param = {
    select_type: "word", // 표준단어
    offset,
    limit: pagingObj.value.limit,
    searchType: searchObj.value.searchType,
    searchWord: searchObj.value.searchWord,
    used: "",
    formChecked: ""
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
  wordList.value.push(obj)
  setTerm()
}

// 선택한 단어 제거
const onClickDel = (index) => {
  wordList.value.splice(index, 1);
  setTerm();
}

// 한글명, 영문약어명, 영문명 자동완성
const setTerm = () => {
  formData.value.name = wordList.value.map((item) => item.name).join('');
  formData.value.english_sign = wordList.value.map((item) => item.english_sign).join('_');
  formData.value.english_name = wordList.value.map((item) => item.word_eng).join(' ');

  const lastIndex = wordList.value.length - 1;
  if (lastIndex === -1) {
    domainClassifiedName.value = '';
    return;
  }
  if (wordList.value[lastIndex]?.form_checked === 'Y') {
    domainClassifiedName.value = wordList.value[lastIndex]?.domain_classified_name;
  }
}

const createDomain = async () => {
  if (_.isEmpty(domainClassifiedName.value)) {
    await errorAlert('용어의 마지막 단어가 분류어가 아닙니다.');
    return;
  }
  const list = dataTypeList.value;
  const domainSign = list.find((item) => item.name === dataType.value.name);
  if (_.isEmpty(domainSign) || (domainSign && domainSign.name == '선택')) {
    await errorAlert('도메인 분류명과 데이터타입을 확인하세요.');
    domain.value = '';
    return;
  }

  formData.value.domain = domainClassifiedName.value + domainSign.sign + dataSize.value;
  if (dataType.value.name === 'NUMERIC' && decimalPointSize.value !== '') {
    formData.value.domain = formData.value.domain + ',' + decimalPointSize.value;
  }
}

// 페이지 번호 클릭
const onClickPage = (page) => {
  getMetaDataStandardList(page);
};

// 취소 버튼 클릭
const onClickCancelButton = () => {
  history.go(-1)
}

// 등록 버튼 클릭
const onClickSaveButton = async () => {
  if (termNameCheck.value === false || termEnglishSignCheck.value === false) {
    errorAlert('용어 중복 체크를 해주세요.');
    return;
  }

  const pattern = /^([가-힣]+)([A-Za-z])(.+)?$/;

  if (!pattern.test(formData.value.domain)) {
    errorAlert('도메인 형식이 올바르지 않습니다.');
    return;
  }

  if (await confirmAlert("등록하시겠습니까?")) {
    await $ofetch(`/api/metadata/standard/createTerm`, {
      method: 'POST',
      body: formData.value
    }).then((res) => {
      console.log(res)
      if (res && res.data) {
        if (res.data.result) {
          successAlert('저장되었습니다.');
          goToList();
        } else {
          errorAlert('등록에 실패하였습니다.')
        }
      } else {
        errorAlert('등록에 실패하였습니다.')
      }
    }).catch((err) => {
      errorAlert('등록에 실패하였습니다.')
    });
  }

}

const goToList = () => {
  router.push(`/admin/data/meta-data/standard`)
}

// 용어명 중복 체크
const checkTermName = async () => {
  const name = formData.value.name;
  if (!validateStringEmpty(name, '용어명')) return;
  if (!validateFormChecked()) return;
  const params = { name: name, type: 'kor' };
  const duplicateResult = await checkTermDuplicate(params);
  if (duplicateResult) {
    termNameCheck.value = true;
    termNameCheckMessage.value = true;
  } else {
    termNameCheck.value = false;
    termNameCheckMessage.value = false;
  }
}

// 영문약어명 중복 체크
const checkTermEnglishSign = async () => {
  const englishSign = formData.value.english_sign;
  if (!validateStringEmpty(englishSign, '영문 약어명')) return;
  if (!validateFormChecked()) return;
  const params = { name: englishSign, type: 'eng_sign' };
  const duplicateResult = await checkTermDuplicate(params);

  if (duplicateResult) {
    termEnglishSignCheck.value = true;
    termEnglishSignCheckMessage.value = true;
  } else {
    termEnglishSignCheck.value = false;
    termEnglishSignCheckMessage.value = false;
  }
}

// 문자열 validation
const validateStringEmpty = (value, message) => {
  if (value === '') {
    defaultAlert(`${message}이/가 빈 값입니다.`);
    return false;
  }
  return true;
}

// 분류어 validation
const validateFormChecked = () => {
  const lastWord = wordList.value[wordList.value.length - 1];
  if (lastWord.form_checked !== 'Y') {
    errorAlert('용어를 구성하는 마지막 단어는 분류어이어야 합니다.');
    return false;
  }
  domainClassifiedName.value = lastWord.domain_classified_name;
  return true;
}

// 용어명 중복체크
const checkTermDuplicate = async ({ name, type }) => {
  let dupCheckOk = false;
  await $ofetch(`/api/metadata/standard/checkTermDuplicated?word=${encodeURI(name)}&type=${type}`).then((res) => {
    if (res) {
      if (res.duplicated) {
        errorAlert('중복된 내용이 있습니다.')
      } else {
        if (!res.duplicatedResult) {
          errorAlert('중복 체크에 실패하였습니다.')
        } else {
          successAlert("중복확인되었습니다.")
          dupCheckOk = true
        }
      }
    }
  }).catch((err) => {
    errorAlert('중복 체크에 실패하였습니다.')
  });

  return dupCheckOk
}

</script>
