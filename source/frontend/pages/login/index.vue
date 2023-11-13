<template>
  <div id="wrap" class="is-full">
    <main id="main" class="login">
      <div class="login-inner">
        <div class="login-logo">
          <h1 class="text-2xl font-bold">데이터 패브릭</h1>
        </div>
          <form action="" class="form login-form">
            <div class="form-list">
              <div class="form-item">
                <label for="id-input" class="form-label">
                  <span  class="form-title">아이디</span>
                </label>
                <div class="form-content">
                  <BaseTextInput id="id-input" class="text-input-lg" placeholder=""></BaseTextInput>
                </div>
              </div>
              <div class="form-item">
                <label for="pw-input" class="form-label">
                  <span class="form-title">패스워드</span>
                </label>
                <div class="form-content relative">
                  <BaseTextInput id="pw-input" type="password" class="text-input-lg pr-8" placeholder=""></BaseTextInput>
                  <!--  TODO:[개발] 패스워드 보기/숨기기 구현 필요 (숨길시 icon name="eye-hide"로 title="패스워드 숨기기"로 변경) -->
                  <baseButton class="button-icon button-sm button-link pw-toggle" title="패스워드 보기">
                    <svg-icon class="svg-icon" name="eye" />
                  </baseButton>
                </div>
              </div>
              <div class="form-item">
                <div class="form-content">
                  <BaseCheckbox class="checkbox-lg" id="check-id-save">아이디 저장</BaseCheckbox>
                </div>
              </div>
              <!--  TODO:[개발] v-if로 상황에 따라 보이게 개발 필요 -->
              <span class="text-red-500" role="alert">아이디 또는 비밀번호를 확인해주세요</span>
              <baseButton class="button-primary button-lg w-full">로그인</baseButton>
            </div>
          </form>
          <div class="login-options">
            <baseButton class="button-sm button-link">아이디/패스워드 찾기</baseButton>
            <baseButton class="button-sm button-link">회원가입</baseButton>
          </div>
      </div>
    </main>
  </div>
</template>

<script>
export default {
  name: "index.vue"
};
</script>

