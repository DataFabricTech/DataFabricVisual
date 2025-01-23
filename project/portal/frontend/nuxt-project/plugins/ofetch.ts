import { jwtDecode } from "jwt-decode"
import { useCookies } from 'vue3-cookies';
import { defaultAlert, errorAlert } from "@/components/functional/alert/alert-default";
import { defineNuxtPlugin, navigateTo, useRuntimeConfig } from 'nuxt/app';
import { useUserStore } from '~/store/user';

export default defineNuxtPlugin((nuxtApp) => {
  const config = useRuntimeConfig()

  const showLoader = () => { nuxtApp.callHook('page:start') }
  const hideLoader = () => { nuxtApp.callHook('page:finish') }

  const { cookies } = useCookies()
  const userStore = useUserStore();

  const $ofetch = $fetch.create({
    baseURL: process.env.NODE_ENV === "development" ? "http://localhost:38080/" : "/",
    credentials: 'include', // 쿠키 자동 전송 설정
    onRequest({ request, options, error }) {
      if (request != '/api/heartbeatToken') {
        console.log("Making request to " + request);
        showLoader()
      }

      // Token
      const xAccessToken = cookies.get("x-access-token");
      const xRefreshToken = cookies.get("x-refresh-token");

      if (xAccessToken) {
        // Add Authorization header
        options.headers = options.headers || {}
        options.headers.authorization = `${xAccessToken}`
      }

      if (xAccessToken && xRefreshToken && !request.includes('/api/menu')) {
        // Token 만료 시간 계산
        let xAccessTokenLeftTime = getTokenExp(xAccessToken)
        let xRefreshTokenLeftTime = getTokenExp(xRefreshToken)

        // 1. Access Token 만료된 경우
        if (xAccessTokenLeftTime <= 60) {
          // 1.1. Refresh Token 만료된 경우
          if (xRefreshTokenLeftTime <= 0) {
            clearSession('Y')
          }
        }
      }
    },
    onRequestError({ request, options, error }) {
      console.log("onRequestError")
      // Handle the request errors
    },
    onResponse({ request, response, options }) {
      if (!request.includes('heartbeatToken')) {
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
          navigateTo('/?err=expired');
        }
      } else {
        if (data.hasOwnProperty("result")
            && !request.includes('/api/authenticate') 
            && !request.includes('/api/regenerateRefreshToken') 
            && !request.includes('/api/regenerateAccessToken') 
            && !request.includes('/api/menu')
            && !request.includes('/api/heartbeatToken')) {

          let errorMessage = data.errorMessage;
          if (errorMessage === null || errorMessage === "") {
            let currentUserId = "";
            
            if (userStore.getUser) {
              if (userStore.getUser.user_id && userStore.getUser.user_id != "") {
                currentUserId = userStore.getUser.user_id

                // Refresh Token 갱신 시도
                regenerateRefreshToken(currentUserId).then(() => {
                }).catch(() => {
                  // 갱신 실패 시 로그아웃 처리
                  navigateTo('/?err=expired');
                });

                // Acess Token 갱신 시도
                regenerateAccessToken(currentUserId).then(() => {
                }).catch(() => {
                  // 갱신 실패 시 로그아웃 처리
                  navigateTo('/?err=expired');
                });
              }
            }
          }
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

  // 토큰(refresh token) 갱신 처리 함수
  const regenerateRefreshToken = async (id: string) => {
    const refreshToken = cookies.get('x-refresh-token');
    const body = {
      username: id,
    }
    if (refreshToken) {
      try {
        const response = await $ofetch("/api/regenerateRefreshToken", {
          method: 'POST',
          body: body
        });

        if(response){
          cookies.set('x-refresh-token', response.data);
          return true;
        }
      } catch (error) {
        console.error("Failed to regenerate refresh tokens: ", error);
        throw new Error("Refresh token regeneration failed");
      }
    } else {
      throw new Error("No refresh token available");
    }
  }

  // 토큰(acceess token) 갱신 처리 함수
  const regenerateAccessToken = async (id: string) => {
    const accessToken = cookies.get('x-access-token');
    const body = {
      username: id,
    }
    if (accessToken) {
      try {
        const response = await $ofetch("/api/regenerateAccessToken", {
          method: 'POST',
          body: body
        });

        if(response){
          cookies.set('x-access-token', response.data);
          return true;
        }
      } catch (error) {
        console.error("Failed to regenerate access tokens: ", error);
        throw new Error("Access token regeneration failed");
      }
    } else {
      throw new Error("No refresh token available");
    }
  }

  // // Token 만료시간 조회
  const getTokenExp = (token: string) => {
    let decodedToken = jwtDecode(token);

    const now = Math.floor(new Date().getTime() / 1000);
    let tokenLeftTime = decodedToken.exp - now

    return tokenLeftTime
  }

  // 세션 초기화
  const clearSession = async (isTokenExpireYn) => {
    if (cookies.keys().includes('x-access-token')) cookies.remove('x-access-token');
    if (cookies.keys().includes('x-refresh-token')) cookies.remove('x-refresh-token');
    let user = {
      user_id: "",
      username: "",
      authority: "",
      dept_id: "",
      dept_nm: "",
      last_login: "",
      user_email: "",
      userMenu: [],
      adminMenu: [],
    };
    userStore.setUser(user);
    
    if (isTokenExpireYn === 'Y') {
      return navigateTo('/?err=expired')
    }else{
      return navigateTo('/')
    }
  }

  // Expose to useNuxtApp().$ofetch
  return {
    provide: {
      ofetch: $ofetch
    }
  }
})
