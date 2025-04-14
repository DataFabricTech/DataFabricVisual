import { Ref, ref } from "vue";
import { TabProps } from "@/components/extends/tab/TabProps";
import { NavigationFunctionality } from "@/components/extends/common/interfaces/functions/Navigation.interface";
import { NavigationEvents } from "@/components/extends/common/interfaces/events/Navigation.interface";

import _ from "lodash";

const INDEX = "index";

interface TabComposition extends TabProps, NavigationFunctionality, NavigationEvents {
  currentIndex: Ref<number>;

  isHided(value: string | number): boolean;

  changeCurrentTabClass(index: number): boolean;
}

export function TabComposition(props: TabProps, onchange: (value: string | number) => void): TabComposition {
  const currentIndex: Ref<number> = ref<number>(0);

  // currentItem 변경을 감지해 값 변경 (부모 컴포넌트에서 선택 값을 초기화하는 경우 존재)
  watch(
    () => props.currentItem,
    (newVal) => {
      if ((props.currentItemType === INDEX && newVal < 0) || newVal === "" || newVal === null) {
        return;
      }

      const disabledList = props.disabledList ?? [];
      const hidedList = props.hidedList ?? [];
      if (_.includes(disabledList, newVal) || _.includes(hidedList, newVal)) {
        return;
      }

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

  const move: (index: number) => void = (index) => {
    if (props.currentItemType === INDEX && !isDisabled(index) && !isHided(index)) {
      onChange(index);
      currentIndex.value = index;
    } else {
      const clickedValue: string | number = (props.data?.[index] as any)?.[props.valueKey];

      if (!clickedValue || isDisabled(clickedValue) || isHided(clickedValue)) {
        return;
      }

      onChange(clickedValue);
      currentIndex.value = index;
    }
  };

  const isDisabled: (value: string | number) => boolean = (value) => {
    const disabledList = props.disabledList ?? [];
    return disabledList.includes(value);
  };

  const isHided: (value: string | number) => boolean = (value) => {
    const hidedList = props.hidedList ?? [];
    return hidedList.includes(value);
  };

  const changeCurrentTabClass: (index: number) => boolean = (index) => {
    return currentIndex.value === index;
  };

  const onChange: (value: string | number) => void = (value) => {
    onchange(value);
  };

  const toggleList: () => void = () => {};

  return { ...props, currentIndex, move, isDisabled, isHided, toggleList, changeCurrentTabClass, onChange };
}
