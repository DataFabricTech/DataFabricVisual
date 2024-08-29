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
        @onClickSearch="onClickSearch"
        :placeholder="'검색어를 입력하세요.'"
        :inp-value="searchInputValue"
        :inp-id="'headerInp1'"
        :label-text="'데이터 모델 검색'"
        @update:value="updateSearchInputValue"
      ></SearchInput>
      <div class="profile ml-auto">
        <span class="profile-avatar"> {{ profileFirstWord }} </span>
        <div class="profile-text">{{ user.name }}</div>
        <button
          class="button button-sm button-neutral-ghost"
          @click="isDropdownOpen = !isDropdownOpen"
        >
          <svg-icon class="svg-icon" name="chevron-down-medium"></svg-icon>
          <span class="hidden-text">내 메뉴</span>
        </button>
        <div
          class="dropdown"
          style="top: 40px; right: 16px"
          v-if="isDropdownOpen"
          ref="dropdown"
        >
          <ul class="dropdown-list">
            <li class="dropdown-item">
              <nuxt-link
                :to="`/portal/my-page?fqn=${user.fullyQualifiedName}`"
                class="dropdown-button"
                @click="isDropdownOpen = false"
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
import SearchInput from "@extends/search-input/SearchInput.vue";

// Store
const searchCommonStore = useSearchCommonStore();
const { setSearchKeyword, resetReloadList } = searchCommonStore;

const userStore = useUserStore();
const { getUserInfo } = userStore;
const { user } = storeToRefs(userStore);

const layoutHeaderStore = useLayoutHeaderStore();
const { searchInputValue } = storeToRefs(layoutHeaderStore);

const { $api } = useNuxtApp();
const router = useRouter();

const header = ref();
const dropdown = ref();
const isDropdownOpen = ref(false);
const profileFirstWord = ref("");

const updateSearchInputValue = (newValue: string) => {
  searchInputValue.value = newValue;
};

const onClickSearch = (value: string) => {
  setSearchKeyword(value);
  resetReloadList();
  router.push({ path: `/portal/search` });
};

const handleClickOutside = (event: any) => {
  if (
    dropdown.value &&
    !dropdown.value.contains(event.target) &&
    !header.value.contains(event.target)
  ) {
    isDropdownOpen.value = false;
  }
};
const setProfileFirstWord = (name: string) => {
  profileFirstWord.value = name.slice(0, 1).toUpperCase();
};

const logOut = async () => {
  await $api(`/api/auth/logout`, {
    method: "POST",
  })
    .then(() => {
      isDropdownOpen.value = false;
      router.push("/portal/login");
    })
    .catch((err: any) => {
      console.log("err: ", err);
    });
};

await getUserInfo();

onMounted(async () => {
  setProfileFirstWord(user.value.name);
  document.addEventListener("click", handleClickOutside);
});

onUnmounted(() => {
  document.removeEventListener("click", handleClickOutside);
});
</script>

<style scoped></style>
