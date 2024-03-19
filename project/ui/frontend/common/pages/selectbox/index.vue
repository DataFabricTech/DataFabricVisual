<template>
    <select-box
    :data="options"
    label-key="label"
    value-key="value"
    :selectedItem="selectedItem"
    :isFirstSelectedEvent= isFirstCheckedEvent
    @select="selectItem"></select-box>
</template>

<script setup lang="ts">
import SelectBox from "~/components/extends/select-box/SelectBox.vue";

const isFirstCheckedEvent:boolean = true;

const options: { [key: string]: string | number }[] = [
    {
        label: "option1",
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

</style>