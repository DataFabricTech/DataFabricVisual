<template>
  <div class="wrap">
    <div class="submit-form">
      <h1 class="submit-form-logo">
        <svg-icon class="svg-icon logo-img" name="logo"></svg-icon>
      </h1>
      <div class="submit-form-form">
        <h2 class="submit-form-title">비밀번호 재 설정</h2>
        <div class="form form-lg gap-6">
          <pw-reset :composition="composition"></pw-reset>
          <div class="form-foot">
            <button
              class="button button-primary button-lg"
              type="button"
              @click="submit"
            >
              확인
            </button>
            <div class="form-foot-group">
              <nuxt-link
                :to="'/portal/login'"
                class="button button-primary-ghost button-sm"
              >
                로그인 페이지로 돌아가기
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
import { useRouter } from "vue-router";
import { loginStore } from "~/store/login";
import { PasswordComposition } from "~/components/login/PasswordComposition";
import { useNuxtApp } from "nuxt/app";

const { $alert } = useNuxtApp();

const route = useRoute();
const router = useRouter();
const uuid = route.query.id;

const store = loginStore();
const composition = PasswordComposition();

const { isPwChangeSuccess, isLinkValid, errorMessage } = storeToRefs(store);
const { getPwChangeSuccessState, getLinkValidState } = store;

if (!_.isUndefined(uuid)) {
  // 서버에 고유링크 유효성 확인
  await getLinkValidState(uuid);
  if (!isLinkValid.value) {
    router.push("/portal/login/error");
  }
}

const submit = async () => {
  if (!composition.isValidatePwReset()) {
    return;
  }

  let param = composition.getPwResetParam();
  await getPwChangeSuccessState(param, uuid);

  if (isPwChangeSuccess.value) {
    $alert("비밀번호가 변경되었습니다.", "success").then(() => {
      router.push("/portal/login");
    });
  } else {
    $alert(errorMessage.value, "error");
  }
};
</script>
<style lang="scss" scoped></style>
