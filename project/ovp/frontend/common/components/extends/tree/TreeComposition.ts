import { TreeProps, TreeViewItem } from "./TreeProps";
import { uuid } from "vue-uuid";
import _ from "lodash";

export interface TreeComposition extends TreeProps {
  treeItems: Ref<TreeViewItem[]>;
  createNewTreeItem(parentId: string, name?: string, desc?: string, order?: number): TreeViewItem;
  findNodeById(items: TreeViewItem[], id: string | number): TreeViewItem | null;
  findParentNode(items: TreeViewItem[], nodeId: string | number): TreeViewItem | null;
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

  const findNodeById = (items: TreeViewItem[], id: string | number): TreeViewItem | null => {
    for (const node of items) {
      if (node.id === id) {
        return node;
      }
      if (node.children) {
        const found = findNodeById(node.children, id);
        if (found) {
          return found;
        }
      }
    }
    return null;
  };

  const findParentNode = (items: TreeViewItem[], nodeId: string | number): TreeViewItem | null => {
    for (const node of items) {
      if (node.children) {
        for (const child of node.children) {
          if (child.id === nodeId) {
            return node;
          }
        }
        const parent: TreeViewItem | null = findParentNode(node.children, nodeId);
        if (parent) {
          return parent;
        }
      }
    }
    return null;
  };

  // NOTE: api 처리를 하지 않고 front 에서 형제 노드 나 자식 노드를 추가 할 경우 아래 코드를 사용한다.
  // // 형제 노드 추가
  // const addSibling = (node: TreeViewItem) => {
  //   const parentNode: TreeViewItem | null = findParentNode(treeItems.value, node.id);
  //   const newNode = createNewTreeItem(node.parentId);
  //   if (_.isNull(parentNode)) {
  //     treeItems.value.push(newNode);
  //   } else {
  //     (parentNode.children ??= []).push(newNode);
  //   }
  // };
  // const getDataModelByCtgId = (id: string | number | undefined) => {
  //   // TODO : 해당 카테고리를 이용하여 생성된 데이터 모델이 있는지 API 조회해야함.
  //   console.log(`데이터 모델 체크 : ${id}`);
  //   const getDataModelByCtgId: boolean = false;
  //   return getDataModelByCtgId;
  // };
  // const addChild = (node: TreeViewItem) => {
  //   const targetNode = findNodeById(treeItems.value, node.id);
  //
  //   // step1. validation check : 설정된 데이터 모델이 있는지 조회
  //   if (getDataModelByCtgId(node.id)) {
  //     return;
  //   }
  //
  //   // step2 설정된 데이터 모델이 없으면 하위 노드를 추가한다.
  //   const newNode: TreeViewItem = createNewTreeItem(node.id);
  //   if (targetNode !== null) {
  //     (targetNode.children ??= []).push(newNode);
  //     targetNode.expanded = true;
  //   }
  // };

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
    findNodeById,
    findParentNode,
    openAll,
    closeAll,
    dropValidatorHandler
  };
}
