import { ComboBoxProps } from "@/components/extends/combo-box/ComboBoxProps";
import { SelectFunctionality } from "@/components/extends/common/interfaces/functions/Select.interface";
import { SelectEvents } from "~/components/extends/common/interfaces/events/Select.interface";
import { Ref, ref, computed, ComputedRef } from "vue";

interface ComboBoxComposition extends ComboBoxProps, SelectFunctionality, SelectEvents {
  isShowBox: Ref<boolean>;
  selectedLabel: Ref<string>;
  filteredOptions: ComputedRef<any>;
  isActive(value: string | number): boolean;
  closeDropdown(): void;
}

export function ComboboxComposition(
  props: ComboBoxProps,
  onselect: (value: string | number) => void
): ComboBoxComposition {
  const isShowBox: Ref<boolean> = ref<boolean>(false);
  const selectedLabel: Ref<string> = ref<string>("");
  let selectedValue: string | number | undefined = props.selectedItem;
  const filteredOptions: ComputedRef<any> = computed(() => {
    if (!selectedLabel.value) {
      // 검색어가 없는 경우, 모든 데이터를 반환
      return props.data;
    } else {
      const filterResult = props.data.filter((option) => option[props.labelKey].includes(selectedLabel.value));
      if (filterResult.length === 0) {
        // 검색 결과가 없는 경우, noSearchMsg를 포함하는 객체를 배열로 반환
        return [
          {
            [props.labelKey]: props.noSearchMsg,
            [props.valueKey]: null
          }
        ];
      } else {
        // 검색 결과가 있는 경우, 해당 결과를 반환
        return filterResult;
      }
    }
  });

  if (!props.data.length) {
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
    const foundItem = props.data?.find((option) => option[props.valueKey] === props.selectedItem);
    if (foundItem) {
      // foundItem의 값이 존재할 경우
      const value = foundItem[props.valueKey];
      selectedLabel.value = foundItem[props.labelKey].toString();
      selectedValue = value; // 선택된 값을 selectedValue로 저장
    }
  }

  if (props.isFirstSelectedEvent) {
    const selectedOption =
      props.selectedItem && props.data?.find((option) => option[props.valueKey] === props.selectedItem);
    if (selectedOption) {
      // foundItem의 값이 존재할 경우
      selectItem(selectedOption);
    }
  }

  return {
    ...props,
    isShowBox,
    selectedLabel,
    filteredOptions,
    selectItem,
    toggleList,
    isDisabled,
    isActive,
    closeDropdown,
    onSelect
  };
}
