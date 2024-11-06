<template>
  <div class="mu-modal" v-if="props.toggle">
    <div class="mu-dialog-background"></div>
    <!-- 모달팝업 : 동의어 삭제 요청 -->
    <div class="mu-dialog w900 ">
      <div class="mu-dialog-head">
        <p class="title">동의어 삭제 요청</p>
      </div>
      <div class="mu-dialog-body">
        <div class="table-wrap">
          <!-- mu-formbox -->
          <table class="mu-formbox">
            <caption>동의어 삭제 요청</caption>
            <colgroup>
              <col style="width:150px">
              <col>
            </colgroup>
            <tbody>
            <tr>
              <th scope="row"> 대표어 </th>
              <td>{{ props.synonymOneList[0].term }}</td>
            </tr>
            <tr>
              <th scope="row">동의어 목록</th>
              <td>
                <div :class="synonym_delete_error ? 'mu-area h125 error' : 'mu-area h125'"><!-- .error추가 : 에러시 에러문구와 같이  -->
                  <div class="synonym-keyword tags-group"> <!-- tag-selected 기능 구현 필요 -->
                    <button v-for="(synonymInfo, c_index) in props.synonymOneList" :key="c_index" type="button" :class="synonymInfo.toggle ? 'tag tag-selected' : 'tag'" @click="toggleSynonym(synonymInfo)" ><span class="tag-label">{{ synonymInfo.synonym }}</span> <i v-if="avaliableDelete(synonymInfo)" class="mu-icon-svg del" @click="deleteMySynonym(synonymInfo)"></i></button>
                  </div>
                </div>
                <p v-if="synonym_delete_error" class="complete-msg error">{{ synonym_error_msg }}</p> <!-- 에러문구노출 -->
              </td>
            </tr>
            <tr>
              <th scope="row">삭제 요청 사유<strong class="required">필수</strong></th>
              <td>
                <textarea :class="synonym_error_msg2 != '' ? 'mu-area h100 error' : 'mu-area h100'" placeholder="삭제 사유를 입력해주세요.(최대 100자)" title="삭제 사유 입력" maxlength="100" v-model="deleteReason" ></textarea>
                <p v-if="synonym_delete_error2" class="complete-msg error bul-refer">{{ synonym_error_msg2 }}</p> <!-- 에러문구노출 -->
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
            @click="onClickRequest()">
            <span>요청</span>
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
import { successAlert } from "@/components/functional/alert/alert-default";
import { errorAlert, confirmAlert } from "@functional/alert/alert-default";
import {useUserStore} from "@/store/user";

const { $ofetch } = useNuxtApp()

const props = defineProps(['toggle', 'synonymOneList'])
const emit = defineEmits(['reloadSynonymList', 'reloadSynonymListOne', 'onCloseModal'])
const userStore = useUserStore();
const deleteReason = ref("") // 삭제 요청 사유
const synonym_delete_error = ref(false) // 삭제 요청 실패 여부(동의어 선택)
const synonym_error_msg = ref("") // 삭제 요청 오류 메세지(동의어 선택)
const synonym_delete_error2 = ref(false) // 삭제 요청 실패 여부(삭제 사유)
const synonym_error_msg2 = ref("") // 삭제 요청 오류 메세지(삭제 사유)
const deleteSynonymList = ref([]) // 삭제 요청 목록

const deleteMySynonym = (synonymInfo) => {
  synonymInfo.toggle = false; // 선택 취소

  const params = {
    term: synonymInfo.term,
    synonyms: [synonymInfo],
    request: 'user delete',
  }

  $ofetch('/api/synonym/deleteMySynonym', {
    method: 'POST',
    body: params
  }).then(async (res) => {
    if (res && res.errorMessage) {
      synonymInfo.toggle = false; // 선택 취소
      await errorAlert('동의어 삭제 실패하였습니다.')
    } else {
      emit('reloadSynonymList')
      emit('reloadSynonymListOne', synonymInfo.term)
      await successAlert('동의어 삭제가 완료되었습니다.')
    }

  }).catch((error) => {
    synonymInfo.toggle = false; // 선택 취소
    errorAlert('동의어 삭제 실패하였습니다.')
    console.log(error)
  })

}
// 동의어 토글 이벤트 핸들러
const toggleSynonym = (synonymInfo) => {
  // 삭제 요청 목록에 있는지 확인
  if (deleteSynonymList.value.length > 0) {
    for (let index in deleteSynonymList.value) {
      // 동일한 동의어 정보가 있으면 삭제
      if (deleteSynonymList.value[index].id == synonymInfo.id) {
        synonymInfo.toggle = false; // 선택 취소
        deleteSynonymList.value.splice(index, 1)
        return
      }
    }
  }

  synonymInfo.toggle = true; // 선택
  deleteSynonymList.value.push(synonymInfo) // 동의어 삭제 요청 목록에 정보 추가
}

const avaliableDelete = (synonymInfo) => {
  return synonymInfo.create_user === userStore.getUser.user_id
}
// 동의어 삭제 요청 버튼 클릭 이벤트 핸들러
const onClickRequest = async () => {
  // 삭제 요청 유효성 검사
  // 동의어를 선택하지 않은 경우
  if (deleteSynonymList.value.length < 1) {
    synonym_error_msg.value = "최소 1개 이상의 삭제할 동의어를 선택해주세요."
    synonym_delete_error.value = true
    synonym_error_msg2.value = ""
    synonym_delete_error2.value = false
    return
  }

  // 삭제 사유를 작성하지 않은 경우
  if (deleteReason.value == "") {
    synonym_error_msg.value = ""
    synonym_delete_error.value = false
    synonym_error_msg2.value = "삭제 요청 사유를 입력해 주세요."
    synonym_delete_error2.value = true
    return
  }

  // 동의어 삭제 요청 정보
  const params = {
    term: props.synonymOneList[0].term,
    synonyms: deleteSynonymList.value,
    request: deleteReason.value,
  }

    // 동의어 삭제 요청처리
    await $ofetch('/api/synonym/requestDeleteSynonym', {
      method: 'POST',
      body: params
    }).then(async (res) => {
      if (res && res.errorMessage) {
        await errorAlert('동의어 삭제요청에 실패하였습니다.')
      } else {
        await successAlert('동의어 삭제요청이 완료되었습니다.')
      }

    }).catch((error) => {
      errorAlert('동의어 삭제요청에 실패하였습니다.')
      console.log(error)
    })


  // 초기화
  deleteReason.value = ""
  synonym_delete_error.value = false
  synonym_error_msg.value = ""
  synonym_delete_error2.value = false
  synonym_error_msg2.value = ""
  deleteSynonymList.value = []

  // 데이터 갱신
  emit('reloadSynonymList')
  emit('reloadSynonymListOne', props.synonymOneList[0].term)

  emit('onCloseModal')
}

// 동의어 삭제 취소 버튼 클릭 이벤트 핸들러
const onClose = async () => {
  if (await confirmAlert('창을 닫으시겠습니까?')) {
    deleteReason.value = ""
    synonym_delete_error.value = false
    synonym_error_msg.value = ""
    synonym_delete_error2.value = false
    synonym_error_msg2.value = ""
    deleteSynonymList.value = []

    emit('onCloseModal')
  }
}
</script>
