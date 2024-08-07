<template>
  <div class="wrap">
    <div class="submit-form">
      <h1 class="submit-form-logo">LOGO</h1>
      <div class="submit-form-form">
        <h2 class="submit-form-title">비밀번호 찾기</h2>
        <p class="submit-form-desc">
          비밀번호 재설정 링크를 받으려면 등록된 이메일을 입력하세요
        </p>
        <div class="form form-lg gap-6">
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
                  />
                  <svg-icon class="text-input-icon" name="user"></svg-icon>
                </div>
                <div class="notification notification-sm notification-success">
                  <svg-icon class="notification-icon" name="success"></svg-icon>
                  <p class="notification-detail">
                    재설정 링크가 이메일로 전송되었습니다.
                  </p>
                </div>
              </div>
            </div>
          </div>
          <div class="form-foot">
            <button class="button button-primary button-lg" type="button">
              제출
            </button>
            <button class="button button-primary-ghost button-sm" type="button">
              로그인 페이지로 돌아가기
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts"></script>

<style lang="scss" scoped></style>
