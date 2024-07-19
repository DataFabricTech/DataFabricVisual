<template>
  <header id="header" ref="header">
    <div class="h-group">
      <h1 class="logo">
        <nuxt-link to="main" class="logo-link"
          >Open VDAP Portal <span class="hidden-text">logo</span>
        </nuxt-link>
      </h1>
      <SearchInput @onClickSearch="onClickSearch"></SearchInput>
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
              <!--              TODO: [개발] 마이페이지 개발 후 라우터 링크 연결 필요 -->
              <button class="dropdown-button" @click="isDropdownOpen = false">
                <span class="dropdown-text">마이페이지</span>
              </button>
            </li>
            <li class="dropdown-item">
              <!--              TODO: [개발] 로그아웃 요청 API 추가 필요 -->
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
import SearchInput from "@extends/search-input/SearchInput.vue";

// Store
const searchCommonStore = useSearchCommonStore();
const { setSearchKeyword, resetReloadList } = searchCommonStore;

const userStore = useUserStore();
const { getUserInfo } = userStore;
const { user } = storeToRefs(userStore);

const router = useRouter();

const header = ref();
const dropdown = ref();
const isDropdownOpen = ref(false);
const profileFirstWord = ref("");

const onClickSearch = (value: string) => {
  // 검색어 셋팅
  setSearchKeyword(value);
  // 항목 갱신
  resetReloadList();
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

const logOut = () => {
  console.log("Logout");
  isDropdownOpen.value = false;
  router.push("/login");
};

onMounted(async () => {
  await getUserInfo();
  setProfileFirstWord(user.value.name);
  document.addEventListener("click", handleClickOutside);
});

onUnmounted(() => {
  document.removeEventListener("click", handleClickOutside);
});
</script>

<style scoped></style>
