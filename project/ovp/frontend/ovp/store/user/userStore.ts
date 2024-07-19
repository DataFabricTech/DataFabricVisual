interface User {
  role: string;
  id: string;
  name: string;
  fullyQualifiedName: string;
  displayName: string;
}

import userJson from "./samples/user.json";

export const useUserStore = defineStore("userStore", () => {
  const user: Ref<User> = ref({
    role: "",
    id: "",
    name: "",
    fullyQualifiedName: "",
    displayName: "",
  });

  const getUserInfo = async () => {
    // TODO: [개발] API 전달 받으면 변경 필요
    // const data: any = await $api(`/api/search/preview/${fqn}`);
    const data: any = await userJson;

    user.value = data;
    console.log("User 정보: ", user.value);
  };

  return {
    user,
    getUserInfo,
  };
});
