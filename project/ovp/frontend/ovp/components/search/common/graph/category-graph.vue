<template>
  <div id="category" class="visual">
    <!-- TODO: [개발] 범례 -->
    <div class="visual-legend">
      <button
        class="button button-lg button-neutral-stroke"
        type="button"
        @click="toggleLegend"
      >
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
        <li class="visual-legend-item visual-legend-item-model">데이터 모델</li>
        <li class="visual-legend-item visual-legend-item-way">
          <div class="edge">
            <div class="edge-line"></div>
          </div>
          노드 관계 방향
        </li>
      </ul>
    </div>
    <!--  TODO: [개발] 페이지 진입 시 모델 리스트 드롭다운이 떠있는 상태임. 처음에는 보여지면 안됨-->
    <drop-down
      v-if="compTypeId"
      :top="top"
      :left="left"
      :compTypeId="compTypeId"
      :nodeId="selectedNodeId"
      @click="onClick"
    />
  </div>
</template>

<script setup lang="ts">
import { CategoryGraph } from "knowledge-graph-canvas";
import { useSearchCommonStore } from "~/store/search/common";
import { NetworkDiagramNodeInfo } from "knowledge-graph-canvas/dist/network-diagram/layout/layout.type";
import dropDown from "./unit/drop-down.vue";
import $constants from "@/utils/constant";
import { useRouter } from "#vue-router";

const router = useRouter();
import _ from "lodash";

const top = ref(200);
const left = ref(200);
const compTypeId = ref("modelList");
const selectedNodeId = ref("");

// lib 에서 노드 정보를 주지 않기 때문에, category id 를 일단 빼둔다.
const getModelNodeList = (node: any): string[] => {
  let result: string[] = [];

  // tagId가 null인 경우 id 추가
  if (node.tagId === null) {
    result.push(node);
  }

  // 자식이 있는 경우 재귀적으로 탐색
  if (node.children && node.children.length > 0) {
    node.children.forEach((child: any) => {
      result = result.concat(getModelNodeList(child));
    });
  }

  return result;
};

const onClick = ({ compTypeId, nodeId }) => {
  console.log("filteredIdAndTagIdData: ", filteredIdAndTagIdData.value);
  const hasDataModelList = _.some(filteredIdAndTagIdData.value, {
    id: nodeId,
  });
  const selectedNode = _.filter(filteredIdAndTagIdData.value, {
    id: nodeId,
  });
  console.log("hasDataModelList: ", hasDataModelList);

  if (compTypeId === $constants.GRAPH.TYPE.MODEL_LIST) {
    // TODO: [확인 필요] 모델 리스트 조회 -> 우측 패널 오픈
  } else {
    // nodeId 기반으로 노드 정보를 찾는다.
    const nodeInfo = _.find(modelNodes, { id: nodeId });
    if (nodeInfo !== undefined) {
      router.push({
        path: "/portal/search/detail",
        query: {
          type: nodeInfo.index,
          id: nodeInfo.id,
          fqn: nodeInfo.fqn,
        },
      });
    } else {
      showGraphModelListMenu.value = true;
      if (hasDataModelList) {
        getModelList(selectedNode[0].tagId);
      } else {
        // TODO: [확인 필요] 기획: 거버넌스>카테고리에서는 자식 요소가 없는 뎁스 | 미분류인 경우에만 모델리스트를 가질 수 있음.
        // 현재 기획서 상에서는 자식 요소가 있는 뎁스에서도 모델리스트 drop-down을 보여주고 있는데, 어떻게 해야할지 문의 필요
        // 임시로 빈값 부여
        graphModelList.value = [];
        console.log("model list가 없습니다.");
      }
    }
  }
};

const isLegendVisible = ref(true);

function toggleLegend() {
  isLegendVisible.value = !isLegendVisible.value;
}

const searchCommonStore = useSearchCommonStore();
const { getGraphData, getModelList, setFilteredIdAndTagIdData } =
  searchCommonStore;
const {
  graphData,
  filteredIdAndTagIdData,
  graphModelList,
  showGraphModelListMenu,
} = storeToRefs(searchCommonStore);

await getGraphData();

const modelNodes = getModelNodeList(graphData.value.nodes);
const modelNodeIds = modelNodes.map((node) => node.id);

const nodeData: any = graphData.value.nodes;
const formattedNodeData: NetworkDiagramNodeInfo = nodeData.children.map(
  (node: any) => ({
    id: node.id,
    name: node.name,
    nodeList: node.children,
    children: [],
  }),
);
const newNode: NetworkDiagramNodeInfo = {
  id: nodeData.id,
  name: nodeData.name,
  children: formattedNodeData,
  nodeList: [],
};

onMounted(() => {
  setFilteredIdAndTagIdData(graphData.value.nodes);

  const categoryContainer = document.getElementById(
    "category",
  ) as HTMLDivElement;

  new CategoryGraph({
    container: categoryContainer,
    categoryData: newNode,
    pixelQuality: "middle",
    eventHandler: {
      onClick: (e: any, id: any, type: any) => {
        if (type === "node" && id) {
          if (modelNodeIds.includes(id)) {
            showGraphModelListMenu.value = false;
          }
          compTypeId.value = modelNodeIds.includes(id)
            ? $constants.GRAPH.TYPE.DETAIL
            : $constants.GRAPH.TYPE.MODEL_LIST;
          selectedNodeId.value = id;
          left.value = e.offsetX;
          top.value = e.offsetY;
        } else {
          compTypeId.value = "";
        }
      },
    },
  });
});
</script>

<style lang="scss" scoped></style>
