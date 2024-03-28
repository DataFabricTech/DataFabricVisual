import { SelectProps } from "~/components/extends/common/interfaces/props/Select.interface";

export interface RadioGroupProps extends SelectProps {
  checkedItem?: string | number;
}
