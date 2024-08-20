import { usePagingStore } from "~/store/common/paging";
import _ from "lodash";
import {
  type Filter,
  FILTER_KEYS,
  type QueryFilter,
  type SelectedFilters,
} from "~/store/search/common";
import type { Ref } from "vue";
import $constants from "~/utils/constant";
import DataModelSample from "~/components/datamodel-creation/datamodel-sample.json";
import { useCreationStore } from "~/store/datamodel-creation/index";
import { useQueryHelpers } from "~/composables/queryHelpers";
import CustomHeader from "@extends/custom-header-cell/custom-header-cell.vue";

interface Filters {
  [FILTER_KEYS.CATEGORY]: Filter;
  [FILTER_KEYS.SERVICE_TYPE]: Filter;
  [FILTER_KEYS.TAGS]: Filter;

  [key: string]: { text: string; data: any[] }; // 인덱스 시그니처 추가
}

interface SingleKeyObj {
  [key: string]: Ref<string>;
}

interface KeyObj {
  term: SingleKeyObj;
}

interface BoolObj {
  bool: {
    should: any[];
  };
}

export const useDataModelSearchStore = defineStore("dataModelSearch", () => {
  const { $api } = useNuxtApp();

  // 인피니티 스크롤 - Pageing 처리 관련
  const pagingStore = usePagingStore();
  const { setFrom, updateIntersectionHandler } = pagingStore;
  const { from, size } = storeToRefs(pagingStore);

  // 데이터 생성 > 추가 모달 관련
  const creationStore = useCreationStore();
  const { selectedModelList } = storeToRefs(creationStore);

  const { setQueryFilterByDepth, getTrinoQuery } = useQueryHelpers();

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

  // 데이터 모델 리스트 - 전체
  const searchResult: Ref<any[]> = ref([]);
  const selectedFilters: Ref<SelectedFilters> = ref({} as SelectedFilters);
  const searchResultLength: Ref<number> = ref<number>(0);

  // 데이터 모델 리스트 - MY
  const mySearchResult: Ref<any[]> = ref([]);
  const mySearchResultLength: Ref<number> = ref<number>(0);

  // List Query data
  let searchKeyword: string = "";
  const sortKey: Ref<string> = ref<string>("totalVotes");
  const sortKeyOpt: Ref<string> = ref<string>("desc");

  /**
   * 데이터 조회 > 쿼리 파라미터 처리
   */
  const getSearchListQuery = () => {
    const queryFilter = getQueryFilter();
    const params: any = {
      // open-meta 에서 사용 하는 key 이기 때문에 그대로 사용.
      // eslint 예외 제외 코드 추가.
      // eslint-disable-next-line id-length
      q: searchKeyword,
      index: currTypeTab.value, // table or storage or model -> tab
      from: from.value,
      size: size.value,
      deleted: false,
      query_filter: JSON.stringify(queryFilter),
      sort_field: sortKey.value,
      sort_order: sortKeyOpt.value,
      trino_query: JSON.stringify(getTrinoQuery(queryFilter)),
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
    return queryFilter;
  };

  const isSelectedData = (selectedList: any[] | null = null, item: any) => {
    const itemId = item.id;
    const modelList =
      selectedList === null ? selectedModelList.value : selectedList;
    const findItem = _.find(modelList, { id: itemId });
    return !!findItem;
  };

  const setSearchListItem = (selectedList: any[] | null = null, item: any) => {
    const isSelected = isSelectedData(selectedList, item);
    return {
      ...item,
      label: item.modelNm,
      value: item.id,
      isChecked: false, // checkbox 선택 여부
      idShowDetail: false, // 단일선택(아이템) 여부
      isShowContextMenu: false, // "복사" 컨텍스트 메뉴 클릭 여부
      isShowContextMenuBtn: false, // 컨텍스트 메뉴 버튼 클릭 여부
      isSelected: isSelected, // 이미 선택여부 확인
    };
  };

  // METHODS
  /**
   * 데이터 조회 > API 통신
   */
  const getSearchListAPI = async (selectedList: any[] | null = null) => {
    if (currTab.value === TAB_DEFAULT) {
      const { data } = await $api(`/api/search/list?${getSearchListQuery()}`);
      const nData = data.data[currTypeTab.value] as any[];
      data.data[currTypeTab.value] = nData.map((item: any) => {
        return setSearchListItem(selectedList, item);
      });
      return data;
    } else {
      // TODO: MY 데이터 연동
      const { data, totalCount } = DataModelSample.my_sampleList;
      mySearchResult.value = data;
      mySearchResultLength.value = totalCount;
    }
  };

  /**
   * 데이터 조회 > 누적
   */
  const addSearchList = async () => {
    const { data, totalCount } = await getSearchListAPI();
    searchResult.value = searchResult.value.concat(data[currTypeTab.value]);
    searchResultLength.value = totalCount;
  };

  /**
   * 데이터 조회 > 갱신
   */
  const getSearchList = async (selectedList: any[] | null = null) => {
    const { data, totalCount } = await getSearchListAPI(selectedList);
    searchResult.value = data[currTypeTab.value];
    searchResultLength.value = totalCount;
  };

  /**
   * 필터 조회
   */
  const getFilters = async () => {
    const { data } = await $api(`/api/search/filters`);

    // 기본값 기준 사용할 필터 key 를 정리
    const defaultFilters = createDefaultFilters();
    const useFilters = Object.keys(defaultFilters);

    useFilters.forEach((key: string) => {
      (filters.value as Filters)[key].data = data[key];
    });
  };

  /**
   * 목록 reset
   * 목록을 '갱신'하는 경우, from 값을 항상 0으로 주어야 하기 때문에 fn 하나로 묶어서 처리.
   */
  const resetReloadList = async (selectedList: any[] | null = null) => {
    setFrom(0);
    await getSearchList(selectedList);
    updateIntersectionHandler(0);
  };

  const setSortInfo = (item: string) => {
    const items = item.split("_");
    sortKey.value = items.shift() ?? ""; // undefined 오류 예외처리
    sortKeyOpt.value = items.pop() ?? "";
  };

  const setSortFilter = (item: string | number = "totalVotes_desc") => {
    if (!_.isUndefined(item) && typeof item === "string") {
      setSortInfo(item);

      // 항목 갱신
      resetReloadList();
    }
  };
  const setSearchKeyword = (keyword: string) => {
    searchKeyword = keyword;
  };
  const setSelectedFilter = (value: any[]) => {
    selectedFilters.value = value;
  };

  /**
   * 중분류 Tab 변경
   * @param item
   */
  const changeTypeTab = (item: string) => {
    currTypeTab.value = item;
    resetReloadList();
  };

  /**
   * 대분류 Tab 변경
   * @param item
   */
  const changeTab = (item: string) => {
    currTab.value = item;
    resetReloadList();
  };

  // 선택 데이터
  const selectedItem: Ref<object> = ref<object>({});

  // 추가 모달 > 선택 항목에 대한 상세 조회 데이터
  const sampleData: Ref<object> = ref<object>({});
  const profileData: Ref<object> = ref<object>({});
  const kgData: Ref<object> = ref<object>({});
  /**
   * 샘플 데이터 조회
   */
  const getSampleData = async () => {
    const value = selectedItem.value.id;
    if (!value) {
      return {};
    }
    return $api(`/api/search/detail/sample-data/${value}`)
      .then((res: any) => {
        if (res.result === 1) {
          const fields = res.data.columns.map((column: any) => ({
            field: column.name,
          }));

          const columnDefs = res.data.columns.map((column: any) => ({
            field: column.name,
            headerComponentFramework: CustomHeader,
            headerComponentParams: {
              gridColumnDefs: fields,
              fqn: selectedItem.value.fqn,
            },
          }));

          return {
            columnDefs: columnDefs,
            rowData: res.data.sampleList,
            fqn: selectedItem.value.fqn,
          };
        }
      })
      .catch((err: any) => {
        console.log("err: ", err);
      });
  };

  const getProfileData = async () => {
    const fqn = selectedItem.value.fqn;
    if (!fqn) {
      return {};
    }
    return $api(`/api/search/detail/profile/${fqn}`)
      .then((res: any) => {
        if (res.result === 1) {
          return {
            columnOptions: $constants.COMMON.DATA_PROFILE_RENDER,
            columnDefs: $constants.COMMON.DATA_PROFILE_COLUMN,
            rowData: res.data,
            fqn: fqn,
          };
        }
      })
      .catch((err: any) => {
        console.log("err: ", err);
      });
  };

  const getKgData = async () => {
    return null;
  };

  const onClickData = async (value: string) => {
    const selectedModelItem = _.find(searchResult.value, { id: value });
    selectedItem.value = selectedModelItem ? selectedModelItem : {};
    await resetDetailBox();
  };

  const onClickAccordData = async (value: string) => {
    let selectedModelItem = null;
    for (const key in mySearchResult.value) {
      const data = mySearchResult.value[key];
      const findData = _.find(data, { id: value });
      if (findData) {
        selectedModelItem = findData;
        break;
      }
    }

    selectedItem.value = selectedModelItem ? selectedModelItem : {};
    await resetDetailBox();
  };

  const DEFAULT_DETAIL_TAB =
    $constants.DATAMODEL_CREATION.ADD.DETAIL_TAB[0].value;
  const currDetailTab: Ref<string> = ref(DEFAULT_DETAIL_TAB);

  const resetDetailBox = async () => {
    currDetailTab.value = DEFAULT_DETAIL_TAB;
    sampleData.value = await getSampleData();
  };

  const changeDetailTab = async (value: string) => {
    currDetailTab.value = value;
    if (value === DEFAULT_DETAIL_TAB) {
      sampleData.value = await getSampleData();
    } else if (
      value === $constants.DATAMODEL_CREATION.ADD.DETAIL_TAB[1].value
    ) {
      profileData.value = await getProfileData();
    } else {
      kgData.value = await getKgData();
    }
  };

  const selectedItemOwner = computed(() => {
    return selectedItem.value.owner;
  });

  const onClickBookmark = () => {
    // TODO: API 연동
  };
  return {
    sortKey,
    sortKeyOpt,
    currTab,
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
    kgData,
    addSearchList,
    getSearchList,
    getFilters,
    setSelectedFilter,
    setSortInfo,
    setSortFilter,
    setSearchKeyword,
    resetReloadList,
    resetDetailBox,
    changeTypeTab,
    changeTab,
    onClickData,
    onClickAccordData,
    changeDetailTab,
    onClickBookmark,
    selectedItemOwner,
  };
});
