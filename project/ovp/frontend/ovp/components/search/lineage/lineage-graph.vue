<template>
  <div class="cytoscape-container">
    <div ref="cyContainer" class="cytoscape-graph"></div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, defineExpose } from "vue";
import cytoscape from "cytoscape";
import dagre from "cytoscape-dagre";
import cytoscapeNodeHtmlLabel from "cytoscape-node-html-label";
import type {
  RawNodeData,
  RawEdgeData,
  NodeData,
  CytoscapeElement,
  CytoscapeNodeStyle,
  CytoscapeEdgeStyle,
} from "./lineage";
import lineageStyle from "./lineage-style";

// dagre layout 유형 등록
cytoscape.use(dagre);
cytoscapeNodeHtmlLabel(cytoscape);

let cyContainer = ref(null);
let cyRef = null;

const tpl = (data: NodeData) => {
  // 라벨 템플릿 정의 메서드
  return `<div style="display: flex; justify-content: center; align-items: center; text-align:center; color:white; background-color:#666; padding:5px; border: 2px solid #000; border-radius:5px; font-size:12px; width: 100px; height: 40px;">
            <div>
              ${data.path}<br/>
              ${data.label}
            </div>
          </div>`;
};

const props = defineProps<{
  lineageData: { nodes: RawNodeData[]; edges: RawEdgeData[] };
}>();

const emit = defineEmits<{ (e: "change", nodeData: NodeData): void }>();

const transformData = (
  nodes: RawNodeData[],
  edges: RawEdgeData[],
): CytoscapeElement[] => {
  const transformedNodes = nodes.map((node) => ({
    data: {
      id: node.id,
      label: node.name,
      path: node.fqn,
    },
  }));

  const transformedEdges = edges.map((edge) => ({
    data: {
      source: edge.fromId,
      target: edge.toId,
    },
  }));

  return [...transformedNodes, ...transformedEdges];
};

const style: (CytoscapeNodeStyle | CytoscapeEdgeStyle)[] = lineageStyle;

const initializeCytoscape = (data: CytoscapeElement[]) => {
  if (cyContainer.value) {
    const options: cytoscape.CytoscapeOptions = {
      container: cyContainer.value,
      elements: data,
      style,
      layout: {
        name: "dagre",
        rankDir: "LR",
        nodeSep: 50, // 노드 간격 설정
        edgeSep: 20, // 엣지 간의 간격 설정
        rankSep: 100, // 각 계층간의 간격 설정
      },
      userZoomingEnabled: false, // 마우스 스크롤 zoom in/out 설정
    };

    cyRef = cytoscape(options);

    if (cyRef) {
      // cyRef를 any로 캐스팅하여 typescript가 nodeHtmlLabel 메서드 인식
      // 노드의 라벨 커스터마이징
      (cyRef as any).nodeHtmlLabel([
        {
          query: "node", // 노드에 html 라벨 적용
          halign: "center", // html 라벨의 수평 정렬 설정
          valign: "center", // html 라벨의 수직 정렬 설정
          halignBox: "center", // 라벨을 포함하는 박스의 수평 정렬 설정
          valignBox: "center", // 라벨을 포함하는 박스의 수직 정렬 설정
          cssClass: "",
          tpl,
        },
      ]);
    }

    cyRef.edges().forEach((edge) => {
      const sourcePos = edge.sourceEndpoint();
      const targetPos = edge.targetEndpoint();

      if (sourcePos.y > targetPos.y) {
        edge.style({
          // 첫 번째 제어점과 두 번째 제어점 간의 거리를 지정
          "control-point-distances": "20 -20",
          // 첫 번째 제어점과 두 번째 제어점의 상대적인 위치
          "control-point-weights": "0.5 0.7",
        });
      } else if (sourcePos.y === targetPos.y) {
        edge.style({
          "control-point-distances": "0 0",
          "control-point-weights": "0.5 0.7",
        });
      } else {
        edge.style({
          "control-point-distances": "-20 20",
          "control-point-weights": "0.5 0.7",
        });
      }
    });

    cyRef.on("tap", "node", handleNodeClick);
  }
};

const handleNodeClick = (event) => {
  const node = event.target;
  emit("change", node.data());
};

const zoomIn = () => {
  if (cyRef) {
    const currentZoom = cyRef.zoom();
    cyRef.zoom(currentZoom + 0.2);
  }
};

const zoomOut = () => {
  if (cyRef) {
    const currentZoom = cyRef.zoom();
    cyRef.zoom(currentZoom - 0.2);
  }
};

const resetView = () => {
  if (cyRef) {
    cyRef.fit(30);
  }
};

// 리니지 초기화
const reset = () => {
  const data = transformData(props.lineageData.nodes, props.lineageData.edges);
  initializeCytoscape(data);
};

onMounted(() => {
  const data = transformData(props.lineageData.nodes, props.lineageData.edges);
  initializeCytoscape(data);
});

watchEffect(() => {
  const newLineageData = props.lineageData;
  if (cyRef) {
    cyRef.off("tap", "node", handleNodeClick); // 기존 이벤트 해제
    cyRef.destroy(); // 이전 cyRef 제거
  }
  const newData = transformData(newLineageData.nodes, newLineageData.edges);
  initializeCytoscape(newData);
});

onBeforeUnmount(() => {
  if (cyRef) {
    cyRef.off("tap", "node", handleNodeClick); // 컴포넌트가 제거될 때 이벤트 해제
    cyRef.destroy();
  }
});

defineExpose({
  zoomIn,
  zoomOut,
  resetView,
  reset,
});
</script>

<style lang="scss" scoped>
.cytoscape-container {
  width: 80%;
  height: 600px;
  position: relative; /* 상대 위치 */
}

.cytoscape-graph {
  width: 100%;
  height: 100%;
}
</style>
