import {ofetch} from 'ofetch'
import { useRuntimeConfig } from "nuxt/app";
import { ActiveLoader, useLoading } from "vue-loading-overlay";

export default defineNuxtPlugin((nuxtApp) => {
  globalThis.$fetch = ofetch.create({
    async onRequest({ request, options }) {
      showLoader();
      options.baseURL = useRuntimeConfig().public.baseUrl;
      options.credentials = "include";
    },
    async onResponse({ request, response, options }) {
      hideLoader();
      let data = response._data;

      if (data.hasOwnProperty("code") && data.code !== 0) {
        errorResponse(data);
      } else if (data instanceof Blob && data.type) {
        const text = await data.text();
        let blobData = data.type.toLowerCase().includes("json") ? JSON.parse(text) : text;
        if (blobData.hasOwnProperty("code") && blobData.code !== 0) {
          errorResponse(blobData);
        } else {
          response._data = data;
        }
      }
      response._data = data.data;
    },
  })

  function errorResponse(data: any) {
    let errorMessage = data.errMsg;
    if (errorMessage === null || errorMessage === "") {
      errorMessage = "시스템 오류가 발생 하였습니다.";
    }
    console.error(errorMessage)
    // TODO : system alert 사용시 로더 멈춤. 추후 errorAlert(개발) 적용 후 테스트 필요
    // alert(errorMessage);
  }

  const $loading = useLoading();
  let load: ActiveLoader;
  function showLoader() {
    try {
      // load = nuxtApp.$loading.show()
      load = $loading.show();
      console.log(load);
    } catch (e) {}
  }

  function hideLoader() {
    try {
      setTimeout(() => {
        load.hide()
      }, 1000);
    } catch (e) {}
  }
})
