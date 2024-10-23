import { usePagingStore } from "@/store/common/paging";
import { useUserStore } from "@/store/user/userStore";
import { useQueryHelpers } from "@/composables/queryHelpers";
import _ from "lodash";
import { ref } from "vue";
import $constants from "@/utils/constant";
import type { PreviewData } from "@/type/common";
import { NetworkDiagramNodeInfo } from "knowledge-graph-canvas/dist/network-diagram/layout/layout.type";
import { storeToRefs } from "pinia";

export const FILTER_KEYS = {
  CATEGORY: "category",
  OWNER: "owner.displayName.keyword",
  TAGS: "tags.tagFQN",
  SERVICE: "service.displayName.keyword",
  SERVICE_TYPE: "serviceType",
  DATABASE: "database.displayName.keyword",
  DATABASE_SCHEMA: "databaseSchema.displayName.keyword",
  COLUMNS: "columns.name.keyword",
} as const;

export interface Filter {
  text: string;
  data: any[] | object;
}

export interface Filters {
  [FILTER_KEYS.CATEGORY]: Filter;
  [FILTER_KEYS.OWNER]: Filter;
  [FILTER_KEYS.TAGS]: Filter;
  [FILTER_KEYS.SERVICE]: Filter;
  [FILTER_KEYS.SERVICE_TYPE]: Filter;
  [FILTER_KEYS.DATABASE]: Filter;
  [FILTER_KEYS.DATABASE_SCHEMA]: Filter;
  [FILTER_KEYS.COLUMNS]: Filter;

  [key: string]: Filter; // 인덱스 시그니처 추가
}

export interface SelectedFilters {
  [key: string]: string[];
}

export interface QueryFilter {
  query: {
    bool: {
      must: any[];
    };
  };
}

export interface SearchResultLength {
  table: number;
  storage: number;
  model: number;
}

export const useSearchCommonStore = defineStore(
  "searchCommon",
  () => {
    const { $api } = useNuxtApp();
    const pagingStore = usePagingStore();
    const { setFrom, setSize, updateIntersectionHandler } = pagingStore;
    const { from, size } = storeToRefs(pagingStore);
    const { setQueryFilterByDepth } = useQueryHelpers();
    const userStore = useUserStore();
    const { user } = storeToRefs(userStore);

    // filters 초기값 부여 (text 처리)
    const createDefaultFilters = (): Filters => {
      return {
        [FILTER_KEYS.CATEGORY]: { text: "카테고리", data: {} },
        [FILTER_KEYS.OWNER]: { text: "소유자", data: [] },
        [FILTER_KEYS.TAGS]: { text: "태그", data: [] },
        [FILTER_KEYS.SERVICE]: { text: "서비스", data: [] },
        [FILTER_KEYS.SERVICE_TYPE]: { text: "서비스타입", data: [] },
        [FILTER_KEYS.DATABASE]: { text: "데이터베이스", data: [] },
        [FILTER_KEYS.DATABASE_SCHEMA]: { text: "스키마", data: [] },
        [FILTER_KEYS.COLUMNS]: { text: "컬럼", data: [] },
      };
    };
    // preview 초기값 부여
    const createDefaultPreview = (): PreviewData => {
      return {
        modelType: "structured",
        modelInfo: {
          model: {
            name: "",
            displayName: "",
            desc: "",
            tableType: "",
            cnt: 0,
            ext: "",
            size: 0,
          },
          columns: [
            {
              name: "",
              dataType: "",
              desc: "",
              constraint: "NULL",
            },
          ],
          details: "",
          url: "",
        },
        tags: [],
        glossaries: [],
      };
    };

    // filter 정보
    const filters = ref<Filters>(createDefaultFilters());
    const currentTab: Ref<string> = ref("table");
    const searchResult: Ref<any[]> = ref([]);
    const previewData: Ref<PreviewData> = ref(createDefaultPreview());
    const selectedFilterItems: Ref<any> = ref({});
    const selectedFilters: Ref<SelectedFilters> = ref({} as SelectedFilters);
    const currentPreviewId: Ref<string | number> = ref("");
    let UNDEFINED_TAG_ID: string = "";

    // GraphView DATA
    const graphData: Ref<any[]> = ref([]);
    const showGraphModelListMenu: Ref<boolean> = ref(false);
    const showDropDown = ref(false);

    // DATA
    const viewType: Ref<string> = ref<string>("listView");
    const isShowPreview: Ref<boolean> = ref<boolean>(false);
    const isBoxSelectedStyle: Ref<boolean> = ref<boolean>(false);
    const searchResultLength: Ref<SearchResultLength> = ref<SearchResultLength>(
      {
        model: 0,
        table: 0,
        storage: 0,
      },
    );
    // List Query data
    const searchKeyword: Ref<string> = ref<string>("");
    const sortKey: Ref<string> = ref<string>("totalVotes");
    const sortKeyOpt: Ref<string> = ref<string>("desc");
    const isSearchResultNoData: Ref<boolean> = ref<boolean>(false);

    const getQueryParam = () => {
      const queryFilter = getQueryFilter(
        selectedFilters.value,
        UNDEFINED_TAG_ID,
      );

      return {
        // open-meta 에서 사용 하는 key 이기 때문에 그대로 사용.
        // eslint 예외 제외 코드 추가.
        // eslint-disable-next-line id-length
        q: searchKeyword.value,
        index: currentTab.value, // table or storage or model -> tab
        from: from.value,
        size: size.value,
        deleted: false,
        query_filter: JSON.stringify(queryFilter),
        sort_field: sortKey.value,
        sort_order: sortKeyOpt.value,
      };
    };
    const getSearchListQuery = (): any => {
      return new URLSearchParams(getQueryParam());
    };
    const getGraphQuery = (): any => {
      const query = getQueryParam();
      query.size = query.from + query.size;
      query.from = 0;
      return new URLSearchParams(query);
    };

    // METHODS
    const getSearchListAPI = async () => {
      const { data } = await $api(`/api/search/list?${getSearchListQuery()}`, {
        showLoader: false,
      });
      // 특수문자 이슈 때문에  backend 에서 1차로 처리(일부 특수문자에 한해서) 했으나,
      // 그외의 특수문자의 경우, backend 에서 에러를 뱉음 -> 공백으로 처리한다.
      return (
        data ?? {
          data: {
            model: [],
            table: [],
            storage: [],
          },
          totalCount: {
            model: 0,
            table: 0,
            storage: 0,
          },
        }
      );
    };
    /**
     * 데이터 조회 -> 누적
     */
    const addSearchList = async () => {
      setSize(20);
      setFrom(stackedFromCount.value);
      stackedFromCount.value = from.value + size.value;

      const { data, totalCount } = await getSearchListAPI();
      searchResult.value = searchResult.value.concat(data[currentTab.value]);
      searchResultLength.value = totalCount;
    };

    /**
     * 데이터 조회 -> 갱신
     */
    const getSearchList = async () => {
      setSize(stackedFromCount.value);

      const { data, totalCount } = await getSearchListAPI();
      searchResult.value = data[currentTab.value];
      searchResultLength.value = totalCount;
      isSearchResultNoData.value = searchResult.value.length === 0;
    };

    const setEmptyFilter = () => {
      selectedFilterItems.value = {};
      selectedFilters.value = {};
    };

    const getFilters = async () => {
      filters.value = (await getUseFilters(createDefaultFilters())) as Filters;

      // 미분류 카테고리 ID 저장
      UNDEFINED_TAG_ID =
        filters.value[FILTER_KEYS.CATEGORY].data.children[0].id;
    };

    const getUseFilters = async (
      defaultFilters: Filters | Partial<Filters>,
    ) => {
      const { data } = await $api(`/api/search/filters`);

      // 기본값 기준 사용할 필터 key 를 정리
      const useFilters = Object.keys(defaultFilters);

      const filtersData = defaultFilters;
      useFilters.forEach((key: string) => {
        (filtersData as Filters)[key].data = data[key];
      });

      return filtersData;
    };

    const getFilter = async (filterKey: string) => {
      // TODO : 서버 연동 후 json 가라 데이터 삭제, 실 데이터로 변경 처리 필요.
      const data = await $api(`/api/search/filter?field=${filterKey}`);
      (filters.value as Filters)[filterKey].data = data.data[filterKey];
    };

    const getPreviewData = async (fqn: string) => {
      const data = await getPreviewAPI(fqn);
      previewData.value = data.data;
    };

    const getPreviewAPI = async (fqn: string) => {
      return await $api(`/api/search/preview/${fqn}`);
    };

    const getContainerPreviewData = async (id: string) => {
      const data = await getContainerPreviewData(id);
      previewData.value = data.data;
    };

    const getContainerPreviewAPI = async (id: string) => {
      return await $api(`/api/containers/${id}`);
    };

    const getCtgIds = (undefinedTagId: string, selectedFilters: object) => {
      return !_.has(selectedFilters, FILTER_KEYS.CATEGORY)
        ? []
        : (selectedFilters[FILTER_KEYS.CATEGORY] as object[]).map(
            (filter: any) =>
              `ovp_category.${
                filter.id === undefinedTagId
                  ? $constants.SERVICE.CATEGORY_UNDEFINED_NAME
                  : filter.id
              }`,
          );
    };

    const getQueryFilter = (
      selectedFilters: object,
      undefinedTagId: string,
    ): QueryFilter => {
      const queryFilter: QueryFilter = {
        query: { bool: { must: [] } },
      };

      for (const key in selectedFilters) {
        const value =
          key === "category"
            ? getCtgIds(undefinedTagId, selectedFilters)
            : (selectedFilters as SelectedFilters)[key];
        const keyValue = key === "category" ? "tags.tagFQN" : key;

        queryFilter.query.bool.must.push(
          setQueryFilterByDepth(keyValue, value),
        );
      }

      return queryFilter;
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
        isShowPreview.value = false;
        isBoxSelectedStyle.value = false;
        currentPreviewId.value = "";
        setSortInfo(item);
        resetReloadList();
      }
    };
    const setSearchKeyword = (keyword: string) => {
      searchKeyword.value = keyword;
    };

    const changeTab = async (item: string, loadList: boolean = true) => {
      isShowPreview.value = false;
      showDropDown.value = false;
      showGraphModelListMenu.value = false;
      currentTab.value = item;

      if (loadList) {
        resetReloadList();
        viewType.value === "graphView" ? await getGraphData() : null;
      }
    };

    const getGraphData = async () => {
      const graphParam: any = getGraphQuery();
      const { data } = await $api(`/api/search/graph/list?${graphParam}`, {
        showLoader: false,
      });
      graphData.value = data;
    };

    // TODO: [개발] 시각화 그래프 마우스 오버했을 때 제목 출력
    const graphCategoryList: NetworkDiagramNodeInfo = ref({});
    const onlyGraphCategoryList = ref([]);
    const graphCategoryPath = ref([]);

    const stackedFromCount: Ref<number> = ref(size.value);
    const filteredSearchList: Ref<any[]> = ref([]);
    const bookmarkList: Ref<any[]> = ref([]);
    const lowestCategoryId = ref("");

    const getBookmarkListQuery = () => {
      const queryFilter: QueryFilter = {
        query: { bool: { must: [] } },
      };
      const query = ` AND followers:${user.value.id}`;

      const params: any = {
        q: `*${searchKeyword.value}*`,
        tempQuery: query,
        index: "all",
        from: 0,
        size: 500,
        deleted: false,
        query_filter: JSON.stringify(queryFilter), // 기본 쿼리 형태는 던져야 backend 에서 오류나지않음.
      };
      return new URLSearchParams(params);
    };

    const getBookmarkListAPI = async () => {
      const { data } = await $api(
        `/api/search/list?${getBookmarkListQuery()}`,
        {
          showLoader: true,
        },
      );
      return data;
    };

    // 북마크 api 가져오기
    const getBookmarkList = async () => {
      const { data } = await getBookmarkListAPI();
      bookmarkList.value = data.all;
    };

    // 북마크 객체 체크 후 담기
    const setBookmarkList = async () => {
      filteredSearchList.value = filteredSearchList.value.map((data) => {
        const isFollow = bookmarkList.value.some(
          (bookmark) => bookmark.id === data.id,
        );

        return {
          ...data, // 기존 데이터 유지
          isFollow: isFollow, // isFollow 속성 추가
        };
      });
    };

    // 북마크 업데이트
    const updateBookmarkList = async (menu: any) => {
      const url: string = `/api/search/detail/${menu.id}/follow?type=${menu.type}`;

      if (menu.isFollow) {
        await $api(url, { method: "DELETE" });
      } else {
        await $api(url, { method: "PUT" });
      }

      filteredSearchList.value = filteredSearchList.value.filter((item) => {
        if (item.id === menu.id) {
          item.isFollow = !item.isFollow;
        }

        return true;
      });
    };

    // 우측 모델 목록 추출
    const setFilteredSearchList = async (nodeId: string) => {
      filteredSearchList.value =
        graphCategoryList.value.id === nodeId
          ? searchResult.value
          : _.filter(searchResult.value, {
              category: lowestCategoryId.value,
            });

      await getBookmarkList();
      setBookmarkList();
    };

    // 우측 모델 목록의 경로 추출
    const setGraphCategoryPath = (nodeId: string) => {
      graphCategoryPath.value = [];

      if (graphCategoryList.value.id === nodeId) {
        graphCategoryPath.value = ["ROOT"];
        return;
      }

      const traverse = (graphList: any) => {
        // 현재 노드가 목표 노드와 일치하면, 상위/현재/하위 탐색 시작
        if (graphList.id === nodeId) {
          // 상위 노드부터 저장
          if (graphList.parentId && graphList.tagId) {
            graphCategoryPath.value.push(graphList.name);
          }
          // 하위 노드들도 추가 (재귀)
          addChildrenNames(graphList.children);
          return true;
        }

        // 하위 children들을 재귀 탐색
        for (const child of graphList.children) {
          if (child.tagId && traverse(child)) {
            // 상위 노드도 결과 배열에 추가
            if (graphList.tagId) {
              graphCategoryPath.value.unshift(graphList.name);
            }
            return true;
          }
        }
      };

      // 하위 children들의 name을 추가하는 함수
      const addChildrenNames = (children: any) => {
        for (const child of children) {
          if (child.tagId) {
            graphCategoryPath.value.push(child.name);
            addChildrenNames(child.children);
          }
        }
      };

      traverse(graphCategoryList.value);

      graphCategoryPath.value =
        graphCategoryPath.value.length > 4
          ? graphCategoryPath.value.slice(0, 1)
          : graphCategoryPath.value;
    };

    return {
      searchKeyword,
      sortKey,
      sortKeyOpt,
      currentTab,
      filters,
      searchResult,
      previewData,
      selectedFilterItems,
      selectedFilters,
      viewType,
      isShowPreview,
      isBoxSelectedStyle,
      searchResultLength,
      isSearchResultNoData,
      currentPreviewId,
      graphData,
      showGraphModelListMenu,
      graphCategoryList,
      showDropDown,
      graphCategoryPath,
      filteredSearchList,
      stackedFromCount,
      onlyGraphCategoryList,
      lowestCategoryId,
      addSearchList,
      getSearchList,
      getFilter,
      getFilters,
      getUseFilters,
      createDefaultPreview,
      getPreviewAPI,
      getPreviewData,
      getContainerPreviewAPI,
      getContainerPreviewData,
      setSortInfo,
      setSortFilter,
      setSearchKeyword,
      resetReloadList,
      changeTab,
      updateIntersectionHandler,
      getQueryFilter,
      setEmptyFilter,
      getGraphData,
      setGraphCategoryPath,
      updateBookmarkList,
      setFilteredSearchList,
    };
  },
  {
    persist: {
      storage: sessionStorage, // 전역설정 해놓았으므로 생략 가능
      // NOTE: store 의 상태 중에서 특정 데이터만 저장
      // 저장할 데이터는 반응성데이터(ref, reactive) 여야 하며, return 값에 포함되어 있어야함
      pick: [
        "searchKeyword",
        "sortKey",
        "sortKeyOpt",
        "currentTab",
        "previewData",
        "selectedFilterItems",
        "selectedFilters",
        "viewType",
      ],
    },
    // NOTE: 특정 데이터를 지정하지 않고 전체 데이터를 저장
    // persist: true
  },
);
