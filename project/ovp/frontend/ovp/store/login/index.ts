import { defineStore } from "pinia";
import RSA from "rsajs";

export const loginStore = defineStore("login", () => {
  const { $api } = useNuxtApp();

  const publicKey = ref("");
  const isloginSuccess = ref(false);
  const errorMessage = ref("");

  const getPublicKey = async () => {
    publicKey.value = await $api(`/api/auth/login/public-key`)
      .then((res: any) => {
        return res.data;
      })
      .catch((err: any) => {
        console.log("err: ", err);
      });
  };

  async function getLoginSuccessState(param: any) {
    await getPublicKey();
    const rsa = new RSA();
    rsa.setKey(publicKey.value);
    param.password = rsa.encrypt(param.password);

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
        console.log("err: ", err);
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
    rsa.setKey(publicKey.value);
    param.password = rsa.encrypt(param.password);

    return $api(`/api/auth/sign-up`, {
      method: "POST",
      body: param,
    });
  }

  return {
    publicKey,
    isloginSuccess,
    errorMessage,
    getPublicKey,
    getLoginSuccessState,
    signUpUser,
    checkDuplicateEmail
  };
});
