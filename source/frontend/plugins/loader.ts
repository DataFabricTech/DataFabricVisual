// import Loader, { ActiveLoader, useLoading } from "vue-loading-overlay";
import { ActiveLoader, LoadingPlugin, useLoading } from "vue-loading-overlay";
import "vue-loading-overlay/dist/css/index.css";

import { h, ref } from "vue";
import { defineNuxtPlugin } from "nuxt/app";

export default defineNuxtPlugin((nuxtApp) => {
  // nuxtApp.vueApp.component("loading", Loader);
  nuxtApp.vueApp.use(LoadingPlugin);

  // slot 테스트
  /*const vnode = h("div", { ariaLive: "assertive", class: "loader loader--lg", role: "alert" }, [
    h(
      "svg",
      { class: "loader__icon", height: "56", viewBox: "0 0 56 56", width: "56", xmlns: "http://www.w3.org/2000/svg" },
      [
        h("path", {
          clipRule: "evenodd",
          d: "M25.5 3C25.5 1.61929 26.6193 0.5 28 0.5C43.1878 0.5 55.5 12.8122 55.5 28C55.5 43.1878 43.1878 55.5 28 55.5C12.8122 55.5 0.5 43.1878 0.5 28C0.5 26.6193 1.61929 25.5 3 25.5C4.38071 25.5 5.5 26.6193 5.5 28C5.5 40.4264 15.5736 50.5 28 50.5C40.4264 50.5 50.5 40.4264 50.5 28C50.5 15.5736 40.4264 5.5 28 5.5C26.6193 5.5 25.5 4.38071 25.5 3Z",
          fillRule: "evenodd"
        })
      ]
    )
  ]);*/

  const loading = useLoading(
    {
      // 디자인 세팅
      // color: '#000000',
      // loader: 'spinner',
      // width: 64,
      // height: 64,
      // backgroundColor: '#ffffff',
      // opacity: 0.5,
      // zIndex: 999,
    },
    {
      // slot 세팅
      // default: h(Loader),
      // default: vnode
    }
  );

  const count = ref(0);
  let load: ActiveLoader;
  function start() {
    console.log("LOADER START - " + count.value);
    if (count.value === 0) {
      console.log("LOADER show - " + count.value);
      load = loading.show();
    }
    count.value++;
  }

  function finish() {
    console.log("LOADER finish - " + count.value);
    count.value--;
    if (count.value > 0) {
      return;
    }
    setTimeout(() => {
      console.log("LOADER HIDE - " + count.value);
      load.hide();
      count.value = 0;
    }, 1000);
  }

  let loader = {
    loading,
    start,
    finish
  };

  return {
    provide: {
      loader
    }
  };
});
