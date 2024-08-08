import {
  DataModelListComposition,
  DataModelListCompositionImpl,
} from "~/components/datamodel-creation/list/base/DataModelListComposition";
import type { DataModelApiListProps } from "~/components/datamodel-creation/list/api/DataModelApiListProps";
import { ComputedRef, ref, Ref } from "vue";
import _ from "lodash";

export interface DataModelApiListCompositionImpl
  extends DataModelListCompositionImpl {
  listData: Ref<any[]>;
  selectedFilter: Record<string, any>;
  searchLabel: Ref<string>;
  selectedSort: Ref<string>;
  onChangeSort(value: string | number): void;
}

export function DataModelApiListComposition(
  props: DataModelApiListProps,
  emitBookmark: (value: string) => void,
  emitItemClick: (value: string) => void,
  emitDeleteItem: (value: string) => void,
  emitFilterChange: (value: {}) => void,
  emitSortChange: (value: string) => void,
  emitSearchChange: (value: string) => void,
  emitCheckItem: (value: string) => void,
): DataModelApiListCompositionImpl {
  const compos = DataModelListComposition(
    props,
    emitBookmark,
    emitItemClick,
    emitDeleteItem,
    emitCheckItem,
  );

  const listData: Ref<any[]> = ref([]);
  const isSelectedData: (item: any) => boolean = (item) => {
    const itemId = item[props.valueKey];
    const findItem = _.find(props.selectedItems, { [props.valueKey]: itemId });
    return !!findItem;
  };

  const setListData: () => void = () => {
    listData.value = props.data.map((item) => {
      const isSelected = isSelectedData(item);
      return {
        label: item[props.labelKey],
        value: item[props.valueKey],
        isChecked: false, // checkbox 선택 여부
        idShowDetail: false, // 단일선택(아이템) 여부
        isShowContextMenu: false, // "복사" 컨텍스트 메뉴 클릭 여부
        isShowContextMenuBtn: false, // 컨텍스트 메뉴 버튼 클릭 여부
        isSelected: isSelected, // 이미 선택여부 확인
      };
    });
  };

  /**
   * 리스트 값이 변경되면 일반 리스트의 속성값도 변경되야하므로 다중 watch
   */
  watchEffect(() => {
    setListData();
    compos.setSearchFilter();
  });

  /**
   * (이벤트) 정렬 변경
   * @param value
   */
  const selectedSort: Ref<string> = ref("");
  const onChangeSort: (value: string) => void = (value) => {
    selectedSort.value = value;
    console.log("onChangeSort", selectedSort.value);
    emitSortChange(selectedSort.value);
  };

  /**
   * (이벤트) 필터 값 변경 검색
   * @param value
   */
  const onSelectFilter: (filterKey: string, value: string) => void = (
    filterKey,
    value,
  ) => {
    compos.selectedFilter[filterKey] = value;
    emitFilterChange(compos.selectedFilter);
  };

  /**
   * (이벤트) 리스트 검색
   * @param value
   */
  const onSearchText: (value: string) => void = (value) => {
    compos.searchLabel = value;
    emitSearchChange(compos.searchLabel);
  };

  /**
   * (이벤트) 리스트 검색 초기화
   */
  const onResetSearchText: () => void = () => {
    compos.searchLabel = "";
    emitSearchChange(compos.searchLabel);
  };

  /**
   * (이벤트) 필터, 검색어, 정렬 모두 초기화
   * @param value
   */
  const onResetSearchFilter: () => void = () => {
    compos.setSearchFilter();
    selectedSort.value = "";

    emitSearchChange(compos.searchLabel);
    emitFilterChange(compos.selectedFilter);
    emitSortChange(selectedSort);
  };

  /**
   * 검색/필터 결과에 따른 listData 길이 확인
   */
  const checkShowListData: ComputedRef<boolean> = computed(() => {
    if (!listData.value || listData.value.length < 1) {
      return false;
    }
    return !_.every(listData.value, { isSelected: true });
  });
  return {
    ...props,
    ...compos,
    selectedSort,
    checkShowListData,
    onSelectFilter,
    onSearchText,
    onResetSearchText,
    onResetSearchFilter,
    onChangeSort,
  };
}
