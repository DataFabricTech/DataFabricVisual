import { MenuSearchItemImpl } from "~/components/extends/menu-seach/MenuSearchComposition";
import { MenuSearchButtonProps } from "~/components/extends/menu-seach/button/MenuSearchButtonProps";
import { ref, Ref } from "vue";

interface MenuSearchButtonComposition extends MenuSearchButtonProps {
  isMenuSearchShow: Ref<boolean>;
  selectedListData: Ref<any[]>;
  onClickOpenMenuSearch(): void;
  onCancel(): void;
  changeMenuSearch(value : any[] | {}): void;
}

export function MenuSearchButtonComposition(
  props: MenuSearchButtonProps,
  applyData: (value: MenuSearchItemImpl | MenuSearchItemImpl[]) => void,
  openMenuSearch: () => void
): MenuSearchButtonComposition {
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

  const changeMenuSearch : (value : any[] | {}) => void = (value) => {
    selectedListData.value = props.isMulti ? value as [] : [value];
    applyData(value);
    onClickOpenMenuSearch();
  };

  return {
    ...props,
    isMenuSearchShow,
    selectedListData,
    changeMenuSearch,
    onClickOpenMenuSearch,
    onCancel
  };
}
