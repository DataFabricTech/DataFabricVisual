export interface SelectProps {
  id?: string;
  data: { [key: string]: string | number }[];
  labelKey: string;
  valueKey: string;
  iconKey?: string;
  selectedItem?: string | number;
  disabledList?: (string | number)[];
  isFirstSelectedEvent?: boolean;
}
