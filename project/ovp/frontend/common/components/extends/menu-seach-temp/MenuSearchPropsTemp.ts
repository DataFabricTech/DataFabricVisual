import { SelectProps } from "@/components/extends/common/interfaces/props/Select.interface";

export interface MenuSearchPropsTemp extends SelectProps {
  selectedItems: { [key: string]: string | number }[] | { [key: string]: string | number };
  isShow : boolean;
  nodataMsg?: string;
  noSearchMsg?: string;
  searchText?: string;
  isMulti?: boolean;
}
