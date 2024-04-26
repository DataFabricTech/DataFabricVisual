<template>
  <combo-box
    :data="options"
    label-key="label"
    value-key="value"
    :selectedItem="selectedItem"
    :placeholder="placeholder"
    :disabled="disabled"
    :disabledList="disabledList"
    :isFirstSelectedEvent="isFirstCheckedEvent"
    :nodataMsg="nodataMsg"
    :noSearchMsg="noSearchMsg"
    @select="selectItem"
  >
  </combo-box>
</template>

<script setup lang="ts">
import ComboBox from "@/components/extends/combo-box/ComboBox.vue";

const isFirstCheckedEvent: boolean = true;

const placeholder = "입력해주세요";
const nodataMsg = "데이터가 존재하지 않습니다.";
const noSearchMsg = "검색한 데이터 결과가 존재하지 않습니다.";
const disabled = false;
const disabledList = ["option33"];
const options: { [key: string]: string | number }[] = [
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
  }
];

const selectedItem = "";
function selectItem(item: string | number) {
  console.log(item);
}
</script>

<style scoped></style>
