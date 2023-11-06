import type { ChartObject, ChartAxis, Grid } from "~/@types/global";
import { ContextItem } from "~/components/base/context";

export interface Overview {
  storageTypeCount: ChartObject;
  storageStatusCount: ChartObject;
  storageStatistics: ChartAxis;
  storageDataCount: ChartAxis;
  history: Grid;
  event: Grid;
}

export interface StorageSortContextItem extends ContextItem {
  direction: "asc" | "desc";
  type: string,
}
export interface StorageItem {
  id: string,
  name: string,
  storageType: string,
  status: string,
  crdDate: string,
  show: boolean
}
export interface StorageFilter {
  name: string,
  storageType: Array<string>,
  status: Array<string>
}
