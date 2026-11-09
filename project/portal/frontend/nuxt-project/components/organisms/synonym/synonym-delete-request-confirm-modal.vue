<template>
  <div class="mu-modal" v-if="props.toggle">
    <div class="mu-dialog-background"></div>
    <!-- 모달팝업 : 동의어 삭제 요청 승인 내역 -->
    <div class="mu-dialog w900 ">
      <div class="mu-dialog-head">
        <p class="title">동의어 삭제 요청 승인 내역</p>
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
              <td> {{ props.synonymDeleteRequestOne.user_nm }} </td>
              <th> 요청일 </th>
              <td> {{ $yyyyMMddHHmmSS(props.synonymDeleteRequestOne.req_date) }} </td>
            </tr>
            <tr>
              <th scope="row"> 대표어 </th>
              <td colspan="3">
                <input type="text" placeholder="" class="mu-input" title="대표어"
                  v-model="props.synonymDeleteRequestOne.term" disabled />
              </td>
            </tr>
            <tr>
              <th scope="row">동의어 목록</th>
              <td colspan="3">
                <div class="mu-area h125 disabled">
                  <div class="tags-group">
                    <button type="button" :class="synonymInfo.toggle == true ? 'tag tag-selected' : 'tag'"
                      v-for="(synonymInfo, c_index) in props.synonymOneList" :key="c_index"><span class="tag-label">{{
                        synonymInfo.synonym }}</span></button>
                  </div>
                </div>
              </td>
            </tr>
            <tr>
              <th scope="row">삭제 요청 사유</th>
              <td colspan="3">
                <textarea class="mu-area h100" title="삭제 사유" maxlength="100"
                  v-model="props.synonymDeleteRequestOne.request" disabled />
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="mu-dialog-foot">
        <button type="button" class="mu-btn mu-btn-primary" @click="onClose">
          <span>닫기</span>
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
const emit = defineEmits(['onCloseModal'])
const props = defineProps(['toggle', 'synonymOneList', 'synonymDeleteRequestOne', 'isAdmin'])

// 동의어 추가 취소 버튼
const onClose = async () => {
  emit('onCloseModal')
}

</script>
