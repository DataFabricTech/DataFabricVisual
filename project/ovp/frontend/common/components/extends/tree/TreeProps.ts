export interface TreeViewItem {
  id: string;
  name: string;
  desc: string;
  order: number;
  parentId: string;
  children: TreeViewItem[];

  // NOTY: isCheckable 이 false 일때, checked key 를 node 에 포함할 경우, vue3-tree-vue 에서 오류를 발생시킴.
  checked?: boolean;
  selected: boolean;
  expanded: boolean;
  disabled: boolean; // When disabled, an item can neither be selected or checked
  meta?: any; // provides meta-data of any type per node.
}

export interface TreeProps {
  items: TreeViewItem[];

  class?: string;

  mode: "view" | "edit";

  showOpenAllBtn?: boolean;
  showCloseAllBtn?: boolean;
  useDraggable?: boolean;

  isCheckable: boolean;
  hideGuideLines: boolean;
  firExpandAll?: boolean;

  dropValidator: (thisNode: TreeViewItem, targetNode: TreeViewItem) => boolean;
}
