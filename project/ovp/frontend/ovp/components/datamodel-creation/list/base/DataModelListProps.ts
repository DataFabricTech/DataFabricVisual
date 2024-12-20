interface DataModelSort {
  value: string;
  label: string;
}

export interface DataModelListProps {
  data: any[];
  sortList?: DataModelSort[];
  filter: any;
  isMulti?: boolean;
  useItemDeleteBtn?: boolean;
  valueKey: string;
  labelKey: string;
  noDataMsg?: string;
  listType?: "selected" | "non-selected";
}
