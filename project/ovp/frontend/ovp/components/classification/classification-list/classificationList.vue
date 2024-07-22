<template>
  <div class="work-list">
    <div class="l-top-bar">
      <span class="title">분류 목록</span>
      <button
          class="button button-secondary-stroke"
          @click="showModalClas = true"
      >
        분류 추가
      </button>
    </div>
    <!-- 결과 없을 시 no-result 표시 / 기본 .work-page로 컨텐츠 표시 -->
    <div class="no-result" style="display: none">
      <div class="notification">
        <svg-icon class="notification-icon" name="info"></svg-icon>
        <p class="notification-detail">등록된 정보가 없습니다.</p>
      </div>
    </div>
    <div class="menu border-none">
      <div class="menu-list">
        <li class="menu-item">
          <button class="menu-button">
            <svg-icon class="svg-icon" name="tag"></svg-icon>
            <a href="" class="menu-text">분류 01</a>
          </button>
        </li>
        <li class="menu-item">
          <button class="menu-button">
            <svg-icon class="svg-icon" name="tag"></svg-icon>
            <a href="" class="menu-text">분류 02</a>
          </button>
        </li>
        <li class="menu-item">
          <button class="menu-button">
            <svg-icon class="svg-icon" name="tag"></svg-icon>
            <a href="" class="menu-text">분류 03</a>
          </button>
        </li>
        <li class="menu-item">
          <button class="menu-button">
            <svg-icon class="svg-icon" name="tag"></svg-icon>
            <a href="" class="menu-text">분류 04</a>
          </button>
        </li>
      </div>
    </div>
  </div>
  <classification-create v-if="showModalClas == true" @close-modal="closeModalClas"></classification-create>
</template>
<script setup lang="ts">
import classificationCreate from "@/components/classification/modal/classification-create.vue"

const showModalClas = ref(false);

const closeModalClas = () => {
  showModalClas.value = !showModalClas.value;
}

</script>
