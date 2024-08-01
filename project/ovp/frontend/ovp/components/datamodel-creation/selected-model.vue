<template>
  <div class="work-list">
    <div class="l-top-bar">
      <span class="font-semibold"
        >선택된 데이터 모델 ({{ modelListCnt}})</span
      >
      <button
        class="button button-secondary-stroke"
        @click="addDataModel(true)"
      >
        추가
      </button>
    </div>

    <data-model-list
      class="menu-lg"
      :filter="dataModelFilter"
      :data="modelList"
      label-key="modelNm"
      value-key="id"
      :use-item-delete-btn="true"
      :is-multi="false"
      :use-sort="false"
      :use-infinite="false"
      :use-live-search="true"
      list-type="selected"
      no-data-msg="선택된 데이터 모델이 없습니다."
      @delete="deleteDataModel"
    ></data-model-list>
  </div>
</template>
<script setup lang="ts">

import { useCreationStore } from "@/store/datamodel-creation/index";
import DataModelList from "~/components/datamodel-creation/list/data-model-list.vue";

const emit = defineEmits<{ (e: "change", option: boolean): void }>();

const store = useCreationStore();
const { modelList, selectedDataList, dataModelFilter,modelListCnt } = storeToRefs(store);

const addDataModel = (option: boolean) => {
  emit("change", option);
};

const deleteDataModel = (value: string) => {
  emit("delete", value);
}
</script>
<style lang="scss" scoped></style>
