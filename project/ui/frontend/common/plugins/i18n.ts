import { createI18n } from "vue-i18n";
import { defineNuxtPlugin } from "#app";

import en from "~/locales/en.json";
import ko from "~/locales/ko.json";

export default defineNuxtPlugin(({ vueApp }) => {
  const i18n = createI18n({
    legacy: false,
    globalInjection: true,
    locale: "ko",
    messages: {
      ko,
      en
    }
  });

  vueApp.use(i18n);
  vueApp.config.globalProperties.gt = vueApp.config.globalProperties.$t;
});
