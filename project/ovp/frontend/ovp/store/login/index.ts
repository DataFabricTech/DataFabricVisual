import { defineStore } from "pinia";
import RSA from "rsajs";

export const loginStore = defineStore("login", () => {
  const { $api } = useNuxtApp();

  const publicKey = ref("");
  const isloginSuccess = ref(false);

  const getPublicKey = async () => {
    // @ts-ignore
    publicKey.value = await $api(`/api/auth/login/public-key`);
  };

  const loginReq = async (param: any) => {
    await getPublicKey();
    console.log("publicKey: ", publicKey.value);
    const rsa = new RSA();
    rsa.setKey(publicKey.value);
    param.password = rsa.encrypt(param);

    console.log("param확인: ", param);

    // TODO: 서버 개발 후 주석 해제
    // @ts-ignore
    return (isloginSuccess.value = await $api(`/api/auth/login`, {
      method: "POST",
      body: param,
    })
      .then((res: any) => {
        return res;
      })
      .catch((err: any) => {
        console.log("err: ", err);
      }));

    // 임시 -> 삭제 예정
    //return (isloginSuccess.value = true);
  };

  return {
    getPublicKey,
    loginReq,
  };
});
