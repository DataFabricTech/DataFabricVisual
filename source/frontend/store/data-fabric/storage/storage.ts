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
 * Storage 관련
 * TODO: 추후 API 서버 연동 후 개발
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

  function changeListBySort(data: any) {
    storage.selectedSort = data;
  }
  function resetSearch() {
    storage.selectedSort = DEFAULT_SORT;
    // NOTE: v-model로 해당 값을 사용중인데 원본 Defulat_filter까지 변경되어있음
    storage.filter = DEFAULT_FILTER;
  }
  function getStorage() {
    // return $api(`${STORAGE_BASE_URL}/search`, {
    //   method: "POST",
    //   // body: search
    // });
    storage.items = OverviewSample.storages;
  }
  function getStorageEvent() {
    // return $api(`${STORAGE_BASE_URL}/event`);
    return OverviewSample.storageEvent;
  }
  function getOverview() {
    // return $api(`${STORAGE_BASE_URL}/overview`);
    return OverviewSample.storageOverview;
  }
  return {
    storage,
    storageList,
    storageSortList,
    changeListBySort,
    resetSearch,
    getStorage,
    getStorageEvent,
    getOverview
  };
})
