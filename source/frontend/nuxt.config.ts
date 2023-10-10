// https://nuxt.com/docs/api/configuration/nuxt-config

export default defineNuxtConfig({
  devtools: { enabled: true },
  ssr: false,
  pages: true,
  modules: ["@nuxtjs/tailwindcss", "@pinia/nuxt"],

  // runtime에서 설정된 정보를 조회 가능
  runtimeConfig: {
    public: {
      baseUrl: `${process.env.VITE_BASE_URL}${process.env.VITE_CONTEXT_PATH}`,
    },
  },
  // dev 서버 설정
  devServer: {
    port: process.env.VITE_SERVER_PORT,
    url: `${process.env.VITE_BASE_URL}${process.env.VITE_CONTEXT_PATH}`,
  },

  // context-path 설정
  app: {
    baseURL: `${process.env.VITE_CONTEXT_PATH}`,
  },

  // tailwind css 설정
  tailwindcss: {
    configPath: "tailwind.config",
  },

  // css 설정
  css: ['vue-final-modal/style.css', "~/assets/css/style.scss"],
});
