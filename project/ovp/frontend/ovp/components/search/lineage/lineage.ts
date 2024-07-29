export interface RawNodeData {
    id: string;
    name: string;
    fqn: string;
}

export interface RawEdgeData {
    fromId: string;
    toId: string;
}

export interface NodeData {
    id: string;
    label: string;
    path: string;
}

export interface EdgeData {
    source: string;
    target: string;
}

export interface CytoscapeElement {
    data: NodeData | EdgeData;
}

export interface CytoscapeNodeStyle {
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

export interface CytoscapeEdgeStyle {
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
