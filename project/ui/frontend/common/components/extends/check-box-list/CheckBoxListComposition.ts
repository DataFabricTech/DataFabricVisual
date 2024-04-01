import { CheckBoxListProps } from "@/components/extends/check-box-list/CheckBoxListProps";
import { ComputedRef } from "vue";
import { uuid } from "vue3-uuid";
import { CheckFunctionality } from "~/components/extends/common/interfaces/functions/Check.interface";
import { CheckEvents } from "~/components/extends/common/interfaces/events/Check.interface";

export interface CheckBoxListComposition extends CheckBoxListProps, CheckFunctionality, CheckEvents {
  checkboxList: ComputedRef<any>;
  changeList(option: string | number): void;
}

export function CheckBoxListComposition(
  props: CheckBoxListProps,
  onchangeList: (option: (string | number)[]) => void
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
  // @ts-ignore
  // NOTE: onChange 이벤트는 지금은 사용 안함.
  const onChange: (value: string | number) => void = (value: string | number) => {};
  const onChangeList: (value: (string | number)[]) => void = (value) => {
    onchangeList(value);
  };
  const changeList: () => void = () => {
    const checkedValues = checkboxList.value
      .filter((item: { checked: boolean }) => item.checked)
      .map((item: { value: string | number }) => item.value);
    onChangeList(checkedValues);
  };

  if (props.isFirstSelectedEvent) {
    const selectedOptions = props.data.filter(
      (item) => props.checkedList?.includes(item[props.valueKey]) && !props.disabledList?.includes(item[props.valueKey])
    );
    if (selectedOptions.length > 0) {
      const selectedValues = selectedOptions.map((item) => item[props.valueKey]);
      onChangeList(selectedValues);
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
    onChangeList,
    isDisabled
  };
}
