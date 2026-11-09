<template>
  <div class="mu-modal" v-if="props.toggle">
    <div class="mu-dialog-background"></div>
    <!-- 모달팝업 : 동의어 세트 추가 -->
    <div class="mu-dialog w900 ">
      <div class="mu-dialog-head">
        <p class="title">동의어 세트 추가</p>
      </div>
      <div class="mu-dialog-body">
        <table class="mu-formbox">
          <caption>동의어 세트 추가 게시판</caption>
          <colgroup>
            <col style="width:150px">
            <col>
          </colgroup>
          <tbody>
            <tr>
              <th scope="row">대표어<strong class="required">필수</strong></th>
              <td>
                <div class="mu-input-icon">
                  <input type="text" :class="synonym_add_error ? 'mu-input error' : 'mu-input'"
                    placeholder="대표어를 입력하세요.(최대50자)" title="대표어 입력" v-model="inputTerm" />
                  <button type="button" class="mu-btn mu-btn-delete" @click="clearInputTerm"><i
                      class="mu-icon-svg item-delete"></i><span>삭제</span></button>
                </div>
                <p v-if="synonym_add_error" class="complete-msg error">{{ synonym_error_msg }}</p>
              </td>
            </tr>
            <tr>
              <th scope="row"> 동의어 </th>
              <td>
                <div class="mu-item-group">
                  <div class="mu-input-icon">
                    <input type="text" placeholder="동의어를 입력하세요.(최대50자)"
                      :class="synonym_add_error2 ? 'mu-input error' : 'mu-input'" v-model="inputSynonym"
                      @keypress.enter.prevent="onClickAddSynonym" />
                    <button type="button" class="mu-btn mu-btn-delete" @click="clearInputSynonym"><i
                        class="mu-icon-svg item-delete"></i><span>삭제</span></button>
                  </div>
                  <button type="button" class="mu-btn" @click="onClickAddSynonym">
                    <span>추가</span>
                  </button>
                </div>
                <p v-if="synonym_add_error2" class="complete-msg error">{{ synonym_error_msg2 }}</p>
              </td>
            </tr>
            <tr>
              <th scope="row">동의어 목록</th>
              <td>
                <div class="mu-area h125">
                  <div class="tags-group">
                    <div class="tag" v-for="(synonymInfo, a_index) in addedSynonymList">
                      <span class="tag-label">{{ synonymInfo.synonym }}</span>
                      <button type="button" class="mu-btn mu-btn-delete" title="해당 동의어 삭제"
                        @click="removeSynonym(a_index)">
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
        <div class="mu-dialog-foot">
          <button type="button" class="mu-btn" @click="onClose">
            <span>취소</span>
          </button>
          <button type="button" class="mu-btn mu-btn-primary" @click="onClickConfirm">
            <span>확인</span>
          </button>
        </div>
        <!-- 접근성 : 내용을 읽고 나서 닫기 나중으로 빼준다 -->
        <div class="mu-dialog-close-btn">
          <button type="button" class="mu-btn mu-btn-close" title="해당 팝업 닫기" @click="onClose">
            <i class="mu-icon-svg close"></i>
            <span>팝업닫기</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { confirmAlert, errorAlert } from "@functional/alert/alert-default"

const { $ofetch } = useNuxtApp();

const emit = defineEmits(['reloadSynonymList', 'onCloseModal'])
const props = defineProps(['toggle', 'synonymList', 'isAdmin'])

const inputTerm = ref("")
const inputSynonym = ref("")
const synonym_add_error = ref(false)
const synonym_error_msg = ref("")
const synonym_add_error2 = ref(false)
const synonym_error_msg2 = ref("")
const addedSynonymList = ref([]) // 동의어 목록

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
  // 대표어 미입력
  if (inputTerm.value == "") {
    synonym_add_error.value = true
    synonym_error_msg.value = "대표어를 입력해주세요."
    synonym_add_error2.value = false
    synonym_error_msg2.value = ""
    return
  }

  // 동의어 미입력
  if (inputSynonym.value == "") {
    synonym_add_error.value = false
    synonym_error_msg.value = ""
    synonym_add_error2.value = true
    synonym_error_msg2.value = "동의어를 입력해주세요."
    return
  }

  // 대표어와 동의어가 일치하는 경우
  if (inputTerm.value.toUpperCase() == inputSynonym.value.toUpperCase()) {
    synonym_add_error.value = false
    synonym_error_msg.value = ""
    synonym_add_error2.value = true
    synonym_error_msg2.value = "존재하는 동의어입니다."
    return
  }

  // 동의어 목록에 존재하는 동의어를 다시 추가하는 경우
  if (addedSynonymList.value.length > 0) { // 최근 추가한 데이터인 경우
    for (let index in addedSynonymList.value) {
      if (addedSynonymList.value[index].synonym.toUpperCase() == inputSynonym.value.toUpperCase()) {
        synonym_add_error.value = false
        synonym_error_msg.value = ""
        synonym_add_error2.value = true
        synonym_error_msg2.value = "이미 입력된 동의어입니다."
        return
      }
    }
  }

  // 중복되지 않은 경우 동의어 목록에 추가
  const synonymData = {
    "term": "",
    "synonym": inputSynonym.value
  }

  addedSynonymList.value.push(synonymData)

  inputSynonym.value = ""
  synonym_add_error.value = false;
  synonym_error_msg.value = ""
  synonym_add_error2.value = false;
  synonym_error_msg2.value = ""
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

// 동의어 목록 내 동의어 제거
const removeSynonym = (index) => {
  addedSynonymList.value.splice(index, 1)
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

  // 동의어 미추가
  if (addedSynonymList.value.length == 0) {
    synonym_add_error.value = false
    synonym_error_msg.value = ""
    synonym_add_error2.value = true
    synonym_error_msg2.value = "최소 1개 이상의 동의어를 등록해주세요."
    return
  }

  // 대표어 중복 확인 - 세트 외 대표어와의 중복
  for (let index in props.synonymList) {
    if (inputTerm.value.toUpperCase() == props.synonymList[index].term.toUpperCase()) {
      await errorAlert("이미 존재하는 대표어 입니다.")
      return
    }
  }

  // 동의어 추가 후 확인 클릭
  if (addedSynonymList.value.length > 0) {
    // 대표어 추가
    for (let index in addedSynonymList.value) {
      addedSynonymList.value[index].term = inputTerm.value
    }

    // 대표어 중복 확인 - 세트 내 추가된 동의어와의 중복
    for (let index in addedSynonymList.value) {
      if (inputTerm.value.toUpperCase() == addedSynonymList.value[index].synonym.toUpperCase()) {
        await errorAlert("동의어 목록에 대표어와 동일한 단어가 존재합니다.")
        return
      }
    }

    // 동의어 등록 처리
    await $ofetch('/api/synonym/addSynonymSet', {
      method: 'POST',
      body: addedSynonymList.value
    })

    // 초기화
    inputTerm.value = ""
    inputSynonym.value = ""
    synonym_add_error.value = false
    synonym_error_msg.value = ""
    synonym_add_error2.value = false
    synonym_error_msg2.value = ""
    addedSynonymList.value = []

    // 데이터 갱신
    emit('reloadSynonymList')
    emit('onCloseModal')
  }
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
    addedSynonymList.value = []
    emit('onCloseModal')
  }
}
</script>
