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
const {
  getGraphData,
  setGraphCategoryPath,
  setFilteredSearchList,
  onlyGraphCategoryList,
} = searchCommonStore;
const {
  showDropDown,
  graphCategoryList,
  graphData,
  showGraphModelListMenu,
  lowestCategoryId,
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
    setLowestCategoryId(nodeId);
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
const setOnlyGraphCategoryList = () => {
  const nodeData: any = graphData.value.nodes;

  // tagId가 null인 경우 해당 객체를 포함하지 않음
  const mapNodeWithChildren = (node: any) => {
    if (node.tagId === null) return null; // tagId가 null이면 제외

    return {
      id: node.id,
      tagId: node.tagId,
      children: node.children
        ? node.children
            .map(mapNodeWithChildren) // 자식 노드도 재귀적으로 처리
            .filter((child) => child !== null) // null인 자식은 제외
        : [],
    };
  };

  // nodeData의 자식들을 재귀적으로 처리하여 새로운 children 구조 생성
  const formattedNodeData = nodeData.children
    .map(mapNodeWithChildren)
    .filter((child) => child !== null); // null인 노드를 제외

  onlyGraphCategoryList.value = {
    id: nodeData.id,
    children: formattedNodeData,
  };
};

// 내가 클릭한 id의 최하위 뎁스 categroy id 추출
// TODO: [개발] 가지가 두 개 이상으로 뻗어 나갈 때 따로 따로 저장해야함..
const setLowestCategoryId = (nodeId: string) => {
  // root 카테고리와 비교
  if (graphCategoryList.value.id === nodeId) {
    return;
  }

  // 특정 카테고리에서 자식 노드 중 가장 깊은 leaf 노드를 찾는 함수
  const findLowestChildId = (category) => {
    while (category.children && category.children.length > 0) {
      category = category.children[0]; // 첫 번째 자식으로 계속 내려감
    }
    return category.id;
  };

  // 1단계 카테고리에서 목표 노드 찾기
  for (const element of onlyGraphCategoryList.value.children) {
    if (element.id === nodeId) {
      // 자식이 없으면 바로 해당 카테고리 반환
      if (element.children.length === 0) {
        lowestCategoryId.value = element.id;
        return;
      }
      // 자식이 있으면 자식 중 가장 아래 노드 설정
      lowestCategoryId.value = findLowestChildId(element);
      return;
    }

    // 2단계 카테고리에서 목표 노드 찾기
    for (const child of element.children) {
      if (child.id === nodeId) {
        if (child.children.length === 0) {
          lowestCategoryId.value = child.id;
          return;
        }
        lowestCategoryId.value = findLowestChildId(child);
        return;
      }

      // 3단계 카테고리에서 목표 노드 찾기
      for (const grandChild of child.children) {
        if (grandChild.id === nodeId) {
          lowestCategoryId.value = grandChild.id;
          return;
        }
      }
    }
  }
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

watch(
  () => graphData.value,
  (newVal) => {
    setGraphCategoryList();
    setOnlyGraphCategoryList();
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
