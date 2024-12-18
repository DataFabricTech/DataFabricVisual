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
import { useLineageStore } from "@/store/search/detail/lineage";

const lineageStore = useLineageStore();
const { isShowPreview, lineageRef, lineageFilterRef } =
  storeToRefs(lineageStore);

// dagre layout 유형 등록
cytoscape.use(dagre);
cytoscapeNodeHtmlLabel(cytoscape);

let cyContainer = ref(null);
let cyRef = null;

const handleClick = (event: MouseEvent) => {
  // 필터 클릭시, 열려있는 tooltip 제거
  if (lineageFilterRef.value?.contains(event.target)) {
    if (tooltip.value) {
      tooltip.value.remove();
    }
  }

  // LineageGraph 내에서 클릭되고, 미리보기가 열려있으면 닫음
  const lineageElement = lineageRef.value?.$el;
  if (lineageElement?.contains(event.target) && isShowPreview.value) {
    isShowPreview.value = false;
  }
};

onMounted(() => {
  document.addEventListener("click", handleClick);
});

onBeforeUnmount(() => {
  document.removeEventListener("click", handleClick);
});

const tpl = (data: NodeData) => {
  // 라벨 템플릿 정의 메서드
  return `<div class="cytoscape-box" style="
    font-family: 'pretendard', 'Segoe UI', 'Open Sans', 'Helvetica Neue';
    width: 115px;
    height: 40px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    background-color: #f7f9fc;
    outline: none;
    border-radius: 4px;
    overflow: hidden;
  ">
    <div style="
      font-size: 8px;
      font-weight: 300;
      color: #868C98;
      line-height: 1.2;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      width: 100px; /* 노드 크기에 맞게 설정 */
      display: inline-block;
    ">${data.path}</div>
    <div style="
      color: #2b3440;
      font-size: 11px;
      line-height: 1.6;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      width: 100px; /* 노드 크기에 맞게 설정 */
      display: inline-block;
    ">${data.label}</div>
</div>`;
};

const props = defineProps<{
  lineageData: { nodes: RawNodeData[]; edges: RawEdgeData[] };
  previewOn: boolean;
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
      path: node.path,
      fqn: node.fqn,
      entityType: node.entityType,
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
const tooltip = ref<HTMLDivElement | null>(null);

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
      // 리니지 default zoom 지정
      cyRef.zoom(1.2);

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

      // 마우스 오버 이벤트
      cyRef.on("mouseover", "node", (event) => {
        const node = event.target;
        const nodeData = node.data(); // 노드 데이터 가져오기

        // 툴팁을 HTML 요소로 만들어 추가 (예: div 요소)
        tooltip.value = document.createElement("div");
        tooltip.value.className = "cy-tooltip";
        tooltip.value.innerHTML = `${nodeData.path}<br>${nodeData.label}`;
        tooltip.value.style.position = "absolute";
        tooltip.value.style.backgroundColor = "#004D57";
        tooltip.value.style.color = "#fff";
        tooltip.value.style.padding = "5px";
        tooltip.value.style.borderRadius = "4px";
        tooltip.value.style.fontSize = "12px";
        tooltip.value.style.pointerEvents = "none";
        tooltip.value.style.zIndex = "1000";

        document.body.appendChild(tooltip.value);

        // 마우스 위치에 툴팁 배치
        const updateTooltipPosition = (e) => {
          tooltip.value.style.left = `${e.pageX + 10}px`;
          tooltip.value.style.top = `${e.pageY + 10}px`;
        };

        updateTooltipPosition(event.originalEvent);

        // 마우스 이동 시 툴팁 위치 업데이트
        cyRef.on("mousemove", updateTooltipPosition);

        // 마우스 아웃 시 툴팁 제거
        cyRef.one("mouseout", "node", () => {
          if (tooltip.value) {
            tooltip.value.remove();
          }
          cyRef.off("mousemove", updateTooltipPosition);
        });
      });
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

const handleNodeOff = () => {
  if (cyRef) {
    // 노드 선택 상태 해제
    cyRef.$("node:selected").unselect();
  }
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
  handleNodeOff,
  zoomIn,
  zoomOut,
  resetView,
  reset,
});
</script>

<style lang="scss" scoped>
@import "./lineage-graph";
</style>
