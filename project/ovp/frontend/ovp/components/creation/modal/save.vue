<template>
  <div class="modal-fixed vfm--fixed vfm--inset">
    <div class="modal modal-padding-16" style="width:480px">
      <div class="modal-head">
        <div class="modal-head-text">
          <span class="modal-head-title">데이터 모델 저장</span>
        </div>
        <button class="button link-button button-sm" type="button" @click="saveDataModel(false)">
          <span class="hidden-text">닫기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
      <div class="modal-body">
        <div class="form form-lg">
          <div class="form-body">
            <div class="form-item">
              <label for="data-model-save-name" class="form-label">
                데이터 모델 이름
                <span class="required">*</span>
              </label>
              <div class="form-detail">
                <input id="data-model-save-name" class="text-input text-input-lg" placeholder="데이터 모델 이름을 입력하세요." />
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">중복된 데이터 모델 이름입니다.</p>
                </div>
              </div>
            </div>
            <div class="form-item">
              <label class="form-label" for="data-model-save-description">
                데이터 모델 설명
              </label>
              <div class="form-detail">
                <textarea id="data-model-save-description" class="textarea h-28"
                          placeholder="데이터 모델 설명을 입력하세요."></textarea>
              </div>
            </div>
            <div class="form-item">
              <span class="form-label">
                카테고리
              </span>
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
                    <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
                  </button>
                  <menu-search style="display:none;"></menu-search>
                </div>
              </div>
            </div>
            <div class="form-item">
              <span class="form-label">
                Tag
              </span>
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
                    <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
                  </button>
                  <menu-search style="display:none;"></menu-search>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-foot">
        <div class="modal-foot-group">
          <div class="notification notification-sm notification-error">
            <svg-icon class="notification-icon" name="error"></svg-icon>
            <p class="notification-detail">쿼리가 정상적으로 동작하지 않습니다.</p>
          </div>
          <button class="button button-primary button-lg">저장</button>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
const emit = defineEmits<{ (e: "change", option: boolean): void }>();
const saveDataModel = (option: boolean) => {
  emit("change", option);
};
</script>
<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
