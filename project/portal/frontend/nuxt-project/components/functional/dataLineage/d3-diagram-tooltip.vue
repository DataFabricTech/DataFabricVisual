<template>
  <div id="tooltip" class="mu-tooltip-info w360" :style="tooltipPosition">
    <div class="mu-tooltip-head">
      <p>{{ meta_name }}</p>
    </div>

    <div class="mu-tooltip-body">
      <!-- data-info2 -->
      <div class="data-info2">
        <dl>
          <dt>데이터 설명</dt>
          <dd>{{ description }}</dd>
        </dl>
        <dl>
          <dt>카테고리</dt>
          <dd>
            <div class="category-group" v-if="classifications && classifications.length > 0">
              <div class="category" v-for="(c, idx) in classifications" :key="idx">
                <span em class="category-label">{{ c.c_description }}</span>
              </div>
            </div>
            <div class="category-group" v-else>
              <div class="category">
                <span em class="category-label">카테고리 미지정</span>
              </div>
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

const props = defineProps(["targetNode"]);
// 툴팁 표시 및 위치 스타일
const tooltipPosition = computed(() => {
  const display = ObjectUtils.getPropertyValue(props.targetNode, "display", "none");
  const top = ObjectUtils.getPropertyValue(props.targetNode, "e/layerY", 0);
  const left = ObjectUtils.getPropertyValue(props.targetNode, "e/layerX", 0);

  return `top: ${top}px; left: ${left}px; display: ${display}`;
});

// 툴팁 헤더 이름
const meta_name = computed(() => {
  return ObjectUtils.getPropertyValue(props.targetNode, "meta_name", "");
});

// 툴팁 내 표시할 데이터 설명
const description = computed(() => {
  return ObjectUtils.getPropertyValue(props.targetNode, "description", "-");
});

// 툴팁 내 표시할 카테고리 목록
const classifications = computed(() => {
  return ObjectUtils.getPropertyValue(props.targetNode, "classifications", []);
});
</script>
