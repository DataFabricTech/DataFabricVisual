import { ref, Ref } from "vue";
import _ from "lodash";
import type { DataModelAccordianListProps } from "~/components/datamodel-creation/list/api/accoridan/DataModelAccoridanListProps";
import type { DataModelListEvent } from "~/components/datamodel-creation/list/base/DataModelListEvent.interface";
import { uuid } from "vue3-uuid";

interface DataModelAccordianListCompositionImpl extends DataModelListEvent {
  listData: Record<string, any[]>;
  searchLabel: Ref<string>;
  checkShowListData(value: any[]): boolean;
  onSearchText(value: string): void;
  onResetSearchText(): void;
  onCheckItem(value: string, checked: boolean): void;
}

export function DataModelAccordianListComposition(
  props: DataModelAccordianListProps,
  emitBookmark: (value: string) => void,
  emitItemClick: (value: string) => void,
  emitDeleteItem: (value: string) => void,
  emitCheckItem: (value: any[]) => void,
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
          defaultId: uuid.v4(),
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
   * @param key
   * @param itemValue
   * @param checked
   */
  const onShowContextMenu = (
    key: string,
    itemValue: string,
    checked: boolean | null,
  ) => {
    listData[key].value = listData.value.map((el) => ({
      ...el,
      isShowContextMenu:
        checked === null ? false : el[props.valueKey] === itemValue,
    }));
  };

  /**
   * (이벤트) Context-menu 제어
   * @param key
   * @param itemValue
   * @param checked
   */
  const onShowContextMenuBtn = (
    key: string,
    itemValue: string,
    checked: boolean | null,
  ) => {
    listData[key].value = listData.value.map((el) => ({
      ...el,
      isShowContextMenuBtn:
        checked === null ? false : el[props.valueKey] === itemValue,
    }));
  };

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

  const onCheckItem = (value: string, checked: boolean) => {
    const checkedList = [];
    // 다른 key에서 value에 해당하는 item을 checked 해줌
    for (const key in listData) {
      listData[key] = listData[key].map((el: any) => {
        return {
          ...el,
          isChecked: el[props.valueKey] === value ? checked : el.isChecked,
        };
      });
      const selectedData = $_filter(listData[key], { isChecked: true });
      checkedList.push(...selectedData);
    }
    const nCheckedList = $_uniqBy(checkedList, props.valueKey);

    const filteredCheckedList = nCheckedList.filter(
      (item) => item !== null && item !== undefined,
    );
    emitCheckItem(filteredCheckedList);
  };
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
    onCheckItem,
  };
}
