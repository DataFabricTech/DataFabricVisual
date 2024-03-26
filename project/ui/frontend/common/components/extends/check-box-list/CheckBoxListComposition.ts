import { CheckBoxListProps } from "@/components/extends/check-box-list/CheckBoxListProps";
import { ComputedRef } from "vue";
import { uuid } from "vue3-uuid";
import { CheckFunctionality } from "~/components/extends/common/interfaces/functions/Check.interface";
import { CheckEvents } from "~/components/extends/common/interfaces/events/Check.interface";

export interface CheckBoxListComposition extends CheckBoxListProps, CheckFunctionality, CheckEvents {
  checkboxList: ComputedRef<any>;
  changeList(option: any): void;
}

export function CheckBoxListComposition(
  props: CheckBoxListProps,
  onchange: (value: any) => void
): CheckBoxListComposition {
  const checkboxList: ComputedRef<any> = computed(() => {
    const labelKey: string = props.labelKey;
    const valueKey: string | number = props.valueKey;
    const checkedList: (string | number)[] | undefined = props.checkedList;
    const disabledList: (string | number)[] | undefined = props.disabledList;

    return props.data.map((value) => {
      return {
        id: value.id ?? uuid.v4(),
        label: value[labelKey],
        value: value[valueKey],
        checked: checkedList?.includes(value[valueKey]),
        disabled: disabledList?.includes(value[valueKey])
      };
    });
  });
  const onChange: (value: any) => void = (value) => {
    onchange(value);
  };
  const changeList: (option: any) => void = () => {
    const checkedValues = checkboxList.value
      .filter((item: { checked: any }) => item.checked)
      .map((item: { value: any }) => item.value);
    onChange(checkedValues);
  };

  if (props.isFirstSelectedEvent) {
    const selectedOptions = props.data.filter(
      (item) => props.checkedList?.includes(item[props.valueKey]) && !props.disabledList?.includes(item[props.valueKey])
    );
    if (selectedOptions.length > 0) {
      const selectedValues = selectedOptions.map((item) => item[props.valueKey]);
      onChange(selectedValues);
    } else {
      console.warn("선택된 값이 목록에 없거나 disabledList 목록에 선택한 값이 존재합니다.");
    }
  }

  const isDisabled: (value: string | number) => boolean = (value) => {
    // @ts-ignore
    return props.disabledList.includes(value);
  };

  return {
    ...props,
    checkboxList,
    changeList,
    onChange,
    isDisabled
  };
}
