<template>
  <div>
    <searchFilterGroup
      v-for="item in filterAddFilter"
      :key="item.idx"
      :item="item"
      :attributeDefsArr_s="props.attributeDefsArr_s"
      :filterAddFilter_s="props.filterAddFilter_s"
      :filterDataTypeVal_s="props.filterDataTypeVal_s"
      :filterCnt_s="props.filterCnt_s"
      :filterCondition_s="filterCondition"
      :filterFactor_s="filterFactor"
      @addGroup="addGroup"
      @deleteGroup="deleteGroup"
      @addFilter="addFilter"
      @deleteFilter="deleteFilter"
      :datetimeType_s="datetimeType"
      :startTimeOptions_s="startTimeOptions"
      :timePickerOptions_s="timePickerOptions"
      :startDate_s="startDate"
      :lang_s="lang"
    />
  </div>
</template>

<script setup lang="ts">
import searchFilterGroup from "./searchfilterGroup.vue";
import { ref, computed, ComputedRef } from "vue";

const emit = defineEmits(["deleteGroupInit"]);
const props = defineProps({
  filterAddFilter_s: Array,
  filterDataTypeSubObj_s: Object,
  filterDataTypeSubVal_s: String,
  attributeDefsArr_s: Array,
  filterDataTypeVal_s: String,
  filterCnt_s: Number,
  filterCondition_s: Object,
  filterFactor_s: Object,
});

// data
const filterAddFilter = ref<any>(props.filterAddFilter_s);
const attributeDefsArr: ComputedRef<any> = computed(() => props.attributeDefsArr_s);
const filterCnt = ref<number>(props.filterCnt_s);

const filterCondition = ref<any>(props.filterCondition_s);
const filterFactor = ref<any>(props.filterFactor_s);

const datetimeType = ref<string>("datetime");

interface TimeOptions {
  start: string;
  step: string;
  end: string;
  format: string;
  timePickerIncrement?: number;
}

const startTimeOptions = ref<TimeOptions>({
  start: "00:00",
  step: "00:30",
  end: "23:45",
  format: "hh:mm",
});
const timePickerOptions = ref<TimeOptions>({
  start: "00:00",
  step: "00:30",
  end: "23:30",
  format: "hh:mm",
  timePickerIncrement: 30,
});
const startDate = ref<number>(new Date().getTime());

interface Lang {
  yearFormat: string;
  days: Array<string>;
  months: Array<string>;
  pickers: Array<string>;
  placeholder: {
    date: string;
    dateRange: string;
  }
}

const lang = ref<Lang>({
  yearFormat: "YYYY년",
  days: ["일", "월", "화", "수", "목", "금", "토"],
  months: [
    "1월",
    "2월",
    "3월",
    "4월",
    "5월",
    "6월",
    "7월",
    "8월",
    "9월",
    "10월",
    "11월",
    "12월",
  ],
  pickers: ["next 7 days", "next 30 days", "previous 7 days", "previous 30 days"],
  placeholder: {
    date: "Select Date",
    dateRange: "Select Date Range",
  },
});

const getFilterGrpNum = (): number => {
  filterCnt.value = filterCnt.value + 1;
  return filterCnt.value;
};

const addGroup = (item: any): void => {
  let attributeValue_s: string|number = "";
  if (props.attributeDefsArr_s[0].typeName === "date") {
    attributeValue_s = startDate.value;
  }

  const groupMap = {
    idx: getFilterGrpNum(),
    condition: "AND",
    criterion: [
      {
        idx: getFilterGrpNum(),
        condition: "AND",
        attributeName: props.attributeDefsArr_s[0].name,
        operator: "=",
        typeName: props.attributeDefsArr_s[0].typeName,
        attributeValue: attributeValue_s,
      },
    ],
  };
  let index = 0;
  if (item === 0) {
    index = filterAddFilter.value.length;
    filterAddFilter.value.splice(index, 0, groupMap);
  } else {
    setAddFilter(filterAddFilter.value, item, groupMap);
  }
};

const setAddFilter = (data: any, subItem: any, groupMap: any): void => {
  if (Array.isArray(data)) {
    data.filter((item, idx) => {
      if (item.idx === subItem.idx) {
        item.criterion.splice(item.criterion.length, 0, groupMap);
      } else {
        if (item.criterion != null) {
          setAddFilter(item.criterion, subItem, groupMap);
        }
      }
    });
  }
};

const deleteGroup = (item: any): void => {
  // 그룹 필터 내용이 전혀 없는 경우
  if (item.idx == null) {
    filterAddFilter.value.splice(0);
    emit("deleteGroupInit");
  } else {
    deleteGroupItem(filterAddFilter.value, item);
  }
};

const deleteGroupItem = (data: any, subItem: any): void => {
  if (Array.isArray(data)) {
    data.filter((item, idx) => {
      if (item.idx === subItem.idx) {
        data.splice(idx, 1);
      } else {
        deleteGroupItem(item.criterion, subItem);
      }
    });
  }
};

const addFilter = (item: any): void => {
  let attributeValue_s: string | number = "";
  if (attributeDefsArr.value[0].typeName === "date") {
    attributeValue_s = startDate.value;
  }
  const filterMap = {
    idx: getFilterGrpNum(),
    condition: "AND",
    attributeName: attributeDefsArr.value[0].name,
    operator: "=",
    typeName: attributeDefsArr.value[0].typeName,
    attributeValue: attributeValue_s,
  };
  setSubAddFilter(filterAddFilter.value, item, filterMap);
};

const setSubAddFilter = (data: any, subItem: any, filterMap: any): void => {
  if (Array.isArray(data)) {
    data.filter((item, idx) => {
      if (item.idx === subItem.idx) {
        data.splice(idx + 1, 0, filterMap);
        return;
      } else {
        if (item.criterion != null) {
          setSubAddFilter(item.criterion, subItem, filterMap);
        }
      }
    });
  }
};

const deleteFilter = (item: any): void => {
  if (item.idx !== 0) {
    deleteFilterItem(filterAddFilter.value, item);
  } else {
  }
};

const deleteFilterItem = (data: any, subItem: any): void => {
  if (Array.isArray(data)) {
    data.filter((item, idx) => {
      if (item.idx === subItem.idx) {
        data.splice(idx, 1);
      } else {
        deleteFilterItem(item.criterion, subItem);
      }
    });
  }
};
</script>
