import { useMenuStore } from "@/store/common/menu";

export default defineNuxtRouteMiddleware((to) => {
  const { setHeaderUrl } = useMenuStore();

  const path = to.fullPath.replace(/^\/portal/, "");
  const url = path.split("/")[1];

  setHeaderUrl(`/portal/${url}`);
});
