import { AgGridVue } from 'ag-grid-vue3';
export default defineNuxtPlugin((nuxtApp) => {
  nuxtApp.vueApp.component('ag-grid-vue', AgGridVue);
});
