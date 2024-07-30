import { storeToRefs } from "pinia";
import { useSearchCommonStore } from "../search/common";
import sampleData from "./samples/sampleData.json";

interface DataModel {
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
  const { setSortInfo, getQueryFilter, getTrinoQuery } = searchCommonStore;
  const { sortKey, sortKeyOpt } = storeToRefs(searchCommonStore);

  const recentQuestData: Ref<DataModel[]> = ref([]);
  const bookmarkData: Ref<DataModel[]> = ref([]);
  const upVotesData: Ref<DataModel[]> = ref([]);
  const lastUpdatedData: Ref<DataModel[]> = ref([]);

  const isRecentQuestDataNoInfo: Ref<boolean> = ref(false);
  const isBookmarkDataNoInfo: Ref<boolean> = ref(false);
  const isUpVotesDataNoInfo: Ref<boolean> = ref(false);
  const isLastUpdatedData: Ref<boolean> = ref(false);

  // TODO: [개발] 임시 코드로 추후 삭제 예정
  const SAMPLE_DATA = sampleData.data.data as DataModel[];
  const SLICE_SIZE = 3;

  const dataResult: Ref<any[]> = ref([]);
  const getMainDataListQuery = () => {
    const queryFilter = getQueryFilter();
    const params: any = {
      q: "",
      index: "all",
      from: 0,
      size: 3,
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
    dataResult.value = data["all"];
  };

  const getMainDataListAPI = async () => {
    const { data } = await $api(`/api/search/list?${getMainDataListQuery()}`);
    return data;
  };

  const getDataList = async (
    apiCall: Ref<string | DataModel[]>,
    dataStatus: Ref<boolean>,
    dataList: Ref<DataModel[]>,
  ) => {
    const data: any[] = await apiCall;

    if (data.length === 0) {
      dataStatus.value = true;
    } else {
      dataList.value = data.slice(0, SLICE_SIZE);
    }
  };

  const getRecentQuestData = async () => {
    await getDataList(SAMPLE_DATA, isRecentQuestDataNoInfo, recentQuestData);

    console.log("최근 탐색 데이터 API 불러오기", recentQuestData.value);
  };

  const getUserInfo = async () => {
    const data: any = await $api(`/api/user/info`);
    const userId = data.data.id;

    await getBookmarkData(userId);
  };

  const getBookmarkData = async (id: string) => {
    const data = await $api(`/api/main/follows/${id}`);
    await getDataList(data.data, isBookmarkDataNoInfo, bookmarkData);

    console.log("북마크 한 데이터 API 불러오기", bookmarkData.value);
  };

  const getUpVotesData = async () => {
    await setSortInfo("totalVotes_desc");
    await getMainDataList();
    await getDataList(dataResult.value, isUpVotesDataNoInfo, upVotesData);

    console.log("추천 많은 순 데이터 API 불러오기", upVotesData.value);
  };

  const getLastUpdatedData = async () => {
    await setSortInfo("updatedAt_desc");
    await getMainDataList();
    await getDataList(dataResult.value, isLastUpdatedData, lastUpdatedData);

    console.log("최근 업데이트 데이터 API 불러오기", lastUpdatedData.value);
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
    getRecentQuestData,
    getBookmarkData,
    getUpVotesData,
    getLastUpdatedData,
  };
});
