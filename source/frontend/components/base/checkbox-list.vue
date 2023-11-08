<template>
  <div class="list">
    <slot name="head"></slot>
    <div class="list-body">
      <slot name="options" v-if="enableCheckAll">
        <div class="list-options">
          <baseCheckbox id="list-check-04" :checked="data.isAllChecked" @change="checkAllItem">전체</baseCheckbox>
          <div class="list-options-count">
            <strong class="list-options-count-selected">{{ checkedList.length }}</strong>
            <span>/</span>
            <span class="list-options-count-all"> {{ data.listData.length }} </span>
          </div>
        </div>
      </slot>
      <ul class="list-list">
        <li class="list-item" v-for="(item, key) in data.listData" :key="key">
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
  checkedItems: {
    type: Array<string>,
    default: () => {
      return [];
    }
  },
  disabledItems: {
    type: Array<string>,
    default: () => {
      return [];
    }
  },
  valueKey : {
    type: String,
    default: "value"
  }
});

const data: {
  listData: Array<BaseCheckBoxListItem>;
  isAllChecked: boolean;
} = reactive({
  listData: [],
  isAllChecked: false
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
  () => props.checkedItems,
  (nValue, oValue) => {
    if (_isEqual(nValue, oValue)) {
      return;
    }
    applyChecked(nValue);
  },
  { immediate: true, deep: true }
);
watch(
  () => props.disabledItems,
  (nValue, oValue) => {
    if (_isEqual(nValue, oValue)) {
      return;
    }
    applyDisabled(nValue);
  },
  { immediate: true, deep: true }
);

function makeList() {
  data.listData = _map(props.items, (item) => {
    return {
      name: item.name,
      value: item[props.valueKey],
      disable: false,
      checked: false
    };
  });
  applyChecked(null);
  applyDisabled(null);
}

function applyChecked(checkedList: Array<any>) {
  checkedList = checkedList || props.checkedItems;

  _forEach(data.listData, (item) => {
    item.checked = _includes(checkedList, item.value);
  });
  data.isAllChecked = _every(data.listData, "checked");
}

function applyDisabled(disabledList: Array<any>) {
  disabledList = disabledList || props.disabledItems;

  _forEach(data.listData, (item) => {
    item.disable = _includes(disabledList, item.value);
  });
}

const emit = defineEmits<{
  (e: "change", list: Array<string>): void;
}>();
function checkAllItem(checked: boolean) {
  data.isAllChecked = checked;
  _forEach(data.listData, (item) => {
    item.checked = checked;
  });
  emit("change", checkedList.value);
}
function checkItem(item: BaseCheckBoxListItem, checked: boolean) {
  _forEach(data.listData, (el) => {
    if (item.value === el.value) {
      el.checked = checked;
    }
  });
  data.isAllChecked = _every(data.listData, "checked");
  emit("change", checkedList.value);
}

const checkedList = computed(() => {
  // NOTE: id 값 출력
  return _chain(data.listData).filter({ checked: true }).map(props.valueKey).value();
});
</script>
