import { defineStore } from "pinia";
import { useUserStore } from "~/store/user/userStore";
import { usePagingStore } from "~/store/common/paging";

export const useMyPageStore = defineStore("my-page", () => {
  const { $api } = useNuxtApp();

  const pagingStore = usePagingStore();
  const { setFrom, updateIntersectionHandler } = pagingStore;
  const { from, size } = storeToRefs(pagingStore);

  const userStore = useUserStore();
  // NOTE: 로그인 사용자 정보
  const { user } = storeToRefs(userStore);

  const targetUserInfo = ref([]);
  const targetFqn = ref("");

  const currentTab = ref("myBookMark");
  const searchKeyword = ref("");
  const searchResult: Ref<any[]> = ref([]);
  const previewData: Ref<any> = ref({
    modelInfo: {
      model: {
        name: "",
      },
    },
  });

  const isSearchResultNoData: Ref<boolean> = ref<boolean>(false);
  const isShowPreview: Ref<boolean> = ref<boolean>(false);
  const isBoxSelectedStyle: Ref<boolean> = ref<boolean>(false);

  const changeUserInfo = async (data: any) => {
    let params = {
      loginId: user.value.id,
      op: data.op,
      path: data.path,
      value: data.value,
    };
    await updateTargetUserInfo(params);
  };

  const getTargetUserData = async (value: string) => {
    targetFqn.value = value;

    await $api(`/api/mypage/users/${targetFqn.value}`)
      .then((res: any) => {
        if (res.result === 1) {
          targetUserInfo.value = res.data;
        }
      })
      .catch((err: any) => {
        console.log("err: ", err);
      });
  };

  const updateTargetUserInfo = async (params: any) => {
    await $api(`/api/mypage/users/${targetUserInfo.value.id}`, {
      method: "PATCH",
      body: params,
    });
    await getTargetUserData(targetFqn.value);
  };

  /**
   * 목록 reset
   * 목록을 '갱신'하는 경우, from 값을 항상 0으로 주어야 하기 때문에 fn 하나로 묶어서 처리.
   */
  const resetReloadList = async () => {
    setFrom(0);
    searchKeyword.value = "";
    await getSearchList();
    updateIntersectionHandler(0);
  };

  const changeTab = async (item: string) => {
    currentTab.value = item;
    resetReloadList();
  };

  const search = async () => {
    setFrom(0);
    await getSearchList();
    updateIntersectionHandler(0);
  };

  const clearSearchText = () => {
    searchKeyword.value = "";
  };

  const getPreviewData = async (fqn: string) => {
    const data: any = await $api(`/api/search/preview/${fqn}`);
    previewData.value = data.data;
  };

  const getSearchListQuery = () => {
    let query =
      currentTab.value === "myBookMark"
        ? `*${searchKeyword.value}* AND followers:${targetUserInfo.value.id}`
        : `*${searchKeyword.value}* AND(owner.id:${targetUserInfo.value.id})`;
    const params: any = {
      // open-meta 에서 사용 하는 key 이기 때문에 그대로 사용.
      // eslint 예외 제외 코드 추가.
      // eslint-disable-next-line id-length
      q: query,
      index: "all", // table or storage or model -> tab
      from: from.value,
      size: size.value,
      deleted: false,
      trino_query: null,
    };
    return new URLSearchParams(params);
  };

  const getSearchListAPI = async () => {
    const { data } = await $api(`/api/search/list?${getSearchListQuery()}`, {
      showLoader: false,
    });
    return data;
  };

  /**
   * 데이터 조회 -> 누적
   */
  const addSearchList = async () => {
    const { data } = await getSearchListAPI();
    searchResult.value = searchResult.value.concat(data.all);
  };

  const getSearchList = async () => {
    const { data } = await getSearchListAPI();
    searchResult.value = data.all;
    isSearchResultNoData.value = searchResult.value.length === 0;
  };

  return {
    targetUserInfo,
    searchResult,
    isSearchResultNoData,
    searchKeyword,
    previewData,
    isShowPreview,
    isBoxSelectedStyle,
    currentTab,
    changeTab,
    changeUserInfo,
    addSearchList,
    getTargetUserData,
    updateTargetUserInfo,
    getSearchList,
    getPreviewData,
    search,
    clearSearchText,
  };
});
