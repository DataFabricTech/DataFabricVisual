import { defineStore } from "pinia";
import RSA from "rsajs";

export const loginStore = defineStore("login", () => {
  const { $api } = useNuxtApp();

  let publicKey = "";
  const isPwChangeSuccess = ref(false);
  const isloginSuccess = ref(false);
  const isLinkValid = ref(false);
  const errorMessage = ref("");

  const isDoingLogin: Ref<boolean> = ref(false);

  const getPublicKey = async () => {
    publicKey = await $api(`/api/auth/login/public-key`)
      .then((res: any) => {
        return res.data;
      })
      .catch((err: any) => {
        console.error("err: ", err);
      });
  };

  async function getLoginSuccessState(param: any) {
    await getPublicKey();
    const rsa = new RSA();
    rsa.setKey(publicKey);
    param.password = rsa.encrypt(param.password);
    isDoingLogin.value = true;

    await $api(`/api/auth/login`, {
      method: "POST",
      body: param,
    })
      .then((res: any) => {
        if (res.result === 1) {
          isloginSuccess.value = true;
        } else {
          isloginSuccess.value = false;
          errorMessage.value = res.errorMessage;
        }
      })
      .catch((err: any) => {
        console.error("err: ", err);
      })
      .finally(() => {
        isDoingLogin.value = false;
      });
  }

  async function getPwChangeSuccessState(param: any, uuid: any) {
    await getPublicKey();
    const rsa = new RSA();
    rsa.setKey(publicKey);
    param.newPassword = rsa.encrypt(param.newPassword);
    param.confirmPassword = rsa.encrypt(param.confirmPassword);

    await $api(`/api/auth/login/password/change/${uuid}`, {
      method: "POST",
      body: param,
    })
      .then((res: any) => {
        if (res.result === 1) {
          isPwChangeSuccess.value = true;
        } else {
          isPwChangeSuccess.value = false;
          errorMessage.value = res.errorMessage;
        }
      })
      .catch((err: any) => {
        console.error("err: ", err);
      });
  }

  async function getPwChangeInMypage(param: any) {
    await getPublicKey();
    const rsa = new RSA();
    rsa.setKey(publicKey);
    param.newPassword = rsa.encrypt(param.newPassword);
    param.confirmPassword = rsa.encrypt(param.confirmPassword);

    return $api(`/api/auth/login/password/change`, {
      method: "POST",
      body: param,
    });
  }

  async function getLinkValidState(id: any) {
    await $api(`/api/auth/login/password/change/check-id/${id}`)
      .then((res: any) => {
        isLinkValid.value = res.data;
      })
      .catch((err: any) => {
        console.error("err: ", err);
      });
  }

  async function checkDuplicateEmail(param: any) {
    return $api(`/api/auth/sign-up/check-email`, {
      method: "POST",
      body: param,
    });
  }

  async function signUpUser(param: any) {
    await getPublicKey();
    const rsa = new RSA();
    rsa.setKey(publicKey);
    param.password = rsa.encrypt(param.password);

    return $api(`/api/auth/sign-up`, {
      method: "POST",
      body: param,
    });
  }
  async function sendMailForPasswordReset(param: any) {
    return $api(`/api/auth/login/password/send-mail`, {
      method: "POST",
      body: param,
    });
  }

  return {
    isDoingLogin,
    isloginSuccess,
    isPwChangeSuccess,
    isLinkValid,
    errorMessage,
    getPublicKey,
    getLoginSuccessState,
    getPwChangeSuccessState,
    getPwChangeInMypage,
    getLinkValidState,
    signUpUser,
    checkDuplicateEmail,
    sendMailForPasswordReset,
  };
});
