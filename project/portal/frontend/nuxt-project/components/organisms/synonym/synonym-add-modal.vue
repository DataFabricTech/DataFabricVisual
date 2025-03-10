<template>
  <div class="mu-modal" v-if="props.toggle">
    <div class="mu-dialog-background"></div>
    <!-- 모달팝업 : 동의어 추가 -->
    <div class="mu-dialog" style="width: 920px">
      <div class="mu-dialog-head">
        <p class="title">동의어 추가</p>
      </div>
      <div class="mu-dialog-body">
        <div class="table-wrap">
          <table class="mu-formbox">
            <caption>동의어 추가</caption>
            <colgroup>
              <col style="width:160px" />
              <col />
            </colgroup>
            <tbody>
            <tr>
              <th scope="row"> 대표어 </th>
              <td>{{ props.synonymOneList[0].term }}</td>
            </tr>
            <tr>
              <th scope="row"> 동의어 </th>
              <td>
                <div class="mu-item-group">
                  <div class="mu-input-icon">
                    <input type="text" placeholder="" :class="synonym_add_error ? 'mu-input error' : 'mu-input'" title="동의어 입력" v-model="inputSynonym" @keypress.enter.prevent="onClickAddSynonym"><!-- .error추가 : 에러시 에러문구와 같이  -->
                    <button v-if="inputSynonym" type="button" class="mu-btn mu-btn-delete" @click="clearInputSynonym"><i class="mu-icon-svg item-delete"></i><span>삭제</span></button>
                  </div>
                  <button type="button" class="mu-btn" @click="onClickAddSynonym()"><span>추가</span></button>
                </div>
                <p v-if="synonym_add_error" class="complete-msg error">{{ synonym_error_msg }}</p><!-- 에러문구노출 -->
              </td>
            </tr>
            <tr>
              <th scope="row">동의어 목록</th>
              <td>
                <div class="mu-area h125">
                  <div class="synonym-keyword tags-group">
                    <div class="tag" v-for="synonymInfo in props.synonymOneList">
                      <span class="tag-label">{{ synonymInfo.synonym }}</span>
                    </div>
                    <div class="tag" v-for="(synonymInfo, a_index) in addedSynonymList">
                      <span class="tag-label">{{ synonymInfo.synonym }}</span>
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
        </div>
        <!-- //mu-formbox -->
        <div class="mu-dialog-foot">
          <button type="button" class="mu-btn" @click="onClose()">
            <span>취소</span>
          </button>
          <button
            type="button"
            class="mu-btn mu-btn-primary"
            @click="onClickConfirm()">
            <span>확인</span>
          </button>
        </div>
        <!-- 접근성 : 내용을 읽고 나서 닫기 나중으로 빼준다 -->
        <div class="mu-dialog-close-btn">
          <button
            type="button"
            class="mu-btn mu-btn-close"
            title="해당 팝업 닫기"
            @click="onClose()">
            <i class="mu-icon-svg close"></i>
            <span>팝업닫기</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { confirmAlert } from "@functional/alert/alert-default";

const { $ofetch } = useNuxtApp()

const props = defineProps(['toggle', 'synonymOneList'])
const emit = defineEmits(['onCloseModal', 'reloadSynonymList', 'reloadSynonymListOne'])

const inputSynonym = ref("")
const synonym_add_error = ref(false)
const synonym_error_msg = ref("")
const addedSynonymList = ref([])

watch(inputSynonym, (newVal, oldVal) => {
  // 새로운 값에 띄어쓰기가 있으면 제거
  if (/\s/.test(newVal)) {
    inputSynonym.value = newVal.replace(/\s+/g, ''); // 띄어쓰기 제거 후 다시 설정
  }
})

// 동의어 추가 버튼 클릭 이벤트 핸들러
const onClickAddSynonym = () => {
  // 1. 동의어 유효성 검사
  if (inputSynonym.value == "") { // 입력하지 않은 경우
    synonym_add_error.value = true
    synonym_error_msg.value = "동의어를 입력해 주세요."

  } else {

    if (inputSynonym.value.toUpperCase() == props.synonymOneList[0].term.toUpperCase()) {
      synonym_add_error.value = true
      synonym_error_msg.value = "대표어와 동의어가 동일합니다."
      return
    } // 대표어와 동일한 경우

    let duplicated = false
    // 동의어 목록에 존재하면 중복 메세지
    for (let index in props.synonymOneList) { // 동의어
      if (props.synonymOneList[index].synonym.toUpperCase() == inputSynonym.value.toUpperCase()) {
        synonym_add_error.value = true
        synonym_error_msg.value = "이미 등록된 동의어입니다."
        duplicated = true;
        break
      }
    }

    if (addedSynonymList.value.length > 0) { // 최근 추가한 데이터인 경우
      for (let index in addedSynonymList.value) {
        if (addedSynonymList.value[0].synonym.toUpperCase() == inputSynonym.value.toUpperCase()) {
          synonym_add_error.value = true
          synonym_error_msg.value = "이미 등록된 동의어입니다."
          duplicated = true;
          break
        }
      }
    }

    // 중복되지 않은 경우 동의어 목록에 추가
    if ( !duplicated ) {
      const newSynonym = {
        "id": null,
        "synonym": inputSynonym.value
      }

      addedSynonymList.value.push(newSynonym)

      inputSynonym.value = ""
      synonym_add_error.value = false;
      synonym_error_msg.value = ""
    }
  }
}

// 동의어 지우기
const clearInputSynonym = () => {
  inputSynonym.value = ""
  synonym_add_error.value = false
  synonym_error_msg.value = ""
}

// 동의어 목록 추가한 동의어 제거
const removeAddedSynonym = (a_index) => {
  addedSynonymList.value.splice(a_index, 1)
}

// 동의어 추가 확인 버튼 클릭 이벤트 핸들러
const onClickConfirm = async () => {
  // 동의어 추가 후 확인 클릭
  if (addedSynonymList.value.length > 0) {
    const params = {
      term: props.synonymOneList[0].term,
      synonyms: addedSynonymList.value,
    }

    // 동의어 등록 처리
    await $ofetch('/api/synonym/addSynonym', {
      method: 'POST',
      body: params
    })
  }

  // 초기화
  inputSynonym.value = ""
  synonym_add_error.value = false
  synonym_error_msg.value = ""
  addedSynonymList.value = []

  // 데이터 갱신
  emit('reloadSynonymList')
  emit('reloadSynonymListOne', props.synonymOneList[0].term)
  emit('onCloseModal')
}

// 동의어 추가 취소 버튼 클릭 이벤트 핸들러
const onClose = async () => {
  if (await confirmAlert("창을 닫으시겠습니까?")) {
    inputSynonym.value = ""
    synonym_add_error.value = false
    synonym_error_msg.value = ""
    addedSynonymList.value = []
    emit('onCloseModal')
  }
}
</script>

<style>
</style>
