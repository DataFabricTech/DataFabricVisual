import { defineStore } from "pinia";
import ObjectUtils from "@/utils/ObjectUtils";
import { $R, isEmpties } from "@/composables/ramda";
import { ref } from "vue";

export const useCategoryTreeStore = defineStore("categoryTreeStore", () => {
  const { $ofetch } = useNuxtApp();

  // state
  const _list = ref([]);
  const _originalList = ref([]); // 트리 형태로 가공 전 카테고리 목록 데이터
  const _tableCountList = ref([]); // 카테고리별 테이블 개수 목록(PG)
  const _toggleTargets = ref({ all: true }); // 카테고리 메뉴 토글 제어
  const _selectedItemId = ref(""); // 선택된 카테고리 메뉴 guid
  const _calledFrom = ref(null); // 좌측 카테고리 메뉴 목록 컴포넌트 호출 페이지
  const _categoryMap = ref({});
  const _dbData = ref({});

  // getters
  // 가공 전 카테고리 목록 데
  const getOriginalList = computed(() => _originalList.value);
  // 카테고리명을 키값으로 하는 카테고리 정보 목록 (한글화용)
  const getClassificationNameKr = computed(() => (key) => {
    if (_categoryMap && _categoryMap.value && _categoryMap.value[key]) {
      return _categoryMap.value[key].description;
    } else {
      return "";
    }
  });

  // 가공된 DB 목록
  const getDbDescription = computed(() => (key) => {
    if (_dbData.value[key]) {
      return _dbData.value[key].description;
    } else {
      return "-";
    }
  });
  // 가공 전 카테고리별 테이블 개수 목록
  const getTableCountList = computed(() => _tableCountList.value);
  const getCategoryList = computed(() => {
    const categories = JSON.parse(JSON.stringify(_list.value));
    return categories;
  });
  const getToggleTargets = computed(() => _toggleTargets);
  const getSelectedItemId = computed(() => _selectedItemId);
  const getCalledFrom = computed(() => _calledFrom);

  // actions
  // 카테고리 목록 컴포넌트 import 선언한 페이지 세팅. 카테고리 검색 페이지에서만 별도로 동작하기 위해 추가 작업
  const setCalledFrom = (payload) => {
    _calledFrom.value = payload && payload.from ? payload.from : null;
  };
  // 카테고리 목록 데이터 저장
  const setCategoryList = (payload) => {
    _list.value = payload;
  };
  // 트리 형태로 가공 전 카테고리 목록 데이터 저장
  const setOriginalList = (payload) => {
    _originalList.value = payload;
  };
  // 카테고리별 테이블 개수 목록 저장
  const setTableCountList = (payload) => {
    _tableCountList.value = payload;
  };
  // 초기 데이터 호출 후 카테고리 메뉴 토글 제어 위해 각 카테고리명만 저장
  const setToggleTargets = (payload) => {
    _toggleTargets.value = payload;
  };
  // 카테고리 메뉴 아이콘 클릭 시 하위 메뉴 표시 여부
  const setToggleTargetOnClick = (payload) => {
    const targetValue = _toggleTargets.value[payload];
    return (_toggleTargets.value[payload] = !targetValue);
  };
  // 트리맵 클릭 시 하위 메뉴 표시 처리
  const setToggleTargetOnClickTreemap = (payload) => {
    return (_toggleTargets.value[payload] = true);
  };
  // 선택된 카테고리의 id 저장
  const setSelectedItemId = (payload) => {
    return (_selectedItemId.value = payload);
  };
  // DB 정보 저장
  const setDbData = (payload) => {
    return (_dbData.value = payload);
  };

  // 카테고리 토글 전체 펼치기
  const expandAll = () => {
    _toggleTargets.value = Object.keys(_toggleTargets.value).reduce((acc, key) => {
      acc[key] = key === "all" ? _toggleTargets.value[key] = true : true;
      return acc;
    }, {});

    return _toggleTargets.value
  }

  // 카테고리 토글 전체 접기
  const collapseAll = () => {
    _toggleTargets.value = Object.keys(_toggleTargets.value).reduce((acc, key) => {
      acc[key] = key === "all" ? _toggleTargets.value[key] = true : false;
      return acc;
    }, {});

    return _toggleTargets.value
  };

  // 카테고리 목록 데이터 조회
  const fetchCategories = async (payload) => {
    let originalList: Array<{ [key: string]: any }> = [];

    const promises = [
      await Promise.resolve($ofetch("/api/search/category/list", { method: "GET" })), // 카테고리 목록 조회
      await Promise.resolve($ofetch("/api/search/category/counts", { method: "GET" })) // 카테고리별 테이블 개수 목록 조회
    ];

    return Promise.allSettled(promises).then((result) => {
      if (result[0].status === "fulfilled" && result[1].status === "fulfilled") {
        // 카테고리 목록
        const data = result[0].value ? result[0].value.data : [];
        // 카테고리별 테이블 개수 목록
        const tableCounts = result[1].value ? result[1].value.data : [];

        // 카테고리 목록 가공
        originalList = data;
        let newCategoryList: Array<{ [key: string]: any }> = [];

        // 하위 메뉴 토글 제어 객체 가공
        const toggleTargetObject = { all: true };
        originalList.forEach((el) => {
          // 카테고리별 테이블 개수 추가
          const { id } = el;

          toggleTargetObject[id] = false;

          const tableCountItem = tableCounts.find((count) => count.id === id);
          el.tableCount = tableCountItem ? tableCountItem.count : 0;
        });

        // 카테고리 목록 데이터 가공 시작
        newCategoryList = buildCategoryTree(originalList);

        // store에 카테고리 관련 정보 저장
        setOriginalList(originalList);
        setTableCountList(tableCounts);
        setCategoryList(newCategoryList);
        setToggleTargets(toggleTargetObject);
        setSelectedItemId("");
        setCalledFrom(payload);
      }

      return originalList;
    });
  };

  const sortDataByOrdNo = (data) => {
    // 재귀적으로 정렬하는 함수
    const sortRecursive = (items) => {
        if (!items || items.length === 0) return items;

        // 현재 배열을 ord_no 기준으로 오름차순 정렬
        items.sort((a, b) => a.ord_no - b.ord_no);

        // 하위 subTypes도 정렬
        items.forEach(item => {
            if (item.subTypes && item.subTypes.length > 0) {
                item.subTypes = sortRecursive(item.subTypes);
            }
        });

        return items;
    }

    return sortRecursive(data);
  }

  // 카테고리 데이터를 트리 구조로 변환
  const buildCategoryTree = (totalList) => {
    let categoryMap: { [key: string]: any } = {};
    let rootCategories: Array<{ [key: string]: any }> = [];

    // 각 카테고리를 Category 객체로 변환하여 map에 저장
    totalList.forEach((categoryData) => {
      categoryMap[categoryData.id] = {
        ...categoryData,
        superTypes: [],
        subTypes: []
      };
    });

    // 부모-자식 관계 설정
    Object.keys(categoryMap).forEach((key: string) => {
      const category = categoryMap[key];

      if (!isEmpties(category.parent_id)) {
        const parentCategory = categoryMap[category.parent_id];
        // 부모 Node가 있을 경우
        if (parentCategory) {
          // 현재 Node에 모든 상위 부모 ID값을 추가
          category.superTypes = [...parentCategory.superTypes, parentCategory.id];
          // 부모 Node에 현재 Node 추가
          parentCategory.subTypes.push(category);
        }
      } else {
        rootCategories.push(category);
      }
    });

    _categoryMap.value = $R.clone(categoryMap);
    sortDataByOrdNo(rootCategories)

    return rootCategories;
  };

  // DB 정보 데이터 가져오기
  const getDbData = async () => {
    let dbData = {};

    const param = {
      url: "/search/dsl",
      params: {
        typeName: "hive_db",
        offset: 0
      }
    };

    let result = await $ofetch("/api/search/dbData", { method: "POST", body: param });
    const dbEntities = ObjectUtils.getPropertyValue(result, "entities", []);
    dbEntities.map((val) => {
      if (!val.attributes.description) {
        val.attributes.description = "";
      }
      dbData[val.displayText] = val.attributes;
    });

    setDbData(dbData);

    return dbData;
  };

  return {
    _list,
    _originalList,
    _tableCountList,
    _toggleTargets,
    _selectedItemId,
    _calledFrom,
    _categoryMap,
    _dbData,
    getOriginalList,
    getClassificationNameKr,
    getDbDescription,
    getTableCountList,
    getCategoryList,
    getToggleTargets,
    getSelectedItemId,
    getCalledFrom,
    setCalledFrom,
    setCategoryList,
    setOriginalList,
    setTableCountList,
    setToggleTargets,
    setToggleTargetOnClick,
    setToggleTargetOnClickTreemap,
    setSelectedItemId,
    setDbData,
    fetchCategories,
    getDbData,
    expandAll,
    collapseAll,
  };
});
