<template>
  <div class="wrapper admin-wrapper">
    <NuxtLayout name="admin-header" />
    <main id="main" class="main-sub">
      <div class="main-contents">
        <h2 class="blind">{{ userStore.getTab.currentPathDepth2 }}</h2>
        <div
          :class="[
            'contents-wrap',
            userStore.getTab.currentPath === '/admin/data/meta-data/detail'
              ? 'sch-contents'
              : '',
          ]"
        >
          <div class="contents-top">
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
                  <h3>{{ userStore.getTab.currentPathDepth2 }}</h3>
                  <!-- 실제 해당 페이지명 -->
                  <p class="subject-desc"></p>
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
    <NuxtLayout name="admin-footer" />
  </div>
</template>

<script setup lang="ts">
import { ModalsContainer } from "vue-final-modal";
import { useUserStore } from "@/store/user";
import { useRoute, useRouter } from "vue-router";
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

const goToCurrentPathDepth = () => {}

onMounted(() => {
  changeRouter();
});

watch(
  () => route.path,
  () => {
    changeRouter();
  }
);
</script>
