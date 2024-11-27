<template>
  <searchFilterGroup
    v-for="item in props.entityFiltersList"
    :key="item.idx"
    :item="item"
    :entityFilterCondition="props.entityFilterCondition"
    :entityFilterCriterion="props.entityFilterCriterion"
    :datetimeType="datetimeType"
    :timePickerOptions="timePickerOptions"
    :lang="lang"
    :startDate="startDate"
    :filterCnt="filterCnt"
    @getFilterGrpNum="getFilterGrpNum"
    @addGroup="addGroup"
    @deleteGroup="deleteGroup"
    @addFilter="addFilter"
    @deleteFilter="deleteFilter"
  />
</template>

<script setup lang="ts">
import { onMounted } from "vue";
import searchFilterGroup from "./searchfilterGroup.vue";

const emit = defineEmits(["deleteGroupNothing"]);
const props = defineProps(["entityFilterCondition", "entityFilterCriterion", "entityFiltersList", "filterCnt"]);

const datetimeType = ref("datetime")
const timePickerOptions = ref({
  start: "00:00",
  step: "00:30",
  end: "23:30",
  format: "hh:mm",
  timePickerIncrement: 30,
})
const startDate = ref(new Date().getTime())
const lang = ref({
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
})

onMounted(() => {
})

const filterCnt = ref(props.filterCnt)

const getFilterGrpNum = () => {
  filterCnt.value = filterCnt.value + 1
  return filterCnt.value
}

const addGroup = (item) => {
  let attributeValue_s = "";
  if (props.entityFiltersList[0].typeName === "date") {
    attributeValue_s = startDate.value;
  }

  const groupMap = {
    idx: getFilterGrpNum(),
    condition: "AND",
    criterion: [
      {
        idx: getFilterGrpNum(),
        condition: "AND",
        attributeName: props.entityFilterCriterion[0].name,
        operator: "=",
        typeName: props.entityFilterCriterion[0].typeName,
        attributeValue: attributeValue_s,
      },
    ],
  };
  let index = 0;
  if (item === 0) {
    index = props.entityFiltersList.length;
    props.entityFiltersList.splice(index, 0, groupMap);
  } else {
    setAddFilter(props.entityFiltersList, item, groupMap);
  }
}

const setAddFilter = (data, subItem, groupMap) => {
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
}

const deleteGroup = (item) => {
  // 그룹 필터 내용이 전혀 없는 경우
  if (item.idx == null) {
    emit("deleteGroupNothing")
  } else {
    deleteGroupItem(props.entityFiltersList, item);
  }
}

const deleteGroupItem = (data, subItem) => {
  if (Array.isArray(data)) {
    data.filter((item, idx) => {
      if (item.idx === subItem.idx) {
        data.splice(idx, 1);
      } else {
        deleteGroupItem(item.criterion, subItem);
      }
    });
  }
}

const addFilter = (item) => {
  let attributeValue_s = "";
  if (props.entityFilterCriterion[0].typeName === "date") {
    attributeValue_s = this.startDate;
  }
  const filterMap = {
    idx: getFilterGrpNum(),
    condition: "AND",
    attributeName: props.entityFilterCriterion[0].name,
    operator: "=",
    typeName: props.entityFilterCriterion[0].typeName,
    attributeValue: attributeValue_s,
  };
  setSubAddFilter(props.entityFiltersList, item, filterMap);
}

const setSubAddFilter = (data, subItem, filterMap) => {
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
}

const deleteFilter = (item) => {
  if (item.idx !== 0) {
    deleteFilterItem(props.entityFiltersList, item);
  } else {
  }
}

const deleteFilterItem = (data, subItem) => {
  if (Array.isArray(data)) {
    data.filter((item, idx) => {
      if (item.idx === subItem.idx) {
        data.splice(idx, 1);
      } else {
        deleteFilterItem(item.criterion, subItem);
      }
    });
  }
}


</script>
