export interface TreeViewItem {
  id: string;
  name: string;
  desc: string;
  order?: number;
  parentId: string;
  tagId?: string;
  children: TreeViewItem[];

  // NOTE: isCheckable 이 false 일때, checked key 를 node 에 포함할 경우, vue3-tree-vue 에서 오류를 발생시킴.
  checked?: boolean;
  selected?: boolean;
  expanded?: boolean;
  disabled?: boolean;
  meta?: any; // provides meta-data of any type per node.
}

export interface TreeProps extends TreeDetailProps {
  items: TreeViewItem[];
  isCheckable: boolean;
  disabledIds?: any[];
  selectedIds?: any[];
  useFirSelect?: boolean;
  compId?: string;
}

export interface TreeDetailProps {
  class?: string;

  mode: "view" | "edit";

  showOpenAllBtn?: boolean;
  showCloseAllBtn?: boolean;
  useDraggable?: boolean;

  hideGuideLines: boolean;
  firExpandAll?: boolean;

  checkedIds?: string[];

  immutableItems?: string[];

  dropValidator?: (thisNode: TreeViewItem, targetNode: TreeViewItem, newNode: TreeViewItem) => any;
}
