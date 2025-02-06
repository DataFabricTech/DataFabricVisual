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
  const _selectedDeptLevelMaxOrdNo = ref(0);
  const _selectedSubDeptLevelMaxOrdNo = ref(0);

  const predefinedDeptCodes = {
    'IT':'IT',
    'PR및커뮤니케이션':'PRC',
    'R&D':'RD',
    '개발':'DEV',
    '경영지원':'ADMIN',
    '고객경험':'CX',
    '고객서비스':'CS',
    '고객관리':'CRM',
    '고객서비스':'CSERV',
    '고객서비스센터':'CSERVCENTER',
    '고객지원':'CS',
    '고객지원센터':'CSCENTER',
    '공급망관리':'SCM',
    '공정관리':'PROC',
    '공정관리센터':'PROCCENTER',
    '교육':'EDU',
    '교육센터':'EDUCENTER',
    '구매및공급':'P&S',
    '구매':'PUR',
    '구매관리':'PURM',
    '구매관리센터':'PURMCENTER',
    '그룹관리':'GRM',
    '기술연구':'TR',
    '기술지원':'TS',
    '기술연구소':'TECHLAB',
    '기술연구소센터':'TECHLABCENTER',
    '기술지원':'TS',
    '기술지원센터':'TSCENTER',
    '기획':'PLN',
    '기획센터':'PLANCENTER',
    '내부감사':'AUDIT',
    '내부감사센터':'AUDITCENTER',
    '대리점관리':'AGM',
    '대외협력':'EXR',
    '데이터분석':'DA',
    '데이터분석':'DA',
    '디자인':'DSN',
    '디자인센터':'DESIGNCENTER',
    '디지털마케팅':'DM',
    '디지털혁신':'DI',
    '리스크관리':'RISK',
    '리스크관리센터':'RISKCENTER',
    '마케팅':'MKT',
    '모바일개발':'MD',
    '물류':'LOG',
    '물류관리':'LOGM',
    '물류관리센터':'LOGMCENTER',
    '법무':'LEGAL',
    '법무지원':'LEGALSUP',
    '법무지원센터':'LEGALSUPCENTER',
    '보안':'SEC',
    '브랜드관리':'BM',
    '비즈니스개발':'BD',
    '비즈니스분석':'BA',
    '사무지원':'SA',
    '사업개발':'BD',
    '사업운영':'BO',
    '사업개발':'BD',
    '사업개발센터':'BDCENTER',
    '사업관리':'BM',
    '사업관리센터':'BMCENTER',
    '생산':'PROD',
    '생산관리':'PRODM',
    '생산관리센터':'PRODMCENTER',
    '서비스관리':'SM',
    '서비스운영':'SO',
    '서비스':'SERV',
    '서비스센터':'SERVCENTER',
    '소셜미디어':'SM',
    '시설관리':'FM',
    '시설관리':'FAC',
    '시설관리센터':'FACCENTER',
    '시스템관리':'SYS',
    '시장조사':'MR',
    '업무프로세스관리':'BPM',
    '연구개발':'RND',
    '연구소':'LAB',
    '연구소센터':'LABCENTER',
    '영업':'SAL',
    '운영':'OPS',
    '운영센터':'OPSCENTER',
    '위험관리':'RM',
    '인사':'HR',
    '인사관리':'HRM',
    '인사관리센터':'HRMCENTER',
    '인프라':'INF',
    '재고관리':'INV',
    '재고관리센터':'INVCENTER',
    '재무계획':'FP',
    '재무':'FIN',
    '전략':'STR',
    '전략기획':'STR',
    '전략센터':'STRATCENTER',
    '전문서비스':'PS',
    '정보기술':'IT',
    '정보보안':'SEC',
    '제품관리':'PM',
    '제품개발':'PD',
    '제품개발센터':'PDCENTER',
    '제품관리':'PM',
    '제품관리센터':'PMCENTER',
    '제품연구소':'PRODLAB',
    '제품연구소센터':'PRODLABCENTER',
    '채용':'REC',
    '총무':'ADM',
    '총무센터':'GENAFFCENTER',
    '커뮤니티관리':'CM',
    '커스터머리서치':'CR',
    '컨설팅':'CONS',
    '통합인사':'HR',
    '파트너십':'PRT',
    '품질관리':'QA',
    '품질보증':'QAM',
    '품질보증센터':'QAMCENTER',
    '프로그래밍':'PROG',
    '혁신':'INNO',
    '혁신센터':'INNOCENTER',
    '협력사관리':'VM',
    '홍보':'PR',
    '홍보마케팅':'PRMKT',
    '홍보마케팅센터':'PRMKTCENTER',
    '환경안전':'EHS',
    '환경안전센터':'EHSCENTER',
    '회계':'ACC',
    '테스트':'TEST',
    'TEST':'TEST',
    '인사관리부서':'HR',
    '재무부서':'FIN',
    '마케팅부서':'MKT',
    '영업부서':'SAL',
    '개발부서':'DEV',
    '품질관리부서':'QA',
    '정보기술부서':'IT',
    '운영부서':'OPS',
    '총괄':'GEN',
    '총괄관리':'GM',
    '총괄부서':'MGT',
    '총괄기획':'GP',
    '총괄운영':'GO',
    '제품':'PD',
  };

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

    return depts;
  });
  const getToggleTargets = computed(() => _toggleTargets);
  const getSelectedItemId = computed(() => _selectedItemId);
  const getCalledFrom = computed(() => _calledFrom);
  const getSelectedDeptLevelMaxOrdNo = computed(() => _selectedDeptLevelMaxOrdNo);
  const getSelectedSubDeptLevelMaxOrdNo = computed(() => _selectedSubDeptLevelMaxOrdNo);
  const getGenerateDeptCode = computed(() => (deptName) => {
    if (predefinedDeptCodes[deptName]) {
      return predefinedDeptCodes[deptName];
    }
  
    if (predefinedDeptCodes[deptName.trim()]) {
      return predefinedDeptCodes[deptName.trim()];
    }
  
    if (predefinedDeptCodes[deptName.replace(/(팀|부서|그룹|사업)/g, '').trim()]) {
      return predefinedDeptCodes[deptName.replace(/(팀|부서|그룹|사업)/g, '').trim()];
    }
  
    const words = deptName.split(' ');
    let deptCode = [];
  
    words.forEach(word => {
      if (predefinedDeptCodes[word]) {
          deptCode.push(predefinedDeptCodes[word]);
      } else {
        deptCode.push('xxx');
      }
    });
  
    return deptCode.join('_');
    // return deptName.split(' ').map(word => word.charAt(0).toUpperCase()).join('') + deptName.length;
  });
  // //
  // const deptDictionary = deptNames.reduce((acc, deptName) => {
  //   const deptCode = generateDeptCode(deptName);
  //   acc[deptCode] = deptName;
  //   return acc;
  // }, {});
  // //
  // const createDeptInfo = (deptCode, parentDeptCode = null) => {
  //   const deptName = deptDictionary[deptCode];
  //   if (!deptName) {
  //     throw new Error(`Invalid department code: ${deptCode}`);
  //   }
  
  //   return {
  //     dept_id: deptCode,
  //     dept_name: deptName,
  //     parent_dept_id: parentDeptCode,
  //     description: `${deptName} Department`,
  //     dept_level: parentDeptCode ? 2 : 1, 
  //     ord_no: 1, 
  //   };
  // };
  

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
  //
  const setSelectedDeptLevelMaxOrdNo = (payload) => {
    return (_selectedDeptLevelMaxOrdNo.value = payload);
  };
  //
  const setSelectedSubDeptLevelMaxOrdNo = (payload) => {
    return (_selectedSubDeptLevelMaxOrdNo.value = payload);
  };
  // 전체 트리 펼치기
  const expandAll = (deptList) => {
    deptList.forEach(dept => {
      _toggleTargets.value[dept.id] = true;
    });
  };
  // 전체 트리 접기
  const collapseAll = (deptList) => {
    deptList.forEach(dept => {
      _toggleTargets.value[dept.id] = false;
    });
  };
  //
  const setGenerateDeptCode = (payload) => {
    return _generateDeptCode.value = payload;
  };
  

  // 부서 목록 데이터 조회
  const fetchDepts = async (payload:any, word:string) => {
    let originalList: Array<{ [key: string]: any }> = [];
    word = word ? word : "";

    const promises = [
      await Promise.resolve($ofetch(`/api/depts?word=${word}`, { method: "GET" })), // 부서 목록 조회
      // await Promise.resolve($ofetch("/api/search/dept/counts", { method: "GET" })) // 부서별 테이블 개수 목록 조회
    ];

    return Promise.allSettled(promises).then((result) => {
      // if (result[0].status === "fulfilled" && result[1].status === "fulfilled") {
      if( result[0]) {
        // 부서 목록
        const data = result[0].value ? result[0].value.data : [];

        // 부서별 테이블 개수 목록
        // const tableCounts = result[1].value ? result[1].value.data : [];
        const tableCounts = [];

        // 부서 목록 가공
        originalList = data;
        let newDeptList: Array<{ [key: string]: any }> = [];

        // // 하위 메뉴 토글 제어 객체 가공
        const toggleTargetObject = { all: true };
        originalList.forEach((el) => {
          // 부서별 테이블 개수 추가
          const { id } = el;

          toggleTargetObject[id] = false;

          const tableCountItem = tableCounts.find((count) => count.id === id);
          el.tableCount = tableCountItem ? tableCountItem.count : 0;
        });

        // // 부서 목록 데이터 가공 시작
        newDeptList = buildDeptTree(originalList);

        // // store에 부서 관련 정보 저장
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
      deptMap[deptData.dept_cd] = {
        ord_no: deptData.ord_no,
        level: deptData.dept_level,
        parent_id: deptData.parent_dept_id,
        parent_nm: deptData.parent_dept_nm,
        file_id: null,
        name: deptData.dept_nm,
        description: deptData.description,
        id: deptData.dept_cd,
        dept_id: null,
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
    _selectedDeptLevelMaxOrdNo,
    getOriginalList,
    getClassificationNameKr,
    getDbDescription,
    getTableCountList,
    getDeptList,
    getToggleTargets,
    getSelectedItemId,
    getCalledFrom,
    getSelectedDeptLevelMaxOrdNo,
    getSelectedSubDeptLevelMaxOrdNo,
    getGenerateDeptCode,
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
    getDbData,
    setSelectedDeptLevelMaxOrdNo,
    setSelectedSubDeptLevelMaxOrdNo,
    expandAll,
    collapseAll
  };
});
