import { useCookies } from 'vue3-cookies';
import { defaultAlert, errorAlert } from "@/components/functional/alert/alert-default";
import { defineNuxtPlugin, navigateTo, useRuntimeConfig } from 'nuxt/app';

export default defineNuxtPlugin((nuxtApp) => {
  const config = useRuntimeConfig()

  const showLoader = () => { nuxtApp.callHook('page:start') }
  const hideLoader = () => { nuxtApp.callHook('page:finish') }

  const { cookies } = useCookies()

  const $ofetch = $fetch.create({
    baseURL: process.env.NODE_ENV === "development" ? "http://localhost:28080/" : "/",
    credentials: 'include', // 쿠키 자동 전송 설정
    onRequest({ request, options, error }) {
      if (request != '/api/heartbeatToken') {
        console.log("Making request to " + request);
        showLoader()
      }


      const xAccessToken = cookies.get('x-access-token')

      if (xAccessToken) {
        // Add Authorization header
        options.headers = options.headers || {}
        options.headers.authorization = `${xAccessToken}`
      }
    },
    onRequestError({ request, options, error }) {
      console.log("onRequestError")
      // Handle the request errors
    },
    onResponse({ request, response, options }) {
      if (request != '/api/heartbeatToken') {
        hideLoader()
        console.log("onResponse")
      }
      // Process the response data
      let data = response._data;

      if (data.hasOwnProperty("result") && (data.result === 0 || data.result === '0')) {
        let errorMessage = data.errorMessage;
        if (errorMessage === null || errorMessage === "") {
          errorMessage = "시스템 오류가 발생 하였습니다.";
        }
        if(errorMessage === 'expired token') {
          cookies.remove('x-access-token')
          cookies.remove('x-refresh-token')
          defaultAlert("로그인이 만료 되었습니다.", {
            confirm: async () => {
              await new Promise((response) => setTimeout(response, 100));
              return document.location.href = '/logoutNotice'
            }
          });

        }
      }
    },
    onResponseError({ request, response, options }) {
      hideLoader()
      // Handle the response errors
      if (response.status === 401) {
        errorAlert("에러 메세지 입니다", {
          confirm: async () => {
            await new Promise((response) => setTimeout(response, 100));
          }
        });
        return navigateTo('/')
      }
    }
  })
  // Expose to useNuxtApp().$ofetch
  return {
    provide: {
      ofetch: $ofetch
    }
  }
})
