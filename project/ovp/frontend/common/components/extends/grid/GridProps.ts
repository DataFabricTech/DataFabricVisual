export interface GridProps {
  rowId?: string;
  class?: string;
  style?: string;
  columnDefs: any[];
  rowData?: any[];

  useRowCheckBox?: boolean;
  useColumnResize?: boolean;
  buttons?: any[];
  selectedNodes?: any[];
  columnWidthList?: any[];

  setColumnFit?: boolean;

  columnRender?: object;

  rowBuffer?: number;
  rowSelection?: object;
  rowModelType?: string;
  cacheBlockSize?: number;
  cacheOverflowSize?: number;
  maxConcurrentDatasourceRequests?: number;
  infiniteInitialRowCount?: number;
  maxBlocksInCache?: number;
}
