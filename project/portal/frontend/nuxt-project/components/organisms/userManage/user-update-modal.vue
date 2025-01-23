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
              사용자 정보 수정
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
              <th scope="row">조직명</th>
              <td>
                <input type="text" placeholder="" class="mu-input w220" title="부서" value="경영지원팀" readonly="readonly" v-model="props.modalParam.userObject.dept_nm" />
                <button type="button" class="mu-btn" @click="onToggleDeptModal" style="margin-left:10px" :disabled="!props.updateFlag">
                  <span>찾아보기</span>
                </button>
              </td>
            </tr>
            <tr>
              <th scope="row">이메일</th>
              <td>
                <input type="text" placeholder="" class="mu-input" title="부서" value="" readonly="readonly" v-model="props.modalParam.userObject.user_email" />
              </td>
            </tr>
            <tr>
              <th scope="row">권한</th>
              <td>
                <div v-if="props.updateFlag" class="mu-selectbox">
                  <select v-model="props.modalParam.userObject.role_name">
                    <option v-for="role in selectRollList" :key="role.role_id" :value="role.role_id">
                      {{role.role_name}}
                    </option>
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

      <div class="mu-dialog-close-btn">
          <button type="button" class="mu-btn mu-btn-close" title="해당 팝업 닫기" @click="close"><i class="mu-icon-svg close"></i><span>팝업닫기</span></button>
      </div>
    </div>

    <select-dept-modal
      :toggle-dept-modal="toggleDeptModal"
      @onSelect="onSelectDept"
      @onCloseModal="onToggleDeptModal"
    />

  </div>
</template>

<script setup lang="ts">
import { confirmAlert, errorAlert } from "@functional/alert/alert-default";
import { successAlert } from "@/components/functional/alert/alert-default";
import selectDeptModal from "@organisms/metaDataManage/select-department-modal";

const { $ofetch } = useNuxtApp()
const emit = defineEmits(['update:visibleUpdateModal', 'searchMethod'])
const props = defineProps(['visibleUpdateModal', 'updateFlag', 'modalParam'])

const toggleDeptModal = ref(false);
const selectRollList = ref();

// 
onMounted(() => getRollList());

// 
const close = () => {
  emit('update:visibleUpdateModal', false)
}

// 
const onClickSaveButton = async () => {
  const result = await confirmAlert("저장하시겠습니까?");
  if(!result) return;

  let param = {
      "user_id" : props.modalParam.userObject.user_id,
      "role_name" : props.modalParam.userObject.role_name,
      "dept_cd" : props.modalParam.userObject.dept_cd,
      "email" : props.modalParam.userObject.user_email,
  }
  $ofetch('/api/admin/manager/user/update', {
    method: 'POST',
    body: param
  })
    .then(async (res) => {
      if(res && !res.errorMessage) {
        await successAlert('저장 되었습니다.');
        emit('searchMethod');
        close();
      }
    })
}

// 
const getRollList = async () => {
  await $ofetch(`/api/rollList?role_type=0`)
      .then((res) => {
        if (res && !res.errorMessage) {
          selectRollList.value = res.data.filter(role => role.role_type === 0);
        }
      })
      .catch((err) => {
        console.log(err)
      });
}

// 
const getKrName = (auth) => {
  if (auth == 'Super-Admin') return '시스템 관리자'
  if (auth == 'Manager') return '데이터 엔지니어'
  if (auth == 'User') return '사용자'
}

// 
const onToggleDeptModal = () => {
  if (toggleDeptModal.value) {
    toggleDeptModal.value = false;
  } else {
    toggleDeptModal.value = true;
  }
};

// 
const onSelectDept = (info) => {
  props.modalParam.userObject.dept_nm = info.dept_nm;
  props.modalParam.userObject.dept_cd = info.dept_cd;
};
</script>
