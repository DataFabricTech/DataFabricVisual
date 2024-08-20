<template>
  <Modal
    modal-id="addUserModal"
    title="사용자 추가"
    :click-to-close="true"
    :esc-to-close="true"
    :display-directive="'show'"
    :width="480"
    :height="516"
    confirmBtnMsg="생성"
  >
    <template v-slot:body>
      <div class="form form-vertical">
        <div class="form-body">
          <div class="form-item">
            <label for="" class="form-label">
              이메일
              <span class="required">*</span>
            </label>
            <div class="form-detail">
              <div class="search-input">
                <label class="hidden-text" for="service-add-input-email"
                  >label</label
                >
                <input
                  id="service-add-input-email"
                  class="text-input text-input-lg"
                  placeholder="이메일"
                />
                <button
                  class="search-input-action-button button button-neutral-ghost button-sm"
                  type="button"
                >
                  <span class="hidden-text">지우기</span>
                  <svg-icon class="button-icon" name="close"></svg-icon>
                </button>
              </div>
              <div class="notification notification-sm notification-error">
                <svg-icon class="notification-icon" name="error"></svg-icon>
                <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
              </div>
            </div>
          </div>
          <div class="form-item">
            <label for="" class="form-label"> 이름표시 </label>
            <div class="form-detail flex flex-col">
              <div class="search-input">
                <label class="hidden-text" for="service-add-input-name"
                  >label</label
                >
                <input
                  id="service-add-input-name"
                  class="text-input text-input-lg"
                  placeholder="이름"
                />
                <button
                  class="search-input-action-button button button-neutral-ghost button-sm"
                  type="button"
                >
                  <span class="hidden-text">지우기</span>
                  <svg-icon class="button-icon" name="close"></svg-icon>
                </button>
              </div>
              <div class="notification notification-sm notification-error">
                <svg-icon class="notification-icon" name="error"></svg-icon>
                <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
              </div>
            </div>
          </div>
          <div class="form-item">
            <label class="form-label" for="service-add-description">
              설명
            </label>
            <div class="form-detail">
              <textarea
                id="service-add-description"
                class="textarea h-28"
                placeholder="설명을 입력하세요."
              ></textarea>
              <div class="notification notification-sm notification-error">
                <svg-icon class="notification-icon" name="error"></svg-icon>
                <p class="notification-detail">설명을 입력하세요.</p>
              </div>
            </div>
          </div>
          <div class="form-item">
            <label for="" class="form-label">비밀번호</label>
            <div class="form-detail gap-2">
              <div class="radio-group">
                <div class="radio">
                  <input
                    type="radio"
                    id="radiosm"
                    class="radio-input"
                    name="radio2"
                    checked
                  />
                  <label for="radiosm" class="radio-label">
                    비밀번호 자동생성
                  </label>
                </div>
                <div class="radio">
                  <input
                    type="radio"
                    id="radiosm3"
                    class="radio-input"
                    name="radio2"
                  />
                  <label for="radiosm3" class="radio-label"
                    >비밀번호 직접생성
                  </label>
                </div>
              </div>
              <!-- 비밀번호 자동생성 -->
              <div class="text-input-group">
                <div class="v-group w-full">
                  <div class="search-input">
                    <label class="hidden-text" for="service-add-input-password"
                      >label</label
                    >
                    <input
                      id="service-add-input-password"
                      type="password"
                      class="text-input text-input-lg"
                      value="prefix icon + button"
                    />
                    <button
                      class="search-input-action-button button button-neutral-ghost button-sm"
                      type="button"
                    >
                      <span class="hidden-text">지우기</span>
                      <svg-icon class="button-icon" name="eye-hide"></svg-icon>
                    </button>
                  </div>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">
                      얼럿 메세지를 입력해주세요.
                    </p>
                  </div>
                </div>
                <button class="button button-neutral-stroke button-xl">
                  <span class="hidden-text"></span>
                  <svg-icon class="icons button-icon" name="reset"></svg-icon>
                </button>
                <button class="button button-neutral-stroke button-xl">
                  <span class="hidden-text"></span>
                  <svg-icon class="icons button-icon" name="copy"></svg-icon>
                </button>
              </div>
              <!-- 비밀번호 직접생성 -->
              <div class="search-input">
                <label
                  class="hidden-text"
                  for="service-add-input-password-direct"
                  >label</label
                >
                <input
                  id="service-add-input-password-direct"
                  type="password"
                  class="text-input text-input-lg"
                  value="prefix icon + button"
                />
                <button
                  class="search-input-action-button button button-neutral-ghost button-sm"
                  type="button"
                >
                  <span class="hidden-text">지우기</span>
                  <svg-icon class="button-icon" name="eye-hide"></svg-icon>
                </button>
              </div>
            </div>
          </div>
          <!-- 비밀번호 직접생성 선택시 노출 -->
          <div class="form-item">
            <label for="" class="form-label">비밀번호 확인</label>
            <div class="form-detail">
              <div class="search-input">
                <label
                  class="hidden-text"
                  for="service-add-input-password-check"
                  >label</label
                >
                <input
                  id="service-add-input-password-check"
                  type="password"
                  class="text-input text-input-lg"
                  value="prefix icon + button"
                />
                <button
                  class="search-input-action-button button button-neutral-ghost button-sm"
                  type="button"
                >
                  <span class="hidden-text">지우기</span>
                  <svg-icon class="button-icon" name="eye-hide"></svg-icon>
                </button>
              </div>
              <div class="notification notification-sm notification-error">
                <svg-icon class="notification-icon" name="error"></svg-icon>
                <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
              </div>
            </div>
          </div>
          <div class="form-item">
            <label for="" class="form-label">관리자</label>
            <div class="form-detail">
              <div class="switch">
                <input type="checkbox" id="manager-sw" class="switch-input" />
                <label for="manager-sw" class="switch-label">
                  <div class="switch-control"></div>
                </label>
              </div>
            </div>
          </div>
        </div>
      </div>
    </template>
  </Modal>
</template>

<script setup lang="ts">
import Modal from "@extends/modal/Modal.vue";
</script>

<style>
/* TODO: Modal 컴포넌트 사용하려면 해당 style 필요, 퍼블리싱 요청 */
.modal-body {
  height: 400px;
  overflow: auto;
}
</style>
