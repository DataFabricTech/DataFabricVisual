<template>
  <div class="wrap is-wrap-height-full">
    <div class="submit-form">
      <h1 class="submit-form-logo">
        <svg-icon class="svg-icon logo-img" name="logo"></svg-icon>
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
                    v-model="form.name"
                  />
                </div>
                <div
                  class="notification notification-sm notification-error"
                  v-if="errors.name"
                >
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">{{ errors.name }}</p>
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
                    :type="pwComposition.inputPasswordType.value"
                    class="text-input text-input-lg"
                    placeholder="비밀번호 입력"
                    v-model="pwComposition.newPassword.value"
                  />
                  <button
                    class="text-input-group-action-button button button-neutral-ghost button-sm"
                    type="button"
                    @click="pwComposition.isHidePw"
                  >
                    <span class="hidden-text">비밀번호 보기 해제</span>
                    <svg-icon
                      class="button-icon"
                      :name="
                        getPwdIconName({
                          inputType: pwComposition.inputPasswordType.value,
                        })
                      "
                    ></svg-icon>
                  </button>
                </div>
                <div
                  class="notification notification-sm notification-error"
                  v-if="pwComposition.errorMsgPassword.value"
                >
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">
                    {{ pwComposition.errorMsgPassword.value }}
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
                    :type="pwComposition.inputConfirmPasswordType.value"
                    class="text-input text-input-lg"
                    placeholder="비밀번호 입력"
                    v-model="pwComposition.confirmPassword.value"
                  />
                  <button
                    class="text-input-group-action-button button button-neutral-ghost button-sm"
                    type="button"
                    @click="pwComposition.isHideConfirmPw"
                  >
                    <span class="hidden-text">지우기</span>
                    <svg-icon
                      class="button-icon"
                      :name="
                        getPwdIconName({
                          inputType:
                            pwComposition.inputConfirmPasswordType.value,
                        })
                      "
                    ></svg-icon>
                  </button>
                </div>
                <div
                  class="notification notification-sm notification-error"
                  v-if="pwComposition.errorMsgConfirmPassword.value"
                >
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">
                    {{ pwComposition.errorMsgConfirmPassword.value }}
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

const pwComposition = PasswordComposition();
const { getPwdIconName } = useCommonUtils();

const form: {
  name: string;
  email: string;
} = reactive({
  name: "",
  email: "",
});

const errors: {
  name: string;
  email: string;
} = reactive({
  name: "",
  email: "",
});

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
  const isErrorPassword = pwComposition.validatePassword();
  const isErrorConfirmPassword = pwComposition.validateConfirmPassword();

  return (
    isErrorName && isErrorEmail && isErrorPassword && isErrorConfirmPassword
  );
};

const validateName = () => {
  errors.name = form.name ? "" : $constants.LOGIN.NAME.INPUT_ERROR_MSG;
  return !errors.name;
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
  const result = await store.checkDuplicateEmail({ email: form.email });
  const isDuplicate = result.data;
  errors.email = isDuplicate ? $constants.LOGIN.EMAIL.DUPLICATE_ERROR_MSG : "";
  return isDuplicate;
};

const signUp = async () => {
  const result = await store.signUpUser({
    email: form.email,
    password: pwComposition.newPassword.value,
    name: form.name,
  });

  if (result.result === 0 && result.errorMessage) {
    alert(result.errorMessage);
  } else {
    alert("회원가입 성공");
    router.push("/portal/login");
  }
};
</script>

<style lang="scss" scoped></style>
