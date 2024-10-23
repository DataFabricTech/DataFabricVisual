<template>
  <div class="wrap">
    <div class="submit-form">
      <h1 class="submit-form-logo">
        <span class="text-neutral-700 text-22 font-bold">노후시설 재난안전 학습 데이터 관리</span>
      </h1>
      <div class="submit-form-form">
        <h2 class="submit-form-title">비밀번호 재설정</h2>
        <p class="submit-form-desc">
          비밀번호 재설정 링크를 받으려면 등록된 이메일을 입력하세요
        </p>
        <form class="form form-lg gap-6" @submit.prevent="onSubmit">
          <div class="form-body">
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
                    v-model="email"
                  />
                  <svg-icon class="text-input-icon" name="user"></svg-icon>
                </div>
                <div
                  class="notification notification-sm"
                  :class="
                    msgEmail ? 'notification-success' : 'notification-error'
                  "
                  v-if="msgEmail || errorMsgEmail"
                >
                  <svg-icon class="notification-icon" name="success"></svg-icon>
                  <p class="notification-detail">
                    {{ notiMsg() }}
                  </p>
                </div>
              </div>
            </div>
          </div>
          <div class="form-foot">
            <button class="button button-primary button-lg" type="submit">
              제출
            </button>
            <button class="button button-primary-ghost button-sm" type="button">
              <nuxt-link :to="'/portal/login'">
                로그인 페이지로 돌아가기
              </nuxt-link>
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import $constants from "~/utils/constant";
import _ from "lodash";
import { loginStore } from "~/store/login";

const email = ref("");
const msgEmail = ref("");
const errorMsgEmail = ref("");

const store = loginStore();
const { sendMailForPasswordReset } = store;

const notiMsg = () => {
  return msgEmail.value ? msgEmail.value : errorMsgEmail.value;
};

const onSubmit = async () => {
  if (!validateEmail()) {
    return;
  }
  console.log("onSubmit");
  let param = {
    email: email.value,
  };
  const result = await sendMailForPasswordReset(param);

  if (result.result === 1) {
    msgEmail.value = "재설정 링크가 이메일로 전송되었습니다.";
  } else {
    errorMsgEmail.value = result.errorMessage;
  }
};

const validateEmail = () => {
  errorMsgEmail.value = email.value ? "" : "사용자 이메일을 입력하세요.";
  if (_.isEmpty(email.value)) return false;

  errorMsgEmail.value = $constants.LOGIN.EMAIL.REGEX.test(email.value)
    ? ""
    : "이메일이 유효하지 않습니다.";
  return _.isEmpty(errorMsgEmail.value);
};
</script>

<style lang="scss" scoped></style>
