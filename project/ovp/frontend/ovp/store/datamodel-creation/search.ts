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
  const currDetailTab: Ref<string> = ref($constants.COMMON.DATA_TYPE[0].value);

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
      index: currDetailTab.value, // table or storage or model -> tab
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

  /**
   * 데이터 조회 > 쿼리 파라미터 처리
   * @param queryFilter
   */
  const getTrinoQuery = (queryFilter: QueryFilter) => {
    // query 구현을 backend 에서 하려니까 코드가 너무 복잡해져서 front 에 해서 넘겨서 처리.
    const trinoFilter: QueryFilter = {
      query: {
        bool: {
          must: [{ bool: { should: [{ term: { serviceType: "trino" } }] } }],
        },
      },
    };
    const trinoMustArray = trinoFilter.query.bool.must;
    queryFilter.query.bool.must = _.concat(
      queryFilter.query.bool.must,
      trinoMustArray,
    );
    return queryFilter;
  };

  /**
   * 데이터 조회 > 쿼리 파라미터 처리
   * @param key
   * @param value
   */
  const setQueryFilterByDepth = (key: string, value: any) => {
    const setTermObj: Ref<any[]> = ref<any[]>([]);
    const setBoolObj: Ref<BoolObj> = ref<BoolObj>({
      bool: { should: [] },
    });

    for (const item of value) {
      const setKeyObj: Ref<KeyObj> = ref<KeyObj>({
        term: { [`${key}`]: ref(item) },
      });

      setTermObj.value.push(setKeyObj.value);
    }
    setBoolObj.value.bool.should = setTermObj.value;
    return setBoolObj.value;
  };

  /**
   * 데이터 조회 > 쿼리 파라미터 처리
   */
  const getQueryFilter = () => {
    const queryFilter: QueryFilter = {
      query: { bool: { must: [] } },
    };
    let setBoolObj: object = {};

    for (const key in selectedFilters.value) {
      const value = selectedFilters.value[key];
      setBoolObj = setQueryFilterByDepth(key, value);
      queryFilter.query.bool.must.push(setBoolObj);
    }
    return queryFilter;
  };

  const isSelectedData: (item: any) => boolean = (item) => {
    const itemId = item.id;
    const findItem = _.find(selectedModelList, { id: itemId });
    return !!findItem;
  };
  const setSearchListItem = (item: any) => {
    const isSelected = isSelectedData(item);
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
  const getSearchListAPI = async () => {
    if (currTab.value === TAB_DEFAULT) {
      const { data } = await $api(`/api/search/list?${getSearchListQuery()}`);
      const nData = data.data[currDetailTab.value] as any[];
      data.data[currDetailTab.value] = nData.map((item: any) => {
        return setSearchListItem(item);
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
    console.log("addSearchList");
    const { data, totalCount } = await getSearchListAPI();
    searchResult.value = searchResult.value.concat(data[currDetailTab.value]);
    searchResultLength.value = totalCount;
  };

  /**
   * 데이터 조회 > 갱신
   */
  const getSearchList = async () => {
    const { data, totalCount } = await getSearchListAPI();
    searchResult.value = data[currDetailTab.value];
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
  const resetReloadList = async () => {
    setFrom(0);
    await getSearchList();
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

  /**
   * 중분류 Tab 변경
   * @param item
   */
  const changeDetailTab = (item: string) => {
    currDetailTab.value = item;
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

  return {
    sortKey,
    sortKeyOpt,
    currTab,
    currDetailTab,
    filters,
    searchResult,
    mySearchResult,
    selectedFilters,
    searchResultLength,
    mySearchResultLength,
    addSearchList,
    getSearchList,
    getFilters,
    setSortInfo,
    setSortFilter,
    setSearchKeyword,
    resetReloadList,
    changeDetailTab,
    changeTab,
  };
});
