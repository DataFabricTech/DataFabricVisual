<template>
  <!--  목록 & 상세에서 공통으로 쓰일 지식 그래프? -->
  <h2>Default Graph</h2>
  <div class="network__block" v-show="false">
    <div id="network-container"></div>
    <div>
      <h4>mouse hover event</h4>
      <div class="network-info"></div>
    </div>
  </div>
  <h2>Category Graph</h2>
  <div id="category" style="width: 95%; height: 100%"></div>
</template>

<script setup lang="ts">
import { CategoryGraph, NetworkDiagram } from "knowledge-graph-canvas";
import { useSearchCommonStore } from "~/store/search/common";
// import { Category } from "knowledge-graph-canvas/dist/category-graph";
// import {
//   NetworkDiagramEdgeInfo,
//   NetworkDiagramNodeInfo,
// } from "knowledge-graph-canvas/dist/network-diagram/layout/layout.type";

const searchCommonStore = useSearchCommonStore();
const { getGraphData } = searchCommonStore;

await getGraphData();

onMounted(() => {
  const categoryContainer = document.getElementById(
    "category",
  ) as HTMLDivElement;

  const categoryData: any = {
    id: "1002",
    name: "서울",

    children: [
      {
        id: "1004",
        name: "강남구",

        children: [],
        nodeList: [
          { id: "411", name: "논현1동" },
          { id: "412", name: "삼성2동" },
          { id: "413", name: "역삼1동" },
          { id: "414", name: "일원본동" },
          { id: "436", name: "논현2동" },
          { id: "437", name: "도곡1동" },
          { id: "464", name: "청담동" },
          { id: "518", name: "수서동" },
          { id: "682", name: "일원1동" },
          { id: "683", name: "일원2동" },
          { id: "369", name: "신사동" },
        ],
      },
      {
        id: "1005",
        name: "강동구",

        children: [],
        nodeList: [
          { id: "349", name: "둔촌1동" },
          { id: "350", name: "성내1동" },
          { id: "351", name: "암사3동" },
          { id: "352", name: "천호1동" },
          { id: "465", name: "둔촌2동" },
          { id: "483", name: "성내2동" },
          { id: "484", name: "천호2동" },
          { id: "519", name: "천호3동" },
          { id: "574", name: "명일1동" },
          { id: "623", name: "암사2동" },
          { id: "647", name: "명일2동" },
          { id: "684", name: "성내3동" },
          { id: "685", name: "암사1동" },
        ],
      },
      {
        id: "1006",
        name: "강북구",

        children: [],
        nodeList: [
          { id: "353", name: "번2동" },
          { id: "354", name: "수유1동" },
          { id: "415", name: "번3동" },
          { id: "438", name: "수유2동" },
          { id: "485", name: "삼각산동" },
          { id: "486", name: "수유3동" },
          { id: "520", name: "삼양동" },
          { id: "548", name: "미아동" },
          { id: "549", name: "우이동" },
          { id: "605", name: "송중동" },
          { id: "606", name: "인수동" },
          { id: "648", name: "번1동" },
          { id: "649", name: "송천동" },
        ],
      },
    ],

    nodeList: [],
  };

  new CategoryGraph({
    container: categoryContainer,
    categoryData,
    pixelQuality: "middle",
  });

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
