import {ofetch} from 'ofetch'
import {useRuntimeConfig} from "nuxt/app";

export default defineNuxtPlugin((nuxtApp) => {
  globalThis.$fetch = ofetch.create({
    async onRequest({ request, options }) {
      options.baseURL = useRuntimeConfig().public.baseUrl;
      options.credentials = "include";
    },
    async onResponse({ request, response, options }) {
        let data = response._data;

        if (data.hasOwnProperty("result") && data.result === 0) {
          errorResponse(data);
        } else if (data instanceof Blob && data.type) {
          const text = await data.text();
          let blobData = data.type.toLowerCase().includes("json") ? JSON.parse(text) : text;
          if (blobData.hasOwnProperty("result") && blobData.result === 0) {
            errorResponse(blobData);
          } else {
            return Promise.resolve(data);
          }
        }
        // TODO: data 값만 반환해주었지만 response._data값이 반환된다.
        return Promise.resolve(data.data);
    },
  })

  function errorResponse(data: any) {
    let errorMessage = data.errorMessage;
    if (errorMessage === null || errorMessage === "") {
      errorMessage = "시스템 오류가 발생 하였습니다.";
    }
    alert(errorMessage);
  }
})
