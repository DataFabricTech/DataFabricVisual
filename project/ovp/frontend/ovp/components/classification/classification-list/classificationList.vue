<template>
  <div class="work-list">
    <div class="l-top-bar">
      <span class="title">분류 목록</span>
      <button class="button button-secondary-stroke" @click="showModalClas = true">
        분류 추가
      </button>
    </div>
    <!-- 데이터 로딩이 완료될 때까지 아무것도 표시하지 않음 -->
    <div v-if="!props.isLoaded">
      <!-- 로딩 중일 때는 아무것도 표시하지 않음 -->
    </div>
    <!-- 데이터 로딩 후 분류 목록 또는 "등록된 정보가 없습니다." 메시지 표시 -->
    <div v-else>
      <div v-if="classificationList.length === 0" class="no-result">
        <div class="notification">
          <svg-icon class="notification-icon" name="info"></svg-icon>
          <p class="notification-detail">등록된 정보가 없습니다.</p>
        </div>
      </div>
      <div v-else class="menu border-none">
        <div class="menu-list">
          <li class="menu-item" v-for="item in classificationList" :key="item.id">
            <button class="menu-button">
              <svg-icon class="svg-icon" name="tag"></svg-icon>
              <a href="" class="menu-text">{{ item.displayName || item.name }}</a>
            </button>
          </li>
        </div>
      </div>
    </div>
  </div>
  <classification-create v-if="showModalClas" @close-modal="closeModalClas"></classification-create>
</template>

<script setup lang="ts">
import { defineProps, ref } from "vue";
import { storeToRefs } from "pinia";
import classificationCreate from "@/components/classification/modal/classification-create.vue";
import { classificationStore } from "@/store/classification/index";

const useClassificationStore = classificationStore();
const { classificationList } = storeToRefs(useClassificationStore);

const showModalClas = ref(false);

const closeModalClas = () => {
  showModalClas.value = !showModalClas.value;
}

const props = defineProps({
  isLoaded: {
    type: Boolean,
    default: false
  }
});

</script>

<style scoped></style>
