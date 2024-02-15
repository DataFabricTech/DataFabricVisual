import DatePicker from "vue-datepicker-next";
import "vue-datepicker-next/index.css";
import "vue-datepicker-next/locale/ko.es";
import "vue-datepicker-next/locale/en.es";

export default defineNuxtPlugin((nuxtApp) => {
  nuxtApp.vueApp.component("DatePicker", DatePicker);
});
