<template>
  <RadioGroup
    :data="options"
    label-key="label"
    value-key="value"
    name="radioGroup"
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

const disabledList: (string | number)[] = ["option44"];
// 기본 선택한 값을 전달하는 이벤트 사용 여부
const isFirstSelectedEvent: boolean = true;

let checkedItem: string | number = "option4";

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
