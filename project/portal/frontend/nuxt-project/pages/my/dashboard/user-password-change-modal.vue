<template>
  <div class="mu-modal" v-if="props.visibleUpdateModal">
    <div class="mu-dialog-background"></div>
    <div class="mu-dialog w600">
      <div class="mu-dialog-head">
        <p class="title">패스워드 변경</p>
      </div>
      <div class="mu-dialog-body">
        <table class="mu-formbox">
          <caption>
            사용자 패스워드 변경
          </caption>
          <colgroup>
            <col style="width: 150px" />
            <col />
          </colgroup>
          <tbody>
            <tr>
              <th scope="row">이름</th>
              <td>
                <input type="text" placeholder="" class="mu-input" title="이름" readonly="readonly"
                  v-model="userStore.getUser.username" />
              </td>
            </tr>
            <tr>
              <th scope="row">사번</th>
              <td>
                <input type="text" placeholder="" class="mu-input" title="사번" readonly="readonly"
                  v-model="userStore.getUser.user_id" />
              </td>
            </tr>
            <tr>
              <th scope="row">기존 패스워드<strong class="required">필수</strong></th>
              <td>
                <div class="mu-input-pw">
                  <input type="password" placeholder="" class="mu-input" title="기존 패스워드" style="width:230px"
                    v-model="verify_password" @keydown.enter.prevent='verifyPassword' />
                  <button type="button" class="mu-btn" @click="verifyPassword()" style="margin-left:10px">
                    <span>확인</span>
                  </button>
                </div>
                <p v-if="!passwordVerify" class="complete-msg error">비밀번호를 확인하여 주세요.</p>
              </td>
            </tr>
            <tr>
              <th scope="row">신규 패스워드<strong class="required">필수</strong></th>
              <td>
                <div class="mu-input-pw">
                  <input v-bind:class="[!reg_password_valid ? 'error' : '', 'mu-input']"
                    :type="isShowPassword ? 'text' : 'password'" id="reg_password" name="reg_password"
                    v-model="reg_password" maxlength="15" required placeholder="비밀번호를 입력해주세요.">
                </div>
                <p v-if="!reg_password_valid" class="complete-msg error">비밀번호를 입력해주세요.</p>
              </td>
            </tr>
            <tr>
              <th scope="row">패스워드 확인<strong class="required">필수</strong></th>
              <td>
                <div class="mu-input-pw">
                  <input :type="isShowPasswordOk ? 'text' : 'password'" id="reg_password_ok" name="reg_password_ok"
                    v-model="reg_password_ok" maxlength="15" required
                    v-bind:class="[!reg_password_ok_valid ? 'error' : '', 'mu-input']" placeholder="비밀번호 확인을 입력해주세요.">
                </div>
                <p v-if="!reg_password_ok_valid" class="complete-msg error">비밀번호확인을 입력해주세요.</p>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="mu-dialog-foot">
        <button type="button" class="mu-btn" @click="close"><span>취소</span></button>
        <button type="button" class="mu-btn mu-btn-primary" @click="onPasswordChange"><span>확인</span></button>
      </div>

      <div class="mu-dialog-close-btn">
        <button type="button" class="mu-btn mu-btn-close" title="해당 팝업 닫기" @click="close"><i
            class="mu-icon-svg close"></i><span>팝업닫기</span></button>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { errorAlert } from "@functional/alert/alert-default";
import { successAlert } from "@/components/functional/alert/alert-default";
import { useUserStore } from "@/store/user";
import { ref } from "vue";


const { $ofetch } = useNuxtApp()
const userStore = useUserStore();
const emit = defineEmits(['update:visibleUpdateModal'])
const props = defineProps(['visibleUpdateModal', 'modalParam'])

const passwordVerify = ref(false);
const verify_password = ref("");
const reg_password = ref("");
const reg_password_ok = ref("");
const isShowPassword = ref(false);
const isShowPasswordOk = ref(false);

const reg_password_valid: ComputedRef<boolean> = computed(() => {
  return reg_password.value.length > 0 ? true : false;
});

const reg_password_ok_valid: ComputedRef<boolean> = computed(() => {
  return reg_password_ok.value.length > 0 && reg_password.value == reg_password_ok.value && verify_password.value != reg_password.value ? true : false;
});

//
const verifyPassword = async () => {
  if (!verify_password.value) {
    await errorAlert('비밀번호를 입력해주세요.');
    return;
  }

  let param = {
    "user_id": userStore.getUser.user_id,
    "password": verify_password.value
  }
  $ofetch('/api/verifyPassword', {
    method: 'POST',
    body: param
  })
    .then(async (res) => {
      if (res && res.data.login_result && res.data.login_result == 'Y') {
        passwordVerify.value = true;
        await successAlert('확인 되었습니다.');
      } else {
        passwordVerify.value = false;
        await errorAlert('비밀번호가 일치하지 않습니다.');
      }
    })
}

//
const onPasswordChange = async () => {
  if (!passwordVerify
    || !reg_password_valid.value
    || !reg_password_ok_valid.value) return;

  let param = {
    "user_id": userStore.getUser.user_id,
    "password": reg_password.value
  }

  $ofetch('/api/changePassword', {
    method: 'POST',
    body: param
  })
    .then(async (res) => {
      if (res && res.data.register_result && res.data.register_result == 'Y') {
        clearForm();

        await successAlert('처리 되었습니다.');

        close();
      }
    })
}

//
const clearForm = () => {
  passwordVerify.value = false;
  verify_password.value = "";
  reg_password.value = ""
  reg_password_ok.value = ""
}

//
const close = () => {
  clearForm();

  emit('update:visibleUpdateModal', false)
}

</script>
