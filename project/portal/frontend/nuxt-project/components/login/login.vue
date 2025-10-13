<template lang="html">
  <div class="common-login">
    <div class="login-wrap">
      <div class="login-container">
        <!-- login-content -->
        <div class="login-content">
          <h3 class="login-title">로그인</h3>
          <form action="">
            <fieldset>
              <legend>로그인</legend>
              <div class="login-box">
                <div class="mu-input-id">
                  <label for="uid">아이디</label>
                  <input
                    class="mu-input"
                    type="text"
                    id="user_id"
                    v-model="user_id"
                    maxlength="15"
                    required
                    placeholder="아이디를 입력해주세요."
                    @input="filterInput"
                  />
                  <button v-if="user_id != ''" type="button" class="mu-btn mu-btn-delete" v-on:click="user_id = ''"><i
                      class="mu-icon-svg item-delete"></i><span>아이디 삭제</span></button>
                  <p v-if="showWarning" class="complete-msg error">
                    영문과 숫자만 입력 가능합니다.
                  </p>
                </div>
                <div class="mu-input-pw">
                  <label for="upw">비밀번호</label>
                  <input :type="isShowPassword ? 'text' : 'password'" id="password" v-model="password" maxlength="15"
                    required class="mu-input" placeholder="비밀번호를 입력해주세요." @keydown.enter.prevent='onLogin'>
                  <button v-if="password != ''" type="button" class="mu-btn mu-btn-delete" v-on:click="password = ''"><i
                      class="mu-icon-svg item-delete"></i><span>비밀번호 삭제</span></button>
                  <button type="button" class="mu-btn mu-btn-pw-mask" v-on:click="isShowPassword = !isShowPassword"><i
                      :class="isShowPassword ? 'mu-icon-svg eye-on' : 'mu-icon-svg eye-off'"></i><span>비밀번호 숨김
                      해제</span></button>
                </div>
                <p v-if="login_error" class="complete-msg error">{{ login_result_message }}</p>
                <button type="button" class="mu-btn mu-btn-primary" @click.prevent="onLogin"><span>로그인</span></button>
              </div>
              <div style="display: flex; justify-content: space-between; align-items: center;">
                <a></a>
                <a @click="goToRegister()">회원가입</a>
              </div>
            </fieldset>
          </form>
        </div>
        <!-- //login-content -->
      </div>
    </div>
  </div>
</template>

<i18n src="./login.json"></i18n>

<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router';
import { onMounted, ref } from "vue";
import { defaultAlert, errorAlert } from "@/components/functional/alert/alert-default";

const route = useRoute();
const router = useRouter();

const props = defineProps({
  login_error: {
    Type: Boolean,
    default: false
  },
  login_result_message: {
    Type: String,
    default: ""
  }
})
const emit = defineEmits(['login', 'toggleRegisterForm'])

const user_id = ref("")
const password = ref("")
const isShowPassword = ref(false)
const showWarning = ref(false)

const onLogin = () => {
  const params = {
    user_id: user_id.value,
    password: password.value
  }
  emit('login', params)
}

const goToRegister = () => {
  emit('toggleRegisterForm')
}

onMounted(() => {
  const err = route.query.err;

  if (err === 'expired') {
    defaultAlert("장시간 활동이 없어 자동으로 로그아웃 되었습니다.");
    router.push({ path: '/', query: {} });
  }
});

const filterInput = (e) => {
  const original = e.target.value
  const filtered = original.replace(/[^a-zA-Z0-9]/g, '')

  // 한글이나 특수문자가 입력되었는지 확인
  showWarning.value = original !== filtered

  // 필터링된 값 반영
  user_id.value = filtered
}

</script>
