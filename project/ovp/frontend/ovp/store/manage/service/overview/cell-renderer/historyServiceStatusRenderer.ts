import { ICellRendererParams } from "@ag-grid-community/core";

export function HistoryServiceStatusRenderer(params: ICellRendererParams) {
  console.log("HistoryServiceStatusRenderer의  params: ", params.data.status);

  const badgeStatus = (status: string) => {
    let badgeTheme = "";
    switch (status) {
      case "Success":
        badgeTheme = "badge-green-lighter";
        break;
      case "Running":
        badgeTheme = "badge-blue-lighter";
        break;
      case "Queued":
        badgeTheme = "badge-gray-lighter";
        break;
      case "Failed":
        badgeTheme = "badge-red-lighter";
        break;
      case "PartialSuccess":
        badgeTheme = "badge-yellow-lighter";
        break;
      default:
        badgeTheme = "badge";
    }

    return badgeTheme;
  };

  const badgeClass = badgeStatus(params.data.status);
  const badge = `<div class="badge ${badgeClass} ag-cell-badge"><p class="badge-text">${params.data.status}</p></div>`;

  return badge;
}
