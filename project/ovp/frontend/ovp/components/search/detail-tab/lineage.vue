<template>
  <div class="lineage">
<!--    TODO: 필터 구현-->
    <div class="filters">
      <div class="select select-clean">
        <button class="select-button">
          <span class="select-button-title">카테고리</span>
          <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
        </button>
      </div>
      <div class="select select-clean">
        <button class="select-button">
          <span class="select-button-title">소유자</span>
          <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
        </button>
      </div>
      <div class="select select-clean">
        <button class="select-button">
          <span class="select-button-title">태그</span>
          <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
        </button>
      </div>
      <button class="button button-error-lighter button-sm" type="button" @click="reset">
        <svg-icon class="button-icon" name="reset"></svg-icon>
        <span class="button-title">초기화</span>
      </button>
    </div>
    <LineageGraph ref="lineageRef" :lineageData="lineageData" @change="modelChoose"></LineageGraph>
    <Preview :preview-data="previewData" @change="getPreviewOn" :is-show-preview="isPreviewOn"></Preview>
    <div class="lineage-control">
      <button class="button button-lg button-neutral-stroke lineage-control-zoom-in" type="button"
              @click="handleZoomIn">
        <span class="hidden-text">확대</span>
        <svg-icon class="button-icon" name="plus"></svg-icon>
      </button>
      <button class="button button-lg button-neutral-stroke lineage-control-zoom-out" type="button"
              @click="handleZoomOut">
        <span class="hidden-text">축소</span>
        <svg-icon class="button-icon" name="minus"></svg-icon>
      </button>
      <button class="button button-lg button-neutral-stroke mt-2" type="button"
              @click="resetView">
        <span class="hidden-text">원좌표</span>
        <svg-icon class="button-icon" name="target-lock"></svg-icon>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import LineageGraph from "~/components/search/lineage/lineage-graph.vue";
import Preview from "~/components/search/preview.vue";
import type {NodeData} from "~/components/search/lineage/lineage";

// TODO: store로 값 바인딩
const rawNodes = [
  {id: "root", name: "Root", fqn: "/path/to/root"},
  {id: "child1", name: "Child 1", fqn: "/path/to/child1"},
  {id: "child2", name: "Child 2", fqn: "/path/to/child2"},
  {id: "child1-1", name: "Child 1-1", fqn: "/path/to/child1-1"},
  {id: "child1-2", name: "Child 1-2", fqn: "/path/to/child1-2"},
  {id: "child2-1", name: "Child 2-1", fqn: "/path/to/child2-1"},
];

// TODO: store로 값 바인딩
const rawEdges = [
  {fromId: "root", toId: "child1"},
  {fromId: "root", toId: "child2"},
  {fromId: "child1", toId: "child1-1"},
  {fromId: "child1", toId: "child1-2"},
  {fromId: "child2", toId: "child2-1"},
];

const lineageData = {nodes: rawNodes, edges: rawEdges};

const lineageRef = ref(null);

const isPreviewOn = ref(false);

// TODO: store로 값 바인딩
const previewData = ref({
  modelType: 'structured',
  modelInfo: {
    model: {
      name: '',
      desc: '',
      tableType: '',
      cnt: 0,
      columns: []
    },
    details: '',
    url: ''
  },
  tags: [],
  glossaries: []
});

// TODO: 필터 store 값 바인딩
const filterDataList = ref(null);

// 자식 컴포넌트에서 이벤트 발생할경우 isPreviewClosed 값 가져옴
const getPreviewOn = (isPreviewClosed: boolean) => {
  if (isPreviewClosed === false) {
    isPreviewOn.value = false;
  }
};

// 자식 컴포넌트에서 이벤트 발생할경우 NodeData값을 가져옴
const modelChoose = (nodeData: NodeData) => {
  if (nodeData) {
    isPreviewOn.value = true;
    previewData.value.modelInfo.model.name = nodeData.label;
  }
}

const handleZoomIn = () => {
  if (lineageRef.value) {
    lineageRef.value.zoomIn();
  }
}

const handleZoomOut = () => {
  if (lineageRef.value) {
    lineageRef.value.zoomOut();
  }
}

const resetView = () => {
  if (lineageRef.value) {
    lineageRef.value.resetView();
  }
}

const reset = () => {
  if (lineageRef.value) {
    lineageRef.value.reset();
  }
}
</script>

<style scoped>
</style>
