import { ref, Ref } from "vue";
import type { DataModelListProps } from "~/components/datamodel-creation/list/DataModelListProps";

interface DataModelListCompositionImpl extends DataModelListProps {
  // Menu-search 컴포넌트에서만 사용하는 data, function
  listData: Ref<any[]>;
  selectedFilter: Record<string, string>;
  searchLabel: Ref<string>;
  //
  onSearchText(value: string): void;
  onSelectFilter(filterKey: string, value: string): void;
  onResetSearchText(): void;
  onResetSearchFilter(): void;
  onShowContextMenu(itemValue: string, checked: boolean | null): void;
  onShowContextMenuBtn(itemValue: string, checked: boolean | null): void;
  onChangeBookmark(value: string): void;
  onClickDataModelItem(value: string): void;
  onDeleteItem(value: string): void;
  onSelectItem(value: string): void;
}

export function DataModelListComposition(
  props: DataModelListProps,
  emitBookmark: (value: string) => void,
  emitItemClick: (value: string) => void,
  emitDeleteItem: (value: string) => void,
  emitSelectItem: (value: string) => void,
): DataModelListCompositionImpl {
  const listData: Ref<any[]> = ref([]);
  const setListData: () => void = () => {
    listData.value = props.data.map((item) => {
      const isChecked = props.listType === "selected";
      return {
        ...item,
        label: item[props.labelKey],
        value: item[props.valueKey],
        isChecked: isChecked, // 선택 여부
        idShowDetail: false, // 단일선택 여부
        isShowContextMenu: false, // "복사" 컨텍스트 메뉴 클릭 여부
        isShowContextMenuBtn: false, // 컨텍스트 메뉴 버튼 클릭 여부
        isShow: true, // 검색 처리
      };
    });
  };

  const selectedFilter = reactive<Record<string, string>>({}); // reactive로 변경
  const setSelectedFilter: () => void = () => {
    for (const key in props.filter) {
      selectedFilter[key] = "";
    }
  };

  /**
   * 선택 리스트 값이 변경되면 일반 리스트의 속성값도 변경되야하므로 다중 watch
   */
  watchEffect(() => {
    setListData();
    setSelectedFilter();
  });

  /**
   * (이벤트) 필터 값 변경 검색
   * @param value
   */
  const onSelectFilter: (filterKey: string, value: string) => void = (
    filterKey,
    value,
  ) => {
    selectedFilter[filterKey] = value;
    searchList();
  };

  /**
   * (이벤트) 필터, 검색어 모두 초기화
   * @param value
   */
  const onResetSearchFilter: () => void = () => {
    setSelectedFilter();
    searchLabel.value = "";
    listData.value = listData.value.map((item) => {
      item.isShow = true;
      return item;
    });
  };

  const searchLabel: Ref<string> = ref("");
  /**
   * (이벤트) 리스트 검색
   * @param value
   */
  const onSearchText: (value: string) => void = (value) => {
    searchLabel.value = value;
    searchList();
  };

  /**
   * (이벤트) 리스트 검색 초기화
   */
  const onResetSearchText: () => void = () => {
    searchLabel.value = "";
    searchList();
  };

  /**
   * 리스트 내 검색
   * @param value
   */
  const searchList: () => void = () => {
    // filter, searchText And 비교.
    listData.value = listData.value.map((item) => {
      item.isShow = checkFilter(item) && checkSearchText(item.label);
      return item;
    });
  };

  /**
   * 리스트 내 검색 > 필터 검색 처리
   * @param item
   */
  const checkFilter = (item: any): boolean => {
    for (const key in selectedFilter) {
      // NOTE: filter의 key는 tags.tagFqn, owner.displayName.keyword 이런식으로 되어 있어서 데이터 목록 아이템 속성 값과 맞지 않음.
      // "." 기준으로 스플릿 처리
      const itemKey = key.split(".")[0];

      // 데이터가 없으면 무조건 false
      let itemValue = item[itemKey];
      if (itemValue === undefined) {
        return false;
      }

      // 데이터 값 비교
      const selectedFilterValue = selectedFilter[key];
      if (!selectedFilterValue) {
        continue;
      }

      if (Array.isArray(itemValue)) {
        itemValue = itemValue as [];
        if (!itemValue.includes(selectedFilterValue)) {
          return false;
        }
      } else {
        // 문자열 일때 비교
        if (itemValue !== selectedFilterValue) {
          return false;
        }
      }
    }
    console.log("checkFilter 모두 통과");
    return true;
  };

  /**
   * 리스트 내 검색 > 검색어 처리
   * @param itemLabel
   */
  const checkSearchText: (itemLabel: string) => boolean = (itemLabel) => {
    // 검색 로직. -> 현재 데이터 목록을 변경하지 않고 검색 결과를 출력해야함.
    return (itemLabel + "")
      .toLowerCase()
      .includes(searchLabel.value.toLowerCase());
  };

  /**
   (이벤트) Context-menu 제어
   * @param itemValue
   * @param checked
   */
  const onShowContextMenu = (itemValue: string, checked: boolean | null) => {
    listData.value = listData.value.map((el) => ({
      ...el,
      isShowContextMenu:
        checked === null ? false : el[props.valueKey] === itemValue,
    }));
  };

  /**
   * (이벤트) Context-menu 제어
   * @param itemValue
   * @param checked
   */
  const onShowContextMenuBtn = (itemValue: string, checked: boolean | null) => {
    listData.value = listData.value.map((el) => ({
      ...el,
      isShowContextMenuBtn:
        checked === null ? false : el[props.valueKey] === itemValue,
    }));
  };

  const onChangeBookmark = (value: string) => {
    emitBookmark(value);
  };

  const onClickDataModelItem = (value: string) => {
    listData.value = listData.value.map((el) => {
      return {
        ...el,
        idShowDetail: el[props.valueKey] === value,
      };
    });
    emitItemClick(value);
  };

  const onDeleteItem = (value: string) => {
    emitDeleteItem(value);
  };

  const onSelectItem = (value: string) => {
    emitSelectItem(value);
  };

  return {
    ...props,
    listData,
    searchLabel,
    selectedFilter,
    onSelectFilter,
    onSearchText,
    onResetSearchText,
    onResetSearchFilter,
    onShowContextMenu,
    onShowContextMenuBtn,
    onChangeBookmark,
    onClickDataModelItem,
    onDeleteItem,
    onSelectItem,
  };
}
