export interface DataModelListEvent {
  // 검색
  onSearchText(value: string): void;
  onResetSearchText(): void;
  // 필터
  onSelectFilter(filterKey: string, value: string): void;
  onResetSearchFilter(): void;
  // Context-menu
  onShowContextMenu(itemValue: string, checked: boolean | null): void;
  onShowContextMenuBtn(itemValue: string, checked: boolean | null): void;
  // emit(북마크, 삭제, 선택, 클릭)
  onChangeBookmark(value: string): void;
  onClickDataModelItem(value: string): void;
  onDeleteItem(value: string): void;
  onCheckItem(value: string, checked: boolean): void;
}
