<template>
  <div class="wrap">
    <div class="submit-form">
      <h1 class="submit-form-logo">LOGO</h1>
      <div class="submit-form-form">
        <h2 class="submit-form-title">로그인</h2>
        <div class="form form-lg gap-8">
          <div class="form-body">
            <div class="form-item">
              <label for="inpId" class="form-label"> 아이디 또는 이메일 </label>
              <div class="form-detail flex flex-col">
                <div class="text-input-group w-full">
                  <input
                    id="inpId"
                    class="text-input text-input-lg"
                    placeholder="아이디 또는 이메일 입력"
                    v-model="loginId"
                  />
                  <svg-icon class="text-input-icon" name="user"></svg-icon>
                </div>
                <div
                  class="notification notification-sm notification-error"
                  v-show="isErrorEmailOrId"
                >
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">{{ emailOrIdErrorMessage }}</p>
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
                    <svg-icon class="button-icon" name="eye"></svg-icon>
                  </button>
                </div>
                <div
                  class="notification notification-sm notification-error"
                  v-show="isErrorPassword"
                >
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">
                    {{ passwordErrorMessage }}
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
            >
              로그인
            </button>
            <div class="form-foot-group">
              <button
                class="button button-primary-ghost button-sm"
                type="button"
              >
                <nuxt-link :to="'/portal/login/pw-find'">
                  비밀번호 찾기
                </nuxt-link>
              </button>
              <button
                class="button button-primary-ghost button-sm"
                type="button"
              >
                <nuxt-link :to="'/portal/login/join'"> 회원가입 </nuxt-link>
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
import { loginStore } from "@/store/login/index";
import { useRouter } from "vue-router";

const router = useRouter();

const loginId = ref("");
const loginPassword = ref("");

const emailOrIdErrorMessage = ref("");
const passwordErrorMessage = ref("");

const isErrorEmailOrId = ref(false);
const isErrorPassword = ref(false);

const inpType = ref("password");

const login = async () => {
  let id = loginId.value;
  let password = loginPassword.value;

  loginValidationReset();

  // validation check

  if (_.isEmpty(id)) {
    isErrorEmailOrId.value = true;
    emailOrIdErrorMessage.value = "사용자 아이디를 입력하세요.";
    return;
  }

  if (_.isEmpty(password)) {
    isErrorPassword.value = true;
    passwordErrorMessage.value = "사용자 비밀번호를 입력하세요.";
    return;
  }

  let param = {
    id: id,
    password: password,
  };

  const store = loginStore();
  const isloginSuccess = store.loginReq(param);

  if (isloginSuccess) {
    // TODO: 로그인 성공 시 메인 화면으로 이동.
    router.push("/"); // 임시 경로
  } else {
    isErrorEmailOrId.value = true;
    isErrorPassword.value = true;

    // TODO: 서버에서 받은 에러 메시지 처리
    passwordErrorMessage.value = "아이디 및 비밀번호가 유효하지 않습니다.";
    emailOrIdErrorMessage.value = "아이디 및 비밀번호가 유효하지 않습니다.";
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
