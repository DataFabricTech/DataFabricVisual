import { TreeDetailProps, TreeViewItem } from "~/components/extends/tree/TreeProps";

export interface MenuSearchTreeContentsProps extends TreeDetailProps {
  data: TreeViewItem[];
  labelKey: string;
  valueKey: string;
  selectedItems: TreeViewItem[];

  nodataMsg?: string;
  noSearchMsg?: string;
  searchText?: string;
  isShow?: boolean;
}
