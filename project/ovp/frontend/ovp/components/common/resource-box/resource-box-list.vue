<template>
  <template v-for="dataObj in props.dataList" :key="dataObj.id">
    <resource-box
      :class="[
        {
          'is-resource-box-selected':
            isBoxSelectedStyle && selectedResourceBoxId === dataObj.id,
        },
        props.class,
      ]"
      :data-obj="dataObj"
      :use-list-checkbox="props.useListCheckbox"
      :showOwner="props.showOwner"
      :showCategory="props.showCategory"
      :use-prv-btn="props.usePrvBtn"
      :use-detail-btn="props.useDetailBtn"
      :useFirModelNm="props.useFirModelNm"
      :use-data-nm-link="props.useDataNmLink"
      :selected-model-list="props.selectedModelList"
      @checked="checked"
      @previewClick="previewClick"
      @open-detail-page="openDetailPage"
    />
  </template>
</template>

<script setup lang="ts">
import ResourceBox from "@/components/common/resource-box/resource-box.vue";
import { defineEmits, ref, watch } from "vue";
import type { ResourceBoxListProps } from "./resource-box-list-props";

const selectedList: Ref<Array<string | number>> = ref([]);
const selectedResourceBoxId: Ref<string | number> = ref("");

const props = withDefaults(defineProps<ResourceBoxListProps>(), {
  usePrvBtn: false,
  useDetailBtn: false,
  useFirModelNm: false,
  useListCheckbox: false,
  useDataNmLink: true,
  isBoxSelectedStyle: false,
  isChecked: false,
  selectedModelList: () => [],
});

const emit = defineEmits<{
  (e: "previewClick", data: object): void;
  (e: "openDetailPage", data: object): void;
  (e: "checkedValueChanged", ids: any[]): void;
}>();

const previewClick = (data: object) => {
  const { id } = data as { id: string };
  selectedResourceBoxId.value = id;
  emit("previewClick", data);
};

const checked = ({
  id,
  checked,
}: {
  id: string | number;
  checked: boolean;
}) => {
  if (checked) {
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

function openDetailPage(data: object) {
  const { id, fqn, type } = data as { id: string; fqn: string; type: string };
  const queryParams = new URLSearchParams({
    type,
    id,
    fqn,
  }).toString();

  const fullPath = `/portal/search/detail?${queryParams}`;

  window.open(fullPath, "_blank", "noopener,noreferrer");
}

watch(
  () => props.selectedModelList,
  (newVal) => {
    selectedList.value = [...newVal];
  },
  { immediate: true },
);
</script>

<style lang="scss" scoped></style>
