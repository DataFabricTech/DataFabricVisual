<template>
  <div class="mu-modal" v-if="props.toggle">
    <div class="mu-dialog-background"></div>
    <!-- 모달팝업 : 동의어 세트 수정 -->
    <div class="mu-dialog w900 ">
      <div class="mu-dialog-head">
        <p class="title">동의어 세트 수정</p>
      </div>
      <div class="mu-dialog-body">
        <table class="mu-formbox">
          <caption>동의어 세트 수정 게시판</caption>
          <colgroup>
            <col style="width:150px">
            <col>
          </colgroup>
          <tbody>
          <tr>
            <th scope="row">대표어<strong class="required">필수</strong></th>
            <td>
              <div class="mu-input-icon">
                <input type="text" :class="synonym_add_error ? 'mu-input error' : 'mu-input'" placeholder="대표어를 입력하세요.(최대50자)" title="대표어 입력" v-model="inputTerm"/>
                <button type="button" class="mu-btn mu-btn-delete" @click="clearInputTerm"><i class="mu-icon-svg item-delete"></i><span>삭제</span></button>
              </div>
              <p v-if="synonym_add_error" class="complete-msg error bul-refer">{{ synonym_error_msg }}</p>
            </td>
          </tr>
          <tr>
            <th scope="row"> 동의어 </th>
            <td>
              <div class="mu-item-group">
                <div class="mu-input-icon">
                  <input type="text" placeholder="동의어를 입력하세요.(최대50자)" :class="synonym_add_error2 ? 'mu-input error' : 'mu-input'" v-model="inputSynonym"/>
                  <button type="button" class="mu-btn mu-btn-delete" @click="clearInputSynonym"><i class="mu-icon-svg item-delete"></i><span>삭제</span></button>
                </div>
                <button type="button" class="mu-btn" @click="onClickAddSynonym" @keypress.enter.prevent="onClickAddSynonym">
                  <span>추가</span>
                </button>
              </div>
              <p v-if="synonym_add_error2" class="complete-msg error bul-refer">{{ synonym_error_msg2 }}</p>
            </td>
          </tr>
          <tr>
            <th scope="row">동의어 목록</th>
            <td>
              <div class="mu-area h125">
                <div class="tags-group">
                  <div class="tag" v-for="(synonymInfo, s_index) in props.synonymOneList" style="margin: 0 5px 0 5px;">
                    <span class="tag-label">{{ synonymInfo.synonym }}</span>
                    <button
                      type="button"
                      class="mu-btn mu-btn-delete"
                      title="해당 동의어 삭제"
                      @click="removeOldSynonym(s_index)">
                      <i class="mu-icon-svg item-delete"></i>
                      <span>동의어 삭제</span>
                    </button>
                  </div>
                  <div class="tag" v-for="(synonymInfo, a_index) in addedSynonymList" style="margin: 0 5px 0 5px;">
                    <em class="tag-label">{{ synonymInfo.synonym }}</em>
                    <button
                      type="button"
                      class="mu-btn mu-btn-delete"
                      title="해당 동의어 삭제"
                      @click="removeAddedSynonym(a_index)">
                      <i class="mu-icon-svg item-delete"></i>
                      <span>동의어 삭제</span>
                    </button>
                  </div>
                </div>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
        <!-- //mu-formbox -->
        <div class="mu-dialog-foot">
          <button type="button" class="mu-btn" @click="onClose">
            <span>취소</span>
          </button>
          <button
            type="button"
            class="mu-btn mu-btn-primary"
            @click="onClickConfirm" :disabled="addedSynonymList.length == 0 && props.synonymOneList.length == 0">
            <span>확인</span>
          </button>
        </div>
        <!-- 접근성 : 내용을 읽고 나서 닫기 나중으로 빼준다 -->
        <div class="mu-dialog-close-btn">
          <button
            type="button"
            class="mu-btn mu-btn-close"
            title="해당 팝업 닫기"
            @click="onClose">
            <i class="mu-icon-svg close"></i>
            <span>팝업닫기</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { confirmAlert, errorAlert } from "@functional/alert/alert-default";

const { $ofetch } = useNuxtApp();

const emit = defineEmits(['reloadSynonymList', 'onCloseModal'])
const props = defineProps(['toggle', 'synonymList', 'synonymOneList', 'isAdmin'])

const inputTerm = ref(null)
const currentTerm = ref("")
const inputSynonym = ref("")
const synonym_add_error = ref(false)
const synonym_error_msg = ref("")
const synonym_add_error2 = ref(false)
const synonym_error_msg2 = ref("")
const deletedSynonymList = ref([]) // 기존 동의어 삭제 목록
const addedSynonymList = ref([]) // 동의어 추가 목록

watch(() => props.synonymOneList, (newVal, oldVal) => {
  if (props.synonymOneList.length > 0) {
    inputTerm.value = props.synonymOneList[0].term
    currentTerm.value = props.synonymOneList[0].term
  }
})

watch(inputTerm, (newVal, oldVal) => {
  // 새로운 값에 띄어쓰기가 있으면 제거
  if (/\s/.test(newVal)) {
    inputTerm.value = newVal.replace(/\s+/g, ''); // 띄어쓰기 제거 후 다시 설정
  }
})

watch(inputSynonym, (newVal, oldVal) => {
  // 새로운 값에 띄어쓰기가 있으면 제거
  if (/\s/.test(newVal)) {
    inputSynonym.value = newVal.replace(/\s+/g, ''); // 띄어쓰기 제거 후 다시 설정
  }
})

// 동의어 추가 버튼 클릭 이벤트 핸들러
const onClickAddSynonym = async () => {
  // 1. 동의어 유효성 검사
  if (inputSynonym.value == "") { // 동의어 미입력
    synonym_add_error2.value = true
    synonym_error_msg2.value = "동의어를 입력해 주세요."

  } else {


    // 동의어 중복 여부 확인
    let synonymDuplicated = false
    if (addedSynonymList.value.length > 0) { // 최근 추가한 데이터인 경우
      for (let index in addedSynonymList.value) {
        if (addedSynonymList.value[index].synonym.toUpperCase() == inputSynonym.value.toUpperCase()) {
          synonym_add_error2.value = true
          synonym_error_msg2.value = "이미 입력된 동의어입니다."
          synonymDuplicated = true;
          return
        }
      }
    }

    // 대표어와 동의어가 일치하는 경우
    if (inputTerm.value.toUpperCase() == inputSynonym.value.toUpperCase()){
      synonym_add_error2.value = true
      synonym_error_msg2.value = "대표어와 동의어가 동일합니다."
      return
    }

    // 중복되지 않은 경우 동의어 목록에 추가
    if ( !synonymDuplicated ) {
      const synonyms = {
        "id": null,
        "synonym": inputSynonym.value,
        "term": currentTerm.value
      }
      addedSynonymList.value.push(synonyms)
      inputSynonym.value = ""
      synonym_add_error2.value = false;
      synonym_error_msg2.value = "";
    }
  }
}

// 대표어 지우기
const clearInputTerm = () => {
  inputTerm.value = ""
  synonym_add_error.value = false
  synonym_error_msg.value = ""
}

// 동의어 지우기
const clearInputSynonym = () => {
  inputSynonym.value = ""
  synonym_add_error2.value = false
  synonym_error_msg2.value = ""
}

// 동의어 목록 내 기존 동의어 제거 버튼 클릭
const removeOldSynonym = (s_index) => {
  deletedSynonymList.value.push(props.synonymOneList[s_index])
  props.synonymOneList.splice(s_index, 1)
}

// 동의어 목록 추가한 동의어 제거
const removeAddedSynonym = (a_index) => {
  addedSynonymList.value.splice(a_index, 1)
}

// 동의어 추가 확인 버튼 클릭 이벤트 핸들러
const onClickConfirm = async () => {
  // 대표어 미입력
  if (inputTerm.value == '') {
    synonym_add_error.value = true
    synonym_error_msg.value = "대표어를 입력해주세요."
    synonym_add_error2.value = false
    synonym_error_msg2.value = ""
    return
  }

  // 대표어 중복 확인 - 세트 내 기존 동의어와의 중복
  for (let index in props.synonymOneList) {
    if (inputTerm.value.toUpperCase() == props.synonymOneList[index].synonym.toUpperCase()) {
      errorAlert("동의어 목록에 대표어와 동일한 단어가 존재합니다.")
      synonym_add_error.value = false
      synonym_error_msg.value = ""
      synonym_add_error2.value = false
      synonym_error_msg2.value = ""
      return
    }
  }

  // 대표어 중복 확인 - 세트 내 추가된 동의어와의 중복
  for (let index in addedSynonymList.value) {
    if (inputTerm.value.toUpperCase() == addedSynonymList.value[index].synonym.toUpperCase()) {
      errorAlert("동의어 목록에 대표어와 동일한 단어가 존재합니다.")
      synonym_add_error.value = false
      synonym_error_msg.value = ""
      synonym_add_error2.value = false
      synonym_error_msg2.value = ""
      return
    }
  }

  // 대표어 중복 확인 - 세트 외 대표어와의 중복
  for (let index in props.synonymList) {
    if (inputTerm.value.toUpperCase() == props.synonymList[index].term.toUpperCase()) {
      // 세트수정을 진행중인 대표어가 아닌 경우
      if (inputTerm.value.toUpperCase() != currentTerm.value.toUpperCase()) {
        errorAlert("이미 존재하는 대표어입니다.")
        return
      }
    }
  }

  let params = {
    deletedSynonymList: [],
    addedSynonymList: [],
  }

  // 대표어를 변경한 경우
  let termUpdated = false
  if (inputTerm.value != currentTerm.value) {
    params.originTerm = currentTerm.value
    params.newTerm = inputTerm.value
    termUpdated = true
  }

  // 수정할 데이터 확인
  if (termUpdated || deletedSynonymList.value.length != 0 || addedSynonymList.value.length != 0) {
    params.deletedSynonymList = deletedSynonymList.value // 기존 동의어 삭제 목록
    params.addedSynonymList = addedSynonymList.value // 추가된 동의어 목록

    // 동의어 수정 처리
    await $ofetch('/api/synonym/modifySynonymSet', {
      method: 'POST',
      body: params
    })

  }
  // 초기화
  inputTerm.value = ""
  inputSynonym.value = ""
  synonym_add_error.value = false
  synonym_error_msg.value = ""
  synonym_add_error2.value = false
  synonym_error_msg2.value = ""
  deletedSynonymList.value = []
  addedSynonymList.value = []

  // 데이터 갱신
  emit('reloadSynonymList')
  emit('onCloseModal')
}

// 동의어 추가 취소 버튼 클릭 이벤트 핸들러
const onClose = async () => {
  if (await confirmAlert('창을 닫으시겠습니까?')) {
    inputTerm.value = ""
    inputSynonym.value = ""
    synonym_add_error.value = false
    synonym_error_msg.value = ""
    synonym_add_error2.value = false
    synonym_error_msg2.value = ""
    deletedSynonymList.value = []
    addedSynonymList.value = []
    emit('onCloseModal')
  }
}
</script>
