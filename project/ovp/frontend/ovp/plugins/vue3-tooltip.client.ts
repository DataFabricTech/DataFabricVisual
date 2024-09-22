import { TooltipDirective, TooltipComponent } from "vue3-tooltip";

export default defineNuxtPlugin((nuxtApp) => {
  nuxtApp.vueApp.directive("tooltip", TooltipDirective);
  nuxtApp.vueApp.component("tooltip", TooltipComponent);
});
