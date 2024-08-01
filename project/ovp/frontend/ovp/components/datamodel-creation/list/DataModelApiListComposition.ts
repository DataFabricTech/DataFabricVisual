import { ref, Ref } from "vue";
import type { DataModelListProps } from "~/components/datamodel-creation/list/DataModelListProps";
import { DataModelListComposition } from "~/components/datamodel-creation/list/DataModelListComposition";

interface DataModelListCompositionImpl {}

interface DataModelApiListCompositionImpl extends DataModelListCompositionImpl {
  // Menu-search 컴포넌트에서만 사용하는 data, function
  searchLabel: Ref<string>;
  //
  onSelectFilter(filterKey: string, value: string): void;
}

export function DataModelApiListComposition(
  props: DataModelListProps,
  emitBookmark: (value: string) => void,
  emitItemClick: (value: string) => void,
  emitDeleteItem: (value: string) => void,
  emitSelectItem: (value: string) => void,
): DataModelApiListCompositionImpl {
  const compos = DataModelListComposition(
    props,
    emitBookmark,
    emitItemClick,
    emitDeleteItem,
    emitSelectItem,
  );

  /**
   * (이벤트) 필터 값 변경 검색
   * @param value
   */
  const onSelectFilter: (filterKey: string, value: string) => void = (
    filterKey,
    value,
  ) => {
    // compos.selectedFilter[filterKey] = value;
    // compos.searchList();
    console.log("onSelectFilter 재정의");
  };

  return {
    ...props,
    ...compos,
    onSelectFilter,
  };
}
