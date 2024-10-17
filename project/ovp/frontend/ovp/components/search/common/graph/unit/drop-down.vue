<template>
  <div
    :id="props.compId"
    class="dropdown"
    :style="{ top: props.top + 'px', left: props.left + 'px' }"
  >
    <ul class="dropdown-list">
      <li class="dropdown-item">
        <button
          class="dropdown-button"
          @click="emit('click', { compId: props.compTypeId, nodeId: nodeId })"
        >
          <svg-icon
            v-if="dropdownType[props.compTypeId].svgIconName"
            class="svg-icon"
            :name="dropdownType[props.compTypeId].svgIconName"
          ></svg-icon>
          <span class="dropdown-text">
            {{ dropdownType[props.compTypeId].text }}</span
          >
        </button>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { defineEmits, defineProps } from "vue";
import $constants from "@/utils/constant";

const dropdownType = {
  [$constants.GRAPH.TYPE.MODEL_LIST]: {
    text: "모델 리스트",
    svgIconName: "model-list",
  },
  [$constants.GRAPH.TYPE.DETAIL]: {
    text: "상세정보",
    svgIconName: "",
  },
};

const props = defineProps({
  compId: {
    type: String,
    default: "dropdownComp01",
  },
  compTypeId: {
    type: String,
    required: true,
    default: "modelList",
  },
  nodeId: {
    type: String,
    required: true,
  },
  top: {
    type: Number,
    required: true,
  },
  left: {
    type: Number,
    required: true,
  },
});

const emit = defineEmits<{
  (e: "click", data: object): void;
}>();
</script>

<style scoped></style>
