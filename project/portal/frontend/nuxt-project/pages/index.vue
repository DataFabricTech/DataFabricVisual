<template>
  <div v-if="!shouldRender">
    <Login
      @login="onLogin"
      :login_error="login_error"
      :login_result_message="login_result_message"
    />
  </div>
</template>

<script setup leng="ts">
import { useCookies } from "vue3-cookies";
import RSA from "rsajs";
import { jwtDecode } from "jwt-decode";
import { useUserStore } from "~/store/user";

const { $ofetch } = useNuxtApp();
const router = useRouter();
const { cookies } = useCookies();
const userStore = useUserStore();

const shouldRender = ref("");
const login_error = ref(false);
const login_result_message = ref("");

onMounted(() => {
  sessionStorage.clear();
});

const onLogin = async (param) => {
  try {
    let publicKey = await getPublicKey();
    let password = param.password;
    if (publicKey && publicKey !== "") {
      password = encrypt(publicKey, param.password);
    }

    // Access Token, Refresh Token
    // let tokenObject = await auth(param.username, password); // RSA PRIVATE KEY 오류 탐색중
    let result = await auth(param.username, param.password, param.authType);

    let xAccessToken = result["x-access-token"];
    let xRefreshToken = result["x-refresh-token"];

    // Token 발급 확인
    if (result.login_result_message) {
      login_error.value = true;
      login_result_message.value = result.login_result_message;
      return;
    }

    // Token이 없으면
    if (!xAccessToken || xAccessToken == "") return;
    if (!xRefreshToken || xRefreshToken == "") return;

    // Access Token, Refresh Token이 있으면 로그인
    if (xAccessToken && xAccessToken !== "" && xRefreshToken && xRefreshToken !== "") {
      // Access Token, Refresh Token 쿠키 설정
      cookies.set("x-access-token", xAccessToken);
      cookies.set("x-refresh-token", xRefreshToken);

      // 메뉴 정보
      const { data: result } = await $ofetch("/api/menu");

      // 권한별 메뉴 정보 설정
      const userMenu = result.filter((a) => a.authority == "User" && a.menu_visible);
      const adminMenu = result.filter(
        (a) => (a.authority == "Manager" || a.authority == "Admin") && a.menu_visible
      );

      let decodedToken = jwtDecode(xAccessToken);
      let user = {
        user_id: decodedToken.user_id,
        username: decodedToken.username,
        authority: decodedToken.authority,
        dept_nm: decodedToken.dept_nm,
        last_login: decodedToken.last_login,
        userMenu: userMenu,
        adminMenu: adminMenu,
      };
      let tab = {
        adminPath: "/admin/data/status/lookup",
        userPath: "/search/keyword",
        currentPath: "/",
        currentPathDepth1: "",
        currentPathDepth2: "",
      };
      userStore.setUser(user);
      userStore.setTab(tab);
      router.push("/main");
    }
  } catch (error) {
    console.log(error);
  }
};

const getPublicKey = async () => {
  const { data } = await $ofetch("/api/authenticate/key");
  return data;
};

const auth = async (username, password) => {
  const { data } = await $ofetch("/api/authenticate", {
    method: "POST",
    body: { username: username, password: password },
  });

  return data;
};

const encrypt = (key, source) => {
  const encrypt = new RSA();
  encrypt.setKey(key);
  return encrypt.encrypt(source);
};
</script>
