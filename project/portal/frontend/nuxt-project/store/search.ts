import { defineStore } from "pinia";

export const useSearchStore = defineStore('searchStore', () => {
  const { $ofetch } = useNuxtApp()
  const { router } = useRouter()

  // state
  const _searchParams = ref({
    query: "",
    systemtype: "",
    metatype: "",
    limit: 10,
    offset: 0,
    classification: {
      id: "",
      name: "",
      description: ""
    },
    sortOrder: "",
    entityFilters: {}
  });


  // getters
  const getSearchParams = computed(() => _searchParams.value)
  const getQuery = computed(() => _searchParams.value.query)
  const getSystemtype = computed(() => _searchParams.value.systemtype)
  const getMetatype = computed(() => _searchParams.value.metatype)
  const getLimit = computed(() => _searchParams.value.limit)
  const getOffset = computed(() => _searchParams.value.offset)
  const getClassification = computed(() => _searchParams.value.classification)
  const getSortOrder = computed(() => _searchParams.value.sortOrder)
  const getEntitiyFilters = computed(() => _searchParams.value.entityFilters)

  // action
  const setSearchParams = (data: any) => { _searchParams.value = data }
  const setQuery = (data: any) => { _searchParams.value.query = data }
  const setSystemtype = (data: any) => { _searchParams.value.systemtype = data }
  const setMetatype = (data: any) => { _searchParams.value.metatype = data }
  const setLimit = (data: any) => { _searchParams.value.limit = data }
  const setOffset = (data: any) => { _searchParams.value.offset = data }
  const setClassification = (data: any) => { _searchParams.value.classification = data }
  const setSortOrder = (data: any) => { _searchParams.value.sortOrder = data }
  const setEntitiyFilters = (data: any) => { _searchParams.value.entityFilters = data }


  return {
    _searchParams,
    getSearchParams,
    getQuery,
    getSystemtype,
    getMetatype,
    getLimit,
    getOffset,
    getClassification,
    getSortOrder,
    getEntitiyFilters,
    setSearchParams,
    setQuery,
    setSystemtype,
    setMetatype,
    setLimit,
    setOffset,
    setClassification,
    setSortOrder,
    setEntitiyFilters
  }
},
  { persist: true }
)
