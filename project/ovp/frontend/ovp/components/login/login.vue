<template>
  <div class="wrap">
    <div class="submit-form">
      <h1 class="submit-form-logo">
        <svg-icon class="svg-icon logo-img" name="logo"></svg-icon>
      </h1>
      <div class="submit-form-form">
        <h2 class="submit-form-title">로그인</h2>
        <div class="form form-lg gap-6">
          <div class="form-body">
            <div class="form-item">
              <label for="inpId" class="form-label"> 아이디 또는 이메일 </label>
              <div class="form-detail flex flex-col">
                <div class="text-input-group w-full">
                  <input
                    id="inpId"
                    class="text-input text-input-lg"
                    placeholder="아이디 또는 이메일 입력"
                    v-model="loginEmailOrId"
                  />
                  <svg-icon class="text-input-icon" name="user"></svg-icon>
                </div>
                <div
                  class="notification notification-sm notification-error"
                  v-show="isErrorEmailOrId"
                >
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">{{ emailOrIdErrorMsg }}</p>
                </div>
              </div>
            </div>
            <div class="form-item">
              <label for="inpPw" class="form-label"> 비밀번호 </label>
              <div class="form-detail flex flex-col">
                <div class="text-input-group w-full">
                  <input
                    id="inpPw"
                    :type="inpType"
                    class="text-input text-input-lg"
                    placeholder="비밀번호 입력"
                    v-model="loginPassword"
                    @keyup.enter="login"
                  />
                  <svg-icon
                    class="text-input-icon"
                    name="lock-locked"
                  ></svg-icon>
                  <button
                    class="text-input-group-action-button button button-neutral-ghost button-sm"
                    type="button"
                    @click="isHidePw"
                  >
                    <span class="hidden-text">지우기</span>
                    <svg-icon
                      class="button-icon"
                      :name="getPwdIconName({ inputType: inpType })"
                    ></svg-icon>
                  </button>
                </div>
                <div
                  class="notification notification-sm notification-error"
                  v-show="isErrorPassword"
                >
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">
                    {{ passwordErrorMsg }}
                  </p>
                </div>
              </div>
            </div>
          </div>
          <div class="form-foot">
            <button
              class="button button-primary button-lg"
              type="button"
              @click="login"
              :disabled="isDoingLogin"
            >
              로그인
            </button>
            <div class="form-foot-group">
              <nuxt-link
                :to="'/portal/login/pw-find'"
                class="button button-primary-ghost button-sm"
              >
                비밀번호 찾기
              </nuxt-link>
              <nuxt-link
                :to="'/portal/login/sign-up'"
                class="button button-primary-ghost button-sm"
              >
                회원가입
              </nuxt-link>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import _ from "lodash";
import { loginStore } from "@/store/login/index";
import { useRouter } from "vue-router";
import $constants from "@/utils/constant";
import { useCommonUtils } from "@/composables/commonUtils";

const { getPwdIconName } = useCommonUtils();

const store = loginStore();
const { isDoingLogin, isloginSuccess, errorMessage } = storeToRefs(store);
const { getLoginSuccessState } = store;

const router = useRouter();

const loginEmailOrId = ref("");
const loginPassword = ref("");

const emailOrIdErrorMsg = ref("");
const passwordErrorMsg = ref("");

const isErrorEmailOrId = ref(false);
const isErrorPassword = ref(false);

const inpType = ref("password");

const login = async () => {
  if (isDoingLogin.value) {
    return;
  }

  let id = loginEmailOrId.value.trim();
  let password = loginPassword.value.trim();

  loginValidationReset();

  // validation check

  if (_.isEmpty(id)) {
    isErrorEmailOrId.value = true;
    emailOrIdErrorMsg.value = $constants.LOGIN.ID.INPUT_ERROR_MSG;
    return;
  }

  if (_.isEmpty(password)) {
    isErrorPassword.value = true;
    passwordErrorMsg.value = $constants.LOGIN.PASSWORD.INPUT_ERROR_MSG;
    return;
  }

  let param = {
    email: id,
    password: password,
  };

  await getLoginSuccessState(param);

  if (isloginSuccess.value) {
    router.push("/");
  } else {
    // TODO: 추후 컴포넌트로 교체 예정
    alert(errorMessage.value);
  }
};

const loginValidationReset = () => {
  isErrorEmailOrId.value = false;
  isErrorPassword.value = false;
};

const isHidePw = () => {
  inpType.value = inpType.value === "password" ? "text" : "password";
};
</script>
<style lang="scss" scoped></style>
