import { ref, Ref } from "vue";
import { MenuSearchTreeProps } from "~/components/extends/menu-seach/tree/MenuSearchTreeProps";
import { TreeViewItem } from "~/components/extends/tree/TreeProps";

export interface MenuSearchTreeCompositionImpl extends MenuSearchTreeProps {
  isMenuSearchShow: Ref<boolean>;
  selectedListData: Ref<any[]>;
  onClickOpenMenuSearch(): void;
  onCancel(): void;
  changeMenuSearch(value: any[]): void;
  applySelectedData(value: any[]): void;
}

export function MenuSearchTreeComposition(
  props: MenuSearchTreeProps,
  applyData: (value: TreeViewItem[]) => void,
  openMenuSearch: () => void,
  panelClosed: () => void
): MenuSearchTreeCompositionImpl {
  const selectedListData: Ref<any[]> = ref([]);
  const setSelectedListData: () => void = () => {
    selectedListData.value = Array.isArray(props.selectedItems) ? props.selectedItems : [props.selectedItems];
  };

  watchEffect(() => {
    setSelectedListData();
  });

  const isMenuSearchShow: Ref<boolean> = ref(false);
  const onClickOpenMenuSearch: () => void = () => {
    isMenuSearchShow.value = !isMenuSearchShow.value;
    openMenuSearch();
  };

  const onCancel: () => void = () => {
    isMenuSearchShow.value = false;
  };

  const changeMenuSearch: (value: any[]) => void = (value) => {
    applySelectedData(value);
    onClickOpenMenuSearch();
  };

  const applySelectedData: (value: any[]) => void = (value) => {
    applyData(value);
  };

  // 선택 panel 이 닫힐때, 이벤트 처리
  watch(
    () => isMenuSearchShow.value,
    (newVal) => {
      if (!newVal) {
        panelClosed();
      }
    }
  );

  return {
    ...props,
    isMenuSearchShow,
    selectedListData,
    changeMenuSearch,
    applySelectedData,
    onClickOpenMenuSearch,
    onCancel
  };
}
