import { usePagingStore } from "~/store/common/paging";

interface User {
  role: string;
  id: string;
  name: string;
  fullyQualifiedName: string;
  displayName: string;
}

const defaultUser: User = {
  role: "",
  id: "",
  name: "",
  fullyQualifiedName: "",
  displayName: "",
};

export const useUserStore = defineStore("userStore", () => {
  const { $api } = useNuxtApp();
  const pagingStore = usePagingStore();
  const { setFrom, updateIntersectionHandler } = pagingStore;
  const { from, size } = storeToRefs(pagingStore);

  // Data
  const user: Ref<User> = ref(defaultUser);
  const userList: Ref<any[]> = ref([]);

  // List Query Data
  let searchKeyword: string = "";

  const setSearchKeyword = (keyword: string) => {
    searchKeyword = keyword;
  };

  const getUserInfo = async () => {
    const data: any = await $api(`/api/user/info`);

    user.value = data.data;
    console.log("User 정보: ", user.value);
  };

  const getUserListQuery = () => {
    const params: any = {
      // open-meta 에서 사용 하는 key 이기 때문에 그대로 사용.
      q: searchKeyword,
      index: "user_search_index",
      from: from.value,
      size: size.value,
      isBot: false,
    };

    return new URLSearchParams(params);
  };

  const getUserListAPI = async () => {
    // TODO: API 구현 완료 되면 주석 처리 제거 및 테스트 데이터 조회 코드 삭제
    /*const { data } = await $api(`/api/user/list?${getUserListQuery()}`, {
      showLoader: false,
    });

    return data;*/

    interface RowData {
      id: number;
      name: string;
      displayName: null | string;
      isAdmin: boolean;
      description: string;
    }
    const rowData: RowData[] = [];
    for (let i = 0; i < 20; i++) {
      rowData.push({
        id: i,
        name: `user${i}`,
        displayName: i % 10 === 0 ? null : "홍길동",
        isAdmin: i % 3 === 0 ? true : false,
        description: "사용자에 대한 설명입니다. ".repeat(7),
      });
    }

    return rowData;
  };

  /**
   * 사용자 정보 조회 -> 갱신
   */
  const getUserList = async () => {
    setFrom(0);
    const data = await getUserListAPI();
    userList.value = data;
    updateIntersectionHandler(0);
  };

  /**
   * 사용자 정보 조회 -> 누적
   */
  const addUserList = async () => {
    const data = await getUserListAPI();
    userList.value = userList.value.concat(data);
  };

  /**
   * 사용자 정보 삭제
   */
  const deleteUser = async (id: string) => {
    await $api(`/api/user/${id}`, {
      method: "DELETE",
    });
  };

  return {
    user,
    userList,
    setSearchKeyword,
    getUserInfo,
    getUserList,
    addUserList,
    deleteUser,
  };
});
