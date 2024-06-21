<template>
  <div class="sample">
  <select-box class="select-lg"
    :data="options"
    label-key="label"
    value-key="value"
    :selectedItem="selectedItem"
    :isFirstSelectedEvent= isFirstCheckedEvent
    @select="selectItem"></select-box>
  <select-box
    :data="options"
    label-key="label"
    value-key="value"
    :selectedItem="selectedItem"
    :isFirstSelectedEvent= isFirstCheckedEvent
    @select="selectItem"></select-box>
    <select-box class="select-sm"
      :data="options"
      label-key="label"
      value-key="value"
      :selectedItem="selectedItem"
      :isFirstSelectedEvent= isFirstCheckedEvent
      @select="selectItem"></select-box>
  </div>
</template>

<script setup lang="ts">
import SelectBox from "~/components/extends/select-box/SelectBox.vue";

const isFirstCheckedEvent:boolean = true;

const options: { [key: string]: string | number }[] = [
    {
        label: "option1option1option1option1option1",
        value: "option11",
    },
    {
        label: "option2",
        value: "option22",
    },
    {
        label: "option3",
        value: "option33",
    }
]

const selectedItem = "";
function selectItem(item:string | number) {
    console.log(item)
}
</script>

<style scoped>
.sample {
  display: flex;
  gap: 8px;
  padding: 30px;
}
</style>
