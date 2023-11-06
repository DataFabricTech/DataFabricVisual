import { defineStore } from "pinia";
import OverviewSample from "./overview-sample.json"
import {
  StorageFilter,
  StorageItem,
  StorageSortContextItem, StorageTypeItem
} from "~/components/project/data-fabric/overview/storage-overview";
const STORAGE_BASE_URL = "/storage/v1"
const DEFAULT_STATUS_LIST = [
  { id: "connection", value: "CONNECTED", name: "연결됨" },
  { id: "disconnection", value: "DISCONNECTED", name: "연결 안됨" }
];

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
  const { $api } = useNuxtApp();
  const storage: {
    items: Array<StorageItem>;
    types: Array<StorageTypeItem>;
    statusTypes: Array<Object>;
    filter: StorageFilter;
    selectedSort: StorageSortContextItem;
    sortList: Array<StorageSortContextItem>;
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
    types: [
      {
        name: "",
        value: "",
        checked: false
      }
    ],
    filter: {
      name: "",
      storageType: [],
      status: []
    },
    selectedSort: DEFAULT_SORT,
    sortList: DEFAULT_SORT_LIST,
    statusTypes: DEFAULT_STATUS_LIST
  });

  const storageSortList = computed(() => {
    // sort - selected  여부 확인
    storage.sortList = _map(storage.sortList, (el: StorageSortContextItem) => {
      el.selected = el.id === storage.selectedSort.id;
      return el;
    });

    return storage.sortList;
  });

  const storageList = computed(() => {
    // 정렬
    storage.items = _orderBy(storage.items, storage.selectedSort.type, storage.selectedSort.direction);

    // 필터
    const filterText = storage.filter.name.toLowerCase();
    _map(storage.items, (el) => {
      el.show =
        el.name.toLowerCase().includes(filterText) &&
        storage.filter.storageType.includes(el.storageType) &&
        storage.filter.status.includes(el.status);
      return el;
    });
    return storage.items;
  });

  const initTypeFilter = computed(() => {
    return _map(storage.types, "value");
  });

  const initStatusFilter = computed(() => {
    return _map(storage.statusTypes, "value");
  });

  /**
   * 연결정보 목록 정렬
   * TODO: 연결정보 API 재조회
   * @param data
   */
  function setSort(data: any) {
    storage.selectedSort = data;
  }
  function setStorageTypeFilter(data: any) {
    storage.filter.storageType = data;
  }

  function setStatusFilter(data: any) {
    storage.filter.status = data;
  }

  /**
   * 연결정보 목록 조회 초기화
   * TODO: 연결정보 목록 API 재조회
   */
  function resetSearch() {
    storage.selectedSort = DEFAULT_SORT;
    storage.filter.name= "";
    initPopupFilter();
  }

  /**
   * 연결정보 필터 > 저장소 타입 목록 조회
   * TODO: API 연동 (미정)
   */
  function getStorageType() {
    storage.types = OverviewSample.storageTypes;
    initPopupFilter();
  }

  /**
   * 연결정보 목록 조회
   * TODO: API 연동 (/search)
   */
  function getStorage() {
    getStorageType();
    storage.items = OverviewSample.storages;
  }

  /**
   * 연결정보 필터 > 저장소 타입 목록 초기값 설정
   */
  function initPopupFilter() {
    storage.filter.storageType = initTypeFilter.value;
    storage.filter.status = initStatusFilter.value;
  }

  return {
    storage,
    storageList,
    storageSortList,
    initTypeFilter,
    initStatusFilter,
    setSort,
    setStorageTypeFilter,
    setStatusFilter,
    resetSearch,
    getStorage
  };
})
