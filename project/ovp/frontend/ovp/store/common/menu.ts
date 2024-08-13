import _ from "lodash";

interface MenuItem {
  id: string;
  label: string;
  linkTo: string;
  iconName: string;
}

export const useMenuStore = defineStore("menuStore", () => {
  const { $api } = useNuxtApp();

  const headerUrl = ref<string>("");

  const menuJson = ref<MenuItem[]>([]);
  const mgmtMenuJson = ref<MenuItem | Record<string, never>>({});

  const getMenuData = async () => {
    const { data } = await $api(`/api/menu/list`);
    mgmtMenuJson.value = _.last(data) ?? {};
    menuJson.value = _.initial(data);
  };

  const setHeaderUrl = (newUrl: string) => {
    headerUrl.value = newUrl;
  };

  return {
    menuJson,
    mgmtMenuJson,
    headerUrl,
    getMenuData,
    setHeaderUrl,
  };
});
