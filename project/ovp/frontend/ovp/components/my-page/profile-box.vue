<template>
  <div class="profile-box">
      <span class="profile-avatar">
        <img class="profile-img" alt="프로필 이미지" />
      </span>
    <div class="profile-group">
      <div class="editable-group">
        <span class="profile-name">root</span>
        <button
            class="button button-neutral-ghost button-sm"
            type="button"
            @click="editDisplayName(true)"
        >
          <span class="hidden-text">수정</span>
          <svg-icon class="button-icon" name="pen"></svg-icon>
        </button>
      </div>
      <!-- TODO : [개발] 수정시 인풋 활성화-->
      <div class="editable-group" v-show="isOpenDisplayEdit">
        <label class="hidden-text" for="profile-name-modify"
        >프로필 이름 입력</label
        >
        <input id="profile-name-modify" class="text-input w-1/4" />
        <div class="h-group gap-2 shrink-0">
          <button
              class="button button-neutral-lighter button-sm"
              type="button"
              @click="editDisplayName(false)"
          >
            취소
          </button>
          <button
              class="button button-primary-lighter button-sm"
              type="button"
          >
            완료
          </button>
        </div>
      </div>
      <div class="profile-list">
        <div class="profile-title">이메일</div>
        <div>admin@mobigen.com</div>
      </div>
      <div class="profile-list">
        <div class="profile-title">역할</div>
        <div>
          <div class="editable-group">
            <span>사용자</span>
            <button
                class="button button-neutral-ghost button-sm"
                type="button"
            >
              <span class="hidden-text">수정</span>
              <svg-icon class="button-icon" name="pen"></svg-icon>
            </button>
          </div>
        </div>
      </div>
      <div class="profile-list">
        <div class="profile-title">역할</div>
        <div>
          <!-- TODO : [개발] 수정시 스위치 활성화-->
          <div class="switch">
            <input type="checkbox" id="manager-sw" class="switch-input" />
            <label for="manager-sw" class="switch-label">
              관리자
              <div class="switch-control"></div>
            </label>
          </div>
        </div>
      </div>
      <div class="editable-group">
        <span class="editable-group-desc">계정에 대한 설명영역입니다.</span>
        <button class="button button-neutral-ghost button-sm" type="button" @click="editDescription(true)">
          <span class="hidden-text">수정</span>
          <svg-icon class="button-icon" name="pen"></svg-icon>
        </button>
      </div>
      <!-- TODO : [개발] 수정시 textarea 활성화-->
      <div class="editable-group w-2/4" v-show="isOpenDescriptionEdit">
        <label class="hidden-text" for="profile-description-modify"
        >계정 설명 수정</label
        >
        <textarea id="profile-description-modify" class="textarea"></textarea>
        <div class="h-group gap-1">
          <button class="button button-neutral-stroke" type="button" @click="editDescription(false)">
            취소
          </button>
          <button class="button button-primary-lighter" type="button">
            완료
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
const isOpenDisplayEdit = ref(false);
const isOpenDescriptionEdit= ref(false);

const editDisplayName = (value: boolean) => {
  isOpenDisplayEdit.value = value;
};

const editDescription = (value: boolean) => {
  isOpenDescriptionEdit.value = value;
}
</script>
<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
