import { SelectProps } from "@/components/extends/common/interfaces/props/Select.interface";

export interface SelectBoxProps extends SelectProps {
  disabledAll?: boolean;
  useDelete?: boolean;
  nodataMsg?: string;
}
