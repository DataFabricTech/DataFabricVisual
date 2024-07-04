import { SelectFunctionality } from "@/components/extends/common/interfaces/functions/Select.interface";
import { SelectEvents } from "~/components/extends/common/interfaces/events/Select.interface";
import { MenuSearchProps } from "~/components/extends/menu-seach/MenuSearchProps";
import { ref, Ref } from "vue";
import _ from "lodash";

export interface MenuSearchItemImpl {
  [key: string]: string | number;
}

interface MenuSearchComposition extends MenuSearchProps, SelectFunctionality, SelectEvents {
  // Menu-search 컴포넌트에서만 사용하는 data, function
  listData: Ref<any[]>;
  selectedListData: Ref<any[]>;
  searchLabel: Ref<string>;

  onSearchText(value: string): void;
  onResetSearchText(): void;
  onCancelSelect(value: any, checked: boolean): void;
  onSelectListData(value: any, checked: boolean): void;
  checkSearchResult(): void;
  onApply(): void;
  onReset(): void;
  onCancel(): void;
}

export function MenuSearchComposition(
  props: MenuSearchProps,
  applyData: (value: MenuSearchItemImpl | MenuSearchItemImpl[]) => void,
  cancelData: () => void
): MenuSearchComposition {
  const isCheckedData: (item: any) => boolean = (item) => {
    const itemId = item[props.valueKey];
    const originData = Array.isArray(props.selectedItems) ? props.selectedItems : [props.selectedItems];
    const findItem = _.find(originData as [], { [props.valueKey]: itemId });
    return !!findItem;
  };

  const originListData: Ref<any> = ref([]);
  const listData: Ref<any[]> = ref([]);

  const setListData: () => void = () => {
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
  };

  /**
  /**
   * 선택 리스트 값이 변경되면 일반 리스트의 속성값도 변경되야하므로 다중 watch
   */
  setListData();
  setSelectedListData();

  /**
   * 선택 리스트 삭제
   * @param value
   * @param checked
   */
  const onCancelSelectData: (value: any, checked: boolean) => void = (value, checked) => {
    // 선택 항목 삭제
    _.remove(selectedListData.value, (item) => item.value === value.value);

    // 원본 리스트 checked 추가
    listData.value = listData.value.map(item => {
      if (item.value === value.value) {
        return {
          ...item,
          isChecked: checked,
        };
      }
      return item;
    });
  };

  /**
   * (이벤트) 선택 리스트 삭제
   * @param value
   * @param checked
   */
  const onCancelSelect: (value: any, checked: boolean) => void = (value, checked) => {
    const checkedData = props.isMulti ? checked : false;
    onCancelSelectData(value, checkedData);
  };
  /**
   * 아이템 선택
   * @param value
   * @param checked
   */
  const onSelectData : (value: any, checked: boolean) => void = (value, checked) => {
    value.isChecked = checked;

    selectedListData.value.push({
      ...value,
      isChecked: true
    });
  };

  /**
   * (이벤트) 아이템 선택
   * @param value
   * @param checked
   */
  const onSelectListData : (value: any, checked: boolean) => void = (value, checked) => {
    // 다중 처리 이벤트
    if (props.isMulti) {
      onSelectData(value, checked);
      return;
    }

    // 단일 처리 이벤트
    const checkSelectedList = selectedListData.value.find(item => item.isChecked);
    if (checkSelectedList) {
      listData.value = listData.value.map((item) => {
        if (item.value === checkSelectedList.value) {
          return {
            ...item,
            isChecked: false,
          };
        }
        return item;
      });
      // 선택된 값이 있다면 해당 값 삭제 후 현재 값 checked
      selectedListData.value = [];
    }
    onSelectData(value, true);
  };

  const searchLabel: Ref<string> = ref<string>("");
  /**
   * (이벤트) 리스트 검색
   * @param value
   */
  const onSearchText: (value: string) => void = (value) => {
    searchLabel.value = value;
    searchList(searchLabel.value);
  };

  /**
   * (이벤트) 리스트 검색 초기화
   */
  const onResetSearchText: () => void = () => {
    searchLabel.value = "";
    searchList(searchLabel.value);
  };

  /**
   * 리스트 내 검색
   * @param value
   */
  const searchList : (value: string) => void = (value) => {
    // 검색 로직. -> 현재 데이터 목록을 변경하지 않고 검색 결과를 출력해야함.
    selectedListData.value = selectedListData.value.map(item => {
      item.isShow = ((item.label + "").toLowerCase()).includes(value.toLowerCase());
      return item;
    });

    listData.value = listData.value.map(item => {
      item.isShow = ((item.label + "").toLowerCase()).includes(value.toLowerCase());
      return item;
    });
  };

  const checkSearchResult : () => void = () => {
    const findSelectedItem = _.every(selectedListData.value, {isShow: false});
    const findListItem = _.every(listData.value, {isShow: false});

    return findSelectedItem && findListItem && searchLabel;
  };

  const onApply: () => void = () => {
    // 원본 데이터로 변경
    const mapData = _.cloneDeep(selectedListData).value
      .map(item => {
        return {
          [props.labelKey]: item.label,
          [props.valueKey]: item.value,
        };
      });

    // 적용된 값을 origin에 저장
    originSelectedListData.value = _.cloneDeep(selectedListData.value);
    originListData.value =  _.cloneDeep(listData.value);

    const result = props.isMulti ? mapData : mapData[0];
    applyData(result);
  };
  const onReset: () => void = () => {
    selectedListData.value = _.cloneDeep(originSelectedListData.value);
    listData.value =  _.cloneDeep(originListData.value);
  };
  const onCancel: () => void = () => {
    onReset();
    cancelData();
  };

  // 기본 Select 기능 재정의
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
    searchLabel,

    toggleList,
    isDisabled,
    onSelect,
    selectItem,

    onApply,
    onReset,
    onCancel,

    onSelectListData,
    onCancelSelect,
    onSearchText,
    onResetSearchText,
    checkSearchResult
  };
}
