<template>
  <div class="modal-overlay vfm--fixed vfm--inset" v-if="false">
    <div class="modal modal-padding-16" style="width: 480px">
      <div class="modal-head">
        <div class="modal-head-text">
          <span class="modal-head-title">용어추가</span>
        </div>
        <button class="button link-button button-sm" type="button">
          <span class="hidden-text">닫기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
      <div class="modal-body">
        <div class="form form-lg">
          <div class="form-body">
            <div class="form-item">
              <label for="data-model-save-name" class="form-label">
                이름
                <span class="required">*</span>
              </label>
              <div class="form-detail">
                <input
                  id="data-model-save-name"
                  class="text-input text-input-lg"
                  placeholder="이름을 입력하세요."
                />
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">이름을 입력하세요.</p>
                </div>
              </div>
            </div>
            <div class="form-item">
              <label class="form-label" for="data-model-save-description">
                설명
                <span class="required">*</span>
              </label>
              <div class="form-detail">
                <textarea
                  id="data-model-save-description"
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
              <span class="form-label"> 태그 </span>
              <div class="form-detail">
                <div class="select select-lg">
                  <button class="select-button">
                    <div class="tag tag-primary tag-sm">
                      <span class="tag-text">tag1</span>
                      <button class="tag-delete-button">
                        <span class="hidden-text">삭제</span>
                        <svg-icon class="svg-icon" name="close"></svg-icon>
                      </button>
                    </div>
                    <svg-icon
                      class="svg-icon select-indicator"
                      name="chevron-down-medium"
                    ></svg-icon>
                  </button>
                  <menu-search style="display: none"></menu-search>
                </div>
              </div>
            </div>
            <div class="form-item">
              <label for="data-model-save-name" class="form-label">
                동의어
              </label>
              <div class="form-detail">
                <input
                  id="data-model-save-name"
                  class="text-input text-input-lg"
                  placeholder="콤마로 구분해서 추가"
                />
              </div>
            </div>
            <div class="form-item">
              <span class="form-label"> 관련용어 </span>
              <div class="form-detail">
                <div class="select select-lg">
                  <button class="select-button">
                    <div class="tag tag-primary tag-sm">
                      <span class="tag-text">용어1</span>
                      <button class="tag-delete-button">
                        <span class="hidden-text">삭제</span>
                        <svg-icon class="svg-icon" name="close"></svg-icon>
                      </button>
                    </div>
                    <svg-icon
                      class="svg-icon select-indicator"
                      name="chevron-down-medium"
                    ></svg-icon>
                  </button>
                  <menu-search style="display: none"></menu-search>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-foot">
        <div class="modal-foot-group">
          <button class="button button-neutral-ghost button-lg">취소</button>
          <button class="button button-primary button-lg">저장</button>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import MenuSearch from "@extends/menu-seach/menu-search.vue";
</script>
