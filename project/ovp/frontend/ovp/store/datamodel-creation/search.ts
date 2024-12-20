import { usePagingStore } from "~/store/common/paging";
import { useUserStore } from "~/store/user/userStore";
import _ from "lodash";
import {
  type Filter,
  FILTER_KEYS,
  type QueryFilter,
  type SelectedFilters,
} from "~/store/search/common";
import type { Ref } from "vue";
import $constants from "~/utils/constant";
import { useQueryHelpers } from "~/composables/queryHelpers";
import CustomHeader from "@extends/custom-header-cell/custom-header-cell.vue";

import { useNuxtApp } from "nuxt/app";

interface Filters {
  [FILTER_KEYS.CATEGORY]: Filter;
  [FILTER_KEYS.SERVICE_TYPE]: Filter;
  [FILTER_KEYS.TAGS]: Filter;

  [key: string]: { text: string; data: any[] }; // 인덱스 시그니처 추가
}

export const useDataModelSearchStore = defineStore("dataModelSearch", () => {
  const { $api, $alert } = useNuxtApp();

  const userStore = useUserStore();
  const { user } = storeToRefs(userStore);

  // 인피니티 스크롤 - Pageing 처리 관련
  const pagingStore = usePagingStore();
  const { setFrom, updateIntersectionHandler, setDataLoadDone } = pagingStore;
  const { from, size } = storeToRefs(pagingStore);

  // 데이터 생성 > 추가 모달 관련
  const selectedModelList = ref([]);
  const nSelectedListData = ref([]);
  const selectedModelListCnt = computed(() => {
    return selectedModelList.value.length;
  });
  const isDoneFirModelListLoad = ref(false);
  const infiniteScrollSettingDone = ref(false);

  const { setQueryFilterByDepth } = useQueryHelpers();

  // filters 초기값 부여 (text 처리)
  const createDefaultFilters = (): Filters => {
    return {
      [FILTER_KEYS.CATEGORY]: { text: "카테고리", data: [] },
      [FILTER_KEYS.SERVICE_TYPE]: { text: "서비스타입", data: [] },
      [FILTER_KEYS.TAGS]: { text: "태그", data: [] },
    };
  };

  // filter 정보
  const filters = ref<Filters>(createDefaultFilters());

  // Tab 정보
  const TAB_DEFAULT = $constants.DATAMODEL_CREATION.ADD.TAB[0].value;
  const currTab: Ref<string> = ref(TAB_DEFAULT); // 대분류 : 전체/MY
  const currTypeTab: Ref<string> = ref($constants.COMMON.DATA_TYPE[0].value);
  const currTypeMyTab: Ref<string> = ref(
    $constants.DATAMODEL_CREATION.ADD.MY_DATA_TAB[0].value,
  );

  // 데이터 모델 리스트 - 전체
  const searchResult: Ref<any[]> = ref([]);
  const selectedFilters: Ref<SelectedFilters> = ref({} as SelectedFilters);
  const searchResultLength: Ref<number> = ref<number>(0);

  // 데이터 모델 리스트 - MY
  const mySearchResult: Ref<any[]> = ref([]);
  const mySearchResultLength: Ref<number> = ref<number>(0);

  // List Query data
  const searchKeyword: Ref<string> = ref<string>("");
  const searchMyyKeyword: Ref<string> = ref<string>("");
  const sortKey: Ref<string> = ref<string>("totalVotes");
  const sortKeyOpt: Ref<string> = ref<string>("desc");

  // 추가 모달 상세 보기
  const DEFAULT_DETAIL_TAB =
    $constants.DATAMODEL_CREATION.ADD.DETAIL_TAB[0].value;
  const currDetailTab: Ref<string> = ref(DEFAULT_DETAIL_TAB);

  // 선택 데이터
  const selectedItem: Ref<object> = ref<object>({});
  const selectedItemOwner = computed(() => {
    return selectedItem.value.owner?.name;
  });

  // 추가 모달 > 선택 항목에 대한 상세 조회 데이터
  const sampleData: Ref<object> = ref<object>({});
  const profileData: Ref<object> = ref<object>({});
  const recommendData: Ref<object> = ref<object>({});
  /**
   * 데이터 조회 > 쿼리 파라미터 처리
   */
  const getSearchListQuery = () => {
    const queryFilter = getQueryFilter();
    const params: any = {
      // open-meta 에서 사용 하는 key 이기 때문에 그대로 사용.
      // eslint 예외 제외 코드 추가.
      // eslint-disable-next-line id-length
      q: searchKeyword.value,
      index: currTypeTab.value, // table or storage or model -> tab
      from: from.value,
      size: size.value,
      deleted: false,
      query_filter: JSON.stringify(queryFilter),
      sort_field: sortKey.value,
      sort_order: sortKeyOpt.value,
    };
    return new URLSearchParams(params);
  };

  const getCtgIds = () => {
    return !_.has(selectedFilters.value, FILTER_KEYS.CATEGORY)
      ? []
      : selectedFilters.value[FILTER_KEYS.CATEGORY].map(
          (filter: any) => `ovp_category.${filter.id}`,
        );
  };

  /**
   * 데이터 조회 > 쿼리 파라미터 처리
   */
  const getQueryFilter = () => {
    const queryFilter: QueryFilter = {
      query: { bool: { must: [] } },
    };

    for (const key in selectedFilters.value) {
      const value =
        key === "category"
          ? getCtgIds()
          : _.map(selectedFilters.value[key], "key");
      if ($_isEmpty(value)) {
        continue;
      }

      const keyValue = key === "category" ? "tags.tagFQN" : key;
      queryFilter.query.bool.must.push(setQueryFilterByDepth(keyValue, value));
    }

    queryFilter.query.bool.must_not = [
      { terms: { fileFormats: ["hwp", "hwpx", "doc", "docs"] } },
    ];
    return queryFilter;
  };

  /**
   * 선택된 데이터 인지 확인
   * @param selectedList
   * @param item
   */
  const isSelectedData = (selectedList: any[] | null = null, item: any) => {
    const itemId = item.id;
    const modelList =
      selectedList === null ? selectedModelList.value : selectedList;
    const findItem = _.find(modelList, { id: itemId });
    return !!findItem;
  };

  const isShowDetailData = (item: any) => {
    const itemId = item.id;
    return itemId === selectedItem.id;
  };

  /**
   * 리스트 변경 - 호출된 데이터를 화면에서 사용하는 속성으로 변경해서 반환
   * @param selectedList
   * @param item
   */
  const setSearchListItem = (selectedList: any[] | null = null, item: any) => {
    const isSelected = isSelectedData(selectedList, item);
    const idShowDetail = isShowDetailData(item);
    return {
      ...item,
      label: item.modelNm,
      value: item.id,
      isChecked: false, // checkbox 선택 여부
      isShowContextMenu: false, // "복사" 컨텍스트 메뉴 클릭 여부
      isShowContextMenuBtn: false, // 컨텍스트 메뉴 버튼 클릭 여부
      idShowDetail: idShowDetail, // 단일선택(아이템) 여부
      isSelected: isSelected, // 이미 선택여부 확인
    };
  };

  /**
   * 데이터 조회 > 갱신
   */
  const getSearchList = async (selectedList: any[] | null = null) => {
    isDoneFirModelListLoad.value = false;

    const { data, totalCount } = await getSearchListAPI(selectedList);
    searchResult.value = data[currTypeTab.value];
    searchResultLength.value = totalCount;

    const types = ["table", "storage", "model"];
    if (
      types.includes(currTypeTab.value) &&
      searchResultLength.value[currTypeTab.value] > 0
    ) {
      isDoneFirModelListLoad.value = true;
    }

    // [데이터 갱신] 이 완료되면 호출한다. infiniteScroll 처리하기 위해 필요한 함수. (modal 한정)
    setDataLoadDone();
  };

  /**
   * 데이터 조회 > 누적
   */
  const addSearchList = async () => {
    const { data, totalCount } = await getSearchListAPI(null, false);
    searchResult.value = searchResult.value.concat(data[currTypeTab.value]);
    searchResultLength.value = totalCount;
  };

  /**
   * My 데이터 조회 > 갱신
   */
  const getMyList = async (selectedList: any[] | null = null) => {
    const { data, totalCount } = await getMyListAPI(selectedList, true);
    mySearchResult.value = data[currTypeMyTab.value];
    mySearchResultLength.value = totalCount;

    const types = ["owner", "bookmark"];
    if (
      types.includes(currTypeMyTab.value) &&
      mySearchResultLength.value[currTypeMyTab.value] > 0
    ) {
      isDoneFirModelListLoad.value = true;
    }
  };
  /**
   * 데이터 조회 > 누적
   */
  const addMySearchList = async () => {
    const { data, totalCount } = await getMyListAPI(null, false);
    mySearchResult.value = mySearchResult.value.concat(
      data[currTypeMyTab.value],
    );
    mySearchResultLength.value = totalCount;
  };

  /**
   * 목록 reset
   * 목록을 '갱신'하는 경우, from 값을 항상 0으로 주어야 하기 때문에 fn 하나로 묶어서 처리.
   */

  const getMyListQuery = (isFirst: boolean) => {
    const params: any = {
      query: searchMyyKeyword.value,
      type: currTypeMyTab.value, // table or storage or model -> tab
      from: isFirst ? 0 : from.value,
      size: size.value,
      id: user.value.id,
    };
    return new URLSearchParams(params);
  };

  const resetReloadList = async (selectedList: any[] | null = null) => {
    setFrom(0);
    if (currTab.value === "my") {
      await getMyList(selectedList);
    } else {
      await getSearchList(selectedList);
    }
    updateIntersectionHandler(0);
  };

  /**
   * 데이터 조회 > 정렬 변경
   * @param item
   */
  const setSortInfo = (item: string) => {
    const items = item.split("_");
    sortKey.value = items.shift() ?? ""; // undefined 오류 예외처리
    sortKeyOpt.value = items.pop() ?? "";
  };

  const setSearchKeyword = (keyword: string) => {
    searchKeyword.value = keyword;
  };
  const setSearchMyKeyword = (keyword: string) => {
    searchMyyKeyword.value = keyword;
  };
  const setSelectedFilter = (value: any[]) => {
    selectedFilters.value = value;
  };
  const setSelectedItem = (value: any) => {
    selectedItem.value = value;
    sampleData.value = value;
    profileData.value = value;
    recommendData.value = value;
  };
  const setCurrTab = (value: any) => {
    currTab.value = value;
  };

  /**
   * 중분류 Tab 변경
   * @param item
   */
  const changeTypeTab = (item: string) => {
    cancelAllSelection();
    setSelectedItem({});
    currTypeTab.value = item;
    resetReloadList(nSelectedListData.value);
  };

  /**
   * My List > 중분류 Tab 변경
   * @param item
   */
  const changeTypeMyTab = (item: string) => {
    currTypeMyTab.value = item;
    setSearchMyKeyword("");
    resetReloadList(nSelectedListData.value);
  };

  /**
   * 대분류 Tab 변경
   * @param item
   */
  const changeTab = (item: string) => {
    cancelAllSelection();
    setSelectedItem({});

    currTab.value = item;
    setSearchKeyword("");
    setSearchMyKeyword("");

    nextTick(() => {
      // 두 tab 다 infinite scroll 이 설정 되어 있기 때문에 tab 전환시 설정 flag 를 초기화해준다.
      // dom 에 infinite scroll 이 적용될 tag가 생성 된 후에 infinite scroll 을 설정해줘야 동작하기 때문에 nextTick 에서 설정함.
      infiniteScrollSettingDone.value = false;
      infiniteScrollSettingDone.value = true;
    }).then(() => {
      resetReloadList(nSelectedListData.value);
    });
  };
  const setSelectedData = (value: string) => {
    const selectedModelItem = _.find(searchResult.value, { id: value });
    const selectedMyModelItem = _.find(mySearchResult.value, { id: value });
    if (
      $_isUndefined(selectedModelItem) &&
      $_isUndefined(selectedMyModelItem)
    ) {
      selectedItem.value = {};
    } else {
      selectedItem.value = selectedModelItem || selectedMyModelItem;
    }
  };

  const updateSelection = (resultList: any[], value: string) => {
    return resultList === undefined
      ? []
      : resultList.map((item: any) => {
          return { ...item, idShowDetail: item.id === value };
        });
  };

  const cancelAllSelection = () => {
    // 탭 초기화
    currDetailTab.value = DEFAULT_DETAIL_TAB;

    nSelectedListData.value = updateSelection(nSelectedListData.value, "");
    searchResult.value = updateSelection(searchResult.value, "");
    mySearchResult.value = updateSelection(mySearchResult.value, "");
  };

  /**
   * 추가 모달 > 데이터 선택(상세보기)
   * @param value
   */
  const onClickData = async (value: string) => {
    setSelectedData(value);

    if (currTab.value === "all") {
      searchResult.value = updateSelection(searchResult.value, value);
    } else {
      mySearchResult.value = updateSelection(mySearchResult.value, value);
    }

    nSelectedListData.value = updateSelection(nSelectedListData.value, "");

    await resetDetailBox();
  };

  const onClickSelectedData = async (value: string) => {
    const selectedMyModelItem = _.find(nSelectedListData.value, { id: value });
    selectedItem.value = $_isUndefined(selectedMyModelItem)
      ? {}
      : selectedMyModelItem;

    nSelectedListData.value = nSelectedListData.value.map((item: any) => {
      return {
        ...item,
        idShowDetail: item.id === selectedItem.value.id,
      };
    });

    searchResult.value = updateSelection(searchResult.value, "");
    mySearchResult.value = updateSelection(mySearchResult.value, "");

    await resetDetailBox();
  };

  /**
   * 추가 모달 > 상세 보기 - tab 값 초기화
   */
  const resetDetailBox = async () => {
    currDetailTab.value = DEFAULT_DETAIL_TAB;
    const value = selectedItem.value.id;
    if (!value) {
      return;
    }
    sampleData.value = await getSampleData(
      value,
      selectedItem.value.fqn,
      selectedItem.value.type,
    );
  };

  /**
   * 추가 모달 > 상세 보기 - tab 값 변경
   * @param value
   */
  const changeDetailTab = async (value: string) => {
    currDetailTab.value = value;
    if (value === DEFAULT_DETAIL_TAB) {
      const value = selectedItem.value.id;
      if (!value) {
        return;
      }
      sampleData.value = await getSampleData(
        value,
        selectedItem.value.fqn,
        selectedItem.value.type,
      );
    } else if (
      value === $constants.DATAMODEL_CREATION.ADD.DETAIL_TAB[1].value
    ) {
      const fqn = selectedItem.value.fqn;
      if (!fqn) {
        return;
      }
      profileData.value = await getProfileData(fqn, selectedItem.value.type);
    } else if (
      value === $constants.DATAMODEL_CREATION.ADD.DETAIL_TAB[2].value
    ) {
      const id = selectedItem.value.id;
      if (!id) {
        return;
      }

      recommendData.value = await getRecommendDataByApiType(
        id,
        selectedItem.value.type,
        sampleData.value > 0 ? "embedding" : "clustering",
      );
    }
  };

  /**
   * 모델 리스트 > 북마크 클릭
   * @param value
   */
  const onClickBookmark = (value: string) => {
    updateBookmark(value);
  };

  /**
   * API - 샘플 데이터 조회
   */
  const getSampleData = async (value: string, fqn: string, type: string) => {
    return $api(`/api/search/detail/sample-data/${value}?type=${type}`)
      .then((res: any) => {
        if (res.result === 0) {
          return {};
        }
        const fields = res.data.columns.map((column: any) => ({
          field: column.name,
        }));

        const columnDefs = res.data.columns.map((column: any) => ({
          field: column.name,
          headerComponentFramework: CustomHeader,
          headerComponentParams: {
            gridColumnDefs: fields,
            fqn: fqn,
          },
          minWidth: 140, // minWidth 추가
          flex: 1,
        }));

        return {
          columnDefs: columnDefs,
          rowData: res.data.sampleList,
          fqn: fqn,
        };
      })
      .catch((err: any) => {
        console.log("err: ", err);
      });
  };

  /**
   * API - 데이터 프로파일링 조회
   */
  const getProfileData = async (fqn: string, type: string) => {
    const isTableOrModel = _.isEqual(type, "table") || $_isEqual(type, "model");
    const url = isTableOrModel
      ? `/api/search/detail/profile/${fqn}`
      : `/api/search/detail/containers/profile/${fqn}`;

    try {
      const res = await $api(url);
      if (res.result === 1) {
        return {
          columnOptions: $constants.COMMON.DATA_PROFILE_RENDER,
          columnDefs: $constants.COMMON.DATA_PROFILE_COLUMN,
          rowData: res.data,
          fqn: fqn,
        };
      }
    } catch (err) {
      console.log("err: ", err);
    }
  };

  /**
   * API - 추천 데이터 조회
   */
  const getRecommendDataByApiType = async (id, type, apiType) => {
    const { data } = await $api(
      `/api/search/detail/recommend/${apiType}/${id}?type=${type}`,
    );
    return data;
  };

  /**
   * API- 필터 조회
   */
  const getFilters = async () => {
    const { data } = await $api(`/api/search/filters`, { showLoader: false });

    // 기본값 기준 사용할 필터 key 를 정리
    const defaultFilters = createDefaultFilters();
    const useFilters = Object.keys(defaultFilters);

    useFilters.forEach((key: string) => {
      (filters.value as Filters)[key].data = data[key];
    });
  };

  /**
   * API - 데이터 조회
   */
  const getSearchListAPI = async (
    selectedList: any[] | null = null,
    isFirst = true,
  ) => {
    const { data } = await $api(`/api/creation/list?${getSearchListQuery()}`, {
      showLoader: isFirst,
    });
    const nData = data.data[currTypeTab.value] as any[];
    data.data[currTypeTab.value] = nData.map((item: any) => {
      return setSearchListItem(selectedList, item);
    });
    return data;
  };

  /**
   * API - 전체
   */
  const getMyListAPI = async (
    selectedList: any[] | null = null,
    isFirst = true,
  ) => {
    const { data } = await $api(
      `/api/creation/my-list?${getMyListQuery(isFirst)}`,
      {
        showLoader: isFirst,
      },
    );
    if (data === null) {
      return { data: [], totalCount: 0 };
    }
    const nData = data.data[currTypeMyTab.value] as any[];
    data.data[currTypeMyTab.value] = nData.map((item: any) => {
      return setSearchListItem(selectedList, item);
    });
    return data;
  };

  /**
   * API - 북마크 데이터 변경
   */
  const updateBookmark = async (value: string) => {
    const selectedModel = _.find(searchResult.value, { id: value });
    const mySelectedModel = _.find(mySearchResult.value, { id: value });
    if ($_isUndefined(selectedModel) && $_isUndefined(mySelectedModel)) {
      $alert("모델을 찾을 수 없습니다.");
      return;
    }

    const model = selectedModel || mySelectedModel;

    const urlType = model.isFollow ? "remove" : "add";
    const methodType = model.isFollow ? "DELETE" : "PUT";
    $api(`/api/creation/bookmark/${urlType}/${value}`, {
      method: methodType,
      params: {
        type: model.type,
      },
    })
      .then((res: any) => {
        if (res.result === 1) {
          searchResult.value = searchResult.value.filter((item) => {
            // selectedModel과 일치하는 항목의 isFollow만 false로 변경
            if (item.fqn === model.fqn) {
              item.isFollow = !item.isFollow;
            }
            return true; // 모든 항목을 유지
          });
          getMyList([]);
        }
      })
      .catch((err: any) => {
        console.log("err: ", err);
      });
  };

  // 데이터 모델 추가 팝업 > 선택된 데이터 모델 > 북마크 변경
  const updateSelectedModelBookmark = async (value: any) => {
    const selectedModel = _.find(nSelectedListData.value, { id: value });

    const urlType = selectedModel.isFollow ? "remove" : "add";
    const methodType = selectedModel.isFollow ? "DELETE" : "PUT";

    $api(`/api/creation/bookmark/${urlType}/${value}`, {
      method: methodType,
      params: {
        type: selectedModel.type,
      },
    })
      .then((res: any) => {
        if (res.result === 1) {
          nSelectedListData.value = nSelectedListData.value.filter((item) => {
            // selectedModel과 일치하는 항목의 isFollow만 false로 변경
            if (item.fqn === selectedModel.fqn) {
              item.isFollow = !item.isFollow;
            }
            return true; // 모든 항목을 유지
          });
          getMyList([]);
        }
      })
      .catch((err: any) => {
        console.log("err: ", err);
      });
  };

  // 데이터 생성 메인 > 선택된 데이터 모델 > 북마크 변경
  const updateMainSelectedModelBookmark = async (value: any) => {
    console.log(value);
    const selectedModel = _.find(selectedModelList.value, { id: value });

    const urlType = selectedModel.isFollow ? "remove" : "add";
    const methodType = selectedModel.isFollow ? "DELETE" : "PUT";

    $api(`/api/creation/bookmark/${urlType}/${value}`, {
      method: methodType,
      params: {
        type: selectedModel.type,
      },
    })
      .then((res: any) => {
        if (res.result === 1) {
          selectedModelList.value = selectedModelList.value.filter((item) => {
            // selectedModel과 일치하는 항목의 isFollow만 false로 변경
            if (item.fqn === selectedModel.fqn) {
              item.isFollow = !item.isFollow;
            }
            return true; // 모든 항목을 유지
          });
        }
      })
      .catch((err: any) => {
        console.log("err: ", err);
      });
  };

  const setNSelectedListData = (value: any) => {
    nSelectedListData.value = value;
  };

  return {
    sortKey,
    sortKeyOpt,
    currTab,
    currTypeMyTab,
    currTypeTab,
    filters,
    searchResult,
    mySearchResult,
    selectedFilters,
    searchResultLength,
    mySearchResultLength,
    selectedItem,
    currDetailTab,
    sampleData,
    profileData,
    recommendData,
    selectedItemOwner,
    nSelectedListData,
    selectedModelList,
    selectedModelListCnt,
    isDoneFirModelListLoad,
    infiniteScrollSettingDone,
    addSearchList,
    addMySearchList,
    getSearchList,
    getFilters,
    getSampleData,
    getProfileData,
    setSelectedFilter,
    setSortInfo,
    setSearchKeyword,
    setSearchMyKeyword,
    setSelectedItem,
    setCurrTab,
    resetReloadList,
    resetDetailBox,
    changeTypeTab,
    changeTypeMyTab,
    changeTab,
    onClickData,
    onClickSelectedData,
    changeDetailTab,
    onClickBookmark,
    updateSelectedModelBookmark,
    updateMainSelectedModelBookmark,
    setNSelectedListData,
    cancelAllSelection,
  };
});
