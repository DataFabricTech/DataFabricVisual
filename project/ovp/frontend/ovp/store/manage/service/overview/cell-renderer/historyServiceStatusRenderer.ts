import { ICellRendererParams } from "@ag-grid-community/core";

export function HistoryServiceStatusRenderer(params: ICellRendererParams) {
  const badgeStatus = (status: string) => {
    let badgeTheme = "";
    switch (status) {
      case "success":
        badgeTheme = "badge-green-lighter";
        break;
      case "running":
        badgeTheme = "badge-blue-lighter";
        break;
      case "queued":
        badgeTheme = "badge-gray-lighter";
        break;
      case "failed":
        badgeTheme = "badge-red-lighter";
        break;
      case "partialsuccess":
        badgeTheme = "badge-yellow-lighter";
        break;
      default:
        badgeTheme = "badge";
    }

    return badgeTheme;
  };

  const badgeClass = badgeStatus(params.data.state);
  const badge = `<div class="badge ${badgeClass} ag-cell-badge"><p class="badge-text">${params.data.state}</p></div>`;

  return badge;
}
