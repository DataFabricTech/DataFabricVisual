import { ICellRendererParams } from "@ag-grid-community/core";

export function HistoryServiceNameRenderer(params: ICellRendererParams) {
  console.log("HistoryServiceNameRenderer의 params: ", params);
  const link = `<a href="${params.value}" target="_self" style="text-decoration: underline">${params.value}</a>`;
  return link;
}
