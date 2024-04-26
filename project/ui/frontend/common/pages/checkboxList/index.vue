<template>
  <div>
    <CheckBoxList
      :data="options"
      label-key="label"
      value-key="value"
      :checkedList="checkedList"
      :disabledList="disabledList"
      :isFirstSelectedEvent="isFirstSelectedEvent"
      @change="checkItem"
    ></CheckBoxList>
  </div>
</template>

<script setup lang="ts">
import CheckBoxList from "@/components/extends/check-box-list/CheckBoxList.vue";
const isFirstSelectedEvent: Boolean = true;

let checkedList = ["option22", "option44"];

const disabledList = ["option44"];

const options = [
  {
    label: "option1",
    value: "option11"
  },
  {
    label: "option2",
    value: "option22"
  },
  {
    label: "option3",
    value: "option33"
  },
  {
    label: "option4",
    value: "option44"
  }
];
const checkItem = (val: (string | number)[]) => {
  console.log(val);
};

onBeforeMount(() => {
  checkedList = checkedList?.filter((item) => !disabledList.includes(item));
});
</script>

<style scoped></style>
