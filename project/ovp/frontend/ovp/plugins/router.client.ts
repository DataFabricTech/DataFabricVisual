import { useRouter } from "vue-router";
import { useMenuStore } from "@/store/common/menu";
import { useUserStore } from "@/store/user/userStore";
import { storeToRefs } from "pinia";
import _ from "lodash";

export default defineNuxtPlugin(() => {
  const menuStore = useMenuStore();
  const { getMenuData } = menuStore;
  const { menuJson, mgmtMenuJson } = storeToRefs(menuStore);
  const userStore = useUserStore();
  const { getUserInfo } = userStore;
  const { user } = storeToRefs(userStore);

  const router = useRouter();

  router.beforeEach(async (to, from, next) => {
    try {
      const path = to.fullPath.replace(/^\/portal/, "");
      const url = "/" + path.split("/")[1];
      console.log(`[권한 테스트 로그] Navigating to: ${url}`);

      // 권한이 필요하지 않은 페이지 예외처리
      const noAuthPages = ["/", "/login", "/_pub"];
      if (noAuthPages.includes(url)) {
        return next();
      }

      // 메뉴 데이터가 없으면 로드
      if (_.isEmpty(menuJson.value)) {
        await getMenuData();
      }
      if (_.isEmpty(user.value.id)) {
        await getUserInfo();
      }

      // concat을 사용하여 메뉴 데이터 병합
      const newMenuArr = [].concat(
        menuJson.value || [],
        mgmtMenuJson.value || [],
      );

      // URL에 해당하는 메뉴 객체를 찾음
      const menuObj = _.find(newMenuArr, (item) => {
        return _.isEmpty(item) ? null : item.linkTo.includes(url);
      });
      if (_.isEmpty(menuObj)) {
        return next("/portal/login/error");
      }
      const menuAuth = menuObj?.menuAuth || "U"; // 기본값 "U"

      // 관리자 권한 확인
      if (user.value.admin) {
        return next();
      }

      // 일반 사용자 권한 확인
      if (menuAuth === "A") {
        return next("/portal/login/error"); // 접근 권한 없는 페이지로 이동
      }

      // 일반 사용자이지만 메뉴가 'U' 임.
      return next();
    } catch (error) {
      console.error("[권한 테스트 로그] Router error:", error);
      next("/portal/login/error");
    }
  });
});
