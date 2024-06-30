import CodeMirror from "vue-codemirror6";

export default defineNuxtPlugin((nuxtApp) => {
  nuxtApp.vueApp.component("code-mirror", CodeMirror);
});
