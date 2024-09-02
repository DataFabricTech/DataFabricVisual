import { storeToRefs } from "pinia";
import { useSearchCommonStore } from "../search/common";
import { useQueryHelpers } from "~/composables/queryHelpers";
import sampleData from "./samples/sampleData.json";

interface DataModel {
  type: string;
  id: string | number;
  serviceIcon: string;
  depth: string[];
  firModelNm: string;
  modelNm: string;
  modelDesc: string;
  owner: string;
  category: string;

  [key: string]: string | number | string[];
}

export const useMainStore = defineStore("mainStore", () => {
  const { $api } = useNuxtApp();

  const searchCommonStore = useSearchCommonStore();
  const { setSortInfo, getQueryFilter } = searchCommonStore;
  const { sortKey, sortKeyOpt } = storeToRefs(searchCommonStore);
  const { getTrinoQuery } = useQueryHelpers();

  const recentQuestData: Ref<DataModel[]> = ref([]);
  const bookmarkData: Ref<DataModel[]> = ref([]);
  const upVotesData: Ref<DataModel[]> = ref([]);
  const lastUpdatedData: Ref<DataModel[]> = ref([]);

  const isRecentQuestDataNoInfo: Ref<boolean> = ref(false);
  const isBookmarkDataNoInfo: Ref<boolean> = ref(false);
  const isUpVotesDataNoInfo: Ref<boolean> = ref(false);
  const isLastUpdatedData: Ref<boolean> = ref(false);

  const size: Ref<number> = ref(3);
  const dataResult: Ref<any[]> = ref([]);

  const getMainDataListQuery = () => {
    const queryFilter = getQueryFilter();
    const params: any = {
      q: "",
      index: "all",
      from: 0,
      size: size.value,
      deleted: false,
      query_filter: JSON.stringify(queryFilter),
      sort_field: sortKey.value,
      sort_order: sortKeyOpt.value,
      trino_query: JSON.stringify(getTrinoQuery(queryFilter)),
    };

    return new URLSearchParams(params);
  };

  const getMainDataList = async () => {
    const { data } = await getMainDataListAPI();
    dataResult.value = data;
  };

  const getMainDataListAPI = async () => {
    const { data } = await $api(`/api/main/list?${getMainDataListQuery()}`);
    return data;
  };

  const getDataList = (
    data: any[],
    dataStatus: Ref<boolean>,
    dataList: Ref<DataModel[]>,
  ) => {
    console.log(data);
    if (data === null || data.length === 0) {
      dataStatus.value = true;
    } else {
      dataList.value = data;
    }
  };

  // TODO: [개발] api/user/info 가져오는 store 가 있다면, 추후 그 곳에서 가져와서 id 만 받아오기
  const getUserInfo = async () => {
    const data: any = await $api(`/api/user/info`);
    const userId = data.data.id;

    await getBookmarkData(userId);
  };

  const getBookmarkData = async (id: string) => {
    const data = await $api(`/api/main/follows/${id}`);
    const list: Ref<any[]> = ref(data.data);
    getDataList(list.value, isBookmarkDataNoInfo, bookmarkData);
  };
  const getUpVotesData = async () => {
    setSortInfo("totalVotes_desc");
    size.value = 6;
    await getMainDataList();
    getDataList(dataResult.value, isUpVotesDataNoInfo, upVotesData);
  };

  const getLastUpdatedData = async () => {
    setSortInfo("updatedAt_desc");
    size.value = 3;
    await getMainDataList();
    getDataList(dataResult.value, isLastUpdatedData, lastUpdatedData);
  };

  return {
    recentQuestData,
    bookmarkData,
    upVotesData,
    lastUpdatedData,
    isRecentQuestDataNoInfo,
    isBookmarkDataNoInfo,
    isUpVotesDataNoInfo,
    isLastUpdatedData,
    getUserInfo,
    getUpVotesData,
    getLastUpdatedData,
  };
});
