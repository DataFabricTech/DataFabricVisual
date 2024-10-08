<template>
  <div id="category" class="visual">
    <!-- TODO: [개발] 범례 -->
    <div class="visual-legend">
      <button class="button button-lg button-neutral-stroke" type="button" @click="toggleLegend">
        <span class="hidden-text">범례</span>
        <svg-icon class="button-icon" name="legend"></svg-icon>
      </button>
      <ul class="visual-legend-content" v-show="isLegendVisible">
        <li class="visual-legend-item visual-legend-item-depth1">
          1depth 카테고리
        </li>
        <li class="visual-legend-item visual-legend-item-depth2">
          2depth 카테고리
        </li>
        <li class="visual-legend-item visual-legend-item-depth3">
          3depth 카테고리
        </li>
        <li class="visual-legend-item visual-legend-item-model">
          데이터 모델
        </li>
        <li class="visual-legend-item visual-legend-item-way">
          <div class="edge">
            <div class="edge-line"></div>
          </div>
          노드 관계 방향
        </li>
      </ul>
    </div>
    <!-- TODO: [개발] 노드 클릭 시 드롭다운 -->
    <div class="dropdown" style="top: 200px; left: 200px">
      <ul class="dropdown-list">
        <li class="dropdown-item">
          <button class="dropdown-button">
            <svg-icon class="svg-icon" name="model-list"></svg-icon>
            <span class="dropdown-text">모델 리스트</span>
          </button>
        </li>
      </ul>
    </div>
    <!-- TODO: [개발] 노드 클릭 시 드롭다운 -->
    <div class="dropdown" style="top: 350px; left: 350px">
      <ul class="dropdown-list">
        <li class="dropdown-item">
          <button class="dropdown-button">
                <span class="dropdown-text"
                >상세정보</span
                >
          </button>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { CategoryGraph } from "knowledge-graph-canvas";
import { useSearchCommonStore } from "~/store/search/common";
import { Category } from "knowledge-graph-canvas/dist/category-graph";
// import {
//   NetworkDiagramEdgeInfo,
//   NetworkDiagramNodeInfo,
// } from "knowledge-graph-canvas/dist/network-diagram/layout/layout.type";

const isLegendVisible = ref(true);

function toggleLegend() {
  isLegendVisible.value = !isLegendVisible.value;
}


const searchCommonStore = useSearchCommonStore();
const { getGraphData } = searchCommonStore;
const { graphData } = storeToRefs(searchCommonStore);

await getGraphData();

const nodeData: any = graphData.value.nodes;
const formattedNodeData = nodeData.children.map((node: any) => ({
  id: node.id,
  name: node.name,
  nodeList: node.children,
  children: [],
}));
const newNode = {
  id: nodeData.id,
  name: nodeData.name,
  children: formattedNodeData,
  nodeList: [],
};

onMounted(() => {
  const categoryContainer = document.getElementById(
    "category",
  ) as HTMLDivElement;

  new CategoryGraph({
    container: categoryContainer,
    categoryData: newNode,
    pixelQuality: "middle",
    eventHandler: {
      onClick: (e: any, id: any, type: any) => {
        // if (type === "node" && id) {
        //   const nodes: any[] = [
        //     { id: `n${idCount++}`, width: 100, height: 100 },
        //     { id: `n${idCount++}`, width: 100, height: 100 },
        //     { id: `n${idCount++}`, width: 100, height: 100 },
        //   ];
        //   const edges: any[] = nodes.map((node) => ({
        //     id: `${id}_${node.id}`,
        //     sources: [id],
        //     targets: [node.id],
        //   }));
        //
        //   diagram.addElement({ nodes, edges });
        // }
      },
      onHover: (e, id, type) => {
        // console.log(e);
      },
    },
  });
});
</script>

<style lang="scss" scoped></style>
