<template>
  <header id="header" ref="header">
    <div class="h-group">
      <h1 class="logo">
        <nuxt-link to="/" class="logo-link">
          <svg-icon class="svg-icon logo-img" name="logo"></svg-icon>
          <span class="hidden-text">logo</span>
        </nuxt-link>
      </h1>
      <SearchInput
        @reset="search('')"
        @onClickSearch="search"
        :placeholder="'검색어를 입력하세요.'"
        :inp-value="searchInputValue"
        :inp-id="'headerInp1'"
        :label-text="'데이터 모델 검색'"
        @update:value="updateSearchInputValue"
      ></SearchInput>
      <div class="profile ml-auto" ref="dropdown">
        <span class="profile-avatar"> {{ profileFirstWord }} </span>
        <div class="profile-text">{{ user.displayName || user.name }}</div>
        <button
          class="button button-sm button-neutral-ghost"
          @click="isDropdownOpen = !isDropdownOpen"
        >
          <svg-icon
            class="svg-icon button-icon"
            :class="{ 'rotate-180': isDropdownOpen }"
            name="chevron-down-medium"
          />
          <span class="hidden-text">내 메뉴</span>
        </button>
        <div
          class="dropdown"
          style="top: 40px; right: 16px"
          v-if="isDropdownOpen"
        >
          <ul class="dropdown-list">
            <li class="dropdown-item">
              <nuxt-link
                :to="`/portal/my-page?fqn=${user.fullyQualifiedName}`"
                class="dropdown-button"
                @click="moveMyPage"
              >
                <span class="dropdown-text">마이페이지</span>
              </nuxt-link>
            </li>
            <li class="dropdown-item">
              <button class="dropdown-button" @click="logOut">
                <span class="dropdown-text">로그아웃</span>
              </button>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { storeToRefs } from "pinia";
import { useRouter } from "vue-router";
import { useSearchCommonStore } from "~/store/search/common";
import { useUserStore } from "@/store/user/userStore";
import { useLayoutHeaderStore } from "~/store/layout/header";
import { useMenuStore } from "@/store/common/menu";
import SearchInput from "@extends/search-input/SearchInput.vue";
import { useDropdownHelper } from "~/composables/dropDownHelper";
import _ from "lodash";

// Store
const searchCommonStore = useSearchCommonStore();
const { setSearchKeyword, changeTab, setEmptyFilter, resetReloadList } =
  searchCommonStore;

const userStore = useUserStore();
const { getUserInfo, setProfileFirstWord } = userStore;
const { user, profileFirstWord } = storeToRefs(userStore);

const layoutHeaderStore = useLayoutHeaderStore();
const { searchInputValue } = storeToRefs(layoutHeaderStore);

const menuStore = useMenuStore();
const { headerUrl } = storeToRefs(menuStore);

const { $api } = useNuxtApp();
const router = useRouter();

const header = ref();
const dropdown = ref();

const { isDropdownOpen, setHandler } = useDropdownHelper();

watch(
  () => headerUrl.value,
  (url) => {
    if (
      !_.isEmpty(searchInputValue.value) &&
      !_.includes(url, "/portal/search")
    ) {
      searchInputValue.value = "";
    }
  },
);

const updateSearchInputValue = (newValue: string) => {
  searchInputValue.value = newValue;
};

const search = (value: string) => {
  setSearchKeyword(value);
  changeTab("table");
  setEmptyFilter();
  resetReloadList();
  router.push({ path: `/portal/search` });
};

const logOut = () => {
  $api(`/api/auth/logout`, {
    method: "POST",
  })
    .then(() => {
      isDropdownOpen.value = false;
    })
    .catch((err: any) => {
      console.log("err: ", err);
    })
    .finally(() => {
      // 로그아웃 성공 또는 실패 여부와 관계없이 로그인 페이지로 이동
      router.push("/portal/login");
    });
};

await getUserInfo();

const moveMyPage = () => {
  isDropdownOpen.value = false;
};

onMounted(() => {
  setProfileFirstWord(user.value.displayName || user.value.name);
  setHandler(dropdown.value, header.value);
});
</script>

<style scoped></style>
