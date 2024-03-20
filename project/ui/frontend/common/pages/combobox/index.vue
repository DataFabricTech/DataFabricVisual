<template>
  <combo-box
    :data="options"
    label-key="label"
    value-key="value"
    :selectedItem="selectedItem"
    :placeholder="placeholder"
    :isFirstSelectedEvent="isFirstCheckedEvent"
    @select="selectItem"
  >
  </combo-box>
</template>

<script setup lang="ts">
import ComboBox from "@/components/extends/combo-box/ComboBox.vue";

const isFirstCheckedEvent: boolean = true;

const placeholder = "입력해주세요";

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
