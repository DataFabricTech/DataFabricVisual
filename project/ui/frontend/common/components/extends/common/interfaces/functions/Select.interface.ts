export interface SelectFunctionality {
  selectItem(options: { [key: string]: string | number }): void;
  toggleList(): void;
  isDisabled(value: string | number): boolean;
}
