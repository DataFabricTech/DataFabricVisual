export interface SelectProps {
  data: { [key: string]: string | number }[];
  labelKey: string;
  valueKey: string;
  iconKey?: string;
  selectedItem?: string | number;
  disabledList?: (string | number)[];
}
