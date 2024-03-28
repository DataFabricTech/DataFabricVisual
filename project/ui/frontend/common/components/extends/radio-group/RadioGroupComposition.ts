import { CheckFunctionality } from "~/components/extends/common/interfaces/functions/Check.interface";
import { RadioGroupProps } from "~/components/extends/radio-group/RadioGroupProps";
import { CheckEvents } from "~/components/extends/common/interfaces/events/Check.interface";
import { ComputedRef } from "vue";
import { uuid } from "vue3-uuid";
export interface RadioGroupComposition extends RadioGroupProps, CheckFunctionality, CheckEvents {
  radioGroup: ComputedRef<any>;
  changeGroup(option: any): void;
}

export function RadioGroupComposition(
  props: RadioGroupProps,
  onchange: (option: string | number) => void
): RadioGroupComposition {
  const radioGroup: ComputedRef<any> = computed(() => {
    const labelKey: string = props.labelKey;
    const valueKey: string | number = props.valueKey;
    const disabledList: (string | number)[] | undefined = props.disabledList;
    const checkedItem: string | number | undefined = props.checkedItem;

    return props.data.map((value) => {
      const isChecked = checkedItem === value[valueKey];
      return {
        id: value.id ?? uuid.v4(),
        label: value[labelKey],
        value: value[valueKey],
        disabled: disabledList?.includes(value[valueKey]),
        checked: isChecked
      };
    });
  });
  const onChange: (value: any) => void = (value) => {
    onchange(value);
  };

  if (props.isFirstSelectedEvent) {
    const selectedOption =
      props.checkedItem && props.data.find((option) => option[props.valueKey] === props.checkedItem);

    if (selectedOption) {
      onChange(selectedOption.value);
    } else {
      console.warn("선택된 값이 목록에 없거나 disabledList 목록에 선택한 값이 존재합니다.");
    }
  }

  // 사용자가 라디오 버튼 중 하나를 선택했을 때 호출되는 함수
  const changeGroup: (item: any) => void = (item) => {
    onChange(item.value);
  };
  const isDisabled: (value: string | number) => boolean = (value) => {
    // @ts-ignore
    return props.disabledList.includes(value);
  };

  return {
    ...props,
    radioGroup,
    onChange,
    isDisabled,
    changeGroup
  };
}
