<template>
  <div class="wrap">
    <div class="submit-form">
      <h1 class="submit-form-logo">LOGO</h1>
      <div class="submit-form-form">
        <h2 class="submit-form-title">비밀번호 재 설정</h2>
        <div class="form form-lg gap-8">
          <div class="form-body">
            <div class="form-item">
              <label for="inpNewResetPw" class="form-label">
                <span class="required">*</span>
                신규비밀번호
              </label>
              <div class="form-detail flex flex-col">
                <div class="text-input-group w-full">
                  <input
                    id="inpNewResetPw"
                    :type="inNewPwType"
                    class="text-input text-input-lg"
                    placeholder="비밀번호 입력"
                    v-model="newPassword"
                  />
                  <svg-icon
                    class="text-input-icon"
                    name="lock-locked"
                  ></svg-icon>
                  <button
                    class="text-input-group-action-button button button-neutral-ghost button-sm"
                    type="button"
                    @click="isHideNewPw"
                  >
                    <span class="hidden-text">지우기</span>
                    <svg-icon class="button-icon" name="eye"></svg-icon>
                  </button>
                </div>
                <div
                  class="notification notification-sm notification-error"
                  v-show="isErrorPw"
                >
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">
                    {{ pwErrorMessage }}
                  </p>
                </div>
              </div>
            </div>
            <div class="form-item">
              <label for="inpResetPw" class="form-label">
                <span class="required">*</span>
                비밀번호확인
              </label>
              <div class="form-detail flex flex-col">
                <div class="text-input-group w-full">
                  <input
                    id="inpResetPw"
                    :type="inCpwType"
                    class="text-input text-input-lg"
                    placeholder="비밀번호 확인"
                    v-model="confirmPassword"
                  />
                  <svg-icon
                    class="text-input-icon"
                    name="lock-locked"
                  ></svg-icon>
                  <button
                    class="text-input-group-action-button button button-neutral-ghost button-sm"
                    type="button"
                    @click="isHideCpw"
                  >
                    <span class="hidden-text">지우기</span>
                    <svg-icon class="button-icon" name="eye"></svg-icon>
                  </button>
                </div>
                <div
                  class="notification notification-sm notification-error"
                  v-show="isErrorCpw"
                >
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">
                    {{ cpwErrorMessage }}
                  </p>
                </div>
              </div>
            </div>
          </div>
          <div class="form-foot">
            <button
              class="button button-primary button-lg"
              type="button"
              @click="submit"
            >
              확인
            </button>
            <div class="form-foot-group">
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
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import _ from "lodash";
import { useRouter } from "vue-router";
import { loginStore } from "~/store/login";
import { PWREGEX, PWREGEX_ERROR_MSG } from "~/utils/constant";
const store = loginStore();
const { isPwChangeSuccess, isLinkValid, errorMessage } = storeToRefs(store);
const { getPwChangeSuccessState, getLinkValidState } = store;

const route = useRoute();
const router = useRouter();
const uuid = route.query.id;

const newPassword = ref("");
const confirmPassword = ref("");

const pwErrorMessage = ref("");
const cpwErrorMessage = ref("");

const isErrorPw = ref(false);
const isErrorCpw = ref(false);

const inNewPwType = ref("password");
const inCpwType = ref("password");

if (!_.isUndefined(uuid)) {
  // 서버에 고유링크 유효성 확인
  await getLinkValidState(uuid);
  if (!isLinkValid.value) {
    // TODO 에러페이지 퍼블리싱 되면 교체 예정
    router.push("/portal/error");
  }
}

const submit = async () => {
  let pw = newPassword.value;
  let cpw = confirmPassword.value;

  passwordValidationReset();

  // validation check

  if (_.isEmpty(pw)) {
    isErrorPw.value = true;
    pwErrorMessage.value = "새로운 비밀번호를 입력하세요.";
    return;
  }

  if (!PWREGEX.test(pw)) {
    isErrorPw.value = true;
    pwErrorMessage.value = PWREGEX_ERROR_MSG;
    return;
  }

  if (_.isEmpty(cpw) || !_.isEqual(pw, cpw)) {
    isErrorCpw.value = true;
    cpwErrorMessage.value = "비밀번호가 일치 하지 않습니다.";
    return;
  }

  let param = {
    confirmPassword: cpw,
    newPassword: pw,
  };

  await getPwChangeSuccessState(param, uuid);

  if (isPwChangeSuccess.value) {
    alert("비밀번호가 변경되었습니다.");
    router.push("/portal/login");
  } else {
    alert(errorMessage.value);
  }
};

const passwordValidationReset = () => {
  isErrorCpw.value = false;
  isErrorPw.value = false;
};

const isHideNewPw = () => {
  inNewPwType.value = inNewPwType.value === "password" ? "text" : "password";
};
const isHideCpw = () => {
  inCpwType.value = inCpwType.value === "password" ? "text" : "password";
};
</script>
<style lang="scss" scoped></style>
