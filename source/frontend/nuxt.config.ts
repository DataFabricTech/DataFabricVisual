// https://nuxt.com/docs/api/configuration/nuxt-config
import { resolve } from "path";

export default defineNuxtConfig({
  devtools: { enabled: true },
  ssr: false,
  pages: true,
  modules: ["@nuxtjs/tailwindcss", "@pinia/nuxt", "@nuxtjs/svg-sprite", "nuxt-lodash", "dayjs-nuxt"],
  components: [
    {
      path: "~/components/project",
      extensions: ["/vue"],
      pathPrefix: false
    },
    {
      path: "~/components/",
      extensions: ["/vue"]
    }
  ],

  // runtime에서 설정된 정보를 조회 가능
  runtimeConfig: {
    public: {
      baseUrl: `${process.env.VITE_BASE_URL}${process.env.VITE_CONTEXT_PATH}`
    }
  },
  // dev 서버 설정
  devServer: {
    port: process.env.VITE_SERVER_PORT as unknown as number,
    url: `${process.env.VITE_BASE_URL}${process.env.VITE_CONTEXT_PATH}`
  },

  app: {
    // context-path 설정
    //   baseURL: `${process.env.VITE_CONTEXT_PATH}`,
    // meta 정보 설정
    head: {
      meta: [
        { name: "viewport", content: "width=device-width, initial-scale=1" },
        { hid: "description", name: "description", content: "데이터 패브릭" },
        { charset: "utf-8" }
      ]
      // link: [],
      // style: [],
      // script: [],
      // noscript: []
    }
  },

  // svg icon
  svgSprite: {
    input: "~/assets/images/icon"
  },

  // tailwind css 설정
  tailwindcss: {
    configPath: "tailwind.config"
  },

  // css 설정
  css: ["vue-final-modal/style.css", "~/assets/css/style.scss"],
  lodash: {
    prefix: "_",
    prefixSkip: ["string"],
    upperAfterPrefix: false
  }
});
