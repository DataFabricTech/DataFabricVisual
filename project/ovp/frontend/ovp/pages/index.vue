<template>
  <div class="section-contents">
    <div ref="loader" class="loader-wrap" style="display: none">
      <Loading :use-loader-overlay="true" class="loader-lg"></Loading>
    </div>
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
        <!--        TODO: [개발] 최근 탐색 데이터 API 확인 후 추가 예정 -->
        <resource-box-list
          v-else
          :use-prv-btn="false"
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
          <nuxt-link :to="'/portal/my-page'" class="button link-button-support">
            <span class="button-title">모두 보기</span>
          </nuxt-link>
        </div>
        <div class="no-result" v-if="isBookmarkDataNoInfo">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">출력할 정보가 없습니다.</p>
          </div>
        </div>
        <resource-box-list
          v-else
          :use-prv-btn="false"
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
          <button
            class="button link-button-support"
            @click="setSearchConditionUrl('totalVotes_desc')"
          >
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
          :use-prv-btn="false"
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
          <span class="main-content-title">최근 업데이트 데이터</span>
          <button
            class="button link-button-support"
            @click="setSearchConditionUrl('updatedAt_desc')"
          >
            <span class="button-title">모두 보기</span>
          </button>
        </div>
        <div class="no-result" v-if="isLastUpdatedData">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">출력할 정보가 없습니다.</p>
          </div>
        </div>
        <resource-box-list
          v-else
          :use-prv-btn="false"
          :data-list="lastUpdatedData"
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
import { ref, onMounted } from "vue";
import { storeToRefs } from "pinia";
import { useMainStore } from "@/store/main/mainStore";
import { useSearchCommonStore } from "@/store/search/common";
import { useRouter } from "nuxt/app";
import Loading from "@base/loading/Loading.vue";

const loader = ref<HTMLElement | null>(null);

const router = useRouter();

const searchCommonStore = useSearchCommonStore();
const { setSortFilter } = searchCommonStore;
const { currentTab } = storeToRefs(searchCommonStore);

const mainCommonStore = useMainStore();
const { getRecentQuestData, getUserInfo, getUpVotesData, getLastUpdatedData } =
  mainCommonStore;
const {
  recentQuestData,
  bookmarkData,
  upVotesData,
  lastUpdatedData,
  isRecentQuestDataNoInfo,
  isBookmarkDataNoInfo,
  isUpVotesDataNoInfo,
  isLastUpdatedData,
} = storeToRefs(mainCommonStore);

const setSearchConditionUrl = (item: string) => {
  currentTab.value = "table";
  setSortFilter(item);

  nextTick(() => {
    router.push({ path: `/portal/search` });
  });
};

const modelNmClick = (data: object) => {
  const { id, fqn, type } = data as { id: string; fqn: string; type: string };
  router.push({
    path: "/portal/search/detail",
    query: {
      id: id,
      fqn: fqn,
      type: type,
    },
  });
};

onMounted(async () => {
  if (loader.value) {
    loader.value.style.display = "block";
  }
  await getRecentQuestData();
  await getUserInfo();
  await getLastUpdatedData();
  await getUpVotesData();
  if (loader.value) {
    loader.value.style.display = "none";
  }
});
</script>

<style scoped></style>
