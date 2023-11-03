import { ofetch } from "ofetch";
import { defineNuxtPlugin, useRuntimeConfig } from "nuxt/app";

export default defineNuxtPlugin((nuxtApp) => {
  const api = ofetch.create({
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
          return;
        }
      }
      response._data = data.data;
    }
  });

  function errorResponse(data: any) {
    let errorMessage = data.errMsg;
    if (errorMessage === null || errorMessage === "") {
      errorMessage = "시스템 오류가 발생 하였습니다.";
    }
    console.error(errorMessage);
    alert(errorMessage);
  }

  function showLoader() {
    try {
      nuxtApp.$loader.start();
    } catch (e) {}
  }

  function hideLoader() {
    try {
      nuxtApp.$loader.finish();
    } catch (e) {}
  }
  return {
    provide: {
      api
    }
  }
});
