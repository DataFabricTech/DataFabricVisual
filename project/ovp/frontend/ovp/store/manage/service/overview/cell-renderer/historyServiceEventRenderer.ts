import { ICellRendererParams } from "@ag-grid-community/core";

export function HistoryServiceEventRenderer(params: ICellRendererParams) {
  const eventStatus = (status: string) => {
    let eventTheme = "";
    // TODO: [개발] 상태 별 데이터 명 문의 필요
    switch (status) {
      case "status_change":
        eventTheme = "현황변경";
        break;
      case "running":
        eventTheme = "badge-blue-lighter";
        break;
      case "queued":
        eventTheme = "badge-gray-lighter";
        break;
      case "failed":
        eventTheme = "badge-red-lighter";
        break;
      case "partialsuccess":
        eventTheme = "badge-yellow-lighter";
        break;
      default:
        eventTheme = "등록";
    }

    return eventTheme;
  };

  const event = eventStatus(params.data.event);

  return event;
}
