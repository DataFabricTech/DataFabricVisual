export interface DataModelAccordianListProps {
  data: { [key: string]: [] };
  selectedItems: any[];
  useLiveSearch?: boolean;
  isMulti?: boolean;
  useItemDeleteBtn?: boolean;
  valueKey: string;
  labelKey: string;
  noDataMsg?: string;
  listType?: "selected" | "non-selected";
}
