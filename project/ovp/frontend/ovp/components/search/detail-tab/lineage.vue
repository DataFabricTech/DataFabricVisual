<template>
  <div class="lineage">
    <div class="filters">
      <data-lineage-filter :data="filters"></data-lineage-filter>
    </div>
    <Preview
      :preview-data="previewData"
      :model-type="dataModelType"
      @change="getPreviewOn"
      :is-show-preview="isShowPreview"
    ></Preview>
    <LineageGraph
      ref="lineageRef"
      :lineageData="lineageData"
      :previewOn="isShowPreview"
      @change="modelChoose"
    ></LineageGraph>
    <div class="lineage-control">
      <button
        class="button button-lg button-neutral-stroke lineage-control-zoom-in"
        type="button"
        @click="handleZoomIn"
      >
        <span class="hidden-text">확대</span>
        <svg-icon class="button-icon" name="plus"></svg-icon>
      </button>
      <button
        class="button button-lg button-neutral-stroke lineage-control-zoom-out"
        type="button"
        @click="handleZoomOut"
      >
        <span class="hidden-text">축소</span>
        <svg-icon class="button-icon" name="minus"></svg-icon>
      </button>
      <button
        class="button button-lg button-neutral-stroke mt-2"
        type="button"
        @click="resetView"
      >
        <span class="hidden-text">원좌표</span>
        <svg-icon class="button-icon" name="target-lock"></svg-icon>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import DataLineageFilter from "@/components/search/lineage/data-lineage-filter.vue";
import LineageGraph from "~/components/search/lineage/lineage-graph.vue";
import Preview from "~/components/common/preview/preview.vue";

import type { NodeData } from "@/components/search/lineage/lineage";
import { useLineageStore } from "@/store/search/detail/lineage";
import { useDataModelDetailStore } from "@/store/search/detail/index";

const lineageStore = useLineageStore();
const { getFilters, getLineageData, getPreviewData } = lineageStore;
const { filters, lineageData, previewData, isShowPreview, lineageRef } =
  storeToRefs(lineageStore);

const dataModelDetailStore = useDataModelDetailStore();
const { getDataModelFqn } = dataModelDetailStore;
const { dataModelType } = storeToRefs(dataModelDetailStore);

onBeforeMount(async () => {
  await getFilters();
  // TODO: param => (fqn(외부스토어에서 호출), 필터) 추가 필요
  // await getLineageData(dataModelType, dataModelFqn);
});

const getPreviewOn = (isPreviewClosed: boolean) => {
  if (isPreviewClosed === false) {
    lineageRef.value.handleNodeOff();
    isShowPreview.value = false;
  }
};

const modelChoose = async (nodeData: NodeData) => {
  if (nodeData) {
    isShowPreview.value = true;
    console.log(nodeData.fqn);
    await getPreviewData(nodeData.fqn);
    previewData.value.modelInfo.model.name = nodeData.label;
  }
};

const handleZoomIn = () => {
  if (lineageRef.value) {
    lineageRef.value.zoomIn();
  }
};

const handleZoomOut = () => {
  if (lineageRef.value) {
    lineageRef.value.zoomOut();
  }
};

const resetView = () => {
  if (lineageRef.value) {
    lineageRef.value.resetView();
  }
};
</script>

<style scoped></style>
