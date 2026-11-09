<template>
  <div class="mu-col mu-col-6">
    <div class="mu-heading">
      <h4 v-if="props.nodeField == 'from'">이전 노드 정보</h4>
      <h4 v-if="props.nodeField == 'to'">이후 노드 정보</h4>
      <div class="mu-item-group">
        <button v-if="props.selectedId" @click="emit('addModalShow')" type="button"
          class="mu-btn mu-btn-sm mu-btn-primary">
          <span>추가</span>
        </button>
        <div v-if="selectedData.length > 0" @click="deletenodelist()" class="mu-btn mu-btn-sm">
          <span>삭제</span>
        </div>
        <div v-if="selectedData.length == 0" class="mu-btn mu-btn-sm">
          <span>삭제</span>
        </div>
        <!-- 기본적으로 비활성화노드 정보 영역에 변경 내역 발생 시 활성화되며, 변경 내역 초기화(기존 값으로 초기화)-->
        <button class="mu-btn mu-btn-sm" @click="refreshBtn()" type="button">
          <span>초기화</span>
        </button>
      </div>
    </div>
    <div class="board-list table-scroll h430 slim-scroll">
      <table class="mu-grid">
        <colgroup>
          <col style="width: 40px" />
          <col style="width: auto" span="2" />
          <col style="width: 50px" />
          <col style="width: 80px" />
        </colgroup>
        <thead>
          <tr>
            <th scope="col">
              <div class="mu-checkbox">
                <input type="checkbox" :id="'ckb-' + props.nodeField" title="전체선택" :checked="manualNodeLinks.length > 0 &&
                  selectedData.length === manualNodeLinks.length
                  " :disabled="manualNodeLinks.length == 0" @click.stop="(e) => onToggleCheckbox(e, true)" />
                <label :for="'ckb-' + props.nodeField">
                  <span class="blind">전체선택</span>
                </label>
              </div>
            </th>
            <th scope="col">노드 ID</th>
            <th scope="col">노드 이름</th>
            <th scope="col">상세</th>
            <th scope="col">선택</th>
          </tr>
        </thead>
        <tbody v-if="props.NodeLinks.length > 0">
          <tr v-for="(node, index) in props.NodeLinks" :key="index">
            <td>
              <div class="mu-checkbox">
                <input type="checkbox" :title="isAutoLink(node) ? AUTO_LINK_TITLE : '행선택'" :id="props.nodeField + '-checkbox-' + index"
                  :checked="isChecked(node)" :disabled="isAutoLink(node)" @click.stop="(e) => onToggleCheckbox(e, false, node)" />
                <label :for="props.nodeField + '-checkbox-' + index">
                  <span class="blind">행선택</span>
                </label>
              </div>
            </td>
            <td>
              <span class="mu-ellipsis" :title="node[props.nodeField + '_node_id']">
                {{ node[props.nodeField + "_node_id"] ? node[props.nodeField + "_node_id"] : "-" }}</span>
            </td>
            <td>
              <div style="display: flex; align-items: center; gap: 4px; min-width: 0">
                <!-- 뷰 계보 자동 적재 링크 : 포털에서 삭제 불가 -->
                <span v-if="isAutoLink(node)" class="tag tag-sm tag-round" :title="AUTO_LINK_TITLE"
                  style="flex-shrink: 0; height: 20px; padding: 0 6px; font-size: 11px">자동 연결</span>
                <!-- 뷰 계보 자동 적재 노드 : 노드 정보는 배치가 갱신 (링크 삭제는 가능) -->
                <span v-if="isAutoNode(node)" class="tag tag-sm tag-round" :title="AUTO_NODE_TITLE"
                  style="flex-shrink: 0; height: 20px; padding: 0 6px; font-size: 11px; border-style: dashed; color: #888">자동 노드</span>
                <span class="mu-ellipsis" :title="node[props.nodeField + '_node_name']">
                  {{ node[props.nodeField + "_node_name"] ? node[props.nodeField + "_node_name"] : "-" }}
                </span>
              </div>
            </td>
            <td>
              <div class="mu-tooltip-icon">
                <i :id="'img' + index" class="mu-icon-svg view" @mouseover="showTooltip(index)"
                  @mouseleave="hideTooltip()"></i>
                <div class="mu-tooltip arrow-ct w400" v-if="show && hoveredIndex == index" :style="{
                  position: 'fixed',
                  left: mouseX - 325 + 'px',
                  top: mouseY + 'px',
                  'z-index': 9999,
                }">
                  <div class="mu-tooltip-inner">
                    <div class="data-info2">
                      <dl>
                        <dt>메타 명</dt>
                        <dd v-if="node.tooltip && node.tooltip.node_name">{{ node.tooltip.node_name }}</dd>
                        <dd v-else-if="node.tooltip && node.tooltip.meta_name">{{ node.tooltip.meta_name }}</dd>
                        <dd v-else>-</dd>
                      </dl>
                      <dl>
                        <dt>메타 설명</dt>
                        <dd v-if="node.tooltip && node.tooltip.description">{{ node.tooltip.description }}</dd>
                        <dd v-else>-</dd>
                      </dl>
                      <dl>
                        <dt>DB 명</dt>
                        <dd v-if="node.tooltip && node.tooltip.db_name">{{ node.tooltip.db_name }}</dd>
                        <dd v-else-if="node.tooltip && node.tooltip.qualified_name">{{
                          node.tooltip.qualified_name.split(".")[1] ? node.tooltip.qualified_name.split(".")[1] : "-" }}
                        </dd>
                        <dd v-else>-</dd>
                      </dl>
                      <dl>
                        <dt>DB 종류</dt>
                        <dd v-if="node.tooltip && node.tooltip.type_name">{{ node.tooltip.type_name }}</dd>
                        <dd v-else-if="node.tooltip && node.tooltip.system_type">{{ node.tooltip.system_type }}</dd>
                        <dd v-else>-</dd>
                      </dl>
                      <dl>
                        <dt>연결 출처</dt>
                        <dd>{{ isAutoLink(node) ? "자동 (뷰 계보, 포털에서 삭제 불가)" : "수동 (포털 입력)" }}</dd>
                      </dl>
                      <dl>
                        <dt>노드 출처</dt>
                        <dd>{{ isAutoNode(node) ? "자동 (배치가 메타 정보로 갱신)" : "수동 (포털 입력)" }}</dd>
                      </dl>
                      <dl>
                        <dt>시스템 ID</dt>
                        <dd v-if="node && node.system_id">{{ node.system_id }}</dd>
                        <dd v-else>-</dd>
                      </dl>
                      <dl>
                        <dt>시스템 명</dt>
                        <dd v-if="node && node.system_name">{{ node.system_name }}</dd>
                        <dd v-else>-</dd>
                      </dl>
                    </div>
                  </div>
                </div>
              </div>
            </td>
            <td>
              <button type="button" class="mu-btn mu-btn-sm" @click="emit('selectEntity', node)">
                선택
              </button>
            </td>
          </tr>
        </tbody>
        <tbody v-if="props.NodeLinks.length == 0">
          <tr>
            <td colspan="5">
              <p class="nodata">노드 정보가 없습니다.</p>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
<script setup lang="ts">
import { confirmAlert } from "@functional/alert/alert-default";
import { onMounted, onUnmounted, ref, computed, onUpdated } from 'vue';

const emit = defineEmits([
  "addModalShow",
  "selectEntity",
  "refreshbtn",
  "deleteNodelinks",
]);
const props = defineProps(["NodeLinks", "nodeField", "selectedId"]);

const selectedData = ref([]);
const show = ref(false);
const hoveredIndex = ref(null);
const mouseX = ref(0);
const mouseY = ref(0);

onMounted(() => window.addEventListener("mousemove", handleMousemove));
onUnmounted(() => window.removeEventListener("mousemove", handleMousemove));

const handleMousemove = (event) => {
  mouseX.value = event.clientX;
  mouseY.value = event.clientY + 10;
};

const AUTO_LINK_TITLE = "뷰 계보에서 자동 생성된 연결입니다. 포털에서 삭제할 수 없습니다.";
const AUTO_NODE_TITLE = "뷰 계보에서 자동 관리되는 노드입니다. 노드 정보는 배치가 메타데이터 기준으로 갱신합니다.";

// 자동 적재(AUTO_VIEW 등) 여부. 포털에서 새로 추가해 아직 저장 전인 항목은 origin 이 없으므로 수동으로 본다
const isAutoOrigin = (origin) => !!origin && origin !== "MANUAL";

// 링크 출처
const isAutoLink = (node) => isAutoOrigin(node.origin);

// 목록에 표시되는 상대 노드(이전 목록은 from, 이후 목록은 to)의 출처
const isAutoNode = (node) => isAutoOrigin(node[props.nodeField + "_node_origin"]);

// 삭제 가능한(수동 입력) 링크 목록
const manualNodeLinks = computed(() => props.NodeLinks.filter((node) => !isAutoLink(node)));

const isChecked = (node) => {
  const matched = selectedData.value.find(
    (el) => el[props.nodeField + "_node_id"] === node[props.nodeField + "_node_id"]
  );
  return matched;
};

const showTooltip = (index) => {
  show.value = true;
  hoveredIndex.value = index;
};

const hideTooltip = () => {
  show.value = false;
};

const refreshBtn = () => {
  emit("refreshbtn");
  selectedData.value = [];
};

const deletenodelist = async () => {
  if (await confirmAlert("노드 정보를 삭제합니다. 정말 삭제 하시겠습니까? ")) {
    await emit("deleteNodelinks", selectedData.value);
  }
  selectedData.value = []; // 선택된 데이터 배열 초기화
};

const onToggleCheckbox = (e, isAll, node) => {
  const {
    target: { checked },
  } = e;
  let newItems = [...selectedData.value];
  if (isAll) {
    // 테이블 최상단 체크박스 클릭 시 전체 선택/해제
    newItems = checked ? manualNodeLinks.value : [];
  } else {
    // 각 행별 체크박스 클릭 시
    if (checked) {
      newItems.push(node);
    } else {
      const targetIndex = newItems.findIndex(
        (el) => el[props.nodeField + "_node_id"] === node[props.nodeField + "_node_id"]
      );
      newItems.splice(targetIndex, 1);
    }
  }
  selectedData.value = newItems;
};
</script>
