<template>
  <div id="tooltip" class="mu-tooltip-info w360" :style="tooltipPosition">
    <div class="mu-tooltip-head">
      <p>{{ name }}</p>
    </div>

    <div class="mu-tooltip-body">
      <!-- data-info2 -->
      <div class="data-info2">
        <dl>
          <dt>데이터 설명</dt>
          <dd>{{ comment }}</dd>
        </dl>
        <dl>
          <dt>분류</dt>
          <dd>
            <div class="category-group">
              <!-- <div class="category" v-for="(cla, idx) in classifications" :key="idx">
                <span em class="category-label">{{ cla.typeName }}</span>
              </div> -->
              <category-label
                v-for="(cla, idx) in classifications"
                :key="idx"
                :category="cla"
                :item="props.targetNode"
                :readOnly=true
              />
            </div>
          </dd>
        </dl>
      </div>
      <!-- //data-info2 -->
    </div>
  </div>
</template>

<script setup lang="ts">
import ObjectUtils from "@/utils/ObjectUtils";
import CategoryLabel from "@functional/categoryLabel/category-label";

const props = defineProps(["targetNode"]);
// 툴팁 표시 및 위치 스타일
const tooltipPosition = computed(() => {
  const display = ObjectUtils.getPropertyValue(props.targetNode, "display", "none");
  const top = ObjectUtils.getPropertyValue(props.targetNode, "e/layerY", 0);
  const left = ObjectUtils.getPropertyValue(props.targetNode, "e/layerX", 0);

  return `top: ${top}px; left: ${left}px; display: ${display}`;
});

// 툴팁 헤더 이름
const name = computed(() => {
  return ObjectUtils.getPropertyValue(props.targetNode, "attributes/name", "");
});

// 툴팁 내 표시할 데이터 설명
const comment = computed(() => {
  return ObjectUtils.getPropertyValue(props.targetNode, "attributes/comment", "");
});

// 툴팁 내 표시할 카테고리 목록
const classifications = computed(() => {
  return ObjectUtils.getPropertyValue(props.targetNode, "classifications", []);
});
</script>
