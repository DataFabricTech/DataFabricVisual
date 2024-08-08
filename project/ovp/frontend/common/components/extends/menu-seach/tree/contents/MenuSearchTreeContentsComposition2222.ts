import { SelectFunctionality } from "@/components/extends/common/interfaces/functions/Select.interface";
import { SelectEvents } from "~/components/extends/common/interfaces/events/Select.interface";
import { ref, Ref } from "vue";
import _ from "lodash";
import { MenuSearchTreeContentsProps } from "~/components/extends/menu-seach/tree/contents/MenuSearchTreeContentsProps";
import { TreeViewItem } from "~/components/extends/tree/TreeProps";

interface MenuSearchTreeContentsCompositionImpl extends MenuSearchTreeContentsProps, SelectFunctionality, SelectEvents {
  // Menu-search 컴포넌트에서만 사용하는 data, function
  listData: Ref<any[]>;
  selectedListData: Ref<any[]>;
  selectedListDataIds: Ref<string[]>;
  searchLabel: Ref<string>;

  onSearchText(value: string): void;
  onResetSearchText(): void;
  onApply(): void;
  onReset(): void;
  onCancel(): void;

  onNodeChecked(checkedNodeIds: TreeViewItem[]): void;
  onNodeClicked(checkedNodeIds: TreeViewItem): void;
}

export function MenuSearchTreeContentsComposition(
  props: MenuSearchTreeContentsProps,
  applyData: (value: TreeViewItem[]) => void,
  cancelData: () => void
): MenuSearchTreeContentsCompositionImpl {
  const originListData: Ref<any[]> = ref([]);
  const listData: Ref<any[]> = ref([]);
  const initListData = () => {
    originListData.value = _.cloneDeep(props.data);
    listData.value = _.cloneDeep(props.data); // 내부에서 사용되는 리스트 데이터 - 검색 등에 사용되는 데이터
    console.log("initListData", listData.value);
  };

  const originSelectedListData: Ref<any> = ref([]);
  const selectedListDataIds: Ref<string[]> = ref([]);
  const selectedListData: Ref<any[]> = ref([]);

  const initSelectedListData: () => void = () => {
    const originData = Array.isArray(props.selectedItems) ? props.selectedItems : [props.selectedItems];
    originSelectedListData.value = _.cloneDeep(originData);
    selectedListData.value = _.cloneDeep(originData);
    selectedListDataIds.value = selectedListData.value.map((el) => el[props.valueKey]); // tree에게 넘겨주는 선택 데이터
    console.log("initSelectedListData", selectedListData.value);
    console.log("initSelectedListData", selectedListDataIds.value);
  };

  /**
   * 선택 리스트 값이 변경되면 일반 리스트의 속성값도 변경되야하므로 다중 watch
   */
  watch(
    () => [props.data, props.selectedItems],
    () => {
      initListData();
      initSelectedListData();
    },
    { deep: true, immediate: true }
  );

  /**
   * (이벤트) 리스트 검색
   * @param value
   */
  const searchLabel: Ref<string> = ref<string>("");
  const onSearchText: (value: string) => void = (value) => {
    searchLabel.value = value;
    searchList(searchLabel.value);
  };

  /**
   * (이벤트) 리스트 검색 초기화
   */
  const onResetSearchText: () => void = () => {
    searchLabel.value = "";
    searchList(searchLabel.value);
  };

  /**
   * 리스트 내 검색
   * @param value
   */
  const searchList: (value: string) => void = (value) => {
    const filteredTree = searchTree(value, originListData.value); //원본 데이터에서 검색 필요
    // TODO: checked 변경 시 원본 데이터도 변경 필요
    console.log("filteredTree", filteredTree);
    listData.value = filteredTree;
  };

  /**
   * 리스트 내 검색 > 재귀
   * @param searchLabel
   * @param items
   */
  const searchTree = (searchLabel: string, items: TreeViewItem[]): TreeViewItem[] => {
    const result: TreeViewItem[] = [];

    for (const item of items) {
      const children = searchTree(searchLabel, item.children);
      if (children.length > 0 || item.name.includes(searchLabel)) {
        result.push({
          ...item,
          children
        });
      }
    }

    return result;
  };

  const onNodeChecked = (checkedNodeList: TreeViewItem[]) => {
    selectedListData.value = checkedNodeList;
    selectedListDataIds.value = checkedNodeList.map((node: TreeViewItem) => node.id);
  };

  const onNodeClicked = (node: TreeViewItem) => {
    selectedListData.value = [node];
    selectedListDataIds.value = [node].map((node: TreeViewItem) => node.id);
  };

  const onApply: () => void = () => {};
  const onReset: () => void = () => {
    console.log("onReset");
    // 선택 값 초기화
    selectedListData.value = [];
    selectedListDataIds.value = [];
  };
  const onCancel: () => void = () => {
    console.log("onCancel");
    // 원본 데이터를 통한 값 초기화
    selectedListData.value = originSelectedListData.value;
    selectedListDataIds.value = originSelectedListData.value.map((node: TreeViewItem) => node.id);
    listData.value = originListData.value;
    cancelData();
  };

  // 기본 Select 기능 재정의 - 사용 X
  const toggleList: () => void = () => {};

  const isDisabled: (value: string | number) => boolean = (value) => {
    console.log(value);
    return false;
  };

  const selectItem: (option: { [key: string]: string | number }) => void = (option) => {
    return option;
  };

  const onSelect: (value: string | number) => void = (value) => {
    return value;
  };
  return {
    ...props,
    listData,
    searchLabel,
    selectedListData,
    selectedListDataIds,
    onSearchText,
    onResetSearchText,

    onNodeChecked,
    onNodeClicked,

    onApply,
    onReset,
    onCancel,

    toggleList,
    isDisabled,
    onSelect,
    selectItem
  };
}
