import { SelectFunctionality } from "@/components/extends/common/interfaces/functions/Select.interface";
import { SelectEvents } from "~/components/extends/common/interfaces/events/Select.interface";
import { ref, Ref } from "vue";
import _ from "lodash";
import {MenuSearchPropsTemp} from "~/components/extends/menu-seach-temp/MenuSearchPropsTemp";

export interface MenuSearchItemImplTemp {
  [key: string]: string | number;
}

interface MenuSearchCompositionTemp extends MenuSearchPropsTemp, SelectFunctionality, SelectEvents {
  // Menu-search 컴포넌트에서만 사용하는 data, function
  listData: Ref<any[]>;
  selectedListData: Ref<any[]>;

  onApply(): void;
  onReset(): void;
  onCancel(): void;

  onCancelSelect(value: any, checked: boolean): void
  onSelectListData(value: any, checked: boolean): void
}

export function MenuSearchCompositionTemp(
    props: MenuSearchPropsTemp,
    applyData: (value: MenuSearchItemImplTemp | MenuSearchItemImplTemp[]) => void,
    cancelData: () => void
): MenuSearchCompositionTemp {
  const isCheckedData: (item: any) => boolean = (item) => {
    const itemId = item[props.valueKey];
    const originData = Array.isArray(props.selectedItems) ? props.selectedItems : [props.selectedItems];
    const findItem = _.find(originData as [], { [props.valueKey]: itemId });
    return !!findItem;
  };

  const originListData: Ref<any> = ref([]);
  const listData: Ref<any[]> = ref([]);

  const setListData: () => void = () => {
    console.log("setListData");
    const result = props.data.map(item => {
      const isChecked = isCheckedData(item);
      return {
        label: item[props.labelKey],
        value: item[props.valueKey],
        isChecked: isChecked, // 체크 여부
        isShow: true // 검색 처리
      };
    });
    originListData.value = _.cloneDeep(result);
    listData.value = result;
  };

  const originSelectedListData: Ref<any> = ref([]);
  const selectedListData: Ref<any[]> = ref([]);

  const setSelectedListData: () => void = () => {
    console.log("setSelectedListData");
    const originData = Array.isArray(props.selectedItems) ? props.selectedItems : [props.selectedItems];
    const result = originData.map(item => {
      const isChecked = isCheckedData(item);
      return {
        label: item[props.labelKey],
        value: item[props.valueKey],
        isChecked: isChecked, // 체크 여부
        isShow: true // 검색 처리
      };
    });
    originSelectedListData.value = _.cloneDeep(result);
    selectedListData.value = result;
    console.log("Initial selectedListData:", selectedListData.value);
  };

  setListData();
  setSelectedListData();


  const onApply: () => void = () => {};

  const onReset: () => void = () => {};

  const onCancel: () => void = () => {};

  const onCancelSelect: (value: any, checked: boolean) => void = (value, checked) => {
    console.log("onCancelSelect", checked);
    console.log("Before removal, selectedListData:", selectedListData.value);

    _.remove(selectedListData.value, (item) => item.value === value.value);

    const listItem = listData.value.find(item => item.value === value.value);
    if (listItem) {
      listItem.isChecked = false;
    }

    selectedListData.value = selectedListData.value.map((item) => {
      return {
        ...item,
        isChecked: true
      };
    });

    console.log("After removal, selectedListData:", selectedListData.value);
    console.log("Updated listData:", listData.value);
  };

  const onSelectListData: (value: any, checked: boolean) => void = (value, checked) => {};

  const toggleList: () => void = () => {};

  const isDisabled: (value: string | number) => boolean = (value) => {
    console.log(value);
    return false;
  };

  const selectItem: (option: { [key: string]: string | number }) => void = (option) => {
    return option;
  };

  const onSelect: (value: string | number) => void = (value) => {
    return value;
  };

  return {
    ...props,
    selectedListData,
    listData,
    toggleList,
    isDisabled,
    onSelect,
    selectItem,
    onCancelSelect,
    onSelectListData,
    onApply,
    onReset,
    onCancel,
  };
}