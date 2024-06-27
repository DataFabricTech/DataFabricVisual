<template>
  <div class="cytoscape-container">
    <div ref="cyContainer" class="cytoscape-graph"></div>
    <div class="controls">
      <button @click="zoomIn">Zoom In</button>
      <button @click="zoomOut">Zoom Out</button>
      <button @click="resetView">Reset View</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import {ref, onMounted, watch} from "vue";
import cytoscape from "cytoscape";
import dagre from "cytoscape-dagre";
import cytoscapeNodeHtmlLabel from "cytoscape-node-html-label";

// dagre layout 유형 등록
cytoscape.use(dagre);
cytoscapeNodeHtmlLabel(cytoscape);

let cyContainer = ref(null);
let cyRef = null;

interface RawNodeData {
  id: string;
  name: string;
  fqn: string;
}

interface RawEdgeData {
  fromId: string;
  toId: string;
}

interface NodeData {
  id: string;
  label: string;
  path: string;
}

interface EdgeData {
  source: string;
  target: string;
}

interface CytoscapeElement {
  data: NodeData | EdgeData;
}

interface CytoscapeNodeStyle {
  selector: string;
  style: {
    "background-color": string;
    color: string;
    shape: string;
    width: string;
    label: string;
    "text-valign": string;
    "text-halign": string;
    height: string;
    "border-color": string;
    "border-width": string;

    // 추가 설정 가능 옵션
    "corner-radius"?: string; // 모양의 모서리 반경입니다. px 또는 em 단위.
    "background-blacken"?: number; // 노드의 몸체를 검게 만듭니다. 0에서 1 사이의 값.
    "background-opacity"?: number; // 노드 배경색의 불투명도 수준입니다.
    "background-fill"?: string; // 노드 본체의 채우기 스타일입니다. solid, linear-gradient 또는 radial-gradient.
    "background-gradient-stop-colors"?: string; // 배경 그라데이션의 색상이 중지됩니다.
    "background-gradient-stop-positions"?: string; // 배경 그라데이션 정지 위치입니다.
    "background-gradient-direction"?: string; // 배경 그라데이션의 방향을 정의합니다. linear-gradient인 경우.

    // 경계 (테두리) 스타일
    "border-style"?: string; // 노드 테두리의 스타일입니다. solid, dotted, dashed 또는 double.
    "border-cap"?: string; // 노드 테두리의 캡 스타일입니다. butt, round 또는 square.
    "border-join"?: string; // 노드 테두리의 결합 스타일입니다. miter, bevel 또는 round.
    "border-dash-pattern"?: number[]; // dashed 선과 간격의 교대 길이를 지정하는 선 패턴입니다.
    "border-dash-offset"?: number; // dashed 라인 오프셋입니다. 가장자리 애니메이션을 만드는 데 유용합니다.
    "border-position"?: string; // 노드 경계의 위치입니다. center, inside, outside.
    "border-opacity"?: number; // 노드 테두리의 불투명도입니다.

    // 외곽선 스타일
    "outline-width"?: string; // 노드 외곽선의 크기입니다.
    "outline-style"?: string; // 노드 외곽선의 스타일입니다. solid, dotted, dashed 또는 double.
    "outline-color"?: string; // 노드 외곽선의 색상입니다.
    "outline-opacity"?: number; // 노드 외곽선의 불투명도입니다.
    "outline-offset"?: string; // 노드 외곽선의 오프셋입니다.

    // 패딩
    "padding"?: string; // 노드의 모든 측면 주변의 패딩 양입니다.
    "padding-relative-to"?: string; // 백분율 단위를 사용하는 경우 패딩 계산 방법을 결정합니다. width, height, average, min, max 중 하나.

    // 복합 상위 크기 조정
    "compound-sizing-wrt-labels"?: string; // 복합 노드 크기 조정에 하위 항목 레이블을 포함할지 여부.
    "min-width"?: string; // 복합 상위 노드의 최소(내부) 너비를 지정합니다.
    "min-width-bias-left"?: string; // 복합 노드가 해당만큼 확대되면 min-width의 값은 노드 왼쪽에 추가 너비의 백분율을 지정합니다.
    "min-width-bias-right"?: string; // 복합 노드가 해당만큼 확대되면 min-width의 값은 노드 오른쪽에 추가 너비의 백분율을 지정합니다.
    "min-height"?: string; // 복합 상위 노드의 최소(내부) 높이를 지정합니다.
    "min-height-bias-top"?: string; // 복합 노드가 로 확대될 때 min-height의 값은 노드의 상단에 추가 너비의 백분율을 지정합니다.
    "min-height-bias-bottom"?: string; // 복합 노드가 로 확대될 때 min-height의 값은 노드의 아래쪽에 추가 너비의 백분율을 지정합니다.
  };
}

interface CytoscapeEdgeStyle {
  selector: string;
  style: {
    width: number;
    "target-arrow-shape": string;
    "line-color": string;
    "target-arrow-color": string;
    "curve-style": string;
    "source-endpoint": string;
    "target-endpoint": string;

    // 추가 설정 가능 옵션
    "line-cap"?: string; // 가장자리 라인의 캡 스타일입니다. butt(기본값), round또는 square일 수 있습니다.
    "line-opacity"?: number; // 가장자리의 선과 화살표의 불투명도입니다. 0(투명)에서 1(불투명) 사이의 값입니다.
    "line-fill"?: string; // 가장자리 선의 채우기 스타일입니다. solid(기본값), linear-gradient 또는 radial-gradient 일 수 있습니다.
    "line-dash-pattern"?: number[]; // dashed선과 간격의 교대 길이를 지정하는 선 패턴입니다. 예 [6, 3].
    "line-dash-offset"?: number; // dashed라인 오프셋입니다. 예: 24. 가장자리 애니메이션을 만드는 데 유용합니다.
  };
}

const props = defineProps<{
  lineageData: { nodes: RawNodeData[]; edges: RawEdgeData[] };
}>();

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

const nodeStyle: CytoscapeNodeStyle = {
  selector: "node",
  style: {
    "background-color": "#666", // 노드 몸체의 색상
    label: "data(label)", // 노드 라벨 데이터 속성
    "text-valign": "center", // 노드 라벨의 수직 정렬
    "text-halign": "center", // 노드 라벨의 수평 정렬
    color: "#fff", // 노드 라벨의 색상
    width: "100px", // 노드 본체의 너비
    height: "40px", // 노드 본체의 높이
    shape: "rectangle", // 노드 몸체의 모양
    "border-color": "#000", // 노드 테두리의 색상
    "border-width": "2px", // 노드 테두리의 크기

    // 추가 설정 가능 옵션
    // "corner-radius": "5px", // 모양의 모서리 반경입니다. px 또는 em 단위.
    // "background-blacken": 0.1, // 노드의 몸체를 검게 만듭니다. 0에서 1 사이의 값.
    // "background-opacity": 0.8, // 노드 배경색의 불투명도 수준입니다.
    // "background-fill": "solid", // 노드 본체의 채우기 스타일입니다. solid, linear-gradient 또는 radial-gradient.
    // "background-gradient-stop-colors": "cyan magenta yellow", // 배경 그라데이션의 색상이 중지됩니다.
    // "background-gradient-stop-positions": "0% 50% 100%", // 배경 그라데이션 정지 위치입니다.
    // "background-gradient-direction": "to-bottom", // 배경 그라데이션의 방향을 정의합니다. linear-gradient인 경우.
    // "border-style": "solid", // 노드 테두리의 스타일입니다. solid, dotted, dashed 또는 double.
    // "border-cap": "butt", // 노드 테두리의 캡 스타일입니다. butt, round 또는 square.
    // "border-join": "miter", // 노드 테두리의 결합 스타일입니다. miter, bevel 또는 round.
    // "border-dash-pattern": [6, 3], // dashed 선과 간격의 교대 길이를 지정하는 선 패턴입니다.
    // "border-dash-offset": 24, // dashed 라인 오프셋입니다. 가장자리 애니메이션을 만드는 데 유용합니다.
    // "border-position": "center", // 노드 경계의 위치입니다. center, inside, outside.
    // "border-opacity": 0.8, // 노드 테두리의 불투명도입니다.
    // "outline-width": "2px", // 노드 외곽선의 크기입니다.
    // "outline-style": "solid", // 노드 외곽선의 스타일입니다. solid, dotted, dashed 또는 double.
    // "outline-color": "#000", // 노드 외곽선의 색상입니다.
    // "outline-opacity": 0.8, // 노드 외곽선의 불투명도입니다.
    // "outline-offset": "2px", // 노드 외곽선의 오프셋입니다.
    // "padding": "10px", // 노드의 모든 측면 주변의 패딩 양입니다.
    // "padding-relative-to": "width", // 백분율 단위를 사용하는 경우 패딩 계산 방법을 결정합니다. width, height, average, min, max 중 하나.
    // "compound-sizing-wrt-labels": "include", // 복합 노드 크기 조정에 하위 항목 레이블을 포함할지 여부.
    // "min-width": "200px", // 복합 상위 노드의 최소(내부) 너비를 지정합니다.
    // "min-width-bias-left": "50%", // 복합 노드가 해당만큼 확대되면 min-width의 값은 노드 왼쪽에 추가 너비의 백분율을 지정합니다.
    // "min-width-bias-right": "50%", // 복합 노드가 해당만큼 확대되면 min-width의 값은 노드 오른쪽에 추가 너비의 백분율을 지정합니다.
    // "min-height": "200px", // 복합 상위 노드의 최소(내부) 높이를 지정합니다.
    // "min-height-bias-top": "50%", // 복합 노드가 로 확대될 때 min-height의 값은 노드의 상단에 추가 너비의 백분율을 지정합니다.
    // "min-height-bias-bottom": "50%", // 복합 노드가 로 확대될 때 min-height의 값은 노드의 아래쪽에 추가 너비의 백분율을 지정합니다.
  },
};

const edgeStyle: CytoscapeEdgeStyle = {
  selector: "edge",
  style: {

    width: 1,
    "line-color": "#ccc",
    "target-arrow-color": "#ccc",
    "target-arrow-shape": "triangle",
    "curve-style": "unbundled-bezier",
    "source-endpoint": "50% 0%",
    "target-endpoint": "-50% 0%",

    // 추가 설정 가능 옵션
    // "line-cap": "butt", // 가장자리 라인의 캡 스타일입니다. butt(기본값), round또는 square일 수 있습니다.
    // "line-opacity": 0.8, // 가장자리의 선과 화살표의 불투명도입니다. 0(투명)에서 1(불투명) 사이의 값입니다.
    // "line-fill": "solid", // 가장자리 선의 채우기 스타일입니다. solid(기본값), linear-gradient 또는 radial-gradient 일 수 있습니다.
    // "line-dash-pattern": [6, 3], // dashed선과 간격의 교대 길이를 지정하는 선 패턴입니다. 예 [6, 3].
    // "line-dash-offset": 24, // dashed라인 오프셋입니다. 예: 24. 가장자리 애니메이션을 만드는 데 유용합니다.
  },
};

const style: (CytoscapeNodeStyle | CytoscapeEdgeStyle)[] = [
  nodeStyle,
  edgeStyle,
];

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
        rankSep: 100 // 각 계층간의 간격 설정
      },
      userZoomingEnabled: false // 마우스 스크롤 zoom in/out 설정
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
          tpl: function (data: NodeData) {  // 라벨 템플릿 정의 메서드
            return `<div style="display: flex; justify-content: center; align-items: center; text-align:center; color:white; background-color:#666; padding:5px; border: 2px solid #000; border-radius:5px; font-size:12px; width: 100px; height: 40px;">
                      <div>
                        ${data.path}<br/>
                        ${data.label}
                      </div>
                    </div>`;
          },
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

onMounted(() => {
  const data = transformData(props.lineageData.nodes, props.lineageData.edges);
  initializeCytoscape(data);
});

watch(
    () => props.lineageData,
    (newLineageData) => {
      if (cyRef) {
        cyRef.destroy(); // 이전 cyRef 제거
      }
      const newData = transformData(newLineageData.nodes, newLineageData.edges);
      initializeCytoscape(newData);
    },
    {deep: true},
);
</script>

<style lang="scss" scoped>
.cytoscape-container {
  width: 100%;
  height: 600px;
  border: 1px solid #ccc;
  position: relative; /* 상대 위치 */
}

.cytoscape-graph {
  width: 100%;
  height: 100%;
}

.controls {
  position: absolute;
  bottom: 10px;
  left: 10px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  z-index: 10;
}

button {
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
}
</style>
