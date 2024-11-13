<template lang="html">
    <div class="common-login">
      <div class="login-wrap">
        <div class="login-container">
          <div class="login-slogan">
            <h1 class="logo">
              <img src="../../assets/style-product/images/common/logo-wh.png" alt="MOBIGEN">
            </h1>
            <h2 class="title">Mobigen<br />Data Catalog</h2>
            <p class="desc">창의적 사고와 끝없는 도전을 통해 새로운 미래를<br />창조함으로써 인류사회의 꿈을 실현하는 모비젠 데이터 카탈로그</p>
            <p class="copyright">ⓒ 2024. MOBIGEN.</p>
          </div>
          <!-- login-content -->
          <div class="login-content">
            <h3 class="login-title">login</h3>
            <form action="">
              <fieldset>
                <legend>로그인</legend>
                <div class="login-box">
                  <div class="mu-input-id">
                    <label for="uid">Username</label>
                    <input class="mu-input" type="text" id="username" v-model="username" maxlength="15" required placeholder="아이디를 입력해주세요." />
                    <button v-if="username != ''" type="button" class="mu-btn mu-btn-delete" v-on:click="username = ''"><i class="mu-icon-svg item-delete"></i><span>아이디 삭제</span></button>
                  </div>
                  <div class="mu-input-pw">
                    <label for="upw">Password</label>
                    <input :type="isShowPassword ? 'text' : 'password'" id="password" v-model="password" maxlength="15" required class="mu-input" placeholder="비밀번호를 입력해주세요." @keydown.enter.prevent='onLogin'>
                    <button v-if="password != ''" type="button" class="mu-btn mu-btn-delete" v-on:click="password = ''"><i class="mu-icon-svg item-delete"></i><span>비밀번호 삭제</span></button>
                    <button type="button" class="mu-btn mu-btn-pw-mask" v-on:click="isShowPassword = !isShowPassword"><i :class="isShowPassword ? 'mu-icon-svg eye-on' : 'mu-icon-svg eye-off'"></i><span>비밀번호 숨김 해제</span></button>
                  </div>
                  <p v-if="login_error" class="complete-msg error">{{ login_result_message }}</p>
                  <button type="button" class="mu-btn mu-btn-primary" @click.prevent="onLogin"><span>로그인</span></button>
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
const emits = defineEmits(['login'])

const username = ref("")
const password = ref("")
const isShowPassword = ref(false)

const onLogin = () => {
  const params = {
    username: username.value,
    password: password.value
  }
  emits('login', params)
}

</script>
