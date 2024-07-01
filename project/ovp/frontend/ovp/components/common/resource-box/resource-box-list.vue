<template>
  <template v-for="(dataObj, index) in props.dataList" :key="dataObj.id">
    <div v-if="props.useListCheckbox">
      <input
        type="checkbox"
        :id="`resource_box_list_${index}`"
        class="checkbox-input"
        @change="checked($event, dataObj.id)"
      />
      <label :for="`resource_box_list_${index}`" class="checkbox-label">
      </label>
    </div>

    <resource-box
      :class="[
        { 'is-resource-box-selected': selectedResourceBoxId === dataObj.id },
        props.class,
      ]"
      :data-obj="dataObj"
      :showOwner="props.showOwner"
      :showCategory="props.showCategory"
      :use-prv-btn="props.usePrvBtn"
      :useFirModelNm="props.useFirModelNm"
      :use-data-nm-link="props.useDataNmLink"
      @previewClick="previewClick"
      @modelNmClick="modelNmClick"
    />
  </template>
</template>

<script setup lang="ts">
import ResourceBox from "~/components/common/resource-box/resource-box.vue";

import type { ResourceBoxListProps } from "./resource-box-list-props";
import { defineEmits } from "vue";

const selectedList: Ref<Array<string | number>> = ref([]);
const selectedResourceBoxId = ref("");

const props = withDefaults(defineProps<ResourceBoxListProps>(), {
  usePrvBtn: true,
  useFirModelNm: false,
  useListCheckbox: false,
  useDataNmLink: true,
});

const emit = defineEmits<{
  (e: "previewClick", id: string | number): void;
  (e: "modelNmClick", id: string | number): void;
  (e: "checkedValueChanged", ids: any[]): void;
}>();
const previewClick = (id: string | number) => {
  selectedResourceBoxId.value = id;
  emit("previewClick", id);
};
const modelNmClick = (id: string | number) => {
  emit("modelNmClick", id);
};

const checked = ($evt: Event, id: string | number) => {
  const target = $evt.target as HTMLInputElement;

  if (target.checked) {
    if (!selectedList.value.includes(id)) {
      selectedList.value.push(id);
    }
  } else {
    selectedList.value = selectedList.value.filter(
      (itemId: string | number) => itemId !== id,
    );
  }

  emit("checkedValueChanged", selectedList.value);
};
</script>

<style lang="scss" scoped></style>
