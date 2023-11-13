<template>
  <div class="list">
    <slot name="head">
      <div class="list-head">
        <div class="list-head-title">{{ title }}</div>
        <base-search-text
          v-if="props.useSearchFilter"
          v-model:text="data.textFilter"
          @reset="data.textFilter = ''"
        ></base-search-text>
      </div>
    </slot>
    <div class="list-body">
      <slot name="options" v-if="enableCheckAll">
        <div class="list-options">
          <baseCheckbox id="list-check-04" :checked="data.isAllChecked" @change="onChangeAll">전체</baseCheckbox>
          <div class="list-options-count">
            <strong class="list-options-count-selected">{{ checkedList.length }}</strong>
            <span>/</span>
            <span class="list-options-count-all"> {{ filteredList.length }} </span>
          </div>
        </div>
      </slot>
      <ul class="list-list">
        <li class="list-item" v-for="(item, key) in filteredList" :key="key">
          <baseCheckbox
            :id="'list-check' + item.value + '_' + key"
            :disabled="item.disable"
            :checked="item.checked"
            @change="checkItem(item, $event)"
          >
            {{ item.name }}
          </baseCheckbox>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { BaseCheckBoxListItem } from "~/components/base/checkbox-list";

const props = defineProps({
  title: {
    type: String,
    default: "Sample"
  },
  useSearchFilter: {
    type: Boolean,
    default: false
  },
  enableCheckAll: {
    type: Boolean,
    default: true
  },
  items: {
    type: Array<BaseCheckBoxListItem>,
    default: () => {
      return [];
    }
  },
  valueKey: {
    type: String,
    default: "value"
  }
});

const data: {
  listData: Array<any>;
  isAllChecked: boolean;
  textFilter: string;
} = reactive({
  listData: [],
  isAllChecked: false,
  textFilter: ""
});

watch(
  () => props.items,
  (nValue, oValue) => {
    if (_isEqual(nValue, oValue)) {
      return;
    }
    makeList();
  },
  { immediate: true, deep: true }
);
watch(
  () => data.textFilter,
  () => {
    emitChange();
  }
);

/**
 * 내부에서 사용되는 List 생성
 */
function makeList() {
  data.listData = _map(props.items, (item) => {
    return {
      name: item.name,
      value: item[props.valueKey],
      disable: item.disable,
      checked: item.checked
    };
  });
  data.isAllChecked = _every(data.listData, "checked");
}

/**
 * filter 된 리스트
 */
const filteredList = computed(() => {
  return _filter(data.listData, (item) => _includes(item.name.toLowerCase(), data.textFilter.toLowerCase()));
});

/**
 * check + filter 된 리스트
 */
const checkedList = computed(() => {
  return _chain(filteredList.value)
    .filter((item) => {
      return item.checked;
    })
    .map(props.valueKey)
    .value();
});

/**
 * 전체 선택/해제
 * @param checked
 */
function onChangeAll(checked: boolean) {
  _forEach(data.listData, (item) => {
    item.checked = checked;
  });
  data.isAllChecked = checked;
  emitChange();
}

/**
 * 단일 아이템 선택/해제
 * @param item
 * @param checked
 */
function checkItem(item: BaseCheckBoxListItem, checked: boolean) {
  _forEach(data.listData, (el) => {
    if (item.value === el.value) {
      el.checked = checked;
    }
  });
  data.isAllChecked = _every(filteredList, ["checked"]);
  emitChange();
}

const emit = defineEmits<(e: "change", list: Array<string>) => void>();
function emitChange() {
  emit("change", checkedList.value);
}
</script>
