import { defineNuxtPlugin } from "nuxt/app";
import { Quasar, QSplitter, QAvatar } from "quasar";

import "assets/quasar/index.sass";

export default defineNuxtPlugin((nuxtApp) => {
  nuxtApp.vueApp.use(Quasar, {
    components: {
      // 필요하면 다른 Quasar 컴포넌트 추가 가능
      QSplitter,
      QAvatar
    },
  });
});