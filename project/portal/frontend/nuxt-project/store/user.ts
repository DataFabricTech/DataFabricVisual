import { defineStore } from "pinia";
import { computed } from "vue";

export const useUserStore = defineStore('userStore', () => {
  const { $ofetch } = useNuxtApp()
  const { router } = useRouter()

  // state
  const _timeoutTimer = ref(null)
  const _user = ref({
    user_id: "",
    username: "",
    authority: "",
    userMenu: "",
    adminMenu: "",
  })
  const _tab = ref({
    adminPath: '/admin/data/status/lookup', // 사용자 화면에서 메뉴바의 'ADMIN' 버튼 클릭 시 이동 대상 메뉴 path
    userPath: '/search/keyword', // 관리자 화면에서 메뉴바의 '사용자홈' 버튼 클릭 시 이동 대상 메뉴 path
    currentPath: '/',   // 현재 위치 메뉴 path
    currentPathDepth1: '',  // 현재 위치 메뉴의 상위 메뉴명(1 depth)
    currentPathDepth2: ''  // 현재 위치 메뉴명
  })
  const _guid = ref('') // 상세조회 페이지 진입 시 조회 파라미터로 사용할 guid
  const _fromPath = ref('') // 이전 페이지 경로
  const _params = ref({ // 통합 검색, 메타 데이터 통합 관리 화면에서 엔티티 목록 조회 시 사용한 파라미터
    path: '' // 파라미터 출처 페이지 경로
  })
  const _currentSearchTab = ref(0) // 통합검색 탭 (0: 데이터 레이크, 1: 외부 데이터)
  const _enableLoader = ref(false) // loader 컴포넌트 활성화 여부 flag
  const _forceRenderLoader = ref(false) // loader 컴포넌트 강제 활성화 여부 flag
  const _answerId = ref(-1) // 데이터요청 상세화면 진입시 조회 파라미터로 사용할 답변id
  const _faqId = ref(null) // 메인화면에서 FAQ 클릭하여 진입 시 열어두기 위해 사용
  const _sampleDataCount = ref(10)  // 상세 조회에 있는 샘플데이터에 대해 한번에 보여주는 row 갯수

  // getters
  const getUser = computed(() => _user.value)
  const getTab = computed(() => _tab.value)
  const getGuid = computed(() => _guid.value)
  const getFromPath = computed(() => _fromPath.value)
  const getCurrentSearchTab = computed(() => _currentSearchTab.value)
  const getEnableLoader = computed(() => _enableLoader.value)
  const getForceRenderLoader = computed(() => _forceRenderLoader.value)
  const getAnswerId = computed(() => _answerId.value)
  const getParams = computed(() => _params.value)
  const getFaqId = computed(() => _faqId.value)
  const getSampleDataCount = computed(() => _sampleDataCount.value)
  const getTimeoutTimer = computed(() => _timeoutTimer.value)

  // action
  const setUser = (data: any) => { _user.value = data }
  const setTab = (data: any) => { _tab.value = data }
  const setGuid = (data: any) => { _guid.value = data }
  const setFromPath = (data: any) => { _fromPath.value = data }
  const setCurrentSearchTab = (data: any) => { _currentSearchTab.value = data }
  const setEnableLoader = (data: any) => { _enableLoader.value = data }
  const setForceRenderLoader = (data: any) => { _forceRenderLoader.value = data }
  const setAnswerId = (data: any) => { _answerId.value = data}
  const setParams = (data: any) => { _params.value = data}
  const setFaqId = (data: any) => { _faqId.value = data}
  const setSampleDataCount = (data: any) => { _sampleDataCount.value = data}
  const setTimeoutTimer = (data: any) => { _timeoutTimer.value = data}

  const getCsrfToken = () => { return true }
  // router 변경 이벤트
  const onChangeRouter = (context: { state: { _user: any; _tab: any; }; }, payload: any) => {
    const { state: { _user, _tab }} = context;
    const {
      to: { fullPath: originalFullPath, params },
      from
    } = payload;

    let fullPath = originalFullPath;

    // 통합 검색 > 데이터 검색 > 외부 데이터인 경우 현재 메뉴 데이터와 일치하도록 경로를 수정
    // fullPath가 '/search/external/detail'인 경우,이를 '/search/external'로 변경하여 메뉴명(menu_path)을 올바르게 매칭하도록 한다.
    if (fullPath.includes('/search/external/detail')) {
      fullPath = '/search/external'
    }

    if (fullPath !== _tab.currentPath) {
      // 이동하려는 메뉴 정보를 추출
      const { targetMenu, menuId } = getTargetMenu(_user, _tab, fullPath);

      // 상세데이터 조회 페이지로 이동 시 guid 저장
      if (/\/detail\b/.test(fullPath) && params.guid) {
        setGuid(params.guid);
      }

      setTab(targetMenu);
      insertMenuHistory(menuId);
    }

    // 직전 페이지 경로 저장
    // let fromPath = '';
    // if (from && from.fullPath) {
    //   fromPath = from.fullPath;
    // }
    // setFromPath(fromPath);
  }

  // 메뉴 사용 이력 insert
  const insertMenuHistory = async (payload: any) => {
    try {
      const res = await $ofetch(`/api/menu/click?menuID=${payload}&userID=${_user.value.user_id}&deptID=${_user.value.dept_nm}`)
    } catch (e) {
      console.log(e)
    }
  }

  // router param 또는 local storage에서 파라미터로 사용할 guid 추출하여 반환
  const getParameterGuid = async () => {
    let guid = '';

    const guidFromStore = _guid.value;
    const guidFromRouterQuery = ObjectUtils.getPropertyValue(router, 'currentRoute/query/guid', '')

    if (guidFromRouterQuery) {
      guid = guidFromRouterQuery;
    } else if (!guidFromRouterQuery && guidFromStore) {
      guid = guidFromStore;
    }

    return guid;
  }

  // 검색어 저장 대상 페이지 경로 (통합 검색, 메타 데이터 통합 관리)
  const SEARCH_PAGE_PATH = ['/search/keyword', '/admin/data/meta-data/manage'];
  const removeStoreGuid = (payload: any) => {
    const pattern = /detail\b/;
    const { to = '', from = '' } = payload;

    // 이동 전 페이지는 상세 조회 페이지이고 이동 후 페이지는 검색어 저장 대상 페이지가 아닐 경우
    if (
      pattern.exec(from)
      && !SEARCH_PAGE_PATH.includes(to)
    ) {
      setGuid('');
    }
  }

  // 라우터 목록 중에서 이동하고자 하는 메뉴 항목 탐색
  const getRouterName = (routes: any, targetMenuPath: any) => routes.find((el: { path: any; }) => el.path === targetMenuPath);

  // 이동하려는 메뉴 path 기반으로 store에 저장할 메뉴 object & 메뉴 ID 반환
  const getTargetMenu = (user: { adminMenu: any; userMenu: any; }, tab: any, targetPath: any) => {
    const targetMenu = {
      ...tab,
      currentPath: targetPath,
    };
    let menuId = '';

    if ( targetPath === '/main' ) {
      menuId = 'MAIN';

      targetMenu.userPath = targetPath;
      targetMenu.currentPath = '/';
      targetMenu.currentPathDepth1 = '';
      targetMenu.currentPathDepth2 = '';
    } else {
      const menu = [
        ...JSON.parse(JSON.stringify(user.adminMenu)),
        ...JSON.parse(JSON.stringify(user.userMenu))
      ];

      const pattern = /\/\d+$/g;
      if ( pattern.test(targetPath) ) {
        // 화면 이동 시 특정 게시물 상세 조회 화면으로 이동하는 경우, 타겟 메뉴 검색 위해 path 가공
        targetPath = targetPath.replace(pattern, '');
      }

      // 엔티티 상세 조회 페이지로의 이동 여부 체크
      const isToDetailPage = /\/detail\b/.test(targetPath);

      for ( let i = 0; i < menu.length; i++ ) {
        const item = menu[i];
        const matched = item.child.find((childMenu: { menu_path: any; }) => childMenu.menu_path === targetPath);
        const matched_detail = item.child.find((childMenu: { menu_path: any; }) => {
          const detailHowIndex = targetPath.indexOf('?')
          const commonTargetPath = targetPath.substring(0, detailHowIndex)
          return childMenu.menu_path === commonTargetPath
        })

        if ( matched ) {
          const { authority, title } = item;
          const { menu_id, menu_name, menu_path } = matched;

          menuId = menu_id;

          targetMenu.currentPath = menu_path;
          targetMenu.currentPathDepth1 = title;
          targetMenu.currentPathDepth2 = menu_name;

          if ( authority === 'User' ) {
            // 이동하려는 메뉴가 사용자 메뉴일 경우
            // 데이터 상세 조회 페이지로 이동 시, '사용자홈' 버튼 클릭하면 데이터 상세 조회 페이지가 아닌 통합 검색 페이지로 이동
            targetMenu.userPath = isToDetailPage ? SEARCH_PAGE_PATH[0] : menu_path;
          } else {
            // 이동하려는 메뉴가 관리자 메뉴일 경우
            // 데이터 상세 조회 페이지로 이동 시, 'ADMIN' 버튼 클릭하면 데이터 상세 조회 페이지가 아닌 메타 데이터 통합 관리 페이지로 이동
            targetMenu.adminPath = isToDetailPage ? SEARCH_PAGE_PATH[1] : menu_path;
          }
          break;
        }

        // 상세 조회 페이지로 이동하는 경우 예외로 배너의 메뉴명 동기화 처리
        if (matched_detail) {
          const { authority, title } = item;
          const { menu_id: detail_menu_id, menu_name: detail_menu_name, menu_path: detail_menu_path } = matched_detail;

          menuId = detail_menu_id;

          targetMenu.currentPath = detail_menu_path;
          targetMenu.currentPathDepth1 = title;
          targetMenu.currentPathDepth2 = detail_menu_name;

          if ( authority === 'User' ) {
            // 이동하려는 메뉴가 사용자 메뉴일 경우
            // 데이터 상세 조회 페이지로 이동 시, '사용자홈' 버튼 클릭하면 데이터 상세 조회 페이지가 아닌 통합 검색 페이지로 이동
            targetMenu.userPath = isToDetailPage ? SEARCH_PAGE_PATH[0] : detail_menu_path;
          } else {
            // 이동하려는 메뉴가 관리자 메뉴일 경우
            // 데이터 상세 조회 페이지로 이동 시, 'ADMIN' 버튼 클릭하면 데이터 상세 조회 페이지가 아닌 메타 데이터 통합 관리 페이지로 이동
            targetMenu.adminPath = isToDetailPage ? SEARCH_PAGE_PATH[1] : detail_menu_path;
          }
          break;
        }
      }
    }

    return { targetMenu, menuId };
  };

  return {
    _user,
    _tab,
    _guid,
    _fromPath,
    _currentSearchTab,
    _params,
    _enableLoader,
    _forceRenderLoader,
    _answerId,
    _timeoutTimer,
    getUser,
    getTab,
    getGuid,
    getFromPath,
    getCurrentSearchTab,
    getParams,
    getEnableLoader,
    getForceRenderLoader,
    getAnswerId,
    getFaqId,
    getSampleDataCount,
    getTimeoutTimer,
    setUser,
    setTab,
    setGuid,
    setFromPath,
    setCurrentSearchTab,
    setParams,
    setEnableLoader,
    setForceRenderLoader,
    setAnswerId,
    setTimeoutTimer,
    getCsrfToken,
    onChangeRouter,
    insertMenuHistory,
    getParameterGuid,
    removeStoreGuid,
    getRouterName,
    getTargetMenu,
    setFaqId,
    setSampleDataCount
  }
},
  { persist: true }
)
