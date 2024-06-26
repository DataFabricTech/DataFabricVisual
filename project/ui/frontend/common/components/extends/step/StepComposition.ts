import { Ref, ref } from "vue";
import { StepProps } from "@/components/extends/step/StepProps";
import { NavigationFunctionality } from "@/components/extends/common/interfaces/functions/Navigation.interface";
import { NavigationEvents } from "@/components/extends/common/interfaces/events/Navigation.interface";
import { ComparisonOperator } from "@/components/extends/step/ComparisonOperator";
const INDEX = "index";
interface StepComposition extends StepProps, NavigationFunctionality, NavigationEvents {
  currentIndex: Ref<number>;
  clickStep(index: number): void;
  changeCurrentStepClass(index: number): boolean;
}

export function StepComposition(props: StepProps, onchange: (value: string | number) => void): StepComposition {
  const currentIndex: Ref<number> = ref<number>(0);

  const clickStep: (index: number) => void = (index) => {
    if(currentIndex.value < index) {
      return;
    }
    return move(index);
  }
  const move: (index: number) => void = (index) => {
    currentIndex.value = index;

    if(props.currentItemType === INDEX) {
      onChange(index);
    } else {
      let clickedValue: string | number = (props.data?.[index] as any)?.[props.valueKey];
      onChange(clickedValue);
    }
  }
  const changeCurrentStepClass: (clickedIdx: number) => boolean = (clickedIdx) => {
    switch (props.comparison) {
      case ComparisonOperator.GREATER_THAN_OR_EQUAL:
        return currentIndex.value >= clickedIdx;
      case ComparisonOperator.EQUAL:
        return currentIndex.value === clickedIdx;
      default:
        console.error(`Unsupported comparison operator: ${props.comparison}`);
        return false;
    }
  }
  const onChange: (value: string | number) =>  void = (value) => {
    onchange(value)
  }

  return { ...props, currentIndex, clickStep, move, changeCurrentStepClass, onChange}
}
