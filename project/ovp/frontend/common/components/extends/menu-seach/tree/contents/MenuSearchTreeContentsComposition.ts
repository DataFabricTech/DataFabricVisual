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
  const initListData: (value: any[]) => void = (value) => {
    listData.value = _.cloneDeep(value);
  };

  const originSelectedListData: Ref<any> = ref([]);
  const selectedListData: Ref<any[]> = ref([]);
  const initSelectedListData: (value: any) => void = (value) => {
    selectedListData.value = _.cloneDeep(value);
  };
  const selectedListDataIds: ComputedRef<string[]> = computed(() => {
    return selectedListData.value.map((el) => el.id);
  });

  /**
   * 선택 리스트 값이 변경되면 일반 리스트의 속성값도 변경되야하므로 다중 watch
   */
  // watchEffect(() => {
  //   originListData.value = _.cloneDeep(props.data);
  //   initListData(props.data);
  //
  //   const originData = Array.isArray(props.selectedItems) ? props.selectedItems : [props.selectedItems];
  //   originSelectedListData.value = _.cloneDeep(originData);
  //   initSelectedListData(originData);
  // });
  const onCancel: () => void = () => {
    // 원본 데이터를 통한 값 초기화
    initSelectedListData(originSelectedListData.value);
    initListData(originListData.value);
    cancelData();
  };

  watch(
    () => props.isShow,
    (newValue) => {
      if (!newValue) {
        // outside 클릭했을 경우 취소( 원본 초기화) 작동
        onCancel();
      }
    },
    {
      immediate: true
    }
  );

  watch(
    () => props.data,
    (newValue) => {
      originListData.value = _.cloneDeep(newValue);
      initListData(newValue);
    },
    {
      immediate: true
    }
  );
  watch(
    () => props.selectedItems,
    (newValue) => {
      const originData = Array.isArray(newValue) ? newValue : [newValue];
      originSelectedListData.value = _.cloneDeep(originData);
      initSelectedListData(originData);
    },
    {
      immediate: true
    }
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
    //원본 데이터에서 검색 필요
    listData.value = searchTree(value, originListData.value);
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
  };

  const onNodeClicked = (node: TreeViewItem) => {
    selectedListData.value = [node];
  };

  const onApply: () => void = () => {
    // 적용된 값을 origin에 저장
    originSelectedListData.value = _.cloneDeep(selectedListData.value);
    originListData.value = _.cloneDeep(listData.value);

    const result = props.isMulti ? selectedListData.value : selectedListData.value[0];
    applyData(result);
  };

  const onReset: () => void = () => {
    // #explorer 내에 존재하는 모든 체크박스 상태 해제
    const checkboxes = document.querySelectorAll('#explorer input[type="checkbox"]');
    checkboxes.forEach((checkbox) => {
      checkbox.checked = false;
    });
    // 선택 값 초기화
    selectedListData.value = [];
    listData.value = originListData.value;
  };

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
