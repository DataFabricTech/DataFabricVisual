<template>
  <h2>Category Graph</h2>
  <div id="category" style="width: 95%; height: 100%"></div>
</template>

<script setup lang="ts">
import { CategoryGraph } from "knowledge-graph-canvas";
import { useSearchCommonStore } from "~/store/search/common";
// import { Category } from "knowledge-graph-canvas/dist/category-graph";
// import {
//   NetworkDiagramEdgeInfo,
//   NetworkDiagramNodeInfo,
// } from "knowledge-graph-canvas/dist/network-diagram/layout/layout.type";

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
        console.log(type);
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
