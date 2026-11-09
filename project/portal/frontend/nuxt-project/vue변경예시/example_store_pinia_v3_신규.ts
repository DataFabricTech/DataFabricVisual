import { defineStore } from "pinia";

export const useUserStore = defineStore('userStore', () => {
  // state
  const _user = ref({
    user_id: "",
    username: "",
    authority: "",
    userMenu: "",
    adminMenu: "",
  })
  const _tab = ref({
    adminPath: '/admin/data/status/dashboard', // 사용자 화면에서 메뉴바의 'ADMIN' 버튼 클릭 시 이동 대상 메뉴 path
    userPath: '/search/keyword', // 관리자 화면에서 메뉴바의 '사용자홈' 버튼 클릭 시 이동 대상 메뉴 path
    currentPath: '/',   // 현재 위치 메뉴 path
    currentPathDepth1: '',  // 현재 위치 메뉴의 상위 메뉴명(1 depth)
    currentPathDepth2: ''  // 현재 위치 메뉴명
  })
  const _fromPath = ref('')
  const _enableLoader = ref(false)
  const _forceRenderLoader = ref(false)

  // getters
  const getUser = computed(() => _user.value)
  const getTab = computed(() => _tab.value)
  const getFromPath = computed(() => _fromPath.value)
  const getEnableLoader = computed(() => _enableLoader.value)
  const getForceRenderLoader = computed(() => _forceRenderLoader.value)

  // action
  const setUser = (data: any) => { _user.value = data}
  const setTab = (data: any) => { _tab.value = data}
  const setFromPath = (data: any) => { _fromPath.value = data}
  const setEnableLoader = (data: any) => { _enableLoader.value = data }
  const setForceRenderLoader = (data: any) => { _forceRenderLoader.value = data }

  const getCsrfToken = () => { console.log('getCsrfToken')}
  const onChangeRouter = (context: any, payload: any) => {
      const { state: { _user, _tab }} = context;
      const {
        to: { fullPath, params },
        from
      } = payload;

      // if ( fullPath !== _tab.currentPath ) {
      //   // 이동하려는 메뉴 정보를 추출
      //   const { targetMenu, menuId } = getTargetMenu(_user, _tab, fullPath);

      //   // 상세데이터 조회 페이지로 이동 시 guid 저장
      //   if ( /\/detail\b/.test(fullPath) && params.guid ) {
      //     context.commit('setGuid', params.guid);
      //   }

      //   context.commit('setTab', targetMenu);
      //   context.dispatch('insertMenuHistory', menuId);
      // }

      // 직전 페이지 경로 저장
      let fromPath = '';
      if ( from && from.fullPath ) {
        fromPath = from.fullPath;
      }
      context.commit('setFromPath', fromPath);
  }

  return {
      _user,
      _tab,
      _fromPath,
      _enableLoader,
      _forceRenderLoader,
      getUser,
      getTab,
      getFromPath,
      getEnableLoader,
      getForceRenderLoader,
      setUser,
      setTab,
      setFromPath,
      setEnableLoader,
      setForceRenderLoader,
      getCsrfToken,
      onChangeRouter
  }
},
{ persist: true }
)
