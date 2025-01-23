import { defineStore } from "pinia";
import ObjectUtils from "@/utils/ObjectUtils";
import { $R, isEmpties } from "@/composables/ramda";
import { ref } from "vue";

export const useDeptStore = defineStore("deptStore", () => {
  const { $ofetch } = useNuxtApp();

  // state
  const _list = ref([]);
  const _originalList = ref([]); // 트리 형태로 가공 전 부서 목록 데이터
  const _tableCountList = ref([]); // 부서별 테이블 개수 목록(PG)
  const _toggleTargets = ref({ all: true }); // 부서 메뉴 토글 제어
  const _selectedItemId = ref(""); // 선택된 부서 메뉴 guid
  const _calledFrom = ref(null); // 좌측 부서 메뉴 목록 컴포넌트 호출 페이지
  const _deptMap = ref({});
  const _dbData = ref({});

  // getters
  // 가공 전 부서 목록 데이타
  const getOriginalList = computed(() => _originalList.value);
  // 부서명을 키값으로 하는 부서 정보 목록 (한글화용)
  const getClassificationNameKr = computed(() => (key) => {
    if (_deptMap && _deptMap.value && _deptMap.value[key]) {
      return _deptMap.value[key].description;
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
  // 가공 전 부서별 테이블 개수 목록
  const getTableCountList = computed(() => _tableCountList.value);
  const getDeptList = computed(() => {
    const depts = JSON.parse(JSON.stringify(_list.value));

    // 메타 데이터 통합 관리에서 호출 시 '부서 미분류' 추가
    depts.unshift({
      name: "_NOT_CLASSIFIED",
      description: "부서 미분류",
      id: "_NOT_CLASSIFIED",
      ord_no: 0,
      level: 1,
      subTypes: [],
      superTypes: []
    });
    return depts;
  });
  const getToggleTargets = computed(() => _toggleTargets);
  const getSelectedItemId = computed(() => _selectedItemId);
  const getCalledFrom = computed(() => _calledFrom);

  // actions
  // 부서 목록 컴포넌트 import 선언한 페이지 세팅. 부서 검색 페이지에서만 별도로 동작하기 위해 추가 작업
  const setCalledFrom = (payload) => {
    _calledFrom.value = payload && payload.from ? payload.from : null;
  };
  // 부서 목록 데이터 저장
  const setDeptList = (payload) => {
    _list.value = payload;
  };
  // 트리 형태로 가공 전 부서 목록 데이터 저장
  const setOriginalList = (payload) => {
    _originalList.value = payload;
  };
  // 부서별 테이블 개수 목록 저장
  const setTableCountList = (payload) => {
    _tableCountList.value = payload;
  };
  // 초기 데이터 호출 후 부서 메뉴 토글 제어 위해 각 부서명만 저장
  const setToggleTargets = (payload) => {
    _toggleTargets.value = payload;
  };
  // 부서 메뉴 아이콘 클릭 시 하위 메뉴 표시 여부
  const setToggleTargetOnClick = (payload) => {
    const targetValue = _toggleTargets.value[payload];
    return (_toggleTargets.value[payload] = !targetValue);
  };
  // 트리맵 클릭 시 하위 메뉴 표시 처리
  const setToggleTargetOnClickTreemap = (payload) => {
    return (_toggleTargets.value[payload] = true);
  };
  // 선택된 부서의 id 저장
  const setSelectedItemId = (payload) => {
    return (_selectedItemId.value = payload);
  };
  // DB 정보 저장
  const setDbData = (payload) => {
    return (_dbData.value = payload);
  };

  // 부서 목록 데이터 조회
  const fetchDepts = async (payload) => {
    let originalList: Array<{ [key: string]: any }> = [];

    const promises = [
      await Promise.resolve($ofetch("/api/search/dept/list", { method: "GET" })), // 부서 목록 조회
      await Promise.resolve($ofetch("/api/search/dept/counts", { method: "GET" })) // 부서별 테이블 개수 목록 조회
    ];

    return Promise.allSettled(promises).then((result) => {
      if (result[0].status === "fulfilled" && result[1].status === "fulfilled") {
        // 부서 목록
        const data = result[0].value ? result[0].value.data : [];
        // 부서별 테이블 개수 목록
        const tableCounts = result[1].value ? result[1].value.data : [];

        // 부서 목록 가공
        originalList = data;
        let newDeptList: Array<{ [key: string]: any }> = [];

        // 하위 메뉴 토글 제어 객체 가공
        const toggleTargetObject = { all: true };
        originalList.forEach((el) => {
          // 부서별 테이블 개수 추가
          const { id } = el;

          toggleTargetObject[id] = false;

          const tableCountItem = tableCounts.find((count) => count.id === id);
          el.tableCount = tableCountItem ? tableCountItem.count : 0;
        });

        // 부서 목록 데이터 가공 시작
        newDeptList = buildDeptTree(originalList);

        // store에 부서 관련 정보 저장
        setOriginalList(originalList);
        setTableCountList(tableCounts);
        setDeptList(newDeptList);
        setToggleTargets(toggleTargetObject);
        setSelectedItemId("");
        setCalledFrom(payload);
      }

      return originalList;
    });
  };

  // 부서 데이터를 트리 구조로 변환
  const buildDeptTree = (totalList) => {
    let deptMap: { [key: string]: any } = {};
    let rootDepts: Array<{ [key: string]: any }> = [];

    // 각 부서를 Dept 객체로 변환하여 map에 저장
    totalList.forEach((deptData) => {
      deptMap[deptData.id] = {
        ...deptData,
        superTypes: [],
        subTypes: []
      };
    });

    // 부모-자식 관계 설정
    Object.keys(deptMap).forEach((key: string) => {
      const dept = deptMap[key];

      if (!isEmpties(dept.parent_id)) {
        const parentDept = deptMap[dept.parent_id];
        // 부모 Node가 있을 경우
        if (parentDept) {
          // 현재 Node에 모든 상위 부모 ID값을 추가
          dept.superTypes = [...parentDept.superTypes, parentDept.id];
          // 부모 Node에 현재 Node 추가
          parentDept.subTypes.push(dept);
        }
      } else {
        rootDepts.push(dept);
      }
    });

    _deptMap.value = $R.clone(deptMap);

    return rootDepts;
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
    _deptMap,
    _dbData,
    getOriginalList,
    getClassificationNameKr,
    getDbDescription,
    getTableCountList,
    getDeptList,
    getToggleTargets,
    getSelectedItemId,
    getCalledFrom,
    setCalledFrom,
    setDeptList,
    setOriginalList,
    setTableCountList,
    setToggleTargets,
    setToggleTargetOnClick,
    setToggleTargetOnClickTreemap,
    setSelectedItemId,
    setDbData,
    fetchDepts,
    getDbData
  };
});
