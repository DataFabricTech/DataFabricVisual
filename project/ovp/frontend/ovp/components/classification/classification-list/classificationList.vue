<template>
  <div class="work-list">
    <div class="l-top-bar">
      <span class="title">분류 목록</span>
      <button class="button button-secondary-stroke" @click="openModal">
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
          <li
            class="menu-item"
            v-for="item in classificationList"
            :key="item.id"
            @click="showClassificationDetail(item.id, item.name)"
          >
            <button class="menu-button">
              <svg-icon class="svg-icon" name="tag"></svg-icon>
              <a href="" class="menu-text">{{
                item.displayName || item.name
              }}</a>
            </button>
          </li>
        </div>
      </div>
    </div>
  </div>
  <classification-create
    :modal-id="MODAL_ID"
    @close-modal="closeModal"
  ></classification-create>
</template>

<script setup lang="ts">
import { defineProps } from "vue";
import { storeToRefs } from "pinia";
import classificationCreate from "@/components/classification/modal/classification-create.vue";
import { classificationStore } from "@/store/classification/index";
const { $vfm } = useNuxtApp();

const useClassificationStore = classificationStore();
const { classificationList } = storeToRefs(useClassificationStore);
const { getClassificationDetail, getClassificationTags } =
  useClassificationStore;

// 분류 추가 모달 ID
const MODAL_ID = "modal-classification";

function openModal() {
  $vfm.open(MODAL_ID);
}
function closeModal() {
  $vfm.close(MODAL_ID);
}

const props = defineProps({
  isLoaded: {
    type: Boolean,
    default: false,
  },
});

// 분류 목록 중 단일 목록 클릭시 실행되는 함수
const showClassificationDetail = async (id: string, name: string) => {
  // 선택한 분류의 상세 조회 API 호출
  await getClassificationDetail(id);
  // 태그 리스트 API 호출
  await getClassificationTags(name);
};
</script>

<style scoped></style>
