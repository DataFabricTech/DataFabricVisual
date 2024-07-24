import { MenuSearchItemImpl } from "~/components/extends/menu-seach/MenuSearchComposition";
import { MenuSearchButtonProps } from "~/components/extends/menu-seach/button/MenuSearchButtonProps";
import { ref, Ref } from "vue";

interface MenuSearchButtonComposition extends MenuSearchButtonProps {
  isShow: Ref<boolean>;
  selectedListData: Ref<any[]>;
  onClickOpenMenuSearch(): void;
  onCancel(): void;
  changeMenuSearch(value: any[] | {}): void;
}

export function MenuSearchButtonComposition(
  props: MenuSearchButtonProps,
  applyData: (value: MenuSearchItemImpl | MenuSearchItemImpl[]) => void,
  openMenuSearch: () => void,
  panelClosed: () => void
): MenuSearchButtonComposition {
  const selectedListData: Ref<any[]> = ref([]);
  const setSelectedListData: () => void = () => {
    selectedListData.value = Array.isArray(props.selectedItems) ? props.selectedItems : [props.selectedItems];
  };

  watchEffect(() => {
    setSelectedListData();
  });

  const isShow: Ref<boolean> = ref(false);
  const onClickOpenMenuSearch: () => void = () => {
    isShow.value = !isShow.value;
    openMenuSearch();
  };

  const onCancel: () => void = () => {
    isShow.value = false;
  };

  const changeMenuSearch: (value: any[] | {}) => void = (value) => {
    selectedListData.value = props.isMulti ? (value as []) : [value];
    applyData(value);
    onClickOpenMenuSearch();
  };

  // 선택 panel 이 닫힐때, 이벤트 처리
  watch(
    () => isShow.value,
    (newVal) => {
      if (!newVal) {
        panelClosed();
      }
    }
  );

  return {
    ...props,
    isShow,
    selectedListData,
    changeMenuSearch,
    onClickOpenMenuSearch,
    onCancel
  };
}
