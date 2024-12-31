<template>
  <div class="mu-col mu-col-6">
    <div class="mu-heading">
      <h4 v-if="props.nodeField == 'from'">이전 노드 정보</h4>
      <h4 v-if="props.nodeField == 'to'">이후 노드 정보</h4>
      <div class="mu-item-group">
        <button
          v-if="props.selectedId"
          @click="emit('addModalShow')"
          type="button"
          class="mu-btn mu-btn-sm mu-btn-primary"
        >
          <span>추가</span>
        </button>
        <div
          v-if="selectedData.length > 0"
          @click="deletenodelist()"
          class="mu-btn mu-btn-sm"
        >
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
                <input
                  type="checkbox"
                  :id="'ckb-' + props.nodeField"
                  title="전체선택"
                  :checked="
                    props.NodeLinks.length > 0 &&
                    selectedData.length === props.NodeLinks.length
                  "
                  @click.stop="(e) => onToggleCheckbox(e, true)"
                />
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
                <input
                  type="checkbox"
                  title="행선택"
                  :id="props.nodeField + '-checkbox-' + index"
                  :checked="isChecked(node)"
                  @click.stop="(e) => onToggleCheckbox(e, false, node)"
                />
                <label :for="props.nodeField + '-checkbox-' + index">
                  <span class="blind">행선택</span>
                </label>
              </div>
            </td>
            <td>
              <span class="mu-ellipsis" :title="node[props.nodeField + '_node_id']">
                {{ node[props.nodeField + "_node_id"] }}</span
              >
            </td>
            <td>
              <span class="mu-ellipsis" :title="node[props.nodeField + '_node_name']">
                {{ node[props.nodeField + "_node_name"] }}
              </span>
            </td>
            <td>
              <div class="mu-tooltip-icon">
                <i
                  :id="'img' + index"
                  class="mu-icon-svg view"
                  @mouseover="showTooltip(index)"
                  @mouseleave="hideTooltip()"
                ></i>
                <div
                  class="mu-tooltip arrow-ct w400"
                  v-if="show && hoveredIndex == index"
                  :style="{
                    position: 'fixed',
                    left: mouseX - 325 + 'px',
                    top: mouseY + 'px',
                    'z-index': 9999,
                  }"
                >
                  <div class="mu-tooltip-inner">
                    <div class="data-info2">
                      <dl>
                        <dt>데이터 이름</dt>
                        <dd>{{ node.tooltip.node_name ? node.tooltip.node_name : node.tooltip.name ? node.tooltip.name : "-" }}</dd>
                      </dl>
                      <dl>
                        <dt>데이터 설명</dt>
                        <dd>{{ node.tooltip.description ? node.tooltip.description : "-" }}</dd>
                      </dl>
                      <dl>
                        <dt>데이터 유형</dt>
                        <dd>{{ node.tooltip.typeName ? node.tooltip.typeName : node.tooltip.metatype ? node.tooltip.metatype : "-" }}</dd>
                      </dl>
                    </div>
                  </div>
                </div>
              </div>
            </td>
            <td>
              <button
                type="button"
                class="mu-btn mu-btn-sm"
                @click="emit('selectEntity', node)"
              >
                선택
              </button>
            </td>
          </tr>
        </tbody>
        <tbody v-if="props.NodeLinks.length == 0">
          <tr>
            <td colspan="5">
              <p class="nodata" >노드 정보가 없습니다.</p>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
<script setup lang="ts">
import { confirmAlert } from "@functional/alert/alert-default";
import { onMounted, onUnmounted, ref, computed } from 'vue';

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
    newItems = checked ? props.NodeLinks : [];
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
