export interface NavigationFunctionality {
  isDisabled(value: string | number): boolean;
  move(index: number): void;
  toggleList(): void;
}
