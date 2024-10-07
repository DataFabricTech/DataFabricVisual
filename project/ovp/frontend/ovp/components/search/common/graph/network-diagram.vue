<template>
  <!--  목록 & 상세에서 공통으로 쓰일 지식 그래프? -->
  <h2>Default Graph</h2>
  <div class="network__block">
    <div id="network-container" style="width: 95%; height: 100%"></div>
    <div>
      <h4>mouse hover event</h4>
      <div class="network-info"></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { NetworkDiagram } from "knowledge-graph-canvas";
import { useSearchCommonStore } from "~/store/search/common";
// import { Category } from "knowledge-graph-canvas/dist/category-graph";
// import {
//   NetworkDiagramEdgeInfo,
//   NetworkDiagramNodeInfo,
// } from "knowledge-graph-canvas/dist/network-diagram/layout/layout.type";

// const searchCommonStore = useSearchCommonStore();
// const { getGraphData } = searchCommonStore;
// const { graphData } = storeToRefs(searchCommonStore);

// await getGraphData();

onMounted(() => {
  const relativeContainer = document.getElementById(
    "network-container",
  ) as HTMLDivElement;

  const info = document.querySelector(".network-info") as HTMLDivElement;

  let idCount = 10;

  const nodes: any[] = [
    { id: "n1", width: 100, height: 100, style: { backgroundColor: "red" } },
    { id: "n2", width: 100, height: 100 },
    { id: "n3", width: 100, height: 100 },
    { id: "n4", width: 100, height: 100 },
  ];
  const edges: any[] = [
    {
      id: "e1",
      sources: ["n1"],
      targets: ["n2"],
      style: { isLabel: true, color: "red", fontSize: 20 },
      labels: [{ text: "최고점수" }],
    },
    { id: "e2", sources: ["n1"], targets: ["n3"] },
    { id: "e3", sources: ["n4"], targets: ["n1"] },
  ];
  info.innerHTML = `
<div>
position x:
</div>
<div>
position y:
</div>
<div>
element id:
</div>
<div>
element type:
</div>
`;
  const diagram = new NetworkDiagram({
    container: relativeContainer,
    nodes,
    edges,
    eventHandler: {
      onClick: (e, id, type) => {
        if (type === "node" && id) {
          const nodes: any[] = [
            { id: `n${idCount++}`, width: 100, height: 100 },
            { id: `n${idCount++}`, width: 100, height: 100 },
            { id: `n${idCount++}`, width: 100, height: 100 },
          ];
          const edges: any[] = nodes.map((node) => ({
            id: `${id}_${node.id}`,
            sources: [id],
            targets: [node.id],
          }));

          diagram.addElement({ nodes, edges });
        }
      },
      onHover: (e, id, type) => {
        info.innerHTML = `
      <div>
      position x: ${e.offsetX || ""}
      </div>
      <div>
      position y: ${e.offsetY || ""}
      </div>
      <div>
      element id: ${id || "none"}
      </div>
      <div>
      element type: ${type || "none"}
      </div>
      `;
      },
    },
  });
});
</script>

<style lang="scss" scoped></style>
