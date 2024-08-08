import { SelectFunctionality } from "@/components/extends/common/interfaces/functions/Select.interface";
import { SelectEvents } from "~/components/extends/common/interfaces/events/Select.interface";
import { ComputedRef, ref, Ref } from "vue";
import _ from "lodash";
import { MenuSearchTreeContentsProps } from "~/components/extends/menu-seach/tree/contents/MenuSearchTreeContentsProps";
import { TreeViewItem } from "~/components/extends/tree/TreeProps";

interface MenuSearchTreeContentsCompositionImpl extends MenuSearchTreeContentsProps, SelectFunctionality, SelectEvents {
  // Menu-search 컴포넌트에서만 사용하는 data, function
  listData: Ref<any[]>;
  selectedListData: Ref<any[]>;
  selectedListDataIds: ComputedRef<string[]>;
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
  const initListData: () => void = () => {
    originListData.value = _.cloneDeep(props.data);
    listData.value = _.cloneDeep(props.data);
  };

  const originSelectedListData: Ref<any> = ref([]);
  const selectedListData: Ref<any[]> = ref([]);
  const initSelectedListData: () => void = () => {
    const originData = Array.isArray(props.selectedItems) ? props.selectedItems : [props.selectedItems];
    originSelectedListData.value = _.cloneDeep(originData);
    selectedListData.value = _.cloneDeep(originData);
  };
  const selectedListDataIds: ComputedRef<string[]> = computed(() => {
    console.log(
      "selectedListDataIds",
      selectedListData.value.map((el) => el.id)
    );
    return selectedListData.value.map((el) => el.id);
  });

  /**
   * 선택 리스트 값이 변경되면 일반 리스트의 속성값도 변경되야하므로 다중 watch
   */
  watchEffect(() => {
    initListData();
    initSelectedListData();
  });

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
  const searchList: (value: string) => void = (value) => {};

  const onNodeChecked = (checkedNodeList: TreeViewItem[]) => {};

  const onNodeClicked = (node: TreeViewItem) => {};

  const onApply: () => void = () => {};
  const onReset: () => void = () => {};
  const onCancel: () => void = () => {};

  // 기본 Select 기능 재정의
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
