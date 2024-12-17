<template>
  <div class="work-list">
    <div class="l-top-bar">
      <span class="font-semibold"
        >선택된 데이터 모델 ({{ props.modelListCnt }})</span
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
      :filter="props.dataModelFilter"
      :data="props.modelList"
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
      @item-click="clickItem"
      @bookmark-change="changeBookmark"
    ></data-model-list>
  </div>
</template>
<script setup lang="ts">
import DataModelList from "~/components/datamodel-creation/list/base/data-model-list.vue";

const props = defineProps({
  modelList: {
    type: Object,
  },
  dataModelFilter: {
    type: Object,
  },
  modelListCnt: {
    type: Number,
    default: 0,
  },
});

const emit = defineEmits<{
  (e: "change", option: boolean): void;
  (e: "delete", value: string): void;
  (e: "item-click", value: string): void;
  (e: "bookmark-change", value: any): void;
}>();

const addDataModel = (option: boolean) => {
  emit("change", option);
};

const deleteDataModel = (value: string) => {
  emit("delete", value);
};

const clickItem = (value: string) => {
  emit("item-click", value);
};

const changeBookmark = (value: any) => {
  emit("bookmark-change", value);
};
</script>
<style lang="scss" scoped></style>
