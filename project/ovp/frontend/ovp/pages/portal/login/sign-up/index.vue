<template>
  <div class="wrap is-wrap-height-full">
    <div class="submit-form">
      <h1 class="submit-form-logo">LOGO</h1>
      <form class="submit-form-form" @submit.prevent="onSubmit">
        <h2 class="submit-form-title">회원가입</h2>
        <div class="form form-lg gap-8">
          <div class="form-body">
            <div class="form-item">
              <label for="inpName" class="form-label">
                이름
                <span class="required">*</span>
              </label>
              <div class="form-detail flex flex-col">
                <div class="text-input-group w-full">
                  <input
                    id="inpName"
                    class="text-input text-input-lg"
                    placeholder="이름 입력"
                    v-model="form.name"
                  />
                </div>
                <div class="notification notification-sm notification-error" v-if="errors.name">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">{{errors.name}}</p>
                </div>
              </div>
            </div>
            <div class="form-item">
              <label for="inpEmail" class="form-label">
                이메일
                <span class="required">*</span>
              </label>
              <div class="form-detail flex flex-col">
                <div class="text-input-group w-full">
                  <input
                    id="inpEmail"
                    class="text-input text-input-lg"
                    placeholder="이메일 입력"
                    v-model="form.email"

                  />
                </div>
                <div class="notification notification-sm notification-error" v-if="errors.email">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">{{errors.email}}</p>
                </div>
              </div>
            </div>
            <div class="form-item">
              <label for="inpPw" class="form-label">
                비밀번호
                <span class="required">*</span>
              </label>
              <div class="form-detail flex flex-col">
                <div class="text-input-group w-full">
                  <input
                    id="inpPw"
                    :type="inputPasswordType"
                    class="text-input text-input-lg"
                    placeholder="비밀번호 입력"
                    v-model="form.password"
                  />
                  <button
                    class="text-input-group-action-button button button-neutral-ghost button-sm"
                    type="button"
                    @click="isHidePw"
                  >
                    <span class="hidden-text">지우기</span>
                    <svg-icon class="button-icon" name="eye"></svg-icon>
                  </button>
                </div>
                <div class="notification notification-sm notification-error" v-if="errors.password">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">
                    <p class="notification-detail">{{errors.password}}</p>
                  </p>
                </div>
              </div>
            </div>
            <div class="form-item">
              <label for="inpPw" class="form-label">
                비밀번호 확인
                <span class="required">*</span>
              </label>
              <div class="form-detail flex flex-col">
                <div class="text-input-group w-full">
                  <input
                    id="inpConfirmPw"
                    :type="inputConfirmPasswordType"
                    class="text-input text-input-lg"
                    placeholder="비밀번호 입력"
                    v-model="form.confirmPassword"
                  />
                  <button
                    class="text-input-group-action-button button button-neutral-ghost button-sm"
                    type="button"
                    @click="isHideConfirmPw"
                  >
                    <span class="hidden-text">지우기</span>
                    <svg-icon class="button-icon" name="eye"></svg-icon>
                  </button>
                </div>
                <div class="notification notification-sm notification-error" v-if="errors.confirmPassword">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">
                    <p class="notification-detail">{{errors.confirmPassword}}</p>
                  </p>
                </div>
              </div>
            </div>
          </div>
          <div class="form-foot">
            <button class="button button-primary button-lg" type="submit">
              회원가입
            </button>
            <button
              class="button button-primary-ghost button-sm"
              type="button"
            >
              <nuxt-link :to="'/portal/login'">
                로그인 페이지로 돌아가기
              </nuxt-link>
            </button>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { loginStore } from "@/store/login/index";
import type { Ref } from "vue";
import { useRouter } from "vue-router";

const form: {
  name: string;
  email: string;
  password: string;
  confirmPassword: string;
} = reactive({
  name: "",
  email: "",
  password: "",
  confirmPassword: ""
});

const errors: {
  name: string;
  email: string;
  password: string;
  confirmPassword: string;
}  = reactive({
  name: '',
  email: '',
  password: '',
  confirmPassword: ''
});

const inputPasswordType: Ref<string> = ref("password");
const isHidePw = () => {
  inputPasswordType.value = inputPasswordType.value === "password" ? "text" : "password";
};
const inputConfirmPasswordType: Ref<string> = ref("password");
const isHideConfirmPw = () => {
  inputConfirmPasswordType.value = inputConfirmPasswordType.value === "password" ? "text" : "password";
};

const store = loginStore();
const router = useRouter();

const onSubmit = async () => {
  if (!validateForm()) {
    return;
  }

  if (await isDuplicateEmail()) {
    return;
  }
  await signUp();
};
const validateForm = () => {
  const isErrorName = validateName();
  const isErrorEmail = validateEmail();
  const isErrorPassword = validatePassword();
  const isErrorConfirmPassword = validateConfirmPassword();

  return isErrorName && isErrorEmail && isErrorPassword && isErrorConfirmPassword;
};

const validateName = () => {
  errors.name = form.name ? "" : "사용자 이름을 입력하세요.";
  return !errors.name;
};

const validateEmail = () => {
  errors.email = form.email ? "" : "사용자 이메일을 입력하세요.";
  if (errors.email) return false;

  // TODO: constants 연결 필요
  const EMAIL_REG = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  errors.email = EMAIL_REG.test(form.email) ? "" : "이메일이 유효하지 않습니다.";
  return !errors.email;
};

const validatePassword = () => {
  errors.password = form.password ? '' : '사용자 비밀번호를 입력하세요.';
  if (errors.password) return false;

  // TODO: constants 연결 필요
  const PASSWORD_REG = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[~`!@#$%^()_\-={}\[\]|;:<>,?/])[A-Za-z\d~`!@#$%^()_\-={}\[\]|;:<>,?/]{8,56}$/;
  errors.password = PASSWORD_REG.test(form.password) ? "" : "비밀번호는 최소 8자, 최대 56자여야 하며 대문자, 소문자, 숫자, 특수 문자를 포함해야 합니다.";
  return !errors.password;
};

const validateConfirmPassword = () => {
  errors.confirmPassword = form.confirmPassword ? "" : "사용자 비밀번호를 입력하세요.";
  if (errors.confirmPassword) return false;
  errors.confirmPassword = form.password === form.confirmPassword ? "" : "비밀번호가 일치하지 않습니다.";
  return !errors.confirmPassword;
};


const isDuplicateEmail = async () => {
  const result = await store.checkDuplicateEmail({ email: form.email });
  const isDuplicate = result.data;
  errors.email = isDuplicate ? "이미 사용 중인 이메일입니다." : "";
  return isDuplicate;
};

const signUp = async () => {
  const result = await store.signUpUser({
    email: form.email,
    password: form.password,
    name: form.name
  });


  if (result.result === 0  && result.errorMessage) {
    alert(result.errorMessage)
  } else {
    alert('회원가입 성공');
    router.push('/portal/login');
  }
};

</script>

<style lang="scss" scoped></style>
