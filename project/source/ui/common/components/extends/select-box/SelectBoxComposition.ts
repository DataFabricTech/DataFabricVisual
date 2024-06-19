import { Ref, ref } from "vue";
import { SelectBoxProps } from "@/components/extends/select-box/SelectBoxProps";
import { SelectFunctionality } from "~/components/extends/common/interfaces/functions/Select.interface";
import { SelectEvents } from "~/components/extends/common/interfaces/events/Select.interface";

interface SelectBoxComposition extends SelectBoxProps, SelectFunctionality, SelectEvents {
  isShowBox: Ref<boolean>;
  selectedLabel: Ref<string>;
  isActive(value: string | number): boolean;
  closeDropdown(): void;
}

export function SelectBoxComposition(
  props: SelectBoxProps,
  onselect: (value: string | number) => void
): SelectBoxComposition {
  const isShowBox: Ref<boolean> = ref<boolean>(false);
  const selectedLabel: Ref<string> = ref<string>("선택하세요");
  let selectedValue: string | number | undefined = props.selectedItem;

  if (!props.data.length) {
    // @ts-ignore
    props.data.push({ [props.labelKey]: props.nodataMsg, [props.valueKey]: null });
  }

  const onSelect: (value: string | number) => void = (value) => {
    onselect(value);
  };

  const toggleList: () => void = () => {
    isShowBox.value = !isShowBox.value;
  };

  const isDisabled: (value: string | number) => boolean = (value) => {
    // @ts-ignore
    return props.disabledList.includes(value);
  };

  const closeDropdown: () => void = () => {
    isShowBox.value = false;
  };

  const selectItem: (option: { [key: string]: string | number }) => void = (option) => {
    const value = option[props.valueKey];
    selectedLabel.value = option[props.labelKey].toString();
    selectedValue = value; // 선택된 값을 selectedValue로 저장

    onSelect(value);

    closeDropdown();
  };

  const isActive: (value: string | number) => boolean = (value) => {
    return value === selectedValue;
  };

  if (props.selectedItem !== undefined) {
    // props.selectedItem이 undefined 아닐 때, 해당 값이 props.data 배열에 포함되어 있는지 확인
    const foundItem = props.data?.find((option) =>
        option[props.valueKey] === props.selectedItem);
    if (foundItem) {
      // foundItem의 값이 존재할 경우
      const value = foundItem[props.valueKey];
      selectedLabel.value = foundItem[props.labelKey].toString();
      selectedValue = value; // 선택된 값을 selectedValue로 저장
    }
  }

  if(props.isFirstSelectedEvent) {
      const selectedOption = props.selectedItem && props.data?.find((option) =>
          option[props.valueKey] === props.selectedItem);
      if (selectedOption) {
        // foundItem의 값이 존재할 경우
        selectItem(selectedOption);
      }
  }

  return { ...props, isShowBox, selectedLabel, selectItem, toggleList, isDisabled, isActive, closeDropdown, onSelect };
}
