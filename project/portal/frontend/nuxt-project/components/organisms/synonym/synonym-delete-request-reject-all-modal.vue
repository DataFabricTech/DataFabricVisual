<template>
  <div class="mu-modal" v-if="props.toggle">
    <div class="mu-dialog-background"></div>
    <!-- 모달팝업 : 동의어 삭제 요청 일괄 반려 -->
    <div class="mu-dialog w900 ">
      <div class="mu-dialog-head">
        <p class="title">동의어 삭제 요청 일괄 반려</p>
      </div>
      <div class="mu-dialog-body">
        <div class="mu-heading">
          <div class="mu-item-group">
            <p class="bul-ast">반려 시 반려 사유 입력</p>
          </div>
        </div>
        <table class="mu-formbox">
          <caption>동의어 삭제 요청 일괄 반려 게시판</caption>
          <colgroup>
            <col style="width:150px">
            <col>
          </colgroup>
          <tbody>
            <tr v-if="isAdmin">
              <th scope="row">반려 사유<strong class="required">필수</strong></th>
              <td>
                <textarea class="mu-area h100" title="동의어 삭제 요청 반려 사유" placeholder="반려 사유를 작성해 주세요.(최대 100자)"
                  v-model="response" />
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="mu-dialog-foot">
        <button type="button" class="mu-btn" @click="onClose">
          <span>취소</span>
        </button>
        <button type="button" class="mu-btn mu-btn-primary" @click="onClickRejectAll">
          <span>반려</span>
        </button>
      </div>
      <!-- 접근성 : 내용을 읽고 나서 닫기 나중으로 빼준다 -->
      <div class="mu-dialog-close-btn">
        <button type="button" class="mu-btn mu-btn-close" title="닫기" @click="onClose">
          <i class="mu-icon-svg close"></i>
          <span>닫기</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { confirmAlert, errorAlert } from "@functional/alert/alert-default";

const { $ofetch } = useNuxtApp();

const emit = defineEmits(['onCloseModal', 'reloadSynonymDeleteRequestList'])
const props = defineProps(['toggle', 'synonymDeleteRequestAll', 'isAdmin'])

const status = ref(0) // 진행 상태 [0:완료 1:요청 2:승인 3:반려]
const response = ref("") // 반려 사유

// 동의어 삭제 요청 반려 취소 버튼
const onClose = async () => {
  response.value = ""
  emit('onCloseModal')
}

// 동의어 삭제 요청 반려 버튼
const onClickRejectAll = async () => {
  if (response.value == "") {
    await errorAlert("반려 사유를 입력해주세요.")
    return
  }

  // 일괄 반려 처리
  if (await confirmAlert("정말 일괄 반려하시겠습니까?")) {
    const params = {
      req_list: props.synonymDeleteRequestAll,
      response: response.value
    }

    await $ofetch(`/api/synonym/rejectDeleteRequestAll`, {
      method: 'POST',
      body: params
    })

    // 데이터 갱신
    emit('reloadSynonymDeleteRequestList')
    emit('onCloseModal')
  }
}
</script>
