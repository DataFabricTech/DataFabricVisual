export interface SplitterProps {
  modelValue: number;
  limits?: number[];
  unit?: "%" | "px";
  horizontal?: boolean;
}
