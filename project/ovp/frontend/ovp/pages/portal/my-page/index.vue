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
      @changeDisplayName="changeUserInfo"
      @changeRole="changeUserInfo"
      @changeDescription="changeUserInfo"
    ></profile-box>
    <tab
      class="tab-line"
      :data="tabOptions"
      :labelKey="'label'"
      :valueKey="'value'"
      :useTabContents="false"
      @change="changeTab"
      :current-item="tabOptions[0].value"
      :current-item-type="'value'"
    >
    </tab>
    <div class="my-contents">
      <search-input
        class="search-input-lg w-96"
        :is-search-input-default-type="false"
        :placeholder="'검색어를 입력하세요.'"
        :inp-value="searchKeyword"
        :inp-id="currentTab"
        :label-text="
          currentTab === 'myBookMark' ? '나의 북마크 검색' : '나의 데이터 검색'
        "
        @on-input="inputSearchKeyword"
        @reset="resetSearchKeyword"
      ></search-input>
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
            <Loading
              id="loader"
              :use-loader-overlay="true"
              class="loader-lg is-loader-inner"
              style="display: none"
            ></Loading>
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
</template>

<script setup lang="ts">
import { storeToRefs } from "pinia";
import Tab from "@extends/tab/Tab.vue";
import Loading from "@base/loading/Loading.vue";
import resourceBoxList from "~/components/common/resource-box/resource-box-list.vue";
import Preview from "~/components/common/preview/preview.vue";
import SearchInput from "@extends/search-input/SearchInput.vue";
import { useRoute } from "vue-router";
import { useModal } from "vue-final-modal";
import profileBox from "~/components/my-page/profile-box.vue";
import { useMyPageStore } from "~/store/my-page/myPageStore";
import { useUserStore } from "~/store/user/userStore";
import { useIntersectionObserver } from "~/composables/intersectionObserverHelper";
import { useRouter } from "nuxt/app";
import pwResetModal from "@/components/my-page/pw-reset-modal.vue";

const { open, close } = useModal({
  component: pwResetModal,
  attrs: {
    onClose() {
      close();
    },
  },
});

const myPageStore = useMyPageStore();
const {
  targetUserInfo,
  currentTab,
  searchResult,
  isSearchResultNoData,
  searchKeyword,
  previewData,
  isShowPreview,
  isBoxSelectedStyle,
} = storeToRefs(myPageStore);
const {
  changeUserInfo,
  getTargetUserData,
  changeTab,
  getPreviewData,
  getContainerPreviewData,
  getSearchList,
  addSearchList,
  search,
  clearSearchText,
} = myPageStore;

const userStore = useUserStore();
// NOTE: 로그인 사용자 정보
const { user } = storeToRefs(userStore);

const route = useRoute();
const router = useRouter();

const showModalPwChange: () => void = () => {
  open();
};

watch(
  () => route.query.fqn as string,
  async (fqn: string) => {
    await getTargetUserData(fqn);
    await getSearchList();
  },
  { deep: true },
);

// 탭 value 초기화
currentTab.value = "myBookMark";

await getTargetUserData(route.query.fqn as string);

await getTargetUserData(route.query.fqn);
await getSearchList();

const tabOptions = [
  { label: "나의 북마크", value: "myBookMark" },
  { label: "나의 데이터", value: "myData" },
];

const inputSearchKeyword = (keyword: string) => {
  searchKeyword.value = keyword;
  search();
};

const resetSearchKeyword = () => {
  clearSearchText();
  search();
};

const getPreviewCloseStatus = (option: boolean) => {
  isShowPreview.value = option;
  isBoxSelectedStyle.value = false;
  currentPreviewId = "";
};

let currentPreviewId: string | number = "";
let previewIndex: string = "";

const previewClick = async (data: object) => {
  const { id, fqn, type } = data as { id: string; fqn: string; type: string };
  if (id === currentPreviewId) {
    return;
  }

  type === "storage"
    ? await getContainerPreviewData(id)
    : await getPreviewData(fqn);
  isShowPreview.value = true;
  isBoxSelectedStyle.value = true;
  currentPreviewId = id;
  previewIndex = type;
};

const modelNmClick = (data: object) => {
  const { id, fqn, type } = data as { id: string; fqn: string; type: string };
  router.push({
    path: "/portal/search/detail",
    query: {
      type: type,
      id: id,
      fqn: fqn,
    },
  });
};

const { scrollTrigger } = useIntersectionObserver({ callback: addSearchList });
</script>

<style scoped></style>
