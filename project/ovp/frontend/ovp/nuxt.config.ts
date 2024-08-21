// https://nuxt.com/docs/api/configuration/nuxt-config
import { fileURLToPath } from "url";

export default defineNuxtConfig({
  devtools: { enabled: true },
  ssr: false,
  nitro: {
    output: {
      publicDir: fileURLToPath(
        new URL("../../server/src/main/resources/static", import.meta.url),
      ),
    },
  },

  modules: [
    "@nuxtjs/tailwindcss",
    "@pinia/nuxt",
    "@nuxtjs/svg-sprite",
    "nuxt-lodash",
    "dayjs-nuxt",
  ],

  // runtime에서 설정된 정보를 조회 가능
  runtimeConfig: {
    public: {
      baseUrl: `${process.env.VITE_BASE_URL}${process.env.VITE_CONTEXT_PATH}`,
    },
  },

  // dev 서버 설정
  devServer: {
    port: process.env.VITE_SERVER_PORT as unknown as number,
    url: `${process.env.VITE_BASE_URL}${process.env.VITE_CONTEXT_PATH}`,
  },

  app: {
    // context-path 설정
    //   baseURL: `${process.env.VITE_CONTEXT_PATH}`,
    // meta 정보 설정
    head: {
      meta: [
        { name: "viewport", content: "width=device-width, initial-scale=1" },
        { hid: "description", name: "description", content: "데이터 패브릭" },
        { charset: "utf-8" },
      ],
      // link: [],
      // style: [],
      // script: [],
      // noscript: []
    },
  },

  components: [
    {
      path: "~/components",
      pathPrefix: false,
    },
  ],

  alias: {
    // common 컴포넌트 별칭
    "@extends": fileURLToPath(
      new URL("../common/components/extends", import.meta.url),
    ),
    "@base": fileURLToPath(
      new URL("../common/components/base", import.meta.url),
    ),
    // common 디자인 요소 별칭
    "@assets": fileURLToPath(new URL("../common/assets", import.meta.url)),

    // public 폴더 경로
    "@": fileURLToPath(new URL("./", import.meta.url)),
    "@assetsPublic": fileURLToPath(new URL("./assets", import.meta.url)),
  },

  // svg icon
  svgSprite: {
    input: "~/assets/images/icon",
  },

  // tailwind css 설정
  tailwindcss: {
    configPath: "tailwind.config",
  },

  // css 설정
  css: [
    "~/assets/css/main.scss",
    "vue-final-modal/style.css",
    "ag-grid-community/dist/styles/ag-grid.css",
    "ag-grid-community/dist/styles/ag-theme-alpine.css",
    "vue3-tooltip/tooltip.css",
  ],

  lodash: {
    prefix: "$_",
    prefixSkip: ["string"],
    upperAfterPrefix: false,
  },

  pinia: {
    autoImports: [
      // automatically imports `defineStore`
      "defineStore", // import { defineStore } from 'pinia'
      ["defineStore", "definePiniaStore"], // import { defineStore as definePiniaStore } from 'pinia'
    ],
  },

  compatibilityDate: "2024-07-25",
});
