import ckeditor5 from "@ckeditor/vite-plugin-ckeditor5";
import { consola, type ConsolaInstance } from "consola";
import { fileURLToPath } from "url";
import svgLoader from "vite-svg-loader";

const larkTheme = require.resolve("@ckeditor/ckeditor5-theme-lark");

//  logger 설정 --
function _interopDefaultLegacy(e: ConsolaInstance) {
  return e && typeof e === "object" && "default" in e ? e : {default: e};
}

const consola__default = /*#__PURE__*/ _interopDefaultLegacy(consola);
const logger: any = consola__default["default"];

// -- logger 설정

function _routeAlias(route: any) {
  if (route.path === undefined) {
    return;
  }

  let alias = route.path.length > 1 ? `${route.path}/index.html` : "/index.html";
  route.alias = alias;

  logger.info(route.path + "---------------");
  for (var key in route) {
    if (key !== "children") logger.info("----" + key + " [" + route[key] + "]");
  }
}

// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  app: {
    head: {
      title: 'DATA PORTAL'
    }
  },

  devtools: {enabled: false},

  typescript: {
    shim: false,
    typeCheck: false
  },

  alias: {
    "@functional": fileURLToPath(new URL("./components/functional", import.meta.url)),
    "@organisms": fileURLToPath(new URL("./components/organisms", import.meta.url)),
    "@molecules": fileURLToPath(new URL("./components/molecules", import.meta.url)),
    "@atoms": fileURLToPath(new URL("./components/atoms", import.meta.url)),
    "@store": fileURLToPath(new URL("./store", import.meta.url)),
    "@assets": fileURLToPath(new URL("./assets", import.meta.url)),
    "@utils": fileURLToPath(new URL("./utils", import.meta.url)),
    "@composables": fileURLToPath(new URL("./composables", import.meta.url)),
  },

  components: true,

  build: {
    extend(config) {
      config.performance.hints = false; // 성능 관련 경고 표기 여부
    },
    // parallel: true, // webpack loader를 thread로 실행
    standalone: process.env.ENV_TYPE === "product" ? true : false, // node_modules를 함께 bundle로 묶을지 여부
    postcss: { // css 처리 로더
      preset: {
        features: {
          "custom-properties": false
        }
      }
    },
    html: { // html 파일 축소 플러그인 이용
      minify: {
        minifyCSS: process.env.ENV_TYPE === "product",
        minifyJS: process.env.ENV_TYPE === "product"
      }
    },
    optimization: {
      minimize: process.env.ENV_TYPE === "product"
    },
    cssSourceMap: process.env.ENV_TYPE === "product"
  },

  // 전역 CSS 설정
  css: [
    "~/assets/style-product/main.scss",
    "~/assets/style-product/css/common.css",
    "~/assets/style-product/css/style.css",
    "~/assets/style-product/css/dev.css",
    "vue-final-modal/style.css",
  ],

  nitro: {
    output: {
      publicDir: "../../src/main/resources/static"
    }
  },

  vite: {
    server: {
      proxy: {
        "/api": {
          target: `${process.env.API_URL}`,
          changeOrigin: true
        },
        "/v1/essync": {
          target: `${process.env.SYNC_API_URL}`,
          changeOrigin: true
        }
      }
    },
    optimizeDeps: {
      include: ['vue', 'vuex', 'vue-router'],
      exclude: ['@vue/server-renderer']
    },
    build: {
      rollupOptions: {
        external: ['@vue/server-renderer']
      }
    },
    plugins: [
      ckeditor5({
        theme: larkTheme,
      }),
      svgLoader()
    ]
  },

  plugins: [
    { src: '~/plugins/highcharts-vue.ts'}
  ],

  modules: [
    "@pinia/nuxt",
    "@pinia-plugin-persistedstate/nuxt",
    "nuxt-monaco-editor"],

  piniaPersistedstate: {
    cookieOptions: {
      sameSite: "strict"
    },
    storage: "localStorage"
  },

  hooks: {
    "pages:extend"(routes) {
      // logger.info("## NuxtLink 처리: 정적 리소스에 대한 html 파일 대응을 위해 아래와 같이 route alias 경로를 변경 합니다.");
      routes.forEach((route) => {
        _routeAlias(route);

        // children url 처리
        if (route.children !== undefined) {
          route.children.forEach((child) => {
            _routeAlias(child);
          });
        }
      });
    }
  },

  ssr: false,

  runtimeConfig: {
    public: {
      baseURL: process.env.NUXT_PUBLIC_API_BASE === "development" ? "http://localhost:38080/" : "/",
      API_URL: `${process.env.API_PREFIX}`,
      SYNC_API_URL: `${process.env.SYNC_API_VERSION}${process.env.SYNC_API_PREFIX}`
    }
  },

  compatibilityDate: "2024-07-09" // 해당 날짜를 기준으로 Nuxt의 기능 호환성 고정
});
