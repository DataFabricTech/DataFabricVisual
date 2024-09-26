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
    const { data }: { data: MenuItem[] } = await $api(`/api/menu/list`);

    const lastItem = _.last(data);
    const hasMgmtMenu = lastItem?.linkTo === "/portal/manage";

    mgmtMenuJson.value = hasMgmtMenu ? lastItem : {};
    menuJson.value = hasMgmtMenu ? _.initial(data) : data;
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
