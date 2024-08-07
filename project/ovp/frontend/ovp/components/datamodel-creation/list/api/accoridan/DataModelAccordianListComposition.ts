import { ref, Ref } from "vue";
import _ from "lodash";
import type { DataModelAccordianListProps } from "~/components/datamodel-creation/list/api/accoridan/DataModelAccoridanListProps";
import type { DataModelListEvent } from "~/components/datamodel-creation/list/base/DataModelListEvent.interface";

interface DataModelAccordianListCompositionImpl extends DataModelListEvent {
  listData: Record<string, any[]>;
  searchLabel: Ref<string>;
  checkShowListData(value: any[]): boolean;
  onSearchText(value: string): void;
  onResetSearchText(): void;
}

export function DataModelAccordianListComposition(
  props: DataModelAccordianListProps,
  emitBookmark: (value: string) => void,
  emitItemClick: (value: string) => void,
  emitDeleteItem: (value: string) => void,
  emitSelectItem: (value: string) => void,
  emitSearchChange: (value: string) => void,
): DataModelAccordianListCompositionImpl {
  const isSelectedData: (item: any) => boolean = (item) => {
    const itemId = item[props.valueKey];
    const findItem = _.find(props.selectedItems, { [props.valueKey]: itemId });
    return !!findItem;
  };

  const listData = reactive<Record<string, any[]>>({}); // reactive로 변경

  const setListData: () => void = () => {
    for (const key in props.data) {
      const dataValue = props.data[key] as any[];
      listData[key] = dataValue.map((item) => {
        const isSelected = isSelectedData(item);
        return {
          ...item,
          label: item[props.labelKey],
          value: item[props.valueKey],
          isChecked: false, // checkbox 선택 여부
          idShowDetail: false, // 단일선택(아이템) 여부
          isShowContextMenu: false, // "복사" 컨텍스트 메뉴 클릭 여부
          isShowContextMenuBtn: false, // 컨텍스트 메뉴 버튼 클릭 여부
          isShow: true, // 검색 처리
          isSelected: isSelected,
        };
      });
    }
  };

  /**
   * 리스트 값이 변경되면 일반 리스트의 속성값도 변경되야하므로 다중 watch
   */
  watchEffect(() => {
    setListData();
  });

  /**
   * (이벤트) 리스트 검색
   * @param value
   */
  const searchLabel: Ref<string> = ref("");

  const onSearchText: (value: string) => void = (value) => {
    searchLabel.value = value;
    emitSearchChange(searchLabel.value);
  };

  /**
   * (이벤트) 리스트 검색 초기화
   */
  const onResetSearchText: () => void = () => {
    searchLabel.value = "";
    emitSearchChange(searchLabel.value);
  };

  /**
   (이벤트) Context-menu 제어
   * @param itemValue
   * @param checked
   */
  const onShowContextMenu = (itemValue: string, checked: boolean | null) => {};

  /**
   * (이벤트) Context-menu 제어
   * @param itemValue
   * @param checked
   */
  const onShowContextMenuBtn = (
    itemValue: string,
    checked: boolean | null,
  ) => {};

  const checkShowListData: (item: any[]) => boolean = (item) => {
    if (!item || item.length < 1) {
      return false;
    }
    if (
      _.every(item, { isSelected: true }) ||
      _.every(item, { isShow: false })
    ) {
      return false;
    }
    return true;
  };
  const onChangeBookmark = (value: string) => {
    emitBookmark(value);
  };

  const onClickDataModelItem = (value: string) => {
    emitItemClick(value);
  };

  const onDeleteItem = (value: string) => {
    emitDeleteItem(value);
  };

  const onSelectItem = (value: string) => {
    emitSelectItem(value);
  };

  const onCheckItem = (value: string, checked: boolean) => {};
  const onResetSearchFilter: () => void = () => {};
  const onSelectFilter: (filterKey: string, value: string) => void = () => {};

  return {
    ...props,
    listData,
    searchLabel,
    checkShowListData,
    onSearchText,
    onSelectFilter,
    onResetSearchFilter,
    onResetSearchText,
    onShowContextMenu,
    onShowContextMenuBtn,
    onChangeBookmark,
    onClickDataModelItem,
    onDeleteItem,
    onSelectItem,
    onCheckItem,
  };
}
