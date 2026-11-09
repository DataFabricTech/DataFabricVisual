import { defineStore } from "pinia";
import ObjectUtils from '@/utils/ObjectUtils';

export const useCategoryStore = defineStore('categoryStore', () => {
  const { $ofetch } = useNuxtApp()

  // state
  const _list = ref([])
  const _originalList = ref([])             // 트리 형태로 가공 전 카테고리 목록 데이터(Atlas API)
  const _tableCountList = ref([])           // 카테고리별 테이블 개수 목록(PG)
  const _toggleTargets = ref({ all: true }) // 카테고리 메뉴 토글 제어
  const _selectedItemGuid = ref('')         // 선택된 카테고리 메뉴 guid
  const _calledFrom = ref(null)             // 좌측 카테고리 메뉴 목록 컴포넌트 호출 페이지
  const _categoryMap = ref({})
  const _dbData = ref({})
  const _searchParams = ref({
    word: "",
    detailFilters: {},
    pagingObj: {}
  }) // 상세
  const _mainSearch = ref(false) // 메인화면에서 검색기능을 이용해 통합검색으로 이동했는지 여부
  const _mainSelectCategory = ref(null) // 메인화면 차트에서 클릭한 카테고리

  // getters
  // 가공 전 카테고리 목록 데
  const getOriginalList = computed(() => _originalList.value)
  // 카테고리명을 키값으로 하는 카테고리 정보 목록 (한글화용)
  const getClassificationNameKr = computed(() => (key) => {
    if (_categoryMap && _categoryMap.value && _categoryMap.value[key]) {
      return _categoryMap.value[key].description
    } else {
      return ""
    }

  })
  // 가공된 DB 목록
  const getDbDescription = computed(() => (key) => {
    if (_dbData.value[key]) {
      return _dbData.value[key].description
    } else {
      return '-'
    }
  })
  // 가공 전 카테고리별 테이블 개수 목록
  const getTableCountList = computed(() => _tableCountList.value)
  const getCategoryList = computed(() => {
  const categories = JSON.parse(JSON.stringify(_list.value));

    // 메타 데이터 통합 관리에서 호출 시 '카테고리 미분류' 추가
    categories.unshift({
      name: '_NOT_CLASSIFIED',
      description: '미분류',
      guid: '_NOT_CLASSIFIED',
      subTypes: [],
      superTypes: [],
    });
    return categories;
  })
  const getToggleTargets = computed(() => _toggleTargets)
  const getSelectedItemGuid = computed(() => _selectedItemGuid)
  const getCalledFrom = computed(() => _calledFrom)

  const getSearchParams = computed(() => _searchParams.value)
  const getMainSearch = computed(() => _mainSearch.value)
  const getMainSelectCategory = computed(() => _mainSelectCategory.value)


  // actions
  // 카테고리 목록 컴포넌트 import 선언한 페이지 세팅. 카테고리 검색 페이지에서만 별도로 동작하기 위해 추가 작업
  const setCalledFrom = (payload) => { _calledFrom.value = payload && payload.from ? payload.from : null }
  // 카테고리 목록 데이터 저장
  const setCategoryList = (payload) => { _list.value = payload }
  // 트리 형태로 가공 전 카테고리 목록 데이터 저장
  const setOriginalList = (payload) => {
    for (let i = 0; i < payload.length; i++) {
      _categoryMap.value[payload[i].name] = payload[i]
    }
    return _originalList.value = payload;
  }
  // 카테고리별 테이블 개수 목록 저장
  const setTableCountList = (payload) => { _tableCountList.value = payload; }
  // 초기 데이터 호출 후 카테고리 메뉴 토글 제어 위해 각 카테고리명만 저장
  const setToggleTargets = (payload) => { _toggleTargets.value = payload }
  // 카테고리 메뉴 아이콘 클릭 시 하위 메뉴 표시 여부
  const setToggleTargetOnClick = (payload) => {
    const targetValue = _toggleTargets.value[payload];
    return _toggleTargets.value[payload] = !targetValue;
  }
  // 트리맵 클릭 시 하위 메뉴 표시 처리
  const setToggleTargetOnClickTreemap = (payload) => { return _toggleTargets.value[payload] = true }
  // 선택된 카테고리의 guid 저장
  const setSelectedItemGuid = (payload) => { return _selectedItemGuid.value = payload }
  // DB 정보 저장
  const setDbData = (payload) => { return _dbData.value = payload }
  // 검색조건 저장
  const setSearchParams = (payload) => { return _searchParams.value = payload }
  // 상세필터 조건 저장
  const setSearchParamsDetailFilters = (payload) => { return _searchParams.value.detailFilters = payload }
  // 페이징 조건 저장
  const setSearchParamsPagingObj = (payload) => { return _searchParams.value.pagingObj = payload }
  // 메인화면에서 검색기능으로 통합검색으로 이동했는지 여부
  const setMainSearch = (payload) => { return _mainSearch.value = payload}

  const setMainSelectCategory = (payload) => { return _mainSelectCategory.value = payload }

  // 카테고리 목록 데이터 조회
  const fetchCategories = async (payload) => {
    let originalList = [];

    const promises = [
      await Promise.resolve($ofetch("/api/search/category/list", { method: "GET" })), // 카테고리 목록 조회
      await Promise.resolve($ofetch('/api/search/category/counts', { method: "GET" })), // 카테고리별 테이블 개수 목록 조회
      await Promise.resolve($ofetch('/api/search/category/dbList', { method: "GET" })), // 카테고리 정렬용 목록 조회
    ];

    return Promise.allSettled(promises)
      .then(result => {
        if (result[0].status === 'fulfilled' && result[1].status === 'fulfilled' && result[2].status === 'fulfilled') {
          const data = result[0].value.data;
          const tableCounts = result[1].value ? result[1].value.data : [];
          const categoryDbList = result[2].value.data;

          // Atlas에서 카테고리 목록 조회
          if (data && data.hasOwnProperty('classificationDefs')) {
            originalList = ObjectUtils.getPropertyValue(data, 'classificationDefs');
          }

          // 하위 메뉴 토글 제어 객체 가공
          const toggleTargetObject = { 'all': true };
          originalList.forEach(el => { // 카테고리별 테이블 개수 추가
            const { name } = el;

            toggleTargetObject[name] = false;

            const tableCountItem = tableCounts.find(count => count.name === name);
            el.tableCount = tableCountItem ? tableCountItem.count : 0;
          });

          // 카테고리 목록 데이터 가공 시작 - 최상위 카테고리부터 가공 시작
          const depth1 = originalList.sort((a, b) => a.createTime - b.createTime)
            .filter(el => el.superTypes.length === 0);

          result = depth1.map(el => {
            const { name = '', subTypes = [], guid } = el;

            // 카테고리별 depth 추가
            let depth = 1;

            const matched = originalList.find(data => data.guid === guid);
            if (matched) {
              matched.depth = depth;
            }

            return {
              ...el,
              subTypes: setSubTypes(originalList, el.guid, subTypes, name, depth),
            };
          });

          // 트리 구조로 가공된 카테고리의 ord_no 기준 정렬 처리
          result.forEach(CE => {
            const item = categoryDbList.find(DE => CE.name === DE.name);
            if (CE.name === "_NOT_CLASSIFIED") {
              CE.ord_no = -1;
            }
            if (item) {
              CE.ord_no = item.ord_no;
              CE.file_id = item.file_id;
            }
            if (CE.subTypes) {
              CE.subTypes.forEach(CES => {
                const sitem = categoryDbList.find(DE => CES.name === DE.name);
                if (sitem) {
                  CES.ord_no = sitem.ord_no;
                }
                if (CES.subTypes) {
                  CES.subTypes.forEach(CESS => {
                    const ssitem = categoryDbList.find(DE => CESS.name === DE.name);
                    if (ssitem) {
                      CESS.ord_no = ssitem.ord_no;
                    }
                  }
                  )
                }
              });
            }
          });

          result.sort((a, b) => {
            if (a.ord_no === undefined && b.ord_no === undefined) {
              return 0; // 두 값이 모두 null이면 위치 변경 없음
            } else if (a.ord_no === undefined) {
              return 1; // a의 ord_no가 null이면 b보다 뒤로 보냄
            } else if (b.ord_no === undefined) {
              return -1; // b의 ord_no가 null이면 a보다 앞으로 보냄
            }
            return Number(a.ord_no) - Number(b.ord_no); // 두 값이 모두 숫자일 경우 오름차순 정렬
          });
          //1단계 정렬

          result.forEach(element => {
            element.subTypes.sort((a, b) => {
              if (a.ord_no === undefined && b.ord_no === undefined) {
                return 0; // 두 값이 모두 null이면 위치 변경 없음
              } else if (a.ord_no === undefined) {
                return 1; // a의 ord_no가 null이면 b보다 앞으로 보냄
              } else if (b.ord_no === undefined) {
                return -1; // b의 ord_no가 null이면 a보다 뒤로 보냄
              }
              return Number(a.ord_no) - Number(b.ord_no); // 두 값이 모두 숫자일 경우 오름차순 정렬
            });
            element.subTypes.forEach(selement => {
              selement.subTypes.sort((a, b) => {
                if (a.ord_no === undefined && b.ord_no === undefined) {
                  return 0; // 두 값이 모두 null이면 위치 변경 없음
                } else if (a.ord_no === undefined) {
                  return 1; // a의 ord_no가 null이면 b보다 앞으로 보냄
                } else if (b.ord_no === undefined) {
                  return -1; // b의 ord_no가 null이면 a보다 뒤로 보냄
                }
                return Number(a.ord_no) - Number(b.ord_no); // 두 값이 모두 숫자일 경우 오름차순 정렬
              }
              )
            });
          });

          // result.sort((a, b) => b.ord_no - a.ord_no);
          // result.forEach(element => {
          //   element.subTypes.sort((a,b) => b.ord_no - a.ord_no);  // 역방향 정렬
          // });

          // store에 카테고리 관련 정보 저장
          setOriginalList(originalList);
          setTableCountList(tableCounts);
          setCategoryList(result);
          setToggleTargets(toggleTargetObject);
          setSelectedItemGuid('');
          setCalledFrom(payload);
        }

        return originalList;
      });
  }

  // 하위 카테고리 가공
  const setSubTypes = (totalList, guid, subTypes = [], superItemName, depth) => {
    let children = subTypes;
    ++depth;

    // 하위 카테고리가 있는 경우
    if (subTypes.length > 0) {
      children = totalList.filter(item => item.superTypes.includes(superItemName));

      // 하위 카테고리에 추가 하위 카테고리 있을 경우 가공
      children.forEach(child => {
        if (child.subTypes.length > 0) {
          child.subTypes = setSubTypes(totalList, child.guid, child.subTypes, child.name, depth);
        } else {
          // 더 이상 하위 카테고리가 없을 경우 해당 카테고리의 메뉴 depth 저장
          const matched = totalList.find(data => data.guid === child.guid);
          if (matched) {
            matched.depth = depth;
          }
        }
      });
    } else {
      // 더 이상 하위 카테고리가 없을 경우 해당 카테고리의 메뉴 depth 저장
      const matched = totalList.find(data => data.guid === guid);
      if (matched) {
        matched.depth = depth;
      }
    }

    return children;
  };

  // DB 정보 데이터 가져오기
  const getDbData = async () => {
    let dbData = {}

    const param = {
      url: '/search/dsl',
      params: {
        typeName: 'hive_db',
        offset: 0
      }
    };

    let result = await $ofetch("/api/search/dbData", { method: "POST", body: param})
    const dbEntities = ObjectUtils.getPropertyValue(result, 'entities', []);
    dbEntities.map((val) => {
      if (!val.attributes.description) {
        val.attributes.description = ""
      }
      dbData[val.displayText] = val.attributes
    })

    setDbData(dbData)

    return dbData
  }

  return {
    _list,
    _originalList,
    _tableCountList,
    _toggleTargets,
    _selectedItemGuid,
    _calledFrom,
    _categoryMap,
    _dbData,
    _mainSearch,
    _mainSelectCategory,
    getOriginalList,
    getClassificationNameKr,
    getDbDescription,
    getTableCountList,
    getCategoryList,
    getToggleTargets,
    getSelectedItemGuid,
    getCalledFrom,
    getSearchParams,
    getMainSearch,
    getMainSelectCategory,
    setCalledFrom,
    setCategoryList,
    setOriginalList,
    setTableCountList,
    setToggleTargets,
    setToggleTargetOnClick,
    setToggleTargetOnClickTreemap,
    setSelectedItemGuid,
    setDbData,
    setSearchParams,
    setSearchParamsPagingObj,
    fetchCategories,
    getDbData,
    setMainSearch,
    setMainSelectCategory,
    setSearchParamsDetailFilters,
  }
})
