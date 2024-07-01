import { SelectProps } from "@/components/extends/common/interfaces/props/Select.interface";

export interface MenuSearchProps extends SelectProps {
  selectedItems: { [key: string]: string | number }[] | { [key: string]: string | number };
  idKey: string;

  nodataMsg?: string;
  noSearchMsg?: string;
  searchText?: string;
  isMulti?: boolean;
}
