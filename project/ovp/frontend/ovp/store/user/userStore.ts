import type { Ref } from "vue";

interface User {
  admin: boolean;
  id: string;
  name: string;
  fullyQualifiedName: string;
  displayName: string;
  email: string;
}

export const useUserStore = defineStore("userStore", () => {
  const { $api } = useNuxtApp();

  const user: Ref<User> = ref(<User>{});

  const getUserInfo = async () => {
    const data: any = await $api(`/api/user/info`);

    user.value = data.data;
    console.log("User 정보: ", user.value);
  };

  return {
    user,
    getUserInfo,
  };
});
