import { createVfm } from "vue-final-modal";
import { defineNuxtPlugin } from "nuxt/app";

export default defineNuxtPlugin((nuxtApp) => {
  const vfm = createVfm() as any;
  nuxtApp.vueApp.use(vfm);
});
