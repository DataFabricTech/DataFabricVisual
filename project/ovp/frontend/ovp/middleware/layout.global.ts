export default defineNuxtRouteMiddleware((to) => {
  /**
   * nuxt3 에 아직 폴더별로 layout 을 지정하는 방법이 없기때문에, router middleware 를 global 로 설정하여
   * 페이지 전환시에 각 페이지별로 url path 를 이용하여 layout을 설정한다.
   */
  if (to.path.includes("/login")) {
    to.meta.layout = "login";
  }
});
