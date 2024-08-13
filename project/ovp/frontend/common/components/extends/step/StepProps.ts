import { NavigationProps } from "@/components/extends/common/interfaces/props/Navigation.interface";

export interface StepProps extends NavigationProps {
  showBtn?: boolean;
  comparison?: "equal" | "greater";
  startPoint?: number;
  parentIndex?: number;
}
