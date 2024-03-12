export interface NavigationProps {
  data: { [key: string]: string | number }[];
  labelKey: string;
  valueKey: string;
  iconKey?: string;
  currentItem: string | number;
  currentItemType: string;
  disabledList?: (string | number)[];
}
