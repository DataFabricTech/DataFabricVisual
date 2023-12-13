// 전역 타입 설정 파일
export interface ChartObject {
  series: Array<any>;
}

export interface ChartAxis extends ChartObject {
  categories: Array<any>
}
export interface Grid {
  colDefs: Array<Object>;
  rowData: Array<Object>;
}
