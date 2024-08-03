import { TreeProps, TreeViewItem } from "./TreeProps";
import { uuid } from "vue3-uuid";
import _ from "lodash";

export interface TreeComposition extends TreeProps {
  treeItems: Ref<TreeViewItem[]>;
  createNewTreeItem(parentId: string, id?: string, name?: string, desc?: string, order?: number): TreeViewItem;
  openAll(): void;
  closeAll(): void;
  dropValidatorHandler: any;
}

export function TreeComposition(props: TreeProps): TreeComposition {
  const updateCheckedStatus = (items: any[], checkedIds: string[]): void => {
    _.forEach(items, (item) => {
      // checkedIds 배열에 현재 항목의 id가 포함되어 있는지 확인
      if (checkedIds.includes(item.id)) {
        item.checked = true;
      }

      // 자식 노드가 있는 경우, 재귀적으로 호출
      if (item.children && item.children.length > 0) {
        updateCheckedStatus(item.children, checkedIds);
      }
    });
  };

  const treeItems: Ref<TreeViewItem[]> = ref<TreeViewItem[]>([]);
  treeItems.value = props.items;

  // 체크박스를 사용하는 경우, 체크여부를 object에 추가해준다.
  if (props.isCheckable) {
    updateCheckedStatus(treeItems.value, props.checkedIds || []);
  }

  const createNewTreeItem: (
    parentId: string,
    id?: string,
    name?: string,
    desc?: string,
    order?: number
  ) => TreeViewItem = (parentId, id, name, desc, order) => {
    return {
      id: id || uuid.v4(),
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

  const findAncestors = (items: any[], targetId: string): string[] => {
    const parentIds: string[] = [];

    const findParents = (nodes: any[], targetId: string, parents: string[]): boolean => {
      for (const node of nodes) {
        if (node.id === targetId) {
          parentIds.push(...parents);
          return true;
        }
        if (node.children && node.children.length > 0) {
          if (findParents(node.children, targetId, [...parents, node.id])) {
            return true;
          }
        }
      }
      return false;
    };

    findParents(items, targetId, []);
    return parentIds;
  };

  const dropValidator = (thisNode: TreeViewItem, targetNode: TreeViewItem) => {
    // 부모노드는 자기 후손 노드로 drop 할수없음.
    // targetNode 기준 부모노드 List 를 조회해서 thisNode 의 id 가 있는지 확인 필요함.
    const parentIds = findAncestors(treeItems.value, targetNode.id);
    if (parentIds.includes(thisNode.id)) {
      return false;
    }

    // TODO: confirm 창 구현 완료 되면 여기서 처리 필요함.
    /**
     * title : 카테고리 이동
     * content : `${thisNode.name} 카테고리를 ${targetNode} 하위로 이동 하시겠습니까?`
     * response가 true 일 때만, props.dropValidator 진행.
     */

    const newNode = _.cloneDeep(thisNode);
    newNode.parentId = targetNode.id;

    return props.dropValidator(thisNode, targetNode, newNode);
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
