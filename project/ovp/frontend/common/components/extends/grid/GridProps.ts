export interface GridProps {
  class?: string;
  style?: string | undefined;
  columnDefs: any[];
  rowData: any[];

  buttons: { [key: string]: any };
}
