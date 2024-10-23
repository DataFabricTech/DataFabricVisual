<template>
  <div class="wrap is-wrap-height-full">
    <div class="submit-form">
      <h1 class="submit-form-logo">
        <span class="text-neutral-700 text-22 font-bold">노후시설 재난안전 학습 데이터 관리</span>
      </h1>
      <form class="submit-form-form" @submit.prevent="onSubmit">
        <h2 class="submit-form-title">회원가입</h2>
        <div class="form form-lg gap-6">
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
                    autocomplete="off"
                    v-model="form.displayName"
                    @input="validateDisplayName"
                  />
                </div>
                <div
                  class="notification notification-sm notification-error"
                  v-if="errors.displayName"
                >
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">{{ errors.displayName }}</p>
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
                    autocomplete="off"
                    v-model="form.email"
                    @input="validateEmail"
                  />
                </div>
                <div
                  class="notification notification-sm notification-error"
                  v-if="errors.email"
                >
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">{{ errors.email }}</p>
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
                    autocomplete="new-password"
                    v-model="newPassword"
                    @input="validatePassword"
                  />
                  <button
                    class="text-input-group-action-button button button-neutral-ghost button-sm"
                    type="button"
                    @click="isHidePw"
                  >
                    <span class="hidden-text">비밀번호 보기 해제</span>
                    <svg-icon
                      class="button-icon"
                      :name="
                        getPwdIconName({
                          inputType: inputPasswordType,
                        })
                      "
                    ></svg-icon>
                  </button>
                </div>
                <div
                  class="notification notification-sm notification-error"
                  v-if="errorMsgPassword"
                >
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">
                    {{ errorMsgPassword }}
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
                    autocomplete="new-password"
                    v-model="confirmPassword"
                    @input="validateConfirmPassword"
                  />
                  <button
                    class="text-input-group-action-button button button-neutral-ghost button-sm"
                    type="button"
                    @click="isHideConfirmPw"
                  >
                    <span class="hidden-text">지우기</span>
                    <svg-icon
                      class="button-icon"
                      :name="
                        getPwdIconName({
                          inputType: inputConfirmPasswordType,
                        })
                      "
                    ></svg-icon>
                  </button>
                </div>
                <div
                  class="notification notification-sm notification-error"
                  v-if="errorMsgConfirmPassword"
                >
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">
                    {{ errorMsgConfirmPassword }}
                  </p>
                </div>
              </div>
            </div>
          </div>
          <div class="form-foot">
            <button class="button button-primary button-lg" type="submit">
              회원가입
            </button>
            <button class="button button-primary-ghost button-sm" type="button">
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
import { useRouter } from "vue-router";
import { PasswordComposition } from "@/components/login/PasswordComposition";
import { useCommonUtils } from "@/composables/commonUtils";
import $constants from "@/utils/constant";
import { useUserStore } from "@/store/user/userStore";
import _ from "lodash";

import { useNuxtApp } from "nuxt/app";
const { $alert } = useNuxtApp();

const {
  newPassword,
  confirmPassword,
  inputPasswordType,
  inputConfirmPasswordType,
  errorMsgPassword,
  errorMsgConfirmPassword,
  isHidePw,
  isHideConfirmPw,
  validatePassword,
  validateConfirmPassword,
} = PasswordComposition();
const { getPwdIconName } = useCommonUtils();

const form: {
  name: string;
  email: string;
  displayName: string;
} = reactive({
  name: "",
  email: "",
  displayName: "",
});

const errors: {
  displayName: string;
  email: string;
} = reactive({
  displayName: "",
  email: "",
});

const store = loginStore();
const userStore = useUserStore();
const router = useRouter();

const { checkDuplicateEmail, signUpUser } = store;
const { checkDuplicateName } = userStore;

const onSubmit = async () => {
  // name 값은 email 의 id 값과 같음
  form.name = _.first(_.split(form.email, "@")) || "";

  const isFormValid = validateForm();
  const isEmailValid = _.isEmpty(errors.email)
    ? !((await isDuplicateEmail()) || (await isDuplicateName()))
    : false;
  if (!isFormValid || !isEmailValid) return;

  await signUp();
};
const validateForm = () => {
  const isErrorDisplayName = validateDisplayName();
  const isErrorEmail = validateEmail();
  const isErrorPassword = validatePassword();
  const isErrorConfirmPassword = validateConfirmPassword();

  return (
    isErrorDisplayName &&
    isErrorEmail &&
    isErrorPassword &&
    isErrorConfirmPassword
  );
};

const validateDisplayName = () => {
  errors.displayName = form.displayName
    ? ""
    : $constants.LOGIN.DISPLAY_NAME.INPUT_ERROR_MSG;
  return !errors.displayName;
};

const validateEmail = () => {
  errors.email = form.email ? "" : $constants.LOGIN.EMAIL.INPUT_ERROR_MSG;
  if (errors.email) return false;

  errors.email = $constants.LOGIN.EMAIL.REGEX.test(form.email)
    ? ""
    : $constants.LOGIN.EMAIL.REGEX_ERROR_MSG;
  return !errors.email;
};

const isDuplicateEmail = async () => {
  const result = await checkDuplicateEmail({ email: form.email });
  const isDuplicate = result.data;
  errors.email = isDuplicate ? $constants.LOGIN.EMAIL.DUPLICATE_ERROR_MSG : "";
  return isDuplicate;
};

const isDuplicateName = async () => {
  const isDuplicate = await checkDuplicateName(form.name);
  errors.email = isDuplicate
    ? $constants.LOGIN.EMAIL.DUPLICATE_ID_ERROR_MSG
    : "";
  return isDuplicate;
};

const signUp = async () => {
  const result = await signUpUser({
    ...form,
    password: newPassword,
  });

  if (result.result === 0 && result.errorMessage) {
    $alert(result.errorMessage, "error");
  } else {
    router.push("/portal/login");
  }
};
</script>

<style lang="scss" scoped></style>
