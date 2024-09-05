import { ComputedRef, ref, Ref } from "vue";
import type { DataModelListProps } from "~/components/datamodel-creation/list/base/DataModelListProps";
import type { DataModelListEvent } from "~/components/datamodel-creation/list/base/DataModelListEvent.interface";
import _ from "lodash";

export interface DataModelListCompositionImpl
  extends DataModelListProps,
    DataModelListEvent {
  listData: Ref<any[]>;
  selectedFilter: Record<string, any>;
  searchLabel: Ref<string>;
  checkShowListData: ComputedRef<boolean>;
  setSearchFilter(): void;
  setListData(): void;
}

export function DataModelListComposition(
  props: DataModelListProps,
  emitBookmark: (value: string) => void,
  emitItemClick: (value: string) => void,
  emitDeleteItem: (value: any[]) => void,
  emitCheckItem: (value: any[]) => void,
): DataModelListCompositionImpl {
  const listData: Ref<any[]> = ref([]);
  const setListData: () => void = () => {
    listData.value = props.data.map((item) => {
      return {
        ...item,
        label: item[props.labelKey],
        value: item[props.valueKey],
        isChecked: false, // checkbox 선택 여부
        idShowDetail: false, // 단일선택(아이템) 여부
        isShowContextMenu: false, // "복사" 컨텍스트 메뉴 클릭 여부
        isShowContextMenuBtn: false, // 컨텍스트 메뉴 버튼 클릭 여부
        isShow: true, // 검색 처리
      };
    });
  };

  const searchLabel: Ref<string> = ref("");
  const selectedFilter = reactive<Record<string, any>>({}); // reactive로 변경
  const setSearchFilter: () => void = () => {
    for (const key in props.filter) {
      selectedFilter[key] = [];
    }
    searchLabel.value = "";
  };

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
    setSearchFilter();
    listData.value = listData.value.map((item) => {
      item.isShow = true;
      return item;
    });
  };

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
      let itemValue = item[key];

      // 필터 선택값이 존재하지 않으면 통과
      const selectedFilterValue: any = selectedFilter[key];
      if (_.isEmpty(selectedFilterValue)) {
        continue;
      }

      // 데이터 값 비교
      if (Array.isArray(itemValue)) {
        itemValue = itemValue as [];
        const isValueNotPresent = selectedFilterValue.some((filterValue) =>
          itemValue.includes(filterValue.key),
        );
        if (!isValueNotPresent) {
          return false;
        }
      } else {
        // 문자열 일때 비교
        const valueExists = selectedFilterValue.some(
          (item) => item.key === itemValue,
        );
        if (!valueExists) {
          return false;
        }
      }
    }
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

  /**
   * (이벤트) 북마크 변경 이벤트
   *  - API 연동 필요
   * @param value
   */
  const onChangeBookmark = (value: string) => {
    emitBookmark(value);
  };

  /**
   * (이벤트) item 클릭 이벤트
   * @param value
   */
  const onClickDataModelItem = (value: string) => {
    // 다른 클릭된 항목은 모두 초기화 처리

    listData.value = listData.value.map((el) => {
      return {
        ...el,
        idShowDetail: el[props.valueKey] === value,
      };
    });
    emitItemClick(value);
  };

  /**
   * (이벤트) 선택 해제
   * @param value
   */
  const onDeleteItem = (value: string) => {
    listData.value = listData.value.map((el) => {
      return {
        ...el,
        isChecked: el[props.valueKey] === value ? false : el.isChecked,
      };
    });
    const selectedList = $_reject(listData.value, { id: value });
    emitDeleteItem(selectedList);
  };

  /**
   * (이벤트) item 선택
   * @param value
   * @param checked
   */
  const onCheckItem = (value: string, checked: boolean) => {
    listData.value = listData.value.map((el) => {
      return {
        ...el,
        isChecked: el[props.valueKey] === value ? checked : el.isChecked,
      };
    });

    const selectedData = $_filter(listData.value, { isChecked: true });
    emitCheckItem(selectedData);
  };

  /**
   * 검색/필터 결과에 따른 listData 길이 확인
   */
  const checkShowListData: ComputedRef<boolean> = computed(() => {
    if (!listData.value || listData.value.length < 1) {
      return false;
    }
    return !_.every(listData.value, { isShow: false });
  });

  return {
    ...props,
    listData,
    searchLabel,
    selectedFilter,
    checkShowListData,
    setSearchFilter,
    setListData,
    onSelectFilter,
    onSearchText,
    onResetSearchText,
    onResetSearchFilter,
    onShowContextMenu,
    onShowContextMenuBtn,
    onChangeBookmark,
    onClickDataModelItem,
    onDeleteItem,
    onCheckItem,
  };
}
