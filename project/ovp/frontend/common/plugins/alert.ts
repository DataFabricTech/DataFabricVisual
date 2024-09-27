import { useAlert } from "~/components/extends/alert/useAlert";

export default defineNuxtPlugin((nuxtApp) => {
  const { showAlert, showConfirm } = useAlert();

  // Nuxt 플러그인으로 alert와 confirm 기능을 등록
  nuxtApp.provide("alert", showAlert);
  nuxtApp.provide("confirm", showConfirm);
});
