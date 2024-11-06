import HighchartsVue from "highcharts-vue";
import Highcharts from "highcharts";
import Treemap from "highcharts/modules/treemap";
import { defineNuxtPlugin } from "nuxt/app";

export default defineNuxtPlugin((nuxtApp) => {
  Treemap(Highcharts);
  nuxtApp.vueApp.use(HighchartsVue);
});
