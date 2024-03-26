import { SelectProps } from "@/components/extends/common/interfaces/props/Select.interface";

export interface CheckBoxListProps extends SelectProps {
  checkedList?: (string | number)[];
}
