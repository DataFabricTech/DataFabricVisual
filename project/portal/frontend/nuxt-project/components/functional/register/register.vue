<template lang="html">
  <div class="common-login">
    <div class="login-wrap">
      <div class="login-container">
        <div class="login-content">
          <h3 class="login-title">회원가입</h3>
          <form autocomplete="off">
            <fieldset>
              <legend>회원가입</legend>
              <div class="login-box">
                <div class="mu-input-id">
                  <label for="runame">이름<strong class="required">필수</strong></label>
                  <input v-bind:class="[!reg_username_valid ? 'error' : '', 'mu-input']" type="text" id="reg_username"
                    name="reg_username" v-model="reg_username" maxlength="15" required placeholder="이름을 입력해주세요."
                    :readonly="readonly" @focus="readonly = false" autocomplete="off" />
                  <button v-if="reg_username != ''" type="button" class="mu-btn mu-btn-delete"
                    v-on:click="reg_username = ''"><i class="mu-icon-svg item-delete"></i><span>아이디 삭제</span></button>
                </div>
                <p v-if="!reg_username_valid" class="complete-msg error">이름을 입력해주세요.</p>
                <div class="mu-input-id">
                  <label for="ruemail">이메일<strong class="required">필수</strong></label>
                  <input v-bind:class="[!reg_useremail_valid ? 'error' : '', 'mu-input']" type="email"
                    id="reg_useremail" name="reg_useremail" v-model="reg_useremail" maxlength="355" required
                    placeholder="이메일을 입력해주세요." />
                  <button v-if="reg_useremail != ''" type="button" class="mu-btn mu-btn-delete"
                    v-on:click="reg_useremail = ''"><i class="mu-icon-svg item-delete"></i><span>아이디 삭제</span></button>
                </div>
                <p v-if="!reg_useremail_valid" class="complete-msg error">이메일을 입력해주세요.</p>
                <div class="mu-input-id">
                  <div><label for="ruid">아이디<strong class="required">필수</strong>
                      <div style="display: inline;"></div>
                    </label></div>
                  <input v-bind:class="[!reg_userid_valid ? 'error' : '', 'mu-input']" type="text"
                    oninput="this.value = this.value.replace(/[^a-zA-Z0-9]/g, '')" id="reg_userid" name="reg_userid"
                    v-model="reg_userid" maxlength="15" required placeholder="아이디를 입력해주세요." />
                  <button v-if="reg_userid != ''" type="button" class="mu-btn mu-btn-delete"
                    v-on:click="reg_userid = ''"><i class="mu-icon-svg item-delete"></i>
                    <span>아이디 삭제</span>
                  </button>
                  <button type="button" class="mu-btn" @click="checkDupUserId"
                    style="bottom: 10px"><span>중복확인</span></button>
                </div>
                <p v-if="!reg_userid_valid" class="complete-msg error">아이디를 입력해주세요.</p>
                <div class="mu-input-pw">
                  <label for="rupw">비밀번호<strong class="required">필수</strong></label>
                  <input v-bind:class="[!reg_password_valid ? 'error' : '', 'mu-input']"
                    :type="isShowPassword ? 'text' : 'password'" id="reg_password" name="reg_password"
                    v-model="reg_password" maxlength="15" required placeholder="비밀번호를 입력해주세요."
                    @keydown.enter.prevent='onRegister'>
                  <button v-if="reg_password != ''" type="button" class="mu-btn mu-btn-delete"
                    v-on:click="reg_password = ''"><i class="mu-icon-svg item-delete"></i><span>비밀번호 삭제</span></button>
                  <button type="button" class="mu-btn mu-btn-pw-mask" v-on:click="isShowPassword = !isShowPassword"><i
                      :class="isShowPassword ? 'mu-icon-svg eye-on' : 'mu-icon-svg eye-off'"></i><span>비밀번호 숨김
                      해제</span></button>
                </div>
                <p v-if="!reg_password_valid" class="complete-msg error">비밀번호를 입력해주세요.</p>

                <div class="mu-input-pw">
                  <label for="rupwok">비밀번호 확인<strong class="required">필수</strong></label>
                  <input :type="isShowPasswordOk ? 'text' : 'password'" id="reg_password_ok" name="reg_password_ok"
                    v-model="reg_password_ok" maxlength="15" required
                    v-bind:class="[!reg_password_ok_valid ? 'error' : '', 'mu-input']" placeholder="비밀번호 확인을 입력해주세요."
                    @keydown.enter.prevent='onRegister'>
                  <button v-if="reg_password_ok != ''" type="button" class="mu-btn mu-btn-delete"
                    v-on:click="reg_password_ok = ''"><i class="mu-icon-svg item-delete"></i><span>비밀번호
                      삭제</span></button>
                  <button type="button" class="mu-btn mu-btn-pw-mask"
                    v-on:click="isShowPasswordOk = !isShowPasswordOk"><i
                      :class="isShowPasswordOk ? 'mu-icon-svg eye-on' : 'mu-icon-svg eye-off'"></i><span>비밀번호 숨김
                      해제</span></button>
                </div>
                <p v-if="!reg_password_ok_valid" class="complete-msg error">비밀번호확인을 입력해주세요.</p>
                <div class="mu-input-id">
                  <label for="rudept">조직<strong class="required">필수</strong></label>
                  <input v-bind:class="[!reg_userdept_valid ? 'error' : '', 'mu-input']" type="text" id="reg_userdeptNm"
                    name="reg_userdeptNm" readonly="readonly" v-model="reg_userdeptNm" required
                    placeholder="조직을 선택하세요." />
                  <input v-bind:class="[!reg_userdept_valid ? 'error' : '', 'mu-input']" type="text" id="reg_userdeptId"
                    name="reg_userdeptId" readonly="readonly" v-model="reg_userdeptId" style="display: none;">
                  <button v-if="reg_userdeptNm != ''" type="button" class="mu-btn mu-btn-delete"
                    v-on:click="reg_userdeptNm = ''"></button>
                  <button type="button" class="mu-btn" style="bottom: 10px"
                    @click="onToggleDeptModal"><span>찾아보기</span></button>
                </div>
                <p v-if="props.register_error" class="complete-msg error">{{ props.register_result_message }}</p>
                <button type="button" class="mu-btn mu-btn-primary"
                  @click.prevent="onRegister"><span>회원가입</span></button>
              </div>
              <div style="text-align: center;">
                <a @click="goToLogin()">로그인 페이지로 돌아가기</a>
              </div>
            </fieldset>
          </form>
        </div>
      </div>
    </div>

    <select-dept-modal :toggle-dept-modal="toggleDeptModal" @onSelect="onSelectDept"
      @onCloseModal="onToggleDeptModal" />

  </div>
</template>

<script setup lang="ts">

import selectDeptModal from "@functional/dept/select-department-modal";

const props = defineProps({
  register_error: {
    Type: Boolean,
    default: false
  },
  register_result_message: {
    Type: String,
    default: ""
  },
  checkedDupUserId: {
    Type: Boolean,
    default: false
  },
})

const toggleDeptModal = ref(false);

const emit = defineEmits(['register', 'toggleRegisterForm', 'checkDupUserId', 'updateCheckedDupUserId'])

const readonly = ref(true)
const reg_username = ref("")
const reg_useremail = ref("")
const reg_userid = ref("")
const reg_password = ref("")
const reg_password_ok = ref("")
const isShowPassword = ref(false)
const isShowPasswordOk = ref(false)
const reg_userdeptId = ref("")
const reg_userdeptNm = ref("")

const reg_username_valid: ComputedRef<boolean> = computed(() => {
  return reg_username.value.length > 0 ? true : false;
});

const reg_useremail_valid: ComputedRef<boolean> = computed(() => {
  return reg_useremail.value.length > 0 && validateEmail(reg_useremail.value) ? true : false;
});

const reg_userid_valid: ComputedRef<boolean> = computed(() => {
  emit('updateCheckedDupUserId')
  return reg_userid.value.length > 0 ? true : false;
});

const reg_password_valid: ComputedRef<boolean> = computed(() => {
  return reg_password.value.length > 0 ? true : false;
});

const reg_password_ok_valid: ComputedRef<boolean> = computed(() => {
  return reg_password_ok.value.length > 0 && reg_password.value == reg_password_ok.value ? true : false;
});

const reg_userdept_valid: ComputedRef<boolean> = computed(() => {
  return reg_userdeptId.value.length > 0 ? true : false;
});

const onRegister = async () => {
  if (!reg_username_valid.value
    || !reg_useremail_valid.value
    || !reg_userid_valid.value
    || !reg_password_valid.value
    || !reg_password_ok_valid.value
    || !reg_userdept_valid.value) return;

  const params = {
    reg_username: reg_username.value,
    reg_useremail: reg_useremail.value,
    reg_userid: reg_userid.value,
    reg_password: reg_password.value,
    reg_userdept: reg_userdeptId.value,
  }
  emit('register', params)
}

const goToLogin = () => {
  clearForm()
  emit('toggleRegisterForm')
}

const clearForm = () => {
  reg_username.value = ""
  reg_useremail.value = ""
  reg_userid.value = ""
  reg_password.value = ""
  reg_password_ok.value = ""
  reg_userdeptId.value = ""
  reg_userdeptNm.value = ""
}

const validateEmail = (email) => {
  const regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  return regex.test(email);
}

const checkDupUserId = () => {
  emit('checkDupUserId', reg_userid.value)
}

const updateCheckedDupUserId = () => {
  emit('updateCheckedDupUserId', false)
}

// 담당부서 선택 모달 토글 제어
const onToggleDeptModal = () => {
  if (toggleDeptModal.value) {
    toggleDeptModal.value = false;
  } else {
    toggleDeptModal.value = true;
  }
};

// 담당부서 선택 이벤트 핸들러
const onSelectDept = (info) => {
  reg_userdeptNm.value = info.dept_nm;
  reg_userdeptId.value = info.dept_cd;
};
</script>
