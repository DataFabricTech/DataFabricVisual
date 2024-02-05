// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  devtools: { enabled: true },
  ssr: false,
  pages: true,
  modules: [
    "@nuxtjs/tailwindcss",
    "@pinia/nuxt",
    // "@nuxtjs/svg-sprite",
    "@nuxtjs/i18n",
    "nuxt-lodash",
    "dayjs-nuxt",
  ],
  components: [
    {
      path: "~/components/extends",
      pathPrefix: false,
    },
  ],
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
    "vue-final-modal/style.css",
    // "~/assets/css/style.scss"
  ],

  lodash: {
    prefix: "$_",
    prefixSkip: ["string"],
    upperAfterPrefix: false,
  },
});
