<template>
  <div class="section-contents">
    <div ref="loader" class="loader-wrap" style="display: none">
      <Loading :use-loader-overlay="true" class="loader-lg"></Loading>
    </div>
    <div class="flex">
      <div class="main-content main-content-full">
        <div class="l-top-bar">
          <span class="main-content-title">추천 많은 데이터</span>
          <button
            class="button link-button-support"
            @click="setSearchConditionUrl('totalVotes_desc')"
          >
            <span class="button-title">모두 보기</span>
          </button>
        </div>
        <div class="recommend-data">
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
      </div>
    </div>
    <div class="l-split">
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
const { getUserInfo, getUpVotesData, getLastUpdatedData } = mainCommonStore;
const {
  bookmarkData,
  upVotesData,
  lastUpdatedData,
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

onMounted(() => {
  if (loader.value) {
    loader.value.style.display = "block";
  }
  getUserInfo();
  getLastUpdatedData();
  getUpVotesData();
  if (loader.value) {
    loader.value.style.display = "none";
  }
});
</script>

<style scoped></style>
