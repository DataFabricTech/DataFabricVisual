// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  devtools: { enabled: true },
  ssr: false,
  modules: ["@nuxtjs/svg-sprite", "nuxt-lodash", "dayjs-nuxt", "@nuxtjs/tailwindcss", "@pinia/nuxt"],

  plugins: [
    { src: "~/plugins/highcharts-vue", mode: "client" },
    { src: "~/plugins/vue-final-modal", mode: "client" },
    { src: "~/plugins/ag-grid-vue", mode: "client" },
    { src: "~/plugins/vue-datepicker-next", mode: "client" },
    { src: "~/plugins/code-mirror", mode: "client" },
    { src: "~/plugins/vue3-tooltip", mode: "client" }
  ],

  components: [
    {
      path: "~/components",
      pathPrefix: false
    }
  ],

  css: [
    "~/assets/css/main.scss",
    "vue-final-modal/style.css",
    "ag-grid-community/dist/styles/ag-grid.css",
    "ag-grid-community/dist/styles/ag-theme-alpine.css",
    "@fortawesome/fontawesome-svg-core/styles.css",
    "vue3-tooltip/tooltip.css"
  ],

  tailwindcss: {
    cssPath: false,
    exposeConfig: true,
    viewer: true
  },

  lodash: {
    prefix: "$_", // lodash 함수에 붙일 접두사
    prefixSkip: [],
    upperAfterPrefix: false,
    exclude: [],
    alias: []
  },

  dayjs: {
    locales: ["ko", "en"],
    plugins: [
      "relativeTime",
      "utc",
      "timezone",
      "timezone",
      "duration",
      "isBetween",
      "IsLeapYear",
      "IsSameOrAfter",
      "IsSameOrBefore",
      "isToday",
      "isTomorrow",
      "IsYesterday",
      "objectSupport",
      "pluralGetSet",
      "toArray",
      "toObject"
    ],
    defaultLocale: "ko",
    defaultTimezone: "Asia/Seoul"
  },

  // svg icon
  svgSprite: {
    input: "~/assets/images/icon"
  },

  pinia: {
    autoImports: [
      // automatically imports `defineStore`
      "defineStore", // import { defineStore } from 'pinia'
      ["defineStore", "definePiniaStore"] // import { defineStore as definePiniaStore } from 'pinia'
    ]
  },

  compatibilityDate: "2024-08-08"
});
