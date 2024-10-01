import { ICellRendererParams } from "@ag-grid-community/core";
import { useRouter } from "nuxt/app";

const router = useRouter();

export function ServiceNameRenderer(params: ICellRendererParams) {
  console.log("ServiceNameRenderer의 params: ", params.data);

  // TODO: [개발] 링크 전달 받으면 추가 필요
  const setRouterLink = () => {
    router.push(`/portal/manage/${params.data.serviceId}`);
  };

  // TODO: [개발] 서비스 이름 수정 필요
  const link = document.createElement("a");
  link.textContent = params.data.serviceName;
  link.style.textDecoration = "underline";
  link.href = "#";
  link.addEventListener("click", (e) => {
    e.preventDefault();
    setRouterLink();
  });

  return link;
}
