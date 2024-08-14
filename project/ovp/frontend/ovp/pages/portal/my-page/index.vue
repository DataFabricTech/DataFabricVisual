<template>
  <div class="section-contents bg-white">
    <div class="interaction-buttons">
      <button class="button button-neutral-stroke" @click="showModalPwChange">
        비밀번호 변경
      </button>
    </div>
    <profile-box
      :targetUserInfo="targetUserInfo"
      :userInfo="user"
      @changeDisplayName="changeDisplayName"
      @changeRole="changeRole"
      @changeDescription="changeDescription"
    ></profile-box>
    <tab
      class="tab-line"
      :data="tabOptions"
      :labelKey="'label'"
      :valueKey="'value'"
      :useTabContents="false"
      @change="changeTab"
      :current-item="initTab"
      :current-item-type="'value'"
    >
    </tab>
    <div class="my-contents">
      <div class="search-input search-input-lg w-96">
        <label class="hidden-text" for="text-input-example-4">label</label>
        <input
          id="text-input-example-4"
          class="text-input"
          placeholder="검색어를 입력하세요."
          v-model="searchKeyword"
          @keyup.enter="search"
        />
        <svg-icon class="text-input-icon" name="search"></svg-icon>
        <button
          class="search-input-action-button button button-neutral-ghost button-sm"
          type="button"
          @click="clearSearchText"
        >
          <span class="hidden-text">지우기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
      <div class="l-split">
        <div class="data-page">
          <div id="dataList" class="data-list" v-show="!isSearchResultNoData">
            <resource-box-list
              :data-list="searchResult"
              :use-list-checkbox="false"
              :show-owner="true"
              :show-category="true"
              :is-box-selected-style="isBoxSelectedStyle"
              @previewClick="previewClick"
              @modelNmClick="modelNmClick"
            />
            <div ref="scrollTrigger" class="w-full h-[1px] mt-px"></div>
          </div>
          <div class="no-result" v-show="isSearchResultNoData">
            <div class="notification">
              <svg-icon class="notification-icon" name="info"></svg-icon>
              <p class="notification-detail">등록된 정보가 없습니다.</p>
            </div>
          </div>
        </div>
        <Preview
          :isShowPreview="isShowPreview"
          :preview-data="previewData"
          :model-type="previewIndex"
          @change="getPreviewCloseStatus"
        ></Preview>
      </div>
    </div>
  </div>
  <PwResetModal :modal-id="PW_RESET_MODAL_ID"></PwResetModal>
</template>

<script setup lang="ts">
import Tab from "@extends/tab/Tab.vue";
import resourceBoxList from "~/components/common/resource-box/resource-box-list.vue";
import Preview from "~/components/common/preview/preview.vue";
import { useNuxtApp } from "nuxt/app";
import { useRoute } from "vue-router";
const { $vfm } = useNuxtApp();
import profileBox from "~/components/my-page/profile-box.vue";
import { useMyPageStore } from "~/store/my-page/myPageStore";
import { useUserStore } from "~/store/user/userStore";
import { useIntersectionObserver } from "~/composables/intersectionObserverHelper";

const PW_RESET_MODAL_ID: string = "pw-reset-modal";

const myPageStore = useMyPageStore();
const {
  targetUserInfo,
  searchResult,
  isSearchResultNoData,
  searchKeyword,
  previewData,
  isShowPreview,
  isBoxSelectedStyle,
} = storeToRefs(myPageStore);
const {
  changeDisplayName,
  changeRole,
  changeDescription,
  getTargetUserData,
  changeTab,
  updateTargetUserInfo,
  getPreviewData,
  getSearchList,
  addSearchList,
  search,
  clearSearchText,
  modelNmClick,
} = myPageStore;

const userStore = useUserStore();
// NOTE: 로그인 사용자 정보
const { user } = storeToRefs(userStore);

const route = useRoute();

const showModalPwChange: () => void = () => {
  console.log(PW_RESET_MODAL_ID);
  $vfm.open(PW_RESET_MODAL_ID);
};

await getTargetUserData(route.query.fqn);
await getSearchList();

const initTab: string = "myBookMark";

const tabOptions = [
  { label: "나의 북마크", value: "myBookMark" },
  { label: "나의 데이터", value: "myData" },
];

const getPreviewCloseStatus = (option: boolean) => {
  isShowPreview.value = option;
  isBoxSelectedStyle.value = false;
  currentPreviewId = "";
};

let currentPreviewId: string | number = "";
let previewIndex: string = "";

const previewClick = async (data: object) => {
  const { id, fqn, type } = data as { id: string; fqn: string };
  if (id === currentPreviewId) {
    return;
  }

  await getPreviewData(fqn);
  isShowPreview.value = true;
  isBoxSelectedStyle.value = true;
  currentPreviewId = id;
  previewIndex = type;
};

const { scrollTrigger } = useIntersectionObserver(addSearchList);
</script>

<style scoped></style>
