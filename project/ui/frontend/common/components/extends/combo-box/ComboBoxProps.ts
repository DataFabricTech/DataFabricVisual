import { SelectProps } from "@/components/extends/common/interfaces/props/Select.interface";

export interface ComboBoxProps extends SelectProps {
  placeholder?: string;
  // disabled?: boolean;
  nodataMsg?: string;
  // noSearchMsg?: string;
}
