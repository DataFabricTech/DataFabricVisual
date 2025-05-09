import { defineNuxtPlugin } from "nuxt/app";
import { Quasar, QSplitter } from "quasar";

import "assets/quasar/index.sass";

export default defineNuxtPlugin((nuxtApp) => {
  nuxtApp.vueApp.use(Quasar, {
    components: {
      QSplitter, // 필요하면 다른 Quasar 컴포넌트 추가 가능
    },
  });
});