import { Ref, ref } from "vue";
import { TabProps } from "@/components/extends/tab/TabProps";
import { NavigationFunctionality } from "@/components/extends/common/interfaces/functions/Navigation.interface";
import { NavigateionEvents } from "@/components/extends/common/interfaces/events/Navigation.interface";
const INDEX = "index";

interface TabComposition extends TabProps, NavigationFunctionality, NavigateionEvents {
  currentIndex: Ref<number>;
  changeCurrentTabClass(index: number): boolean;
}

export function TabComposition(props: TabProps, onchange: (value: string | number) => void): TabComposition {
  const currentIndex: Ref<number> = ref<number>(0);
  const move: (index: number) => void = (index) => {
    currentIndex.value = index;

    if (props.currentItemType === INDEX) {
      onChange(index);
    } else {
      let clickedValue: string | number = (props.data?.[index] as any)?.[props.valueKey];
      onChange(clickedValue);
    }
  };

  const isDisabled: (value: string | number) => boolean = (value) => {
    const disabledList = props.disabledList ?? [];
    return disabledList.includes(value);
  };

  const changeCurrentTabClass: (index: number) => boolean = (index) => {
    return currentIndex.value === index;
  };

  const onChange: (value: string | number) => void = (value) => {
    onchange(value);
  };

  return { ...props, currentIndex, move, isDisabled, changeCurrentTabClass, onChange };
}
