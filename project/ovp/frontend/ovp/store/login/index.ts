import { defineStore } from "pinia";

export const loginStore = defineStore("login", () => {
  const { $api } = useNuxtApp();

  const publicKey = ref("");
  const isloginSuccess = ref(false);

  const getPublicKey = async () => {
    // @ts-ignore
    return (publicKey.value = await $api(`/api/login/public-key`));
  };

  const loginReq = async (param: any) => {
    // @ts-ignore

    // TODO: 서버 개발되면 주석 해제 예상
    // return isloginSuccess.value = await $api('/api/login', {
    //     method : 'POST',
    //     body: param
    // }).then((res: any) => {
    //     return res;
    // }).catch((err: any) => {
    //     console.log("err: ", err);
    // });

    // 임시 -> 삭제 예정
    return (isloginSuccess.value = true);
  };

  return {
    getPublicKey,
    loginReq,
  };
});
