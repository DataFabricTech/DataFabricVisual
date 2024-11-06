<template>
  <div class="wrapper">
    <NuxtLayout name="user-header" />
    <slot></slot>
    <modals-container></modals-container>
    <NuxtLayout name="user-footer" />
  </div>
</template>

<script setup lang="ts">
import {ModalsContainer} from "vue-final-modal";
import { watch } from 'vue';
import { useRoute } from 'vue-router';
import { useUserStore } from '@/store/user';

const userStore = useUserStore();
const route = useRoute();

// 상태를 포함한 context 객체를 생성하는 함수
const createContext = (): any => {
  return {
    state: {
      _user: userStore._user,
      _tab: userStore._tab,
    },
  };
}

watch(
  // 메뉴 클릭 시 발생하는 라우터 설정
  () => route.fullPath,
  (newPath, oldPath) => {
    // 여기에서 route 객체의 fullPath와 params를 사용하여 to 객체를 구성
    const to = {
      fullPath: route.fullPath,
      params: route.params,
    };

    // 변경된 to 객체와 함께 state를 포함한 context 객체를 onChangeRouter에 전달
    userStore.onChangeRouter(createContext(), { to: to, from: oldPath });
  }
);

</script>
