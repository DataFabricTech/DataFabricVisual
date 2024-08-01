<template>
  <div class="wrap">
    <div class="submit-form">
      <h1 class="submit-form-logo">LOGO</h1>
      <div class="submit-form-form">
        <h2 class="submit-form-title">로그인</h2>
        <div class="form form-lg gap-6">
          <div class="form-body">
            <div class="form-item">
              <label for="inpId" class="form-label">
                아이디 또는 이메일
                <span class="required">*</span>
              </label>
              <div class="form-detail flex flex-col">
                <div class="text-input-group w-full">
                  <input
                    id="inpId"
                    class="text-input text-input-lg"
                    placeholder="아이디 또는 이메일 입력"
                  />
                  <svg-icon class="text-input-icon" name="user"></svg-icon>
                </div>
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">사용자 아이디를 입력하세요.</p>
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
          </div>
          <div class="form-foot">
            <button class="button button-primary button-lg" type="button">
              로그인
            </button>
            <div class="form-foot-group">
              <button
                class="button button-primary-ghost button-sm"
                type="button"
              >
                비밀번호 찾기
              </button>
              <button
                class="button button-primary-ghost button-sm"
                type="button"
              >
                회원가입
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts"></script>

<style lang="scss" scoped></style>
