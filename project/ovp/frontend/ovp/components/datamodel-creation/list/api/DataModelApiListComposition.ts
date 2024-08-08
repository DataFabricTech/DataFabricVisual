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

  const setListData: () => void = () => {
    // TOOD: store로 빼기 -> listData 초기화를 store에서 진행하기.,
    compos.listData.value = props.data;
    console.log(compos.listData.value);
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
    if (!compos.listData.value || compos.listData.value.length < 1) {
      return false;
    }
    return !_.every(compos.listData.value, { isSelected: true });
  });

  return {
    ...props,
    ...compos,
    // listData,
    // selectedFilter,
    // searchLabel,
    selectedSort,
    checkShowListData,
    onSelectFilter,
    onSearchText,
    onResetSearchText,
    onResetSearchFilter,
    onChangeSort,
  };
}
