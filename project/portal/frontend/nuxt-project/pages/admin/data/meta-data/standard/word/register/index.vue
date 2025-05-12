<template>
  <div class="contents">
    <div class="inner">
      <div class="board-wrap">
        <div class="mu-heading">
          <h4>표준단어 상세</h4>
          <div class="mu-item-group">
            <p class="txt-info">(필수<strong class="required">필수</strong>)</p>
          </div>
        </div>
        <div class="board-body">
          <!-- mu-formbox -->
          <table class="mu-formbox">
            <caption>
              단어 정보
            </caption>
            <colgroup>
              <col style="width: 200px" />
              <col />
            </colgroup>
            <tbody>
              <tr>
                <th scope="row">단어명<strong class="required">필수</strong></th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600" style="margin-right: 5px;"
                    :title="formData.name" v-model="formData.name" maxlength="100" />
                  <button type="button" class="mu-btn mu-btn-secondary" @click="checkDuplicateName"><span>중복확인</span>
                  </button>
                </td>
              </tr>
              <tr>
                <th scope="row">영문약어명<strong class="required">필수</strong></th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600" style="margin-right: 5px;"
                    :title="formData.english_sign" v-model="formData.english_sign" maxlength="100" />
                  <button type="button" class="mu-btn mu-btn-secondary" @click="checkDuplicateEnglishSign"><span>중복확인</span>
                  </button>
                </td>
              </tr>
              <tr>
                <th scope="row">단어영문명<strong class="required">필수</strong></th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600" style="margin-right: 5px;"
                    :title="formData.word_eng" v-model="formData.word_eng" maxlength="100" />
                  <button type="button" class="mu-btn mu-btn-secondary" @click="checkDuplicateWordEng"><span>중복확인</span>
                  </button>
                </td>
              </tr>
              <tr>
                <th scope="row">단어설명<strong class="required">필수</strong></th>
                <td>
                  <textarea class="mu-area h100 w600" :title="formData.description" placeholder="" v-model="formData.description" />
                </td>
              </tr>
              <tr>
                <th scope="row">형식단어여부</th>
                <td>
                  <div class="mu-checkbox">
                    <input type="checkbox" :id="'chk_box_1'" name="form_checked"
                    v-model="formData.form_checked"
                    true-value="Y"
                    false-value="N"/>
                    <label :for="'chk_box_1'"><span>형식단어</span></label>
                  </div>
                </td>
              </tr>
              <tr>
                <th scope="row">도메인분류명</th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600"
                    :title="formData.domain_classified_name" v-model="formData.domain_classified_name" maxlength="100"/>
                </td>
              </tr>
              <tr>
                <th scope="row">이음동의어목록</th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600"
                    :title="formData.synonym" v-model="formData.synonym" maxlength="100"/>
                </td>
              </tr>
              <tr>
                <th scope="row">금칙어목록</th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600"
                    :title="formData.forbidden" v-model="formData.forbidden" maxlength="100"/>
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
                      v-model="formData.used" readonly/>
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
          <span>등록</span>
        </button>
      </div>
      <!-- //mu-button-wrap -->
    </div>
  </div>
</template>

<script setup lang="ts">
import { confirmAlert, defaultAlert, errorAlert, successAlert } from "@/components/functional/alert/alert-default";
import { useNuxtApp, useRouter } from "nuxt/app";

definePageMeta({
  name: "AdminMetaDataStandardWordRegister",
  layout: "admin",
});

const { $ofetch } = useNuxtApp();
const route = useRoute();
const router = useRouter();

const formData = ref({
  id: undefined, // UUID
  name: '', // 한글명
  english_sign: '', // 영문약어명
  word_eng: '', // 단어영문명
  description: '', // 설명(공통표준용어 설명)
  form_checked: 'N', // 형식단어여부(Y, N)
  domain_classified_name: '', // 도메인 분류명
  synonym: '', // 이음동의어
  forbidden: '', // 금칙어목록
  final_version_date: '', // 최종버전일
  used: 'Y', // 사용/미사용
  created_date: '', // 생성일
  modified_date: '', // 수정일
  creator: '', // 생성자
  modifier: '', // 수정자
})

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

const wordNameCheck = ref(false)
const wordEnglishSignCheck = ref(false)
const wordEnglishCheck = ref(false)

onMounted(() => {
  const id = route.query.id
  if (id && id != "") getMetaDataStandardInfo(id, "word")
})

// 표준단어 검색목록 조회 (50개씩 검색)
const getMetaDataStandardInfo = async (id, select_type) => {
  await $ofetch(`/api/metadata/standard/info?id=${id}&select_type=${select_type}`).then(async (res) => {
    console.log(res)
    if (res && res.data && res.data.result) {
      formData.value = res.data.info
    } else {
      await errorAlert("조회에 실패하였습니다.")
    }
  }).catch(async (err) => {
    await errorAlert("조회에 실패하였습니다.")
  });
}

// 취소 버튼 클릭
const onClickCancelButton = async () => {
  if (await confirmAlert("등록을 취소하시겠습니까?")) {
    router.push("/admin/data/meta-data/standard")
  }
}

// 등록 버튼 클릭
const onClickUpdateButton = async () => {
  if (wordNameCheck.value === false) {
    errorAlert("단어명 중복 체크를 해주세요.");
    return;
  }

  if (wordEnglishSignCheck.value === false) {
    errorAlert("영문약어명 중복 체크를 해주세요.");
    return;
  }

  if (wordEnglishCheck.value === false) {
    errorAlert("단어영문명 중복 체크를 해주세요.");
    return;
  }

  if (await confirmAlert("등록하시겠습니까?")) {
    await $ofetch(`/api/metadata/standard/createWord`, {
      method: 'POST',
      body: formData.value
    }).then(async (res) => {
      console.log(res)
      if (res && res.data) {
        if (res.data.result) {
          await successAlert('저장되었습니다.');
          goToList()
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

// 단어명 중복확인
const checkDuplicateName = async () => {
  const name = formData.value.name;
  if (!validateStringEmpty(name, '단어명')) return;
  const params = { name: name, type: 'kor' };
  const duplicateResult = await checkWordDuplicate(params);
  wordNameCheck.value = duplicateResult;
}

// 영문약어명 중복확인
const checkDuplicateEnglishSign = async () => {
  const name = formData.value.english_sign;
  if (!validateStringEmpty(name, '영문약어명')) return;
  const params = { name: name, type: 'eng_sign' };
  const duplicateResult = await checkWordDuplicate(params);
  wordEnglishSignCheck.value = duplicateResult;
}

// 단어영문명 중복확인
const checkDuplicateWordEng = async () => {
  const name = formData.value.word_eng;
  if (!validateStringEmpty(name, '영문약어명')) return;
  const params = { name: name, type: 'eng' };
  const duplicateResult = await checkWordDuplicate(params);
  wordEnglishCheck.value = duplicateResult;
}

// 문자열 validation
const validateStringEmpty = (value, message) => {
  if (value === '') {
    defaultAlert(`${message}이/가 빈 값입니다.`);
    return false;
  }
  return true;
}

const checkWordDuplicate = async ({ name, type }) => {
  let dupCheckOk = false;
  await $ofetch(`/api/metadata/standard/checkWordDuplicated?word=${encodeURI(name)}&type=${type}`).then((res) => {
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
