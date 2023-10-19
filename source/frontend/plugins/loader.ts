import Loader from "vue-loading-overlay";
import {useLoading} from "vue-loading-overlay";
import loader from "../components/global/loader/loader.vue";
import "vue-loading-overlay/dist/css/index.css";

export default defineNuxtPlugin((nuxtApp) => {
  nuxtApp.vueApp.component("loading", Loader);
  // const loading = useLoading(
  //   // { container: loader }
  // );
  // return {
  //   provide: {
  //     loading
  //   }
  // }
});
