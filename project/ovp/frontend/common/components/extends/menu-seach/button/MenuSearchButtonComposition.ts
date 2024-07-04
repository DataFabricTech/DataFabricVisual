import { MenuSearchItemImpl } from "~/components/extends/menu-seach/MenuSearchComposition";
import { MenuSearchButtonProps } from "~/components/extends/menu-seach/button/MenuSearchButtonProps";
import { ref, Ref } from "vue";

interface MenuSearchButtonComposition extends MenuSearchButtonProps {
  isShow: Ref<boolean>;
  selectedListData: Ref<any[]>;
  onClickOpenMenuSearch(): void;
  changeMenuSearch(value : any[] | {}): void;
}

export function MenuSearchButtonComposition(
  props: MenuSearchButtonProps,
  applyData: (value: MenuSearchItemImpl | MenuSearchItemImpl[]) => void,
): MenuSearchButtonComposition {
  const selectedListData: Ref<any[]> = ref([]);
  const setSelectedListData: () => void = () => {
    selectedListData.value = Array.isArray(props.selectedItems) ? props.selectedItems : [props.selectedItems];
  };

  setSelectedListData();

  const isShow: Ref<boolean> = ref(false);
  const onClickOpenMenuSearch: () => void = () => {
    isShow.value = !isShow.value;
  };

  const changeMenuSearch : (value : any[] | {}) => void = (value) => {
    selectedListData.value = props.isMulti ? value as [] : [value];
    applyData(value);
    onClickOpenMenuSearch();
  };

  return {
    ...props,
    isShow,
    selectedListData,
    changeMenuSearch,
    onClickOpenMenuSearch
  };
}
