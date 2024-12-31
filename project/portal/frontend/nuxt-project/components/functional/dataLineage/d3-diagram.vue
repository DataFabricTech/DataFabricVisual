<template>
  <div ref="roots" id="test">
    <div id="diagram" className="container-border" v-show="showDataLineage" />

    <table class="mu-grid mu-grid-data" v-if="!showDataLineage">
      <thead>
        <tr>
          <th>리니지 데이터가 없습니다.</th>
        </tr>
      </thead>
    </table>

    <d3-diagram-tooltip :target-node="targetNode" />
  </div>
</template>

<script setup lang="ts">
import * as d3 from "d3";
import * as d3Dag from "d3-dag";
import ObjectUtils from "@/utils/ObjectUtils";
import D3DiagramTooltip from "@functional/dataLineage/d3-diagram-tooltip";
import { errorAlert } from "@functional/alert/alert-default";
import { useEventStore  } from "~/store/event.ts";
import { onMounted, onBeforeUnmount, ref } from 'vue';

const roots = shallowRef(null);
const { $ofetch } = useNuxtApp();
const eventStore = useEventStore();

const props = defineProps(["startNode"]);
const firstNodeId = ref(""); // 데이터 흐름도 시작 노드 guid
const data = ref({}); // 데이터 흐름도 렌더 데이터 저장
const nodeInfoList = ref([]); // 툴팁 렌더 시 사용할 데이터 저장
const showDataLineage = ref(true); // 데이터 흐름 구성 렌더 여부 flag
const targetNode = ref({
  // 툴팁 렌더 제어
  display: "none",
  e: null,
});

const treemap = ref(null);
const dragHandler = ref(null);
const svg = ref(null);
const g = ref(null);
const root = ref(null);
const dag = ref(null);
const zoom = ref(null);
const duration = ref(300);
const screen_width = ref(0);
const screen_height = ref(0);
const isCircular = ref(false);

watch(
  () => props.startNode,
  (newVal, oldVal) => {
    firstNodeId.value = newVal;
    data.value = {};
    draw();
  }
);

onMounted(() => {
  // 데이터 흐름 수정 모달 내에서 데이터 수정 후 화면에 반영되도록 event bus에 함수 등록
  eventStore.on('onChangeLineage', redraw);
  initDiagram();
  draw();
});

onBeforeUnmount(() => {
  // event bus에 등록된 함수 제거
  eventStore.off('onChangeLineage', redraw);
});
const draw = async () => {
  if (firstNodeId.value !== "") {
    await getInitData();
    drawInitData();

    // svg.value
    //   .transition()
    //   .duration(duration.value)
    //   .call(zoom.value.transform, d3.zoomTransform(g.value.node()));
    svg.value
      .transition()
      .duration(duration.value)
      .call(zoom.value.transform, d3.zoomIdentity.translate(1, 1).scale(0.88));
  }
};

// 데이터 흐름도 구성 노드 데이터 수정 시 데이터 흐름도 re-render
const redraw = async () => {
  // 렌더에 활용되는 state 초기화
  data.value = {};

  // 이미 그려진 노드 포함 svg 모두 제거
  d3.selectAll("svg").remove();

  initDiagram();
  await draw();
};

const initDiagram = () => {
  // 다이어그램 위치
  screen_width.value = roots.value.offsetWidth;
  if (screen_width.value > 1200) {
    screen_width.value = roots.value.offsetWidth - 62
  }
  screen_height.value = 400;
  const x_sep = 400;
  const y_sep = 100;

  // 객체 드래그 선언
  dragHandler.value = d3.drag().on("drag", dragHandlerEvent);

  // 줌 설정
  zoom.value = d3.zoom().on("zoom", (event) => {
    return g.value.attr("transform", event.transform);
  });

  // 리니지 그려지는 공간 설정
  svg.value = d3
    .select("#diagram")
    .append("svg")
    .attr("width", screen_width.value)
    .attr("height", screen_height.value)
    .call(zoom.value);


  const filterDef = svg.value.append("defs"); // 설정 필터
  const filter_shadow1 = filterDef.append("filter").attr("id", "shadow1");

  filter_shadow1
    .append("feDropShadow")
    .attr("dx", "0")
    .attr("dy", "0")
    .attr("stdDeviation", "4")
    .attr("flood-color", "rgba(72, 106, 108, 0.15)")
    .attr("flood-opacity", "1");

  // 화살표 설정
  const arrow = filterDef
    .append("marker")
    .attr("id", "triangle-downstream")
    .attr("viewBox", "0 0 10 10")
    .attr("refX", "10")
    .attr("refY", "5")
    .attr("markerUnits", "strokeWidth")
    .attr("markerWidth", "5")
    .attr("markerHeight", "5")
    .attr("orient", "auto");

  arrow.append("path").attr("d", "M 0 0 L 10 5 L 0 10 z").attr("fill", "#BFBFBF");

  // let plus = filterDef.append("marker")
  filterDef
    .append("marker")
    .attr("id", "plus-marker")
    .attr("viewBox", "0 0 10 10")
    .attr("refX", "10")
    .attr("refY", "5")
    .attr("markerUnits", "strokeWidth")
    .attr("markerWidth", "5")
    .attr("markerHeight", "5")
    .attr("orient", "auto");

  arrow.append("path").attr("d", "M 2 2 L 8 2 M 2 5 L 8 5 z").attr("fill", "#FFFFFF");

  g.value = svg.value.append("g");

  treemap.value = d3Dag
    .sugiyama()
    .layering(d3Dag.layeringSimplex())
    // .decross(d3Dag.decrossOpt())
    .nodeSize(() => [y_sep, x_sep]);
};

// 초기 데이터 흐름도 구성 노드 조회
const getInitData = async () => {
  const nodeInfo = await $ofetch(`/api/node/all/${firstNodeId.value}`);
  data.value = makeInitData(nodeInfo.data, firstNodeId.value);
};

// 초기 데이터 흐름도 렌더
const drawInitData = () => {
  const links = ObjectUtils.getPropertyValue(data.value, "links", []);

  if (links.length > 0) {
    // 링크 1개 남은 상태에서 링크 삭제 시 에러 발생, 데이터 조회 결과 링크 1개 이상 있을 경우에만 이하 실행
    dag.value = d3Dag.dagConnect()(data.value.links);
    if (dag.value.id !== undefined) {
      const root = dag.value.copy();
      root.id = undefined;
      root.children = [dag.value];
      dag.value = root;
    }

    // 초기 데이터 선언  //최초에는 영역 하나를 잡음
    const all_nodes = dag.value.descendants();

    all_nodes.forEach((n) => {
      n.id = n.data.id;
      n.data = data.value.unions[n.id];
      n.inserted_nodes = [];
      n.inserted_roots = [];
      n.neighbors = [];
      n.inserted_connections = [];
    });

    // find root node and assign data
    root.value = all_nodes.find((n) => n.id === data.value.start);
    root.value.x0 = screen_height.value / 2;
    root.value.y0 = screen_width.value / 2;
    // root.value.x0 = 1000;
    // root.value.y0 = 600;
    dag.value.children = [root.value];
    update(root.value);
  }
};

const dragHandlerEvent = (event, d) => {
  const node = g.value.selectAll(`#node${d.id}`);

  d.x0 = d.x;
  d.y0 = d.y;
  d.x = d.x0 + event.dy;
  d.y = d.y0 + event.dx;
  d.customX = d.x;
  d.customY = d.y;

  node.attr("transform", function (d) {
    return `translate(${d.y}, ${d.x})`;
  });

  const srcLink = g.value.selectAll(`path[srcNode=node${d.id}]`);
  const targetLink = g.value.selectAll(`path[targetNode=node${d.id}]`);

  srcLink.attr("d", function (d) {
    return diagonal(d.source, d.target);
  });

  targetLink.attr("d", function (d) {
    return diagonal(d.source, d.target);
  });
};

// 초기 데이터에 대한 처리
const makeInitData = (responseData, startNodeId) => {
  // const gd = this;

  const nodeList = ObjectUtils.getPropertyValue(responseData, "nodeInfo", []);
  const linkList = ObjectUtils.getPropertyValue(responseData, "linkInfo", []);

  // 데이터 흐름도 영역 렌더 여부
  showDataLineage.value = linkList.length > 0;

  const initData = {
    start: startNodeId,
    persons: {},
    unions: {}, // { id2: { id: "id2", name: "Berta", partner: [], children: ["u1"]  },}  // partner 은 왼쪽 children 은 오른쪽
    links: [], // [["id1", "u1"], ["id2", "u3"]
  };

  let guidList = []

  nodeList.forEach((element) => {
    const { node_id, childcnt, parentcnt, node_type } = element;

    // 노드 추가
    element.id = node_id;
    initData.unions[element.id] = element;

    // 툴팁 렌더 대상일 경우 조회 파라미터 추가
    if (!node_type == 1) {
      guidList.push(node_id)
    }

    element.children = linkList
      .filter(
        (link) =>
          link.from_node_id === node_id && hasNode(initData.unions, link.to_node_id)
      )
      .map((v) => v.to_node_id);

    element.partner = linkList
      .filter(
        (link) =>
          link.to_node_id === node_id && hasNode(initData.unions, link.from_node_id)
      )
      .map((v) => v.from_node_id);

    if (!childcnt || !parentcnt) {
      element.direction = "";
    }
  });

  linkList.forEach((element) => {
    const { from_node_id, to_node_id } = element;

    if (initData.unions[from_node_id] && initData.unions[to_node_id]) {
      initData.links.push([from_node_id, to_node_id]);
    }
  });

  if (guidList.length > 0) {
    // 툴팁 렌더용 데이터 조회
    getMetaDatasForTooltip(guidList);
  }
  return initData;
};

// 툴팁 랜더용 데이터 조회
const getMetaDatasForTooltip = async (guidList) => {
  const response = await $ofetch('/api/node/getMetaDataForTooltip', {
    method: 'POST',
    body: guidList
  })
  const nodeInfo = ObjectUtils.getPropertyValue(response, "data", [])
  nodeInfoList.value.push(...nodeInfo)
}

// 노드 id에 대한 node_info 존재 여부
const hasNode = (nodeMap, node_id) => {
  return nodeMap[node_id] ? true : false;
};

// 노드 클릭 이벤트
const rectClickHandler = (event, node) => {
  const nodes = dag.value.descendants();
  nodes.forEach(function (d) {
    d.selected = false;
  });
  node.selected = true;
  update(root.value);
};

// 노드 확장 버튼 (+ 모양) 클릭 이벤트 핸들러
const expandClick = async (event, node) => {
  const leftExpandClick = node.data.children.length > 0; // 왼쪽 확장 버튼 (+ 모양)
  const rightExpandClick = node.data.partner.length > 0; // 오른쪽 확장 버튼 (+ 모양)

  // const gd = this;
  const {
    id,
    data: { direction },
  } = node;

  if (!direction) return
  const res = await $ofetch(`/api/node/${direction}/${id}`);
  const linkList = ObjectUtils.getPropertyValue(res.data, "linkInfo", []);
  const nodeList = ObjectUtils.getPropertyValue(res.data, "nodeInfo", []);

  // 순환 여부
  let notCircle = true;

  linkList.forEach(async (element) => {
    if (leftExpandClick) {
      // 왼쪽 확장 버튼
      for (let key in data.value.unions) {
        if (element.from_node_id == key) {
          await errorAlert("데이터가 순환되어 오류가 발생합니다.");
          notCircle = false;
          break;
        }
        if (!notCircle) {
          break;
        }
      }
    } else if (rightExpandClick) {
      // 오른쪽 확장 버튼
      for (let key in data.value.unions) {
        if (element.to_node_id == key) {
          await errorAlert("데이터가 순환되어 오류가 발생합니다.");
          notCircle = false;
          break;
        }
        if (!notCircle) {
          break;
        }
      }
    }
  });

  if (notCircle) {
    let guidList = []; // 툴팁 렌더용 데이터 조회 시 사용할 파라미터 가공
    nodeList.forEach((element) => {
      const { node_id, childcnt, parentcnt, node_type, children = [] } = element;

      // 노드 추가
      element.id = node_id;
      data.value.unions[element.id] = element;

      // 툴팁 렌더 대상일 경우 조회 파라미터 추가
      if (!node_type == 1) {
        guidList.push(node_id)
      }

      //데이터 생성
      element.children = linkList
        .filter(
          (link) =>
            link.from_node_id === node_id && hasNode(data.value.unions, link.to_node_id)
        )
        .map((v) => v.to_node_id);

      element.partner = linkList
        .filter(
          (link) =>
            link.to_node_id === node_id + "" &&
            hasNode(data.value.unions, link.from_node_id)
        )
        .map((v) => v.from_node_id);

      if (direction === "child") {
        element.direction =
          childcnt !== 0 && childcnt !== children.length ? direction : "";
        element.partner.forEach((pid) => {
          data.value.unions[pid].direction = "";
        });
        // 파트너쪽에 차일드 추가
      } else {
        element.direction =
          parentcnt !== 0 && parentcnt !== parentcnt.length ? direction : "";
        element.children.forEach((cid) => {
          data.value.unions[cid].direction = "";
        });
      }
    });

    // 링크 추가
    linkList.forEach((element) => {
      const { from_node_id, to_node_id } = element;

      if (data.value.unions[from_node_id] && data.value.unions[to_node_id]) {
        data.value.links.push([from_node_id, to_node_id]);
      }
    });

    if (guidList.length > 0) {
      // 툴팁 렌더용 데이터 조회
      getMetaDatasForTooltip(guidList);
    }
    node.data.direction = "";
    drawInitData();
  }
};

const diagonal = (s, d) => {
  const offsetX = 20;
  const offsetY = 150;
  const lineWidth = 10;
  let path = "";

  if (s.y + offsetY + 160 < d.y) {
    path = `M ${s.y + offsetY + 50} ${s.x + offsetX}

      C ${(s.y + d.y) / 2} ${s.x + offsetX},
      ${(s.y + d.y) / 2} ${d.x + offsetX},
      ${d.y - offsetY - lineWidth + 50} ${d.x + offsetX}

      L ${d.y - offsetY + 50} ${d.x + offsetX}
        `;
  } else {
    const distinctY = s.y + 100 - d.y + 30; // 기본 범위 + 노드 넓이 + 화살표 패딩

    path = `M ${s.y + offsetY + 50} ${s.x + offsetX}

      C ${s.y + offsetY + lineWidth + 160} ${s.x + offsetX},
      ${s.y - offsetY - lineWidth - distinctY} ${d.x + offsetX},
      ${d.y - offsetY - lineWidth} ${d.x + offsetX}

      L ${d.y - offsetY + 50} ${d.x + offsetX}
      `;
  }

  return path;
};

const update = (source) => {
  // Assigns the x and y position for the nodes
  treemap.value(dag.value);
  const nodes = dag.value.descendants();
  const links = dag.value.links();
  // ****************** Nodes section ***************************
  // Update the nodes...
  const node = g.value.selectAll("g.nodeGroup").data(nodes, function (d) {
    return d.id || "blankNode";
  });
  // Enter any new nodes at the parent's previous position.
  const nodeEnter = node
    .enter()
    .append("g")
    .attr("class", (d) => {
      let iconTypeNum = "3";

      if (firstNodeId.value === d.id) {
        iconTypeNum = "1";
      }

      if (d.data.node_type == 1) {
        iconTypeNum = "4";
      }

      return `nodeGroup node-type${iconTypeNum}`;
    })
    .attr("id", (d) => `node${d.id}`)
    .attr("transform", `translate(${source.y0}, ${source.x0})`)
    .attr("visible", true);

  dragHandler.value(nodeEnter);

  // Add Circle for the nodes  확장 원
  const expandBtn = nodeEnter
    .append("g")
    .attr("class", "expandBtn")
    .attr("transform", "scale(0.00000004 0.00000004)")
    .style("fill", "#BFBFBF");

  // 노드 확장 이벤트 핸들러 바인딩
  expandBtn.on("click", expandClick);

  expandBtn.append("circle").attr("r", 0.1).attr("cx", 0).attr("cy", 0);

  expandBtn
    .append("path")
    .attr(
      "d",
      "M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm192 472c0 4.4-3.6 8-8 8H544v152c0 4.4-3.6 8-8 8h-48c-4.4 0-8-3.6-8-8V544H328c-4.4 0-8-3.6-8-8v-48c0-4.4 3.6-8 8-8h152V328c0-4.4 3.6-8 8-8h48c4.4 0 8 3.6 8 8v152h152c4.4 0 8 3.6 8 8v48z"
    );

  // 노드 클릭 이벤트 핸들러 바인딩
  nodeEnter.on("click", rectClickHandler);

  // 노드 배경 설정
  nodeEnter
    .append("rect")
    .attr("class", "node-body")
    .attr("y", -20)
    .attr("x", -100)
    .attr("rx", 40)
    .on("mouseover", (e, d) => showTooltip(e, d))
    .on("mouseout", hideTooltip);

  // 이미지 추가
  nodeEnter
    .append("image")
    .attr("href", function (d) {
      let nodeImage =
        "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='50' height='50' viewBox='0 0 50 50' x='-90' y='-10'%3E%3Cg transform='translate(-20.052 -16)'%3E%3Crect width='50' height='50' rx='25' transform='translate(20.052 16)' style='fill:%23f3f9f9'/%3E%3Cpath d='M20 21v-4h8v4zm-10 0v-4h8v4zM0 21v-4h8v4zm20-6v-4h8v4zm-10 0v-4h8v4zM0 15v-4h8v4zm20-6V5h8v4zM10 9V5h8v4zM0 9V5h8v4zm20-6v-4h8v4zM10 3v-4h8v4zM0 3v-4h8v4z' transform='translate(31.052 31)' style='fill:%2344a5a1'/%3E%3C/g%3E%3C/svg%3E";
      if (firstNodeId.value === d.id) {
        nodeImage =
          "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='50' height='50' viewBox='0 0 50 50'%3E%3Cg transform='translate(-20.052 -16)'%3E%3Crect width='50' height='50' rx='25' transform='translate(20.052 16)' style='fill:%23fdf4f1'/%3E%3Cpath d='M20 21v-4h8v4zm-10 0v-4h8v4zM0 21v-4h8v4zm20-6v-4h8v4zm-10 0v-4h8v4zM0 15v-4h8v4zm20-6V5h8v4zM10 9V5h8v4zM0 9V5h8v4zm20-6v-4h8v4zM10 3v-4h8v4zM0 3v-4h8v4z' transform='translate(31.052 31)' style='fill:%23f15a24'/%3E%3C/g%3E%3C/svg%3E";
      }

      if (d.data.node_type == 1) {
        nodeImage =
          "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='50' height='50' viewBox='0 0 50 50' x='-90' y='-10'%3E%3Cg transform='translate(-20.052 -16)'%3E%3Crect width='50' height='50' rx='25' transform='translate(20.052 16)' style='fill:%23f5f5f6'/%3E%3Cpath d='M20 21v-4h8v4zm-10 0v-4h8v4zM0 21v-4h8v4zm20-6v-4h8v4zm-10 0v-4h8v4zM0 15v-4h8v4zm20-6V5h8v4zM10 9V5h8v4zM0 9V5h8v4zm20-6v-4h8v4zM10 3v-4h8v4zM0 3v-4h8v4z' transform='translate(31.052 31)' style='fill:%23656d78'/%3E%3C/g%3E%3C/svg%3E";
      }

      return nodeImage;
    })
    .attr("width", 32)
    .attr("height", 32)
    .attr("x", -84)
    .attr("y", 0)
    .on("mouseover", (e, d) => showTooltip(e, d))
    .on("mouseout", hideTooltip);

  // Add names as node labels
  let nameDy = 8
  nodeEnter
    .append("text")
    .attr("dy", (d) => (d.data.node_type === 1 ? 20 : nameDy))
    .attr("x", -35)
    .attr("text-anchor", "start")
    .text((d) => {
      // return d.data.node_name
      // 길이가 긴 경우 .. 표기
      const maxByte = 29;
      if (d.data && d.data.node_name) {
        if (getTextByteLength(d.data.node_name) > maxByte) {
          return `${getLimitedByteText(d.data.node_name, maxByte)}..`;
        } else {
          return d.data.node_name;
        }
      }
    })
    .on("mouseover", (e, d) => showTooltip(e, d))
    .on("mouseout", hideTooltip)
    .on("mousedown", function (e) {
      // 텍스트 클릭시 복사 가능하게
      e.stopPropagation();
    });

  nodeEnter
    .append("text")
    .attr("dy", 33)
    .attr("x", -35)
    .attr("text-anchor", "start")
    .text((d) => {
      // return d.data.node_name
      // 길이가 긴 경우 .. 표기
      const maxByte = 29;
      if (d.data && d.data.description && d.data.node_type != 1) {
        if (getTextByteLength(d.data.description) > maxByte) {
          return `${getLimitedByteText(d.data.description, maxByte)}..`;
        } else {
          return d.data.description;
        }
      }
      else {
        if (d.data.node_type != 1) return '미지정 한글명';
      }
    })
    .on("mouseover", (e, d) => showTooltip(e, d))
    .on("mouseout", hideTooltip)
    .on("mousedown", function (e) {
      // 텍스트 클릭시 복사 가능하게
      e.stopPropagation();
    });

  // Add names as node title
  nodeEnter.append("title").text((d) => {
    return d.data.node_type == 1 ? d.data.node_name : null;
  });

  // UPDATE
  const nodeUpdate = nodeEnter.merge(node);

  // Transition to the proper position for the node
  nodeUpdate
    .transition()
    .duration(duration.value)
    .attr("transform", function (d) {
      if (d.customX && d.customY) {
        d.x = d.customX;
        d.y = d.customY;
      }
      return `translate(${d.y}, ${d.x})`;
    });

  nodeUpdate.select("rect.node-body").style("stroke", (node) => {
    return node.selected ? "#1890FF" : "rgba(192, 190, 190, 0.25)";
  });

  // Update the node attributes and style
  nodeUpdate
    .select("g.expandBtn")
    .attr("transform", (d) => {
      if (!d.data.direction) {
        return "scale(0.0000001 0.0000001)";
      }

      const xValue = d.data.direction === "child" ? 210 : -150; // (+) 버튼 가로 위치 (왼쪽 : 오른쪽)
      return `translate(${xValue} -5) scale(0.04 0.04)`;
    })
    .style("fill", "#BFBFBF")
    .attr("cursor", "pointer");

  // (+) 버튼 : 클릭 이벤트 수신 위해 선언
  nodeUpdate
    .select("g.expandBtn circle")
    .attr("r", 400)
    .attr("cx", 500)
    .attr("cy", 500)
    .attr("cursor", "pointer");

  // Remove any exiting nodes
  const nodeExit = node
    .exit()
    .transition()
    .duration(duration.value)
    .attr("transform", `translate(${source.y}, ${source.x})`)
    .attr("visible", false)
    .remove();

  // On exit reduce the node circles size to 0
  nodeExit.select("circle").attr("r", 1e-6);

  // On exit reduce the opacity of text labels
  nodeExit.select("text").style("fill-opacity", 1e-6);

  // ****************** links section ***************************

  // Update the links...
  const link = g.value.selectAll("path.link").data(links, function (d) {
    return d.source.id + d.target.id;
  });

  // Enter any new links at the parent's previous position.
  const linkEnter = link
    .enter()
    .insert("path", "g")
    .attr("class", "link")
    .attr("marker-end", "url(#triangle-downstream)")
    .attr("srcNode", function (d) {
      return "node" + d.source.id;
    })
    .attr("targetNode", function (d) {
      return "node" + d.target.id;
    })
    .attr("d", function (d) {
      const o = { x: source.x0, y: source.y0 };
      return diagonal(o, o);
    });

  // UPDATE
  const linkUpdate = linkEnter.merge(link);

  // Transition back to the parent element position
  linkUpdate
    .transition()
    .duration(duration.value)
    .attr("d", (d) => diagonal(d.source, d.target));

  // Remove any exiting links
  link
    .exit()
    .transition()
    .duration(duration.value)
    .attr("d", function (d) {
      var o = { x: source.x, y: source.y };
      return diagonal(o, o);
    })
    .remove();
};

// 노드 mouseover 이벤트 핸들러
const showTooltip = (e, node) => {
  const { id } = node;

  const matched = nodeInfoList.value.find((el) => el.id === id);
  if (matched) {
    matched.display = "block";
    matched.e = e;
    targetNode.value = matched;
  }
};

// 노드 mouseout 이벤트 핸들러
const hideTooltip = () => {
  targetNode.value = {
    display: "none",
    e: null,
  };
};

//문장 byte 구하기
const getTextByteLength = (str) => {
  var len = 0;
  for (var i = 0; i < str.length; i++) {
    if (escape(str.charAt(i)).length == 6) {
      len++;
    }
    len++;
  }
  return len;
};

//글자 byte 구하기
const getLetterByteLength = (decimal) => {
  const LINE_FEED = 10;
  return decimal >> 7 || LINE_FEED === decimal ? 2 : 1;
};

// node_name byte 기준으로 자르기
const getLimitedByteText = (inputText, maxByte) => {
  const characters = inputText.split("");
  // const characters = "aa_aa_aaa_aaa_aaa_aaaaaa_aaaaaa_aa".split('')
  let validText = "";
  let totalByte = 0;

  for (let i = 0; i < characters.length; i += 1) {
    const character = characters[i];
    const decimal = character.charCodeAt(0);
    const byte = getLetterByteLength(decimal); // 글자 한 개가 몇 바이트 길이인지 구해주기

    // 현재까지의 바이트 길이와 더해 최대 바이트 길이를 넘지 않으면
    if (totalByte + byte <= maxByte) {
      totalByte += byte; // 바이트 길이 값을 더해 현재까지의 총 바이트 길이 값을 구함
      validText += character; // 글자를 더해 현재까지의 총 문자열 값을 구함
    } else {
      break;
    }
  }

  return validText;
};
</script>
<style>
#diagram {
  border: 1px solid #dddddd;
  background-color: #fafafa;
}

#diagram circle {
  fill: #fff;
  stroke: #99cc00;
  stroke-width: 1px;
}

#diagram .node text {
  font: 12px sans-serif;
}

#diagram .link {
  fill: none;
  stroke: #ccc;
  stroke-width: 2px;
}
</style>
