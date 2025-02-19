<template>
  <div class="mu-modal" v-if="props.toggle">
    <div class="mu-dialog-background"></div>
    <!-- 모달팝업 : 동의어 삭제 요청 내역 -->
    <div class="mu-dialog w900 ">
      <div class="mu-dialog-head">
        <p class="title">동의어 삭제 요청 내역</p>
      </div>
      <div class="mu-dialog-body">
        <table class="mu-formbox">
          <caption>동의어 삭제 요청 내역 게시판</caption>
          <colgroup>
            <col style="width:150px">
            <col>
            <col style="width:150px">
            <col>
          </colgroup>
          <tbody>
          <tr v-if="props.isAdmin">
            <th> 요청자 </th>
            <td>  {{ props.synonymDeleteRequestOne.user_nm }} </td>
            <th> 요청일 </th>
            <td> {{ $yyyyMMddHHmmSS(props.synonymDeleteRequestOne.req_date) }} </td>
          </tr>
          <tr>
            <th scope="row"> 대표어 </th>
            <td colspan="3">
              <input type="text" placeholder="" class="mu-input" v-model="props.synonymDeleteRequestOne.term" :disabled="props.isAdmin"/>
            </td>
          </tr>
          <tr>
            <th scope="row">동의어 목록</th>
            <td colspan="3">
              <div class="mu-area h125 disabled">
                <div class="tags-group">
                  <button type="button" :class="synonymInfo.toggle == true ? 'tag tag-selected' : 'tag'" v-for="(synonymInfo, c_index) in props.synonymOneList" :key="c_index"><span class="tag-label">{{ synonymInfo.synonym }}</span></button>
                </div>
              </div>
            </td>
          </tr>
          <tr>
            <th scope="row">삭제 요청 사유</th>
            <td colspan="3">
              <textarea class="mu-area h100" title="삭제 사유" maxlength="100" v-model="props.synonymDeleteRequestOne.request" disabled />
            </td>
          </tr>
          </tbody>
        </table>
        <div class="mu-button-wrap" v-if="props.isAdmin">
          <button
            type="button"
            class="mu-btn mu-btn-lg mu-btn-primary"
            @click="onClickRequestConfirm(props.synonymDeleteRequestOne)">
            <span>승인</span>
          </button>
        </div>
        <div class="mu-heading" v-if="props.isAdmin">
          <div class="mu-item-group">
            <p class="bul-ast">반려 시 반려 사유 입력</p>
          </div>
        </div>
        <table class="mu-formbox" v-if="props.isAdmin">
          <caption>동의어 삭제 요청 내역 게시판</caption>
          <colgroup>
            <col style="width:150px">
            <col>
          </colgroup>
          <tbody>
            <tr>
              <th scope="row">반려 사유<strong class="required">필수</strong></th>
              <td>
                <textarea class="mu-area h100" placeholder="반려 사유를 입력해주세요.(최대 100자)"  title="반려 사유 입력" v-model="response" maxlength="100"></textarea>
              </td>
            </tr>
          </tbody>
        </table>
        <div v-if="props.isAdmin" class="mu-dialog-foot">
          <button type="button" class="mu-btn" @click="onClose">
            <span>취소</span>
          </button>
          <button
            type="button"
            class="mu-btn mu-btn-primary"
            @click="onClickRequestReject(props.synonymDeleteRequestOne)">
            <span>반려</span>
          </button>
        </div>

        <div v-else class="mu-dialog-foot">
          <button type="button" class="mu-btn" @click="onClose">
            <span>닫기</span>
          </button>
          <button
            type="button"
            class="mu-btn mu-btn-primary"
            @click="onClickRequestCancel(props.synonymDeleteRequestOne.req_id)">
            <span>요청 취소</span>
          </button>
        </div>

        <!-- 접근성 : 내용을 읽고 나서 닫기 나중으로 빼준다 -->
        <div class="mu-dialog-close-btn">
          <button
            type="button"
            class="mu-btn mu-btn-close"
            title="닫기"
            @click="onClose">
            <i class="mu-icon-svg close"></i>
            <span>닫기</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { confirmAlert, errorAlert } from "@/components/functional/alert/alert-default";

const { $ofetch } = useNuxtApp();

const emit = defineEmits(['reloadSynonymDeleteRequestList', 'onCloseModal'])
const props = defineProps(['toggle', 'synonymOneList', 'synonymDeleteRequestOne', 'isAdmin'])
const status = ref(0) // 진행 상태 [0:완료 1:요청 2:승인 3:반려]
const response = ref("") // 반려 사유

// 동의어 삭제 요청 취소 버튼
const onClickRequestCancel = async (req_id) => {
  if (await confirmAlert('해당 삭제 요청을 취소합니다. 정말 취소하시겠습니까?')) {

    // 요청 취소하는 시기에 진행 상태 확인
    await $ofetch(`/api/synonym/getDeleteRequestOneStatus/${req_id}`)
      .then((res) => {
        if (res && res.data) {
          status.value = res.data
        } else {
          errorAlert('삭제 요청취소에 실패하였습니다.')
        }
      });

    if (status.value == 1) { // 요청 상태
      await $ofetch(`/api/synonym/deleteRequestOne/${req_id}`, {
        method: 'DELETE'
      })
        .then((res) => {
          if (res && res.errorMessage) {
            errorAlert('삭제 요청취소에 실패하였습니다.')
          }
        })
    } else if (status.value == 2) { // 이미 승인된 경우
      await errorAlert('이미 승인 처리된 요청입니다.')

    } else if (status.value == 3) { // 이미 반려된 경우
      await errorAlert('이미 반려 처리된 요청입니다.')
    }

    // 데이터 갱신
    emit('reloadSynonymDeleteRequestList')
    emit('onCloseModal')
  }
}

// 동의어 삭제 요청 승인 버튼
const onClickRequestConfirm = async (req) => {
  if (await confirmAlert("정말 승인하시겠습니까?")) {

    // 승인 정보
    const params = {
      id: req.req_id,
      request: req.request,
      req_user: req.req_user,
      req_date: req.req_date,
      term: req.term,
      synonyms: req.synonyms,
    }

    // 승인 처리
    await $ofetch(`/api/synonym/confirmDeleteRequestOne`, {
      method: 'POST',
      body: params
    })

    // 데이터 갱신
    emit('reloadSynonymDeleteRequestList')
    emit('onCloseModal')
  }
}

// 동의어 삭제 요청 반려 버튼
const onClickRequestReject = async (req) => {
  if (response.value == "") {
    errorAlert("반려 사유를 작성해 주세요.")
    return
  }

  if (await confirmAlert("정말 반려하시겠습니까?")) {
    // 반려 정보
    const params = {
      id: req.req_id,
      request: req.request,
      response: response.value,
      req_date: req.req_date,
      req_user: req.req_user,
      term: req.term,
      synonyms: req.synonyms
    }

    // 반려 처리
    await $ofetch(`/api/synonym/rejectDeleteRequestOne`, {
      method: 'POST',
      body: params
    })

    // 데이터 갱신
    response.value = "";
    emit('reloadSynonymDeleteRequestList')
    emit('onCloseModal')
  }
}

// 동의어 추가 취소 버튼
const onClose = async () => {
  response.value = ""
  emit('onCloseModal')
}

</script>
