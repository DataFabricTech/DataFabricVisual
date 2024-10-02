import { ICellRendererParams } from "@ag-grid-community/core";

export function HistoryServiceEventRenderer(params: ICellRendererParams) {
  const eventStatus = (status: string) => {
    let eventTheme = "";
    switch (status) {
      case "status_change":
        eventTheme = "현황변경";
        break;
      case "created":
        eventTheme = "등록";
        break;
      case "updated":
        eventTheme = "편집";
        break;
      // TODO: [개발] 삭제 정보 확인 필요
      case "delete":
        eventTheme = "삭제";
        break;
      default:
        eventTheme = "등록";
    }

    return eventTheme;
  };

  const event = eventStatus(params.data.event);

  return event;
}
