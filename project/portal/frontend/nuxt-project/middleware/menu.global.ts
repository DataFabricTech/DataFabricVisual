import { jwtDecode } from "jwt-decode"
import { useCookies } from 'vue3-cookies';
import { useUserStore } from '~/store/user';
import { defaultAlert, errorAlert } from "@/components/functional/alert/alert-default";
import { navigateTo } from "nuxt/app";

export default defineNuxtRouteMiddleware((to, from) => {
  const { $ofetch } = useNuxtApp()
  const { cookies } = useCookies()
  const userStore = useUserStore()

  // 파라미터를 제거한 경로를 가져오는 함수
  const getPathWithoutParams = (path) => {
    const url = new URL(path, window.location.origin);
    return url.pathname.replace(/\/$/, '');
  };

  // 권한 체크
  const searchMenuPath = (roleMenuList : List) => {
    let isMenuExists = false;

    for (const menu of roleMenuList) {
      if (getPathWithoutParams(menu.menu_path) === getPathWithoutParams(to.path)) {
        isMenuExists = true;
        return true;
      }

      if (menu.child && menu.child.length > 0) {
        if (searchMenuPath(menu.child)) {
          return true;
        }
      }
    }

    return isMenuExists;
  };

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

  // Token 만료시간 조회
  const getTokenExp = (token: string) => {
    let decodedToken = jwtDecode(token);

    const now = Math.floor(new Date().getTime() / 1000);
    let tokenLeftTime = decodedToken.exp - now

    return tokenLeftTime
  }

  // Refresh Token 재발급
  const regenerateRefreshToken = async (id: string) => {
    const body = {
      username: id,
    }

    let { data: resultRefreshToken } = await $ofetch("/api/regenerateRefreshToken", {
      method: 'POST',
      body: body
    });

    cookies.set('x-refresh-token', resultRefreshToken);
  }

  // Access Token 재발급
  const regenerateAccessToken = async (id: string) => {
    const body = {
      username: id,
    }

    let { data: resultAccessToken } = await $ofetch("/api/regenerateAccessToken", {
      method: 'POST',
      body: body
    });

    cookies.set('x-access-token', resultAccessToken);
  }

  const getMenu = async () => {
    try {
      let { data: result } = await $ofetch('/api/menu')

      // 메뉴 정보가 없는 경우
      if (!result || result.length == 0) {
        await defaultAlert('로그아웃 되었습니다. 다시 로그인 해주시기 바랍니다.')
        clearSession()
      }

      // Token
      let xAccessToken = cookies.get("x-access-token");
      let xRefreshToken = cookies.get("x-refresh-token");

      if (xAccessToken && xRefreshToken) {
        // Token 만료 시간 계산
        let xAccessTokenLeftTime = getTokenExp(xAccessToken)
        let xRefreshTokenLeftTime = getTokenExp(xRefreshToken)

        // 1. Access Token 만료된 경우
        if (xAccessTokenLeftTime <= 60) {
          // 1.1. Refresh Token 만료된 경우
          if (xRefreshTokenLeftTime <= 0) {
            // await defaultAlert("장시간 활동이 없어 자동으로 로그아웃되었습니다.");
            clearSession('Y')
          }
          // 1.2. Refresh Token 만료되지 않은 경우
          else {
            let currentUserId = ""
            if (userStore.getUser) {
              if (userStore.getUser.user_id && userStore.getUser.user_id != "") {
                // Access Token 재발급
                currentUserId = userStore.getUser.user_id
                regenerateAccessToken(currentUserId)

                // 1.2.1. Refresh Token 만료가 임박한 경우 갱신 (10분 남음)
                if (xRefreshTokenLeftTime < 10*60) {
                  regenerateRefreshToken(currentUserId)
                }
              } else {
                await errorAlert("인증 오류가 발생하였습니다.. 다시 로그인 해주시기 바랍니다.");
                clearSession();
              }
            }
          }
        }
        else { // 2. Access Token이 만료되지 않은 경우
          // 2-1. Access Token 갱신
          let currentUserId = jwtDecode(cookies.get('x-access-token')).user_id
          regenerateAccessToken(currentUserId);

          // 1.2.1. Refresh Token 만료가 임박한 경우 갱신 (30분 남음)
          if (xRefreshTokenLeftTime < 30 * 60) {
            regenerateRefreshToken(currentUserId)
          }
        }

        let decodedToken = jwtDecode(cookies.get('x-access-token'));

        // 권한별 메뉴 정보 설정
        const userMenu = result[0].roleMenuList.filter((a: { authority: string; menu_visible: boolean; }) => a.authority == "User" && a.menu_visible);
        const adminMenu = result[0].roleMenuList.filter((a: { authority: string; menu_visible: boolean; }) => (a.authority == "Manager" || a.authority == "Admin") && a.menu_visible);

        let decodedRoleMenuList = result[1].decodeRoleMenuList;

        let user = {
          user_id: decodedToken.user_id,
          username: decodedToken.username,
          authority: decodedToken.authority,
          dept_id: decodedToken.dept_id,
          dept_nm: decodedToken.dept_nm ? (decodedToken.dept_nm != "null" ? decodedToken.dept_nm : "부서없음") : "부서없음",
          last_login: decodedToken.last_login,
          user_email: decodedToken.user_email,
          user_role_name: decodedToken.user_role_name,
          userMenu: userMenu,
          adminMenu: adminMenu,
          roleMenu: decodedRoleMenuList
        };

        userStore.setUser(user);
      } else {
        await clearSession()
      }

    } catch (error) {
      cookies.remove('x-access-token');
      cookies.remove('x-refresh-token');
    }
  }

  // 실행 코드 시작 부분
  userStore.setFromPath(from.path) // 이전경로 저장
  // 공지사항, 데이터 요청 상세페이지의 배너 배경 제거 flag
  if (to.path != '/search/detail' && to.path.endsWith('/detail')) {
    userStore.setIsDetailBannerVisible(true)
  } else {
    userStore.setIsDetailBannerVisible(false)
  }

  // 라우팅 시 권한 확인 로직
  if (to.path.startsWith('/admin')) {
    if (userStore.getUser.authority != 'Super-Admin' && userStore.getUser.authority != 'Manager' && userStore.getUser.authority != 'Admin') {
      errorAlert('권한이 없습니다.')
      return navigateTo('/main')
    }
  }

  if (to.path === '/') {
    if(cookies.get('x-access-token') && cookies.get('x-refresh-token')) {
      return navigateTo('/main')
    } else {
      clearSession()
    }
  } else if (to.path === '/main') {
    // cookies.set('redirected', 'false', { path: '/' });  // 리디렉션을 기록
    getMenu()
  } else if (to.path === '/error') {
    console.log('에러 페이지')
  } else {
    if(userStore._user.roleMenu && userStore._user.roleMenu != ""){
      let decodedRoleMenuList = jwtDecode(userStore._user.roleMenu);

      if(!searchMenuPath(decodedRoleMenuList.roleMenuList)){
        errorAlert('접근 권한이 없습니다.');
        // cookies.set('redirected', 'true', { path: '/' });  // 리디렉션을 기록
        return navigateTo('/main')
      }
    }
  }

})
