import { Ref, ref } from "vue";
import { TabProps } from "@/components/extends/tab/TabProps";
import { NavigationFunctionality } from "@/components/extends/common/interfaces/functions/Navigation.interface";
import { NavigationEvents } from "@/components/extends/common/interfaces/events/Navigation.interface";

import _ from "lodash";

const INDEX = "index";

interface TabComposition extends TabProps, NavigationFunctionality, NavigationEvents {
  currentIndex: Ref<number>;

  changeCurrentTabClass(index: number): boolean;
}

export function TabComposition(props: TabProps, onchange: (value: string | number) => void): TabComposition {
  const currentIndex: Ref<number> = ref<number>(0);

  // currentItem 변경을 감지해 값 변경 (부모 컴포넌트에서 선택 값을 초기화하는 경우 존재)
  watch(
    () => [props.currentItem],
    () => {
      setCurrentIndex();
    }
  );

  const setCurrentIndex: () => void = () => {
    if (props.currentItemType === INDEX) {
      if (typeof props.currentItem === "number") {
        currentIndex.value = props.currentItem;
      }
    } else {
      currentIndex.value = _.findIndex(props.data, ["value", props.currentItem]);
    }
  };

  setCurrentIndex();

  watch(
    () => props.currentItem,
    (newVal) => {
      if (props.currentItemType === INDEX) {
        currentIndex.value = typeof newVal === "number" ? newVal : 0;
      } else {
        currentIndex.value = _.findIndex(props.data, ["value", newVal]);
      }
    }
  );

  const move: (index: number) => void = (index) => {
    currentIndex.value = index;

    if (props.currentItemType === INDEX) {
      onChange(index);
    } else {
      const clickedValue: string | number = (props.data?.[index] as any)?.[props.valueKey];
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

  const toggleList: () => void = () => {};

  return { ...props, currentIndex, move, isDisabled, toggleList, changeCurrentTabClass, onChange };
}
