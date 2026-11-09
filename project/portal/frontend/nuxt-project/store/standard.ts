import { defineStore } from "pinia";

export const useStandardStore = defineStore('standardStore', () => {
  // state
  const _searchParams = ref({
    select_type: "term",
    pagingObj: {
      total: 0,
      limit: 10,
      block: 10,
      currentPage: 1,
    },
    searchObj: {
      searchType: "all",
      searchWord: "",
      changedTab: false,
    },
    used: "Y",
    formChecked: ""
  }); // 상세

  // getters
  const getSearchParams = computed(() => _searchParams.value)
  const getSelectType = computed(() => _searchParams.value.select_type)
  const getPagingObj = computed(() => _searchParams.value.pagingObj)
  const getTotal = computed(() => _searchParams.value.pagingObj.total)
  const getBlock = computed(() => _searchParams.value.pagingObj.block)
  const getCurrentPage = computed(() => _searchParams.value.pagingObj.currentPage)
  const getSearchObj = computed(() => _searchParams.value.searchObj)
  const getSearchType = computed(() => _searchParams.value.searchObj.searchType)
  const getSearchWord = computed(() => _searchParams.value.searchObj.searchWord)
  const getChangedTab = computed(() => _searchParams.value.searchObj.changedTab)
  const getUsed = computed(() => _searchParams.value.used)
  const getFormChecked = computed(() => _searchParams.value.formChecked)

  // action
  const setSearchParams = (data: any) => { _searchParams.value = data }
  const setSelectType = (data: any) => { _searchParams.value.select_type = data }
  const setPagingObj = (data: any) => { _searchParams.value.pagingObj = data }
  const setTotal = (data: any) => { _searchParams.value.pagingObj.total = data }
  const setBlock = (data: any) => { _searchParams.value.pagingObj.block = data }
  const setCurrentPage = (data: any) => { _searchParams.value.pagingObj.currentPage = data }
  const setSearchObj = (data: any) => { _searchParams.value.searchObj = data }
  const setSearchType = (data: any) => { _searchParams.value.searchObj.searchType = data }
  const setSearchWord = (data: any) => { _searchParams.value.searchObj.searchWord = data }
  const setChangedTab = (data: any) => { _searchParams.value.searchObj.changedTab = data }
  const setUsed = (data: any) => { _searchParams.value.used = data }
  const setFormChecked = (data: any) => { _searchParams.value.formChecked = data }

  return {
    _searchParams,
    getSearchParams,
    getSelectType,
    getPagingObj,
    getTotal,
    getBlock,
    getCurrentPage,
    getSearchObj,
    getSearchType,
    getSearchWord,
    getChangedTab,
    getUsed,
    getFormChecked,
    setSearchParams,
    setSelectType,
    setPagingObj,
    setTotal,
    setBlock,
    setCurrentPage,
    setSearchObj,
    setSearchType,
    setSearchWord,
    setChangedTab,
    setUsed,
    setFormChecked,
  }
},
  { persist: true }
)
