import { ofetch } from "ofetch";
import { defineNuxtPlugin, useRuntimeConfig } from "nuxt/app";

export default defineNuxtPlugin((nuxtApp: any) => {
  // NOTY : api 호출하는 코드에서 'showLoader' options 을 추가할 경우
  // FetchContext type 이 아니라고 typescript 오류라고 판단하기 때문에
  // 아래처럼 한번 더 wrap 한다.
  const api = (request: string, options?: any) => {
    return _api(request, options);
  };

  const _api = ofetch.create({
    async onRequest(context) {
      const { options } = context;
      options.baseURL = useRuntimeConfig().public.baseUrl;
      options.credentials = "include";

      const showLoader = (options as any).showLoader ?? true;

      if (showLoader) {
        nuxtApp.$loading.start();
      }
    },
    async onResponse(context) {
      const { response, options } = context;
      const data = response._data;

      const showLoader = (options as any).showLoader ?? true;
      if (showLoader) {
        nuxtApp.$loading.stop();
      }
      if (
        Object.prototype.hasOwnProperty.call(data, "result") &&
        data.result === 0
      ) {
        errorResponse(data);
      } else if (data instanceof Blob && data.type) {
        const text = await data.text();
        const blobData = data.type.toLowerCase().includes("json")
          ? JSON.parse(text)
          : text;
        if (
          Object.prototype.hasOwnProperty.call(blobData, "code") &&
          blobData.code !== 0
        ) {
          errorResponse(blobData);
        }
      }
    },
  });

  function errorResponse(data: any) {
    // TODO : 세션 아웃시 로그아웃 처리 테스트 코드 작성
    console.log("response error log");
    console.log(data);
    let errorMessage = data.errorMessage;
    if (errorMessage === null || errorMessage === "") {
      errorMessage = "시스템 오류가 발생 하였습니다.";
    }
    console.error(errorMessage);
  }

  return {
    provide: {
      api,
    },
  };
});
