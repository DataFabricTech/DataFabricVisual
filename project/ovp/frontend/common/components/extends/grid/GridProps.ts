export interface GridProps {
  rowId?: string;
  class?: string;
  style?: string;
  fqn: string;
  columnDefs: any[];
  rowData: any[];

  useRowCheckBox?: boolean;
  useColumnResize?: boolean;
  useColumnCopy?: boolean;
  buttons?: any[];
  selectedNodes?: any[];
  columnWidthList?: any[];

  setColumnFit?: boolean;

  columnRender?: object;
}
