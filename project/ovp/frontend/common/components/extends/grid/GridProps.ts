export interface GridProps {
  rowId?: string;
  class?: string;
  style?: string;
  columnDefs: any[];
  rowData: any[];

  useRowCheckBox?: boolean;

  buttons?: any[];
  selectedNodes?: any[];
  columnWidthList?: any[];

  setColumnFit?: boolean;

  columnRender?: object;
}
