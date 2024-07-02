import {CytoscapeNodeStyle, CytoscapeEdgeStyle} from "./lineage";

const nodeStyle: CytoscapeNodeStyle = {
    selector: "node",
    style: {
        "background-color": "#666",
        label: "data(label)",
        "text-valign": "center",
        "text-halign": "center",
        color: "#fff",
        width: "100px",
        height: "40px",
        shape: "rectangle",
        "border-color": "#050505",
        "border-width": "2px",

        // 추가 설정 가능 옵션
        // "corner-radius": "5px", // 모양의 모서리 반경입니다. px 또는 em 단위.
        // "background-blacken": 0.1, // 노드의 몸체를 검게 만듭니다. 0에서 1 사이의 값.
        // "background-opacity": 0.8, // 노드 배경색의 불투명도 수준입니다.
        // "background-fill": "solid", // 노드 본체의 채우기 스타일입니다. solid, linear-gradient 또는 radial-gradient.
        // "background-gradient-stop-colors": "cyan magenta yellow", // 배경 그라데이션의 색상이 중지됩니다.
        // "background-gradient-stop-positions": "0% 50% 100%", // 배경 그라데이션 정지 위치입니다.
        // "background-gradient-direction": "to-bottom", // 배경 그라데이션의 방향을 정의합니다. linear-gradient인 경우.

        // 경계 (테두리) 스타일
        // "border-style": "solid", // 노드 테두리의 스타일입니다. solid, dotted, dashed 또는 double.
        // "border-cap": "butt", // 노드 테두리의 캡 스타일입니다. butt, round 또는 square.
        // "border-join": "miter", // 노드 테두리의 결합 스타일입니다. miter, bevel 또는 round.
        // "border-dash-pattern": [6, 3], // dashed 선과 간격의 교대 길이를 지정하는 선 패턴입니다.
        // "border-dash-offset": 24, // dashed 라인 오프셋입니다. 가장자리 애니메이션을 만드는 데 유용합니다.
        // "border-position": "center", // 노드 경계의 위치입니다. center, inside, outside.
        // "border-opacity": 0.8, // 노드 테두리의 불투명도입니다.

        // 외곽선 스타일
        // "outline-width": "2px", // 노드 외곽선의 크기입니다.
        // "outline-style": "solid", // 노드 외곽선의 스타일입니다. solid, dotted, dashed 또는 double.
        // "outline-color": "#000", // 노드 외곽선의 색상입니다.
        // "outline-opacity": 0.8, // 노드 외곽선의 불투명도입니다.
        // "outline-offset": "2px", // 노드 외곽선의 오프셋입니다.

        // 패딩
        // "padding": "10px", // 노드의 모든 측면 주변의 패딩 양입니다.
        // "padding-relative-to": "width", // 백분율 단위를 사용하는 경우 패딩 계산 방법을 결정합니다. width, height, average, min, max 중 하나.

        // 복합 상위 크기 조정
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

const lineageStyle = [nodeStyle, edgeStyle];

export default lineageStyle;
