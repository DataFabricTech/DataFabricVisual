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
            <div class="form-item">
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
            <button class="button button-primary button-lg" type="button">
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
<script setup lang="ts"></script>
<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
