<template>
  <div v-if="!shouldRender">
    <Login v-if="!showRegisterForm" @login="onLogin" @toggleRegisterForm="toggleRegisterForm" :login_error="login_error"
      :login_result_message="login_result_message" />
    <Register v-else-if="showRegisterForm" @register="onRegister" @toggleRegisterForm="toggleRegisterForm"
      @checkDupUserId="checkDupUserId" @updateCheckedDupUserId="updateCheckedDupUserId"
      :checkedDupUserId="checkedDupUserId" :register_error="register_error"
      :register_result_message="register_result_message" />
  </div>
</template>

<script setup leng="ts">
import { confirmAlert, defaultAlert, errorAlert, successAlert } from "@/components/functional/alert/alert-default";
import Register from "@functional/register/register";
import { jwtDecode } from "jwt-decode";
import RSA from "rsajs";
import { useCookies } from "vue3-cookies";
import { useUserStore } from "~/store/user";

const { $ofetch } = useNuxtApp();
const router = useRouter();
const { cookies } = useCookies();
const userStore = useUserStore();

const shouldRender = ref(false);
const login_error = ref(false);
const login_result_message = ref("");

const showRegisterForm = ref(false)
const register_error = ref(false);
const register_result_message = ref("");
const checkedDupUserId = ref(false)

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
    let result = await auth(param.user_id, param.password, param.authType);

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

const auth = async (user_id, password) => {
  const { data } = await $ofetch("/api/authenticate", {
    method: "POST",
    body: { user_id: user_id, password: password },
  });

  return data;
};

const encrypt = (key, source) => {
  const encrypt = new RSA();
  encrypt.setKey(key);
  return encrypt.encrypt(source);
};

const checkDupUserId = async (reg_userid) => {
  if (reg_userid && reg_userid != "") {
    await $ofetch(`/api/checkDupUserId?reg_userid=${reg_userid}`).then(async (res) => {
      if (res && res.data) {
        if (res.data.register_result == 'Y') {
          await defaultAlert("사용 가능한 아이디입니다.")
          checkedDupUserId.value = true
        } else {
          checkedDupUserId.value = false
          if (res && res.data && res.data.register_result_message) {
            errorAlert(res.data.register_result_message);
          } else {
            errorAlert("아이디 중복확인에 실패하였습니다.")
          }
        }
      } else {
        checkedDupUserId.value = false
        if (res && res.data) {
          register_result_message.value = res.data.register_result_message
        } else {
          register_result_message.value = "아이디 중복확인에 실패하였습니다."
        }
      }
    })
  } else {
    errorAlert("아이디를 입력해주세요.")
  }
}

const updateCheckedDupUserId = () => {
  checkedDupUserId.value = false
}

const onRegister = async (params) => {
  if (!checkedDupUserId.value) {
    errorAlert("아이디 중복확인이 필요합니다.")
    return
  }

  if (await confirmAlert("회원가입을 진행하시겠습니까?")) {
    await $ofetch("/api/register", {
      method: "POST",
      body: params
    }).then(async (res) => {
      if (res && res.data) {
        if (res.data.register_result == 'Y') {
          await successAlert("회원가입이 완료되었습니다.")
          showRegisterForm.value = false
        } else {
          if (res && res.data) {
            register_result_message.value = res.data.register_result_message
          } else {
            register_result_message.value = "회원가입에 실패하였습니다."
          }
        }
      } else {
        if (res && res.data) {
          register_result_message.value = res.data.register_result_message
        } else {
          register_result_message.value = "회원가입에 실패하였습니다."
        }
      }
    })


  }
}

const toggleRegisterForm = () => {
  showRegisterForm.value = !showRegisterForm.value
  shouldRender.vaue = !shouldRender.value
}
</script>
