import { defineStore } from "pinia";

export const useSearchStore = defineStore('searchStore', () => {
  const { $ofetch } = useNuxtApp()
  const { router } = useRouter()

  // state
  const _searchParams = ref({
    query: "",
    detailFilters: {},
    pagingObj: {},
    system_type: "",
    meta_type: "",
    tag: "",
    limit: 10,
    offset: 0,
    classification: {
      id: "",
      name: "",
      description: ""
    },
    sortBy: "createtime",
    sortOrder: "",
    entityFilters: {},
  }); // 상세
  const _mainSearch = ref(false) // 메인화면에서 검색기능을 이용해 통합검색으로 이동했는지 여부
  const _mainSelectCategory = ref(null) // 메인화면 차트에서 클릭한 카테고리


  // getters
  const getSearchParams = computed(() => _searchParams.value)
  const getQuery = computed(() => _searchParams.value.query)
  const getSystemType = computed(() => _searchParams.value.system_type)
  const getMetaType = computed(() => _searchParams.value.meta_type)
  const getLimit = computed(() => _searchParams.value.limit)
  const getOffset = computed(() => _searchParams.value.offset)
  const getClassification = computed(() => _searchParams.value.classification)
  const getSortOrder = computed(() => _searchParams.value.sortOrder)
  const getEntityFilters = computed(() => _searchParams.value.entityFilters)

  const getMainSearch = computed(() => _mainSearch.value)
  const getMainSelectCategory = computed(() => _mainSelectCategory.value)

  // action
  const setSearchParams = (data: any) => { _searchParams.value = data }
  // 상세필터 조건 저장
  const setSearchParamsDetailFilters = (payload) => { return _searchParams.value.detailFilters = payload }
  // 페이징 조건 저장
  const setSearchParamsPagingObj = (payload) => { return _searchParams.value.pagingObj = payload }
  const setQuery = (data: any) => { _searchParams.value.query = data }
  const setSystemType = (data: any) => { _searchParams.value.system_type = data }
  const setMetaType = (data: any) => { _searchParams.value.meta_type = data }
  const setLimit = (data: any) => { _searchParams.value.limit = data }
  const setOffset = (data: any) => { _searchParams.value.offset = data }
  const setClassification = (data: any) => { _searchParams.value.classification = data }
  const setSortOrder = (data: any) => { _searchParams.value.sortOrder = data }
  const setEntityFilters = (data: any) => { _searchParams.value.entityFilters = data }
  // 메인화면에서 검색기능으로 통합검색으로 이동했는지 여부
  const setMainSearch = (payload) => { return _mainSearch.value = payload}

  const setMainSelectCategory = (payload) => { return _mainSelectCategory.value = payload }


  return {
    _searchParams,
    getSearchParams,
    getQuery,
    getSystemType,
    getMetaType,
    getLimit,
    getOffset,
    getClassification,
    getSortOrder,
    getEntityFilters,
    getMainSearch,
    getMainSelectCategory,
    setSearchParams,
    setSearchParamsPagingObj,
    setSearchParamsDetailFilters,
    setQuery,
    setSystemType,
    setMetaType,
    setLimit,
    setOffset,
    setClassification,
    setSortOrder,
    setEntityFilters,
    setMainSearch,
    setMainSelectCategory,
  }
},
  { persist: true }
)
