export interface DatepickerProps {
  type?: "date" | "datetime" | "year" | "month" | "time" | "week";
  range?: boolean;
  format?: string;
  formatter?: object;
  valueType?: string;
  lang?: string;
  placeholder?: string | null;
  editable?: boolean;
  clearable?: boolean;
  confirm?: boolean;
  confirmText?: string;
  multiple?: boolean;
  disabled?: boolean;
  disabledDateRange?: string[] | null;
  disabledTimeRange?: string[] | null;
  titleFormat?: string;
  separator?: string;
  hourOptions?: number[];
  minuteOptions?: number[];
  secondOptions?: number[];
  showTimePanel?: boolean;
}
