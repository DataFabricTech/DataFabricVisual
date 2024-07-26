<template>
  <div class="section-top-bar">
    <div class="l-top-bar">
      <h3 class="title">사용자 관리</h3>
    </div>
  </div>
  <div class="section-contents bg-white">
    <div class="l-between mb-3">
      <div class="search-input">
        <label class="hidden-text" for="data-menu-search">데이터 모델 검색</label>
        <input id="data-menu-search" class="text-input" value="검색어 입력" />
        <svg-icon class="text-input-icon" name="search"></svg-icon>
        <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
          <span class="hidden-text">지우기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
      <button
          class="button button-secondary ml-auto"
          @click="showModal = true"
      >
        사용자 추가
      </button>
    </div>
    <table class="table table-align-center">
      <colgroup>
        <col style="width: 15%" />
        <col style="width: 15%" />
        <col style="" />
        <col style="width: 15%" />
      </colgroup>
      <tr>
        <th>사용자 이름</th>
        <th>역할</th>
        <th class="align-left">설명</th>
        <th>관리</th>
      </tr>
      <tr>
        <td>
          <a href="" class="link-button link-button link-button-underline">홍길동</a>
        </td>
        <td>사용자</td>
        <td class="align-left">사용자에 대한 설명 입니다.</td>
        <td>
          <div class="button-group">
            <button class="button button button-error-stroke">
              삭제
            </button>
          </div>
        </td>
      </tr>
      <tr>
        <td>
          <a href="" class="link-button link-button link-button-underline">홍길동</a>
        </td>
        <td>관리자</td>
        <td class="align-left">사용자에 대한 설명 입니다.</td>
        <td>
          <div class="button-group">
            <button class="button button button-error-stroke">
              삭제
            </button>
          </div>
        </td>
      </tr>
      <tr>
        <td>
          <a href="" class="link-button link-button link-button-underline">홍길동</a>
        </td>
        <td>사용자</td>
        <td class="align-left">사용자에 대한 설명 입니다.</td>
        <td>
          <div class="button-group">
            <button class="button button button-error-stroke">
              삭제
            </button>
          </div>
        </td>
      </tr>
      <tr>
        <td>
          <a href="" class="link-button link-button link-button-underline">홍길동</a>
        </td>
        <td>관리자</td>
        <td class="align-left">사용자에 대한 설명 입니다.</td>
        <td>
          <div class="button-group">
            <button class="button button button-error-stroke">
              삭제
            </button>
          </div>
        </td>
      </tr>
    </table>
  </div>
  <!--  TODO: Modal 카테고리 추가  -->
  <div class="modal-fixed vfm--fixed vfm--inset" v-if="showModal">
    <div class="modal modal-padding-16" style="width: 480px">
      <div class="modal-head">
        <div class="modal-head-text">
          <span class="modal-head-title">서비스 추가</span>
        </div>
        <button
            class="button link-button button-sm"
            type="button"
            @click="showModal = false"
        >
          <span class="hidden-text">닫기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
      <div class="modal-body" style="height: 400px; overflow: auto">
        <div class="form form-vertical">
          <div class="form-body">
            <div class="form-item">
              <label for="" class="form-label">
                이메일
                <span class="required">*</span>
              </label>
              <div class="form-detail">
                <div class="search-input">
                  <label class="hidden-text" for="service-add-input-email">label</label>
                  <input id="service-add-input-email" class="text-input text-input-lg" placeholder="이메일" />
                  <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
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
              <label for="" class="form-label">
                이름표시
              </label>
              <div class="form-detail flex flex-col">
                <div class="search-input">
                  <label class="hidden-text" for="service-add-input-name">label</label>
                  <input id="service-add-input-name" class="text-input text-input-lg" placeholder="이름" />
                  <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
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
                    <input type="radio" id="radiosm" class="radio-input" name="radio2" checked />
                    <label for="radiosm" class="radio-label">
                      비밀번호 자동생성
                    </label>
                  </div>
                  <div class="radio">
                    <input type="radio" id="radiosm3" class="radio-input" name="radio2" />
                    <label for="radiosm3" class="radio-label">비밀번호  직접생성
                    </label>
                  </div>
                </div>
                <!-- 비밀번호 자동생성 -->
                <div class="text-input-group">
                  <div class="v-group w-full">
                    <div class="search-input">
                      <label class="hidden-text" for="service-add-input-password">label</label>
                      <input id="service-add-input-password" type="password" class="text-input text-input-lg" value="prefix icon + button" />
                      <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
                        <span class="hidden-text">지우기</span>
                        <svg-icon class="button-icon" name="eye-hide"></svg-icon>
                      </button>
                    </div>
                    <div class="notification notification-sm notification-error">
                      <svg-icon class="notification-icon" name="error"></svg-icon>
                      <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
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
                  <label class="hidden-text" for="service-add-input-password-direct">label</label>
                  <input id="service-add-input-password-direct" type="password" class="text-input text-input-lg" value="prefix icon + button" />
                  <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
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
                  <label class="hidden-text" for="service-add-input-password-check">label</label>
                  <input id="service-add-input-password-check" type="password" class="text-input text-input-lg" value="prefix icon + button" />
                  <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
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

      </div>
      <div class="modal-foot">
        <div class="modal-foot-group">
          <button
              class="button button-neutral-ghost button-lg"
              @click="showModal = false"
          >
            취소
          </button>
          <button class="button button-primary button-lg">생성</button>
        </div>
      </div>
    </div>
  </div>

</template>

<style scoped></style>

<script setup lang="ts">

const showModal = ref(false)

</script>
