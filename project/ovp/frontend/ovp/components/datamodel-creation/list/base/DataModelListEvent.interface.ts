export interface DataModelListEvent {
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
  onCheckItem(value: string, checked: boolean): void;
}
