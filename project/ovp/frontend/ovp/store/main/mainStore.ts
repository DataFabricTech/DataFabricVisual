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
  // const { $api } = useNuxtApp();

  const searchCommonStore = useSearchCommonStore();
  const { getSearchList, setSortInfo } = searchCommonStore;
  const { searchResult } = storeToRefs(searchCommonStore);

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

  const getDataList = async (
    apiCall: Ref<string | DataModel[]>,
    dataStatus: Ref<boolean>,
    dataList: Ref<DataModel[]>,
  ) => {
    let data: any[] = [];

    if (typeof apiCall === "object") {
      data = await apiCall;
    } else {
      // TODO[개발] API 개발 후 적용 예정
      // data = await $api(`${apiCall}`);
      console.log("api 전달");
    }

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

  const getBookmarkData = async () => {
    await getDataList(
      "/api/search/filters",
      isBookmarkDataNoInfo,
      bookmarkData,
    );

    console.log("북마크 한 데이터 API 불러오기", bookmarkData.value);
  };

  const getUpVotesData = async () => {
    await setSortInfo("totalVotes_desc");
    await getSearchList();
    await getDataList(searchResult.value, isUpVotesDataNoInfo, upVotesData);

    console.log("추천 많은 순 데이터 API 불러오기", upVotesData.value);
  };

  const getLastUpdatedData = async () => {
    await setSortInfo("updatedAt_desc");
    await getSearchList();
    await getDataList(searchResult.value, isLastUpdatedData, lastUpdatedData);

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
    getRecentQuestData,
    getBookmarkData,
    getUpVotesData,
    getLastUpdatedData,
  };
});
