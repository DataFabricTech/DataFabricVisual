import type { Pinia } from "pinia";
import { createPersistedState } from "pinia-plugin-persistedstate";

export default defineNuxtPlugin((nuxtApp) => {
  const $pinia = nuxtApp.$pinia as Pinia;

  // NOTE: sessionStorage 에 저장되도록 전역설정
  $pinia.use(
    createPersistedState({
      storage: sessionStorage,
    }),
  );
});
