<template>
  <div class="work-page">
    <div class="l-top-bar">
      <div class="editable-group">
        <span class="editable-group-title">분류 01</span>
        <button class="button button-neutral-ghost button-sm" type="button" @click="modifyClas">
          <span class="hidden-text">수정</span>
          <svg-icon class="button-icon" name="pen"></svg-icon>
        </button>
      </div>
      <!-- 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
      <div class="editable-group" v-if="showModifyClas">
        <lable class="hidden-text" for="title-modify">분류 이름 수정</lable>
        <input id="title-modify" class="text-input w-4/5" />
        <div class="h-group gap-1">
          <button class="button button-neutral-stroke" type="button" @click="modifyClas">
            취소
          </button>
          <button class="button button-primary-lighter" type="button">
            완료
          </button>
        </div>
      </div>
      <button class="button button-error-lighter">삭제</button>
    </div>
    <div class="work-contents">
      <!-- 결과 없을 시 no-result 표시  -->
      <div class="no-result" style="display: none">
        <div class="notification">
          <svg-icon class="notification-icon" name="info"></svg-icon>
          <p class="notification-detail">등록된 정보가 없습니다.</p>
        </div>
      </div>
      <div class="editable-group">
            <span class="editable-group-desc"
            >GDPR special category data is personal information of data
              subjects that is especially sensitive, the exposure of which could
              significantly impact the rights and freedoms of data subjects and
              potentially be used against them for unlawful
              discrimination.</span
            >
        <button class="button button-neutral-ghost button-sm" type="button" @click="modifyClasDescription">
          <span class="hidden-text">수정</span>
          <svg-icon class="button-icon" name="pen"></svg-icon>
        </button>
      </div>
      <!-- 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
      <div class="editable-group" v-if="showModifyClasDescription">
        <lable class="hidden-text" for="description-modify"
        >분류 설명 수정
        </lable>
        <textarea
            id="description-modify"
            class="textarea"
            width="300px"
        ></textarea>
        <div class="h-group gap-1">
          <button class="button button-neutral-stroke" type="button" @click="modifyClasDescription">
            취소
          </button>
          <button class="button button-primary-lighter" type="button">
            완료
          </button>
        </div>
      </div>
      <div>
        <tag-list></tag-list>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import tagList from "./tagList.vue"

const showModifyClas = ref(false);
const showModifyClasDescription = ref(false);

const modifyClas = () => {
  showModifyClas.value = !showModifyClas.value;
}
const modifyClasDescription = () => {
  showModifyClasDescription.value = !showModifyClasDescription .value;
}
</script>

<style lang="scss" scoped></style>
