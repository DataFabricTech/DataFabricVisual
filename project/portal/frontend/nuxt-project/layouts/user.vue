<template>
  <div class="wrapper wrapper-sub">
    <NuxtLayout name="user-header" />
    <main id="main" class="main-sub">
      <div class="main-contents">
        <div
          v-bind:class="[
            'contents-wrap',
            ['/data/request', '/my/dashboard'].includes(userStore.getTab.currentPath)
              ? 'floating-contents'
              : '',
            [
              '/search/keyword',
              `/search/detail?guid=${router.currentRoute.value.query.guid}`,
              '/search/category',
            ].includes(userStore.getTab.currentPath)
              ? 'sch-contents'
              : '',
          ]"
        >
          <div class="contents-top" v-if="userStore.getTab.currentPath !== '/main'">
            <div class="breadcrumbs-wrap">
              <div class="breadcrumbs">
                <ol class="mu-path">
                  <li class="home"><a href="">HOME</a></li>
                  <li>
                    <a @click="goToCurrentPathDepth()">{{ userStore.getTab.currentPathDepth1 }}</a>
                  </li>
                  <li>
                    <a @click="goToCurrentPathDepth()">{{ userStore.getTab.currentPathDepth2 }}</a>
                  </li>
                </ol>
              </div>
            </div>
            <div class="subject-wrap">
              <div class="subject">
                <div class="subject-title">
                  <h3 v-if="checkCurrentPath()">{{ userStore.getTab.currentPathDepth2 }}</h3>
                  <!-- 실제 해당 페이지명 -->
                </div>
              </div>
            </div>
          </div>
          <div class="contents">
            <slot />
          </div>
        </div>
      </div>
    </main>
    <modals-container></modals-container>
    <NuxtLayout name="user-footer" />
  </div>
</template>

<script setup lang="ts">
import { ModalsContainer } from "vue-final-modal";
import { useUserStore } from "@/store/user";
import { useRoute, useRouter } from "nuxt/app";
import { watch, onMounted } from "vue";

const route = useRoute();
const router = useRouter();
const userStore = useUserStore();

// 상태를 포함한 context 객체를 생성하는 함수
const createContext = (): any => {
  return {
    state: {
      _user: userStore._user,
      _tab: userStore._tab,
    },
  };
}

const changeRouter = (): void => {
  const { fullPath, params } = router.currentRoute.value;
  userStore.onChangeRouter(createContext(), { to: { fullPath, params } });
};

onMounted(() => {
  changeRouter();
});

watch(
  () => route.path,
  () => {
    changeRouter();
  }
);

// 상세 페이지에서 페이지명 안보이게 하기
const checkCurrentPath = (): boolean => {
  const path = userStore.getTab.currentPath
  const cleanPath = path.split('?')[0];

  // 데이터 검색 > 통합검색 > 상세조회 || 데이터 검색 > 외부데이터 > 상세조회 페이지의 경우 표기
  if (cleanPath == '/search/detail' || cleanPath == '/search/external/detail') return true

  return !cleanPath.endsWith('/detail')
}

const goToCurrentPathDepth = () => {}
</script>
