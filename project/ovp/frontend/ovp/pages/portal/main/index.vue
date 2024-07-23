<template>
  <div class="section-contents">
    <div class="l-split">
      <div class="main-content">
        <div class="l-top-bar">
          <span class="main-content-title">최근 탐색 데이터</span>
        </div>
        <div class="no-result" v-if="isRecentQuestDataNoInfo">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">출력할 정보가 없습니다.</p>
          </div>
        </div>
        <resource-box-list
          v-else
          :data-list="recentQuestData"
          :use-list-checkbox="false"
          :show-owner="true"
          :show-category="true"
          :is-box-selected-style="false"
          @modelNmClick="modelNmClick"
        />
      </div>
      <div class="main-content">
        <div class="l-top-bar">
          <span class="main-content-title">북마크 한 데이터</span>
          <!--         TODO: [개발] 마이페이지 > 나의 북마크 리스트 이동-->
          <button class="button link-button-support">
            <span class="button-title">모두 보기</span>
          </button>
        </div>
        <div class="no-result" v-if="isBookmarkDataNoInfo">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">출력할 정보가 없습니다.</p>
          </div>
        </div>
        <resource-box-list
          v-else
          :data-list="bookmarkData"
          :use-list-checkbox="false"
          :show-owner="true"
          :show-category="true"
          :is-box-selected-style="false"
          @modelNmClick="modelNmClick"
        />
      </div>
    </div>

    <div class="l-split">
      <div class="main-content">
        <div class="l-top-bar">
          <span class="main-content-title">추천 많은 데이터</span>
          <!--         TODO: [개발] 탐색 > 정렬 필터: 추천 맣은 순 내림차순 선택-->
          <button class="button link-button-support">
            <span class="button-title">모두 보기</span>
          </button>
        </div>
        <div class="no-result" v-if="isUpVotesDataNoInfo">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">출력할 정보가 없습니다.</p>
          </div>
        </div>
        <resource-box-list
          v-else
          :data-list="upVotesData"
          :use-list-checkbox="false"
          :show-owner="true"
          :show-category="true"
          :is-box-selected-style="false"
          @modelNmClick="modelNmClick"
        />
      </div>
      <div class="main-content">
        <div class="l-top-bar">
          <span class="main-content-title">나의 데이터</span>
          <!--         TODO: [개발] 마이페이지 > 나의 데이터 리스트 이동-->
          <button class="button link-button-support">
            <span class="button-title">모두 보기</span>
          </button>
        </div>
        <div class="no-result" v-if="isMyDataNoInfo">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">출력할 정보가 없습니다.</p>
          </div>
        </div>
        <resource-box-list
          v-else
          :data-list="myData"
          :use-list-checkbox="false"
          :show-owner="true"
          :show-category="true"
          :is-box-selected-style="false"
          @modelNmClick="modelNmClick"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from "vue";
import { storeToRefs } from "pinia";
import { useMainStore } from "@/store/main/mainStore";
import { useRouter } from "nuxt/app";

const router = useRouter();

const mainCommonStore = useMainStore();
const { getRecentQuestData, getBookmarkData, getUpVotesData, getMyData } =
  mainCommonStore;
const {
  recentQuestData,
  bookmarkData,
  upVotesData,
  myData,
  isRecentQuestDataNoInfo,
  isBookmarkDataNoInfo,
  isUpVotesDataNoInfo,
  isMyDataNoInfo,
} = storeToRefs(mainCommonStore);

const modelNmClick = (data: object) => {
  const { id, fqn } = data as { id: string; fqn: string };
  router.push({
    path: "/portal/search/detail",
    query: {
      id: id,
      fqn: fqn,
    },
  });
};

onMounted(() => {
  getRecentQuestData();
  getBookmarkData();
  getUpVotesData();
  getMyData();
});
</script>

<style scoped></style>
