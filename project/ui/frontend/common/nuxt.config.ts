// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  devtools: { enabled: true },

  modules: ['@nuxtjs/storybook', "@nuxtjs/svg-sprite", 'nuxt-lodash', 'dayjs-nuxt'],

  plugins: [
    { src: '~/plugins/highcharts-vue', mode: 'client' },
    { src: '~/plugins/vue-final-modal', mode: 'client' },
    { src: '~/plugins/ag-grid-vue', mode: 'client' },
    { src: "~/plugins/vue-datepicker-next", mode: "client" }
  ],

  components: [
    {
      path: "~/components",
      pathPrefix: false
    },
  ],

  css: [
    '~/assets/css/main.scss',
    'vue-final-modal/style.css',
    'ag-grid-community/dist/styles/ag-grid.css',
    'ag-grid-community/dist/styles/ag-theme-alpine.css'
  ],
  postcss: {
    plugins: {
      tailwindcss: {},
      autoprefixer: {},
    },
  },

  lodash: {
    prefix: '$_', // lodash 함수에 붙일 접두사
    prefixSkip: [],
    upperAfterPrefix: false,
    exclude: [],
    alias: []
  },
  dayjs: {
    locales: ['ko', 'en'],
    plugins: [
      'relativeTime',
      'utc',
      'timezone',
      'timezone',
      'duration',
      'isBetween',
      'IsLeapYear',
      'IsSameOrAfter',
      'IsSameOrBefore',
      'isToday',
      'isTomorrow',
      'IsYesterday',
      'objectSupport',
      'pluralGetSet',
      'toArray',
      'toObject'
    ],
    defaultLocale: "ko",
    defaultTimezone: "Asia/Seoul"
  },

  // svg icon
  svgSprite: {
    // input: "~/assets/images/icon"
  }
})
