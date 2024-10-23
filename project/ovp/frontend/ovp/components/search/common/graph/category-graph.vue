<template>
  <div class="visual-wrap">
    <div id="category" class="visual"></div>
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
    <drop-down
      v-if="compTypeId && showDropDown"
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
import { ref } from "vue";

const searchCommonStore = useSearchCommonStore();
const { getGraphData, setGraphCategoryPath, setFilteredSearchList } =
  searchCommonStore;
const {
  showDropDown,
  graphCategoryList,
  graphCategoryName,
  graphData,
  showGraphModelListMenu,
} = storeToRefs(searchCommonStore);

const top = ref(200);
const left = ref(200);
const compTypeId = ref("modelList");
const selectedNodeId = ref("");
const isLegendVisible = ref(true);

const toggleLegend = () => {
  isLegendVisible.value = !isLegendVisible.value;
};

const onClick = ({ compId, nodeId }) => {
  // 내가 선택한 id가 카테고리인 경우 실행
  if (compId === $constants.GRAPH.TYPE.MODEL_LIST) {
    showDropDown.value = false;
    showGraphModelListMenu.value = true;
    setFirstNodeName(nodeId);
    setGraphCategoryPath(nodeId);
    setFilteredSearchList(nodeId);
  }
  // 내가 선택한 id가 상세 정보인 경우 실행
  else {
    // nodeId 기반으로 노드 정보를 찾는다.
    const nodeInfo = _.find(modelDetailList, { id: nodeId });
    if (nodeInfo !== undefined) {
      router.push({
        path: "/portal/search/detail",
        query: {
          type: nodeInfo.index,
          id: nodeInfo.id,
          fqn: nodeInfo.fqn,
        },
      });
    }
  }
};

// graph에 들어갈 전체 데이터 정제
const setGraphCategoryList = () => {
  const nodeData: any = graphData.value.nodes;
  const mapNodeWithChildren = (node: any) => ({
    id: node.id,
    parentId: node.parentId,
    tagId: node.tagId,
    name: node.name,
    desc: node.desc,
    children: node.children ? node.children.map(mapNodeWithChildren) : [],
    nodeList: [],
  });

  // nodeData의 자식들을 재귀적으로 처리하여 새로운 children 구조 생성
  const formattedNodeData: NetworkDiagramNodeInfo =
    nodeData.children.map(mapNodeWithChildren);

  graphCategoryList.value = {
    id: nodeData.id,
    name: nodeData.name,
    children: formattedNodeData,
    nodeList: [],
  };
};

// 상세정보로 가는 모델 목록만 추출 (tagId가 null이면 모델 목록임)
const getModelDetailList = (node: any): string[] => {
  let result: string[] = [];

  // tagId가 null인 경우 id 추가
  if (node.tagId === null) {
    result.push(node);
  }

  // 자식이 있는 경우 재귀적으로 탐색
  if (node.children && node.children.length > 0) {
    node.children.forEach((child: any) => {
      result = result.concat(getModelDetailList(child));
    });
  }

  return result;
};

const modelDetailList = getModelDetailList(graphData.value.nodes);
const modelDetailIds = modelDetailList.map((node) => node.id);

// graph 구현
const setCategoryGraph = () => {
  const categoryContainer = document.getElementById(
    "category",
  ) as HTMLDivElement;

  if (!categoryContainer) {
    return;
  }

  categoryContainer.innerHTML = "";

  new CategoryGraph({
    container: categoryContainer,
    categoryData: graphCategoryList.value,
    pixelQuality: "middle",
    isFitScreenInit: true,
    eventHandler: {
      onClick: (e: any, id: any, type: any) => {
        if (!showDropDown.value) {
          showDropDown.value = true;
        }

        if (type === "node" && id) {
          if (modelDetailIds.includes(id)) {
            showGraphModelListMenu.value = false;
          }
          compTypeId.value = modelDetailIds.includes(id)
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
};

// 그래프 카테고리 목록에서 1depth 까지만 탐색해서 선택한 id의 미분류를 찾는다.
const setFirstNodeName = (nodeId: string) => {
  for (const child of graphCategoryList.value.children) {
    if (nodeId === child.id) {
      graphCategoryName.value = child.name;
      return;
    }
  }
};
watch(
  () => graphData.value,
  (newVal) => {
    setGraphCategoryList();
    setCategoryGraph();
  },
  { immediate: true },
);

onMounted(async () => {
  await getGraphData();
  setCategoryGraph();
});
</script>

<style lang="scss" scoped></style>
