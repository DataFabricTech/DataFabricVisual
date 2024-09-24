import { useRouter } from "vue-router";
import { useMenuStore } from "@/store/common/menu";
import { useUserStore } from "@/store/user/userStore";
import { storeToRefs } from "pinia";
import _ from "lodash";

// TODO : 서버 배포 완료 후에 console.log 삭제 예정
export default defineNuxtPlugin(() => {
  const menuStore = useMenuStore();
  const { getMenuData } = menuStore;
  const { menuJson, mgmtMenuJson } = storeToRefs(menuStore);
  const userStore = useUserStore();
  const { user } = storeToRefs(userStore);

  const router = useRouter();

  router.beforeEach(async (to, from, next) => {
    try {
      const path = to.fullPath.replace(/^\/portal/, "");
      const url = "/" + path.split("/")[1];
      console.log(`[권한 테스트 로그] Navigating to: ${url}`);

      // 권한이 필요하지 않은 페이지 예외처리
      const noAuthPages = ["/", "/login"];
      if (noAuthPages.includes(url)) {
        console.log(
          "[권한 테스트 로그] 권한이 필요하지 않은 페이지. 사용자 권한 상관없이 접근 가능.",
        );
        return next();
      }

      // 메뉴 데이터가 없으면 로드
      if (_.isEmpty(menuJson.value)) {
        await getMenuData();
      }

      // concat을 사용하여 메뉴 데이터 병합
      const newMenuArr = [].concat(
        menuJson.value || [],
        mgmtMenuJson.value || [],
      );

      // URL에 해당하는 메뉴 객체를 찾음
      const menuObj = _.find(newMenuArr, (item) => item.linkTo.includes(path));
      const menuAuth = menuObj?.menuAuth || "U"; // 기본값 "U"

      // 관리자 권한 확인
      if (user.value.admin) {
        console.log("[권한 테스트 로그] 관리자 계정. 접근 가능.");
        return next();
      }

      // 일반 사용자 권한 확인
      if (menuAuth === "A") {
        console.error(
          "[권한 테스트 로그] 사용자 계정. 관리자만 접근 가능한 메뉴. 접근 불가능.",
        );
        // return next("/no-auth"); // 접근 권한 없는 페이지로 이동
        return next();
      }

      // 일반 사용자이지만 메뉴가 'U' 임.
      console.log(
        "[권한 테스트 로그] 사용자 계정. 사용자가 접근 가능한 메뉴. 접근 가능.",
      );
      return next();
    } catch (error) {
      console.error("[권한 테스트 로그] Router error:", error);
      // TODO: router 배포 후 테스트 후에, 오류 발생 시 에러 페이지로 이동될수 있게 처리 필요함.
      // next(false);
      next();
    }
  });
});
