<template>
  <RadioGroup
    :data="options"
    label-key="label"
    value-key="value"
    :name="nameValue"
    @change="checkItem"
    :disabledList="disabledList"
    :checkedItem="checkedItem"
    :isFirstSelectedEvent="isFirstSelectedEvent"
  ></RadioGroup>
</template>

<script setup lang="ts">
// import { useI18n } from "vue-i18n";
// import messages from "./index.json";
//
// const { t } = useI18n({
//   messages
// });
import RadioGroup from "~/components/extends/radio-group/RadioGroup.vue";
import { uuid } from "vue3-uuid";

const disabledList: (string | number)[] = ["option44"];

const isFirstSelectedEvent: boolean = true;

const nameValue: string = "name";

let checkedItem: string | number = "option22";

const options: object[] = [
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
  },
  {
    label: "option5",
    value: "option55"
  }
];

const checkItem = (val: string | number) => {
  console.log(val);
};

onBeforeMount(() => {
  if (disabledList.includes(checkedItem)) {
    checkedItem = "";
  }
});
</script>

<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
