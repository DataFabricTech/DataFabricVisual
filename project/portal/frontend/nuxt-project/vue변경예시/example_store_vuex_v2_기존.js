import ObjectUtils from "@/utility/ObjectUtils";

export const state = () => ({
  _user: null,
  _tab: {
    admPath: '/admin/data/meta-data/manage', // 사용자 화면에서 메뉴바의 'ADMIN' 버튼 클릭 시 이동 대상 메뉴 path
    userPath: '/search/keyword', // 관리자 화면에서 메뉴바의 '사용자홈' 버튼 클릭 시 이동 대상 메뉴 path
    currentPath: '/',   // 현재 위치 메뉴 path
    currentPathDepth1: '',  // 현재 위치 메뉴의 상위 메뉴명(1 depth)
    currentPathDepth2: '',  // 현재 위치 메뉴명
  },
  _guid: '',  // 상세조회 페이지 진입 시 조회 파라미터로 사용할 guid
  _fromPath: '',  // 이전 페이지 경로

  // 통합 검색, 메타 데이터 통합 관리 화면에서 엔티티 목록 조회 시 사용한 파라미터
  _params: {
    path: '', // 파라미터 출처 페이지 경로
  },

  _enableLoader: false, // loader 컴포넌트 활성화 여부 flag
  _forceRenderLoader: false, // loader 컴포넌트 강제 활성화 여부 flag

  _answerId: -1, // 데이터요청 상세화면 진입시 조회 파라미터로 사용할 답변id
});

export const getters = {
  user(state) {
    return state._user;
  },
  tab(state) {
    return state._tab;
  },
  guid({ _guid }) {
    return _guid;
  },
  fromPath({ _fromPath }) {
    return _fromPath;
  },
  params({ _params }) {
    return _params;
  },
  enableLoader({ _enableLoader }) {
    return _enableLoader;
  },
  forceRenderLoader({ _forceRenderLoader }) {
    return _forceRenderLoader;
  },
  answerId({ _answerId }) {
    return _answerId;
  },
};

export const mutations = {
  setUser(state, data) {
    state._user = data;
  },
  setTab(state, data) {
    state._tab = data;
  },
  setGuid(state, guid) {
    state._guid = guid;
  },
  setFromPath(state, fromPath) {
    state._fromPath = fromPath;
  },
  setParams(state, params) {
    state._params = params;
  },
  setEnableLoader(state, enableLoader) {
    state._enableLoader = enableLoader;
  },
  setForceRenderLoader(state, forceRenderLoader) {
    state._forceRenderLoader = forceRenderLoader;
  },
  setAnswerId(state, answerId) {
    state._answerId = answerId;
  }
};

export const actions = {
  getCsrfToken() {
    return this.$axios.get("/api/csrf-token");
  },

  /**
   * @desc          router 변경 이벤트 핸들러
   *
   * @to            이동하려는 메뉴 path 관련 object
   */
  onChangeRouter(context, payload) {
    const { state: { _user, _tab }} = context;
    const {
      to: { fullPath, params },
      from
    } = payload;

    if ( fullPath !== _tab.currentPath ) {
      // 이동하려는 메뉴 정보를 추출
      const { targetMenu, menuId } = getTargetMenu(_user, _tab, fullPath);

      // 상세데이터 조회 페이지로 이동 시 guid 저장
      if ( /\/detail\b/.test(fullPath) && params.guid ) {
        context.commit('setGuid', params.guid);
      }

      context.commit('setTab', targetMenu);
      context.dispatch('insertMenuHistory', menuId);
    }

    // 직전 페이지 경로 저장
    let fromPath = '';
    if ( from && from.fullPath ) {
      fromPath = from.fullPath;
    }
    context.commit('setFromPath', fromPath);
  },

  // 메뉴 사용 이력 insert
  insertMenuHistory(context, payload) {
    const currentUser = context.state._user;
    try {
      this.$axios.get(`/api/menu/click?menuID=${payload}&userID=${currentUser.user_id}`);
    } catch (error) {
      console.log(error);
    }
  },

  // router param 또는 local storage에서 파라미터로 사용할 guid 추출하여 반환
  async getParameterGuid(context) {
    let guid = '';

    const guidFromStore = context.state._guid;
    const guidFromRouterQuery = ObjectUtils.getPropertyValue(this.$router, 'currentRoute/query/guid', '')

    if ( guidFromRouterQuery ) {
      guid = guidFromRouterQuery;
    } else if ( !guidFromRouterQuery && guidFromStore ) {
      guid = guidFromStore;
    }

    return guid;
  },

  // 저장되어 있는 guid 제거
  removeStoredGuid(context, payload) {
    const pattern = /detail\b/;
    const { to = '', from = '' } = payload;

    // 이동 전 페이지는 상세 조회 페이지이고 이동 후 페이지는 검색어 저장 대상 페이지가 아닐 경우
    if (
      pattern.exec(from)
      && !SEARCH_PAGE_PATH.includes(to)
    ) {
      context.commit('setGuid', '');
    }
  },
};

// 검색어 저장 대상 페이지 경로 (통합 검색, 메타 데이터 통합 관리)
const SEARCH_PAGE_PATH = [ '/search/keyword', '/admin/data/meta-data/manage'];

// 라우터 목록 중에서 이동하고자 하는 메뉴 항목 탐색
const getRouterName = (routes, targetMenuPath) => routes.find(el => el.path === targetMenuPath);

// 이동하려는 메뉴 path 기반으로 store에 저장할 메뉴 object & 메뉴 ID 반환
const getTargetMenu = (user, tab, targetPath) => {
  const targetMenu = {
    ...tab,
    currentPath: targetPath,
  };
  let menuId = '';

  if ( targetPath === '/main' ) {
    menuId = 'MAIN';

    targetMenu.userPath = targetPath;
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
      const matched = item.child.find(childMenu => childMenu.menu_path === targetPath);

      if ( matched ) {
        const { authority, title } = item;
        const { menu_id, menu_name, menu_path } = matched;

        menuId = menu_id;

        targetMenu.currentPathDepth1 = title;
        targetMenu.currentPathDepth2 = menu_name;

        if ( authority === 'User' ) {
          // 이동하려는 메뉴가 사용자 메뉴일 경우
          // 데이터 상세 조회 페이지로 이동 시, '사용자홈' 버튼 클릭하면 데이터 상세 조회 페이지가 아닌 통합 검색 페이지로 이동
          targetMenu.userPath = isToDetailPage ? SEARCH_PAGE_PATH[0] : menu_path;
        } else {
          // 이동하려는 메뉴가 관리자 메뉴일 경우
          // 데이터 상세 조회 페이지로 이동 시, 'ADMIN' 버튼 클릭하면 데이터 상세 조회 페이지가 아닌 메타 데이터 통합 관리 페이지로 이동
          targetMenu.admPath = isToDetailPage ? SEARCH_PAGE_PATH[1] : menu_path;
        }
        break;
      }
    }
  }

  return { targetMenu, menuId };
};
