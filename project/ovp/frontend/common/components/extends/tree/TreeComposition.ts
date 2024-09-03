import { TreeProps, TreeViewItem } from "./TreeProps";
import { uuid } from "vue3-uuid";
import _ from "lodash";

export interface TreeComposition extends TreeProps {
  treeItems: Ref<TreeViewItem[]>;
  showTree: Ref<boolean>;
  createNewTreeItem(parentId: string, id?: string, name?: string, desc?: string, order?: number): TreeViewItem;
  openAll(): void;
  closeAll(): void;
  dropValidatorHandler: any;
}

export function TreeComposition(props: TreeProps): TreeComposition {
  const showTree = ref(true);

  const updateStatus = (items: any[], ids: string[], statusKey: string): void => {
    _.forEach(items, (item) => {
      // ids 배열에 현재 항목의 id가 포함되어 있는지 확인
      if (ids.includes(item.id)) {
        item[statusKey] = true;
      }

      // 자식 노드가 있는 경우, 재귀적으로 호출
      if (item.children && item.children.length > 0) {
        updateStatus(item.children, ids, statusKey);
      }
    });
  };

  const treeItems: Ref<TreeViewItem[]> = ref<TreeViewItem[]>([]);
  treeItems.value = _.cloneDeep(props.items ?? []);

  if (props.useFirSelect) {
    treeItems.value[0].selected = true;
  }

  // 체크박스를 사용하는 경우, 체크여부를 object에 추가해준다.
  if (props.isCheckable && props.checkedIds && props.checkedIds.length > 0) {
    updateStatus(treeItems.value, props.checkedIds, "checked");
  }
  // NOTE : 처음 선택값도 disabled 랑 같이 처리하려 했으나, tree lib 가 제대로 동작하지 않아 주석처리함.
  // if (props.selectedIds && props.selectedIds.length > 0) {
  //   updateStatus(treeItems.value, props.selectedIds, "selected");
  // }
  // 비활성화 하는 node id 항목이 있을 경우, 비활성화 여부를 object 에 추가해준다.
  if (props.disabledIds && props.disabledIds.length > 0) {
    updateStatus(treeItems.value, props.disabledIds, "disabled");
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

  const dropValidator = (thisNode: TreeViewItem, targetNode: TreeViewItem): any => {
    // 자기노드 밑으로 drop 할 수 없음.
    if (thisNode.id === targetNode.id) {
      return false;
    }
    // 부모노드는 자기 후손 노드로 drop 할수없음.
    // targetNode 기준 부모노드 List 를 조회해서 thisNode 의 id 가 있는지 확인 필요함.
    const parentIds = findAncestors(treeItems.value, targetNode.id);
    if (parentIds.includes(thisNode.id)) {
      return false;
    }

    // immutableItems 의 항목은 drag/drop 할수없음.
    if (Array.isArray(props.immutableItems) && props.immutableItems.some((item) => thisNode.id.includes(item))) {
      alert("미분류 항목은 이동 불가능 합니다.");
      return false;
    }

    // TODO: NOTIFICATION - confirm 창 구현 완료 되면 아래 window.prompt 창 변경처리 필요.
    const msg = `${thisNode.name} 카테고리를 ${targetNode.name} 하위로 이동 하시겠습니까?`;

    const res = window.prompt(`${msg} (answer : 'yes'`);
    if (res === "yes") {
      const newNode = _.cloneDeep(thisNode);
      newNode.parentId = targetNode.id;

      return props.dropValidator(thisNode, targetNode, newNode);
    }
  };
  // vue3-tree-vue 에서는 dropValidator 이 undefined 일때만 drag/drop 이 동작하지 않음.
  const dropValidatorHandler: any = props.useDraggable ? dropValidator : undefined;

  watch(
    () => props.items,
    (newItems) => {
      // tree 문제로 인해서 showTree 를 false/true 해서 tree 를 refresh 시켜줘야함.
      showTree.value = false;
      setTimeout(() => {
        showTree.value = true;
      }, 0);

      treeItems.value = _.cloneDeep(newItems ?? []);
      openAll();

      // 리스트 변경 시 checked 항목 추가
      if (props.isCheckable && props.checkedIds) {
        updateStatus(treeItems.value, props.checkedIds, "checked");
      }
    }
  );

  watch(
    () => [props.disabledIds, props.selectedIds],
    ([disabledIds]) => {
      // disabled 상태 업데이트
      if (disabledIds && disabledIds.length > 0) {
        updateStatus(treeItems.value, disabledIds, "disabled");
      }

      // selected 상태 업데이트
      // if (selectedIds && selectedIds.length > 0) {
      //   updateStatus(treeItems.value, selectedIds, "selected");
      // }
    }
  );

  return {
    ...props,
    showTree,
    treeItems,
    createNewTreeItem,
    openAll,
    closeAll,
    dropValidatorHandler
  };
}
