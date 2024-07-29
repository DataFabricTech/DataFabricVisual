import { ofetch } from "ofetch";
import { defineNuxtPlugin, useRuntimeConfig } from "nuxt/app";

export default defineNuxtPlugin(() => {
  const api = ofetch.create({
    async onRequest({ options }) {
      // TODO: 로더 추가
      options.baseURL = useRuntimeConfig().public.baseUrl;
      options.credentials = "include";
    },
    async onResponse({ response }) {
      // TODO: 로더 추가
      const data = response._data;
      if (Object.prototype.hasOwnProperty.call(data, "result") && data.result === 0) {
        errorResponse(data);
      } else if (data instanceof Blob && data.type) {
        const text = await data.text();
        const blobData = data.type.toLowerCase().includes("json") ? JSON.parse(text) : text;
        if (Object.prototype.hasOwnProperty.call(blobData, "code") && blobData.code !== 0) {
          errorResponse(blobData);
        }
      }
    }
  });

  function errorResponse(data: any) {
    let errorMessage = data.errorMessage;
    if (errorMessage === null || errorMessage === "") {
      errorMessage = "시스템 오류가 발생 하였습니다.";
    }
    console.error(errorMessage);
  }

  return {
    provide: {
      api
    }
  };
});
