<template>
  <div class="wrap">
    <div class="submit-form">
      <h1 class="submit-form-logo">LOGO</h1>
      <div class="submit-form-form">
        <h2 class="submit-form-title" v-show="showLogin">{{ t("login.login") }}</h2>
        <h2 class="submit-form-title" v-show="showPwReset">비밀번호 재 설정</h2>
        <div class="form form-lg gap-8">
          <div class="form-body">
            <div class="form-item" v-show="showLogin">
              <label for="inpId" class="form-label">
                {{ t("login.idOrEmail") }}
              </label>
              <div class="form-detail flex flex-col">
                <div class="text-input-group w-full">
                  <input
                      id="inpId"
                      class="text-input text-input-lg"
                      :placeholder="t('login.placeholder.idOrEmail')"
                      v-show="showLogin"
                      v-model="loginId"
                  />
                  <svg-icon class="text-input-icon" name="user"></svg-icon>
                </div>
                <div class="notification notification-sm notification-error" v-show="isErrorEmailOrId">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">{{ emailOrIdErrorMessage }}</p>
                </div>
              </div>
            </div>
            <div class="form-item" v-show="showLogin">
              <label for="inpPw" class="form-label">
                비밀번호
              </label>
              <div class="form-detail flex flex-col">
                <div class="text-input-group w-full">
                  <input
                      id="inpPw"
                      :type="inpType"
                      class="text-input text-input-lg"
                      :placeholder="t('login.placeholder.password')"
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
                <div class="notification notification-sm notification-error" v-show="isErrorPassword">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">
                    {{ passwordErrorMessage }}
                  </p>
                </div>
              </div>
            </div>
            <div class="form-item" v-show="showPwReset">
              <label for="inpNewResetPw" class="form-label">
                <span class="required">*</span>
                신규비밀번호
              </label>
              <div class="form-detail flex flex-col">
                <div class="text-input-group w-full">
                  <input
                      id="inpNewResetPw"
                      class="text-input text-input-lg"
                      placeholder="비밀번호 입력"
                  />
                  <svg-icon
                      class="text-input-icon"
                      name="lock-locked"
                  ></svg-icon>
                  <button
                      class="text-input-group-action-button button button-neutral-ghost button-sm"
                      type="button"
                  >
                    <span class="hidden-text">지우기</span>
                    <svg-icon class="button-icon" name="eye"></svg-icon>
                  </button>
                </div>
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">
                    사용자 비밀번호를 입력하세요.
                  </p>
                </div>
              </div>
            </div>
            <div class="form-item" v-show="showPwReset">
              <label for="inpResetPw" class="form-label">
                <span class="required">*</span>
                비밀번호확인
              </label>
              <div class="form-detail flex flex-col">
                <div class="text-input-group w-full">
                  <input
                      id="inpResetPw"
                      class="text-input text-input-lg"
                      placeholder="비밀번호 확인"
                  />
                  <svg-icon
                      class="text-input-icon"
                      name="lock-locked"
                  ></svg-icon>
                  <button
                      class="text-input-group-action-button button button-neutral-ghost button-sm"
                      type="button"
                  >
                    <span class="hidden-text">지우기</span>
                    <svg-icon class="button-icon" name="eye"></svg-icon>
                  </button>
                </div>
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">
                    사용자 비밀번호를 입력하세요.
                  </p>
                </div>
              </div>
            </div>
          </div>
          <div class="form-foot">
            <button class="button button-primary button-lg" type="button" v-show="showLogin" @click="login">
              {{ t("login.login") }}
            </button>
            <button class="button button-primary button-lg" type="button" v-show="showPwReset">
              확인
            </button>
            <div class="form-foot-group">
              <button
                  class="button button-primary-ghost button-sm"
                  type="button"
                  v-show="showLogin"
              >
                <nuxt-link :to="'/portal/login/pw-find'">
                  {{ t("login.pwFind") }}
                </nuxt-link>
              </button>
              <button
                  class="button button-primary-ghost button-sm"
                  type="button"
                  v-show="showLogin"
              >
                <nuxt-link :to="'/portal/login/join'">
                  {{ t("login.join") }}
                </nuxt-link>
              </button>
              <button
                  class="button button-primary-ghost button-sm"
                  type="button"
                  v-show="showPwReset"
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
import RSA from "rsajs";
import {useI18n} from "vue-i18n";
import messages from "./index.json";
import {loginStore} from "@/store/login/index";
import {useRouter} from "vue-router";

const {t} = useI18n({
  messages
});
const router = useRouter();

const props = defineProps({
  showLogin: Boolean,
  showPwReset: Boolean
});

const loginId = ref('');
const loginPassword = ref('');

const emailOrIdErrorMessage = ref('');
const passwordErrorMessage = ref('');

const isErrorEmailOrId = ref(false);
const isErrorPassword = ref(false);

const inpType = ref('password');


const login = async () => {
  let id = loginId.value;
  let password = loginPassword.value;

  loginValidationReset()

  // validation check

  if (_.isEmpty(id)) {
    isErrorEmailOrId.value = true;
    emailOrIdErrorMessage.value = t("login.validation.emailOrId.empty");
    return
  }

  if (_.isEmpty(password)) {
    isErrorPassword.value = true;
    passwordErrorMessage.value = t("login.validation.password.empty");
    return
  }

  let param = {
    "id": id,
    "password": getEncryptString(password)
  }

  const store = loginStore();
  const isloginSuccess = store.loginReq(param);

  if (isloginSuccess) {
    // TODO: 로그인 성공 시 메인 화면으로 이동..
    router.push("/") // 임시 경로
  } else {
    isErrorEmailOrId.value = true;
    isErrorPassword.value = true;
    passwordErrorMessage.value = t("login.validation.fail");
    emailOrIdErrorMessage.value = t("login.validation.fail");
  }

}

const loginValidationReset = () => {
  isErrorEmailOrId.value = false;
  isErrorPassword.value = false;
}

const isHidePw = () => {
  inpType.value = inpType.value === 'password' ? 'text' : 'password';
}

const getEncryptString = (param) => {
  const store = loginStore();
  const publicKey = store.getPublicKey();

  const rsa = new RSA();
  rsa.setKey(publicKey);

  return rsa.encrypt(param);
}

</script>

<style lang="scss" scoped></style>
