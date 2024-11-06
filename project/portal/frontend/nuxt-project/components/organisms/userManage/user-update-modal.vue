<template>
  <div class="mu-modal" v-if="props.visibleUpdateModal">
    <div class="mu-dialog-background"></div>
    <div class="mu-dialog w600">
      <div class="mu-dialog-head">
        <p class="title">{{ props.updateFlag ? "사용자 정보 수정" : "사용자 정보 확인" }}</p>
      </div>
      <div class="mu-dialog-body">
        <table class="mu-formbox">
          <caption>
              사용자 정보 수정 게시판
          </caption>
          <colgroup>
              <col style="width: 150px" />
              <col />
          </colgroup>
          <tbody>
            <tr>
              <th scope="row">이름</th>
              <td>
                <input type="text" placeholder="" class="mu-input" title="이름" readonly="readonly" v-model="props.modalParam.userObject.user_nm" />
              </td>
            </tr>
            <tr>
              <th scope="row">사번</th>
              <td>
                <input type="text" placeholder="" class="mu-input" title="사번" readonly="readonly" v-model="props.modalParam.userObject.user_id" />
              </td>
            </tr>
            <tr>
              <th scope="row">부서</th>
              <td>
                <input type="text" placeholder="" class="mu-input" title="부서" value="경영지원팀" readonly="readonly" v-model="props.modalParam.userObject.dept_nm" />
              </td>
            </tr>
            <tr>
              <th scope="row">권한</th>
              <td>
                <!-- mu-selectbox -->
                <div v-if="props.updateFlag" class="mu-selectbox">
                  <select v-model="props.modalParam.userObject.role_name">
                    <option value="User">사용자</option>
                    <option value="Manager">데이터 엔지니어</option>
                    <option value="Super-Admin">시스템 관리자</option>
                  </select>
                </div>
                <input v-if="!props.updateFlag" type="text" placeholder="" class="mu-input" readonly="readonly" :value="getKrName(props.modalParam.userObject.role_name)" />
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="mu-dialog-foot">
          <button v-if="!props.updateFlag" type="button" class="mu-btn mu-btn-primary" @click="close"><span>닫기</span></button>
          <button v-if="props.updateFlag" type="button" class="mu-btn" @click="close"><span>취소</span></button>
          <button v-if="props.updateFlag" type="button" class="mu-btn mu-btn-primary" @click="onClickSaveButton"><span>확인</span></button>
      </div>
      <!-- 접근성 : 내용을 읽고 나서 닫기 나중으로 빼준다  -->
      <div class="mu-dialog-close-btn">
          <button type="button" class="mu-btn mu-btn-close" title="해당 팝업 닫기" @click="close"><i class="mu-icon-svg close"></i><span>팝업닫기</span></button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { confirmAlert, errorAlert } from "@functional/alert/alert-default";
import { successAlert } from "@/components/functional/alert/alert-default";

const { $ofetch } = useNuxtApp()

const emit = defineEmits(['update:visibleUpdateModal', 'searchMethod'])
const props = defineProps(['visibleUpdateModal', 'updateFlag', 'modalParam'])

const close = () => {
  emit('update:visibleUpdateModal', false)
}

// 저장버튼 클릭 이벤트
const onClickSaveButton = async () => {
  const result = await confirmAlert("저장하시겠습니까?");
  if(!result) return;

  let param = {
      "user_id" : props.modalParam.userObject.user_id,
      "role_name" : props.modalParam.userObject.role_name,
  }
  $ofetch('/api/admin/manager/user/update', {
    method: 'POST',
    body: param
  })
    .then(async (res) => {
      if(res && !res.errorMessage) {
        await successAlert('저장되었습니다.');
        emit('searchMethod'); // 사용자 Tab: 사용자목록조회 함수 호출
        close(); // 창닫기
      }
    })
}

const getKrName = (auth) => {
  if (auth == 'Super-Admin') return '시스템 관리자'
  if (auth == 'Manager') return '데이터 엔지니어'
  if (auth == 'User') return '사용자'
}
</script>
