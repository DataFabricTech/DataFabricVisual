interface User {
  role: string;
  id: string;
  name: string;
  fullyQualifiedName: string;
  displayName: string;
}

export const useUserStore = defineStore("userStore", () => {
  const { $api } = useNuxtApp();

  const user: Ref<User> = ref({
    role: "",
    id: "",
    name: "",
    fullyQualifiedName: "",
    displayName: "",
  });

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
