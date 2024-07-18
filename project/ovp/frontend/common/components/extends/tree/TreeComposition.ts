import { TreeProps, TreeViewItem } from "./TreeProps";
import { uuid } from "vue-uuid";
import _ from "lodash";

export interface TreeComposition extends TreeProps {
  treeItems: Ref<TreeViewItem[]>;
  createNewTreeItem(parentId: string, name?: string, desc?: string, order?: number): TreeViewItem;
  openAll(): void;
  closeAll(): void;
  dropValidatorHandler: any;
}

export function TreeComposition(props: TreeProps): TreeComposition {
  const treeItems: Ref<TreeViewItem[]> = ref<TreeViewItem[]>([]);
  treeItems.value = props.items;

  const createNewTreeItem: (parentId: string, name?: string, desc?: string, order?: number) => TreeViewItem = (
    parentId,
    name,
    desc,
    order
  ) => {
    return {
      id: uuid.v4(),
      name: name || "",
      desc: desc || "",
      order: order || 0,
      parentId: parentId,
      expanded: false,
      selected: false,
      disabled: false,
      children: []
    };
  };

  const updateAllNode = (nodes: TreeViewItem[], key: string, bool: boolean) => {
    _.forEach(nodes, (node) => {
      if (_.has(node, key)) {
        node[key] = bool;
      }
      if (node.children && node.children.length > 0) {
        updateAllNode(node.children, key, bool);
      }
    });
  };

  const openAll = () => {
    updateAllNode(treeItems.value, "expanded", true);
  };
  const closeAll = () => {
    updateAllNode(treeItems.value, "expanded", false);
  };

  const dropValidator = (thisNode: TreeViewItem, targetNode: TreeViewItem) => {
    // TODO: confirm 창 구현 완료 되면 여기서 처리 필요함.
    /**
     * title : 카테고리 이동
     * content : `${thisNode.name} 카테고리를 ${targetNode} 하위로 이동 하시겠습니까?`
     * response가 true 일 때만, props.dropValidator 진행.
     */

    return props.dropValidator(thisNode, targetNode);
  };
  // vue3-tree-vue 에서는 dropValidator 이 undefined 일때만 drag/drop 이 동작하지 않음.
  const dropValidatorHandler: any = props.useDraggable ? dropValidator : undefined;

  return {
    ...props,
    treeItems,
    createNewTreeItem,
    openAll,
    closeAll,
    dropValidatorHandler
  };
}
