import VueDragResize from "vue3-drag-resize";

export default defineNuxtPlugin((nuxtApp) => {
  nuxtApp.vueApp.component("VueDragResize", VueDragResize);
});
