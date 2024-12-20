import { MenuSearchItemImpl } from "~/components/extends/menu-seach/MenuSearchComposition";
import { MenuSearchTypeProps } from "~/components/extends/menu-seach/type/MenuSearchTypeProps";
import { MenuSearchTypeCompositionImpl, MenuSearchTypeComposition } from "../type/MenuSearchTypeComposition";

interface MenuSearchTagComposition extends MenuSearchTypeCompositionImpl {
  onDeleteTag(value: any): void;
}

export function MenuSearchTagComposition(
  props: MenuSearchTypeProps,
  applyData: (value: MenuSearchItemImpl | MenuSearchItemImpl[]) => void,
  openMenuSearch: () => void,
  panelClosed: () => void
): MenuSearchTagComposition {
  const composition = MenuSearchTypeComposition(props, applyData, openMenuSearch, panelClosed);

  /**
   * Tag 삭제
   * @param value
   */
  const onDeleteTag: (value: any) => void = (value) => {
    composition.selectedListData.value = composition.selectedListData.value.filter(
      (item) => item[props.valueKey] !== value[props.valueKey]
    );
    composition.applySelectedData(composition.selectedListData.value);
  };

  return {
    ...props,
    ...composition,
    onDeleteTag
  };
}
