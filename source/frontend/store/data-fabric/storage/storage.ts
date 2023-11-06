import { defineStore } from "pinia";
import OverviewSample from "./overview-sample.json"
import _ from "lodash";
import {
  StorageFilter,
  StorageItem,
  StorageSortContextItem
} from "~/components/project/data-fabric/overview/storage-overview";
const STORAGE_BASE_URL = "/storage/v1"

const DEFAULT_SORT_LIST: Array<StorageSortContextItem> = [
  {
    name: "연결정보 이름",
    id: "name-asc",
    type: "name",
    disable: false,
    selected: false,
    icon: "string-ascending",
    direction: "asc"
  },
  {
    name: "연결정보 이름",
    id: "name-desc",
    type: "name",
    disable: false,
    selected: false,
    icon: "string-descending",
    direction: "desc"
  },
  {
    name: "연결상태",
    id: "status-asc",
    type: "status",
    disable: false,
    selected: false,
    icon: "string-ascending",
    direction: "asc"
  },
  {
    name: "연결상태",
    id: "status-desc",
    type: "status",
    disable: false,
    selected: false,
    icon: "string-descending",
    direction: "desc"
  },
  {
    name: "등록일시",
    id: "crdDate-asc",
    type: "crdDate",
    disable: false,
    selected: false,
    icon: "string-ascending",
    direction: "asc"
  },
  {
    name: "등록일시",
    id: "crdDate-desc",
    type: "crdDate",
    disable: false,
    selected: false,
    icon: "string-descending",
    direction: "desc"
  }
];
const DEFAULT_SORT: StorageSortContextItem = {
  name: "",
  disable: false,
  selected: false,
  icon: "",
  id: "name-asc",
  type: "name",
  direction: "asc"
}
const DEFAULT_FILTER: StorageFilter = {
  name: "",
  storageType: [],
  status: []
};

/**
 * Storage - Overview 화면 관련
 */
export const useOverviewStore = defineStore("overview", () => {
  /**
   * Overview - 연결정보 변경사항(얼럿) 목록 조회
   * TODO: API 연동 (API 미정)
   */
  function getStorageEvent() {
    return OverviewSample.storageEvent;
  }

  /**
   * Overview - 차트, 그리드 등 목록 조회
   * TODO: API 연동 (/overview)
   */
  function getOverview() {
    return OverviewSample.storageOverview;
  }
  return {
    getStorageEvent,
    getOverview
  };
})



/**
 * Storage - 연결정보 목록 관련
 */
export const useStorageStore = defineStore("storage", () => {
  const {$api} = useNuxtApp();
  const storage: {
    items: Array<StorageItem>;
    filter: StorageFilter;
    selectedSort: StorageSortContextItem;
    sortList: Array<StorageSortContextItem>;
    storeType: Array<Object>
  } = reactive({
    items: [
      {
        id: "",
        name: "",
        storageType: "",
        status: "",
        crdDate: "",
        show: true
      }
    ],
    filter: {
      name: "",
      storageType: [],
      status: []
    },
    selectedSort: DEFAULT_SORT,
    sortList: DEFAULT_SORT_LIST,
    storeType: [],
  });

  const storageSortList = computed(() => {
    // sort - selected  여부 확인
    storage.sortList = _map(storage.sortList, (el: StorageSortContextItem) => {
      el.selected = el.id === storage.selectedSort.id;
      return el;
    });
    return storage.sortList;
  })

  const storageList = computed(() => {
    // 정렬
    storage.items = _.orderBy(storage.items, storage.selectedSort.type, storage.selectedSort.direction);

    const filterText = storage.filter.name.toLowerCase();
    // 필터 - 텍스트
    _.map(storage.items, el =>{
        el.show = el.name.toLowerCase().includes(filterText);
        return el;
    })
    return storage.items;
  })

  /**
   * 연결정보 목록 정렬
   * TODO: 연결정보 API 재조회
   * @param data
   */
  function changeListBySort(data: any) {
    storage.selectedSort = data;
  }

  /**
   * 연결정보 목록 조회 초기화
   * TODO: 연결정보 목록 API 재조회
   */
  function resetSearch() {
    storage.selectedSort = DEFAULT_SORT;
    // NOTE: v-model로 해당 값을 사용중인데 원본 Defulat_filter까지 변경되어있음
    storage.filter = _.cloneDeep(DEFAULT_FILTER);
  }

  /**
   * 연결정보 목록 조회
   * TODO: API 연동 (/search)
   */
  function getStorage() {
    storage.items = OverviewSample.storages;
  }

  return {
    storage,
    storageList,
    storageSortList,
    changeListBySort,
    resetSearch,
    getStorage
  };
})
