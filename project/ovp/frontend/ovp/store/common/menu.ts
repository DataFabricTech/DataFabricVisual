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
    // 권한 체크를 위해서 router 쪽에서 먼저 한번 조회하는 경우가 있음
    // 그 경우 메뉴를 중복 조회 하지 않는다.
    if (!_.isEmpty(menuJson.value)) {
      return;
    }
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
