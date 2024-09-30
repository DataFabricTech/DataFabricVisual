import { ICellRendererParams } from "@ag-grid-community/core";
import { useOverviewStore } from "~/store/manage/service/overview";

export function HistoryEventDateRenderer(params: ICellRendererParams) {
  const overviewStore = useOverviewStore();
  const { convertDateTime } = overviewStore;

  return `${convertDateTime(params.data.eventAt)}`;
}
