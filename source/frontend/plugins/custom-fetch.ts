import {ofetch} from 'ofetch'
import {useRuntimeConfig} from "nuxt/app";

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

      if (data.hasOwnProperty("result") && data.result === 0) {
        errorResponse(data);
      } else if (data instanceof Blob && data.type) {
        const text = await data.text();
        let blobData = data.type.toLowerCase().includes("json") ? JSON.parse(text) : text;
        if (blobData.hasOwnProperty("result") && blobData.result === 0) {
          errorResponse(blobData);
        } else {
          response._data = data;
        }
      }
      response._data = data.data;
    },
  })

  function errorResponse(data: any) {
    let errorMessage = data.errorMessage;
    if (errorMessage === null || errorMessage === "") {
      errorMessage = "시스템 오류가 발생 하였습니다.";
    }
    alert(errorMessage);
  }

  function showLoader() {
    try {
      startLoader();
    } catch (e) {}
  }

  function hideLoader() {
    try {
      finishLoader();
    } catch (e) {}
  }
})
