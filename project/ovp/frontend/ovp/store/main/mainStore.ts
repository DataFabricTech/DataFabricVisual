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

  const recentQuestData: Ref<DataModel[]> = ref([]);
  const bookmarkData: Ref<DataModel[]> = ref([]);
  const upVotesData: Ref<DataModel[]> = ref([]);
  const myData: Ref<DataModel[]> = ref([]);

  const isRecentQuestDataNoInfo: Ref<boolean> = ref(false);
  const isBookmarkDataNoInfo: Ref<boolean> = ref(false);
  const isUpVotesDataNoInfo: Ref<boolean> = ref(false);
  const isMyDataNoInfo: Ref<boolean> = ref(false);

  // TODO: [개발] 임시 코드로 추후 삭제 예정
  const SAMPLE_DATA = sampleData.data.data as DataModel[];
  const SLICE_SIZE = 3;

  const getDataList = async (
    apiCall: Ref<DataModel[]>,
    dataStatus: Ref<boolean>,
    dataList: Ref<DataModel[]>,
  ) => {
    // TODO[개발] API 개발 후 적용 예정
    // const data: any = await $api(`${apiCall}`);
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

  const getBookmarkData = async () => {
    await getDataList(SAMPLE_DATA, isBookmarkDataNoInfo, bookmarkData);

    console.log("북마크 한 데이터 API 불러오기", bookmarkData.value);
  };

  const getUpVotesData = async () => {
    await getDataList(SAMPLE_DATA, isUpVotesDataNoInfo, upVotesData);

    console.log("추천 많은 데이터 API 불러오기", upVotesData.value);
  };

  const getMyData = async () => {
    await getDataList(SAMPLE_DATA, isMyDataNoInfo, myData);

    console.log("나의 데이터 API 불러오기", myData.value);
  };

  return {
    recentQuestData,
    bookmarkData,
    upVotesData,
    myData,
    isRecentQuestDataNoInfo,
    isBookmarkDataNoInfo,
    isUpVotesDataNoInfo,
    isMyDataNoInfo,
    getRecentQuestData,
    getBookmarkData,
    getUpVotesData,
    getMyData,
  };
});
