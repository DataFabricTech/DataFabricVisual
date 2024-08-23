import { Ref, ref } from "vue";
import { StepProps } from "@/components/extends/step/StepProps";
import { NavigationFunctionality } from "@/components/extends/common/interfaces/functions/Navigation.interface";
import { NavigationEvents } from "@/components/extends/common/interfaces/events/Navigation.interface";
import { ComparisonOperator } from "./ComparisonOperator";
const INDEX = "index";
interface StepComposition extends StepProps, NavigationFunctionality, NavigationEvents {
  currentIndex: Ref<number>;
  clickStep(index: number): void;
  changeCurrentStepClass(index: number): boolean;
}

export function StepComposition(props: StepProps, onchange: (value: string | number) => void): StepComposition {
  const currentIndex: Ref<number> = ref<number>(0);

  const clickStep: (index: number) => void = (index) => {
    if (currentIndex.value < index) {
      return;
    }
    return move(index);
  };
  const move: (index: number) => void = (index) => {
    currentIndex.value = index;

    if (props.currentItemType === INDEX) {
      onChange(index);
    } else {
      const clickedValue: string | number = (props.data?.[index] as any)?.[props.valueKey];
      onChange(clickedValue);
    }
  };
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
  };
  const onChange: (value: string | number) => void = (value) => {
    onchange(value);
  };

  const isDisabled: (value: string | number) => boolean = (value) => {
    console.log("사용하지 않은 함수이지만 오버라이딩을 해야 함", value);
    return false;
  };

  const toggleList: () => void = () => {};

  // 버튼을 사용하지 않는 경우, parent 에서 보내주는 index 값으로 currentIndex 값을 제어한다.
  if (!props.showBtn) {
    watch(
      () => props.parentIndex,
      (newValue) => {
        currentIndex.value = (newValue || 0) - 1;
      }
    );
  }

  return { ...props, currentIndex, clickStep, isDisabled, move, toggleList, changeCurrentStepClass, onChange };
}
