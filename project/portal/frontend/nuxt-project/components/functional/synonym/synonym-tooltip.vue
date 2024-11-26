<template>
  <div class="mu-tooltip arrow-ct" :style="tooltipPosition">
    <div class="mu-tooltip-inner">
      <div class="data-info2">
        <dl>
          <dt>동의어</dt>
          <dd>{{ synonym }}</dd>
        </dl>
        <dl>
          <dt>등록자</dt>
          <dd>{{ user_nm }}</dd>
        </dl>
        <dl>
          <dt>등록일</dt>
          <dd>{{ $yyyyMMddHHmmSS(create_date) }}</dd>
        </dl>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import ObjectUtils from "@utils/ObjectUtils";

const props = defineProps(['synonymInfo'])

const tooltipPosition = computed(() => {
  const display = ObjectUtils.getPropertyValue(props.synonymInfo, 'targetTag/display', 'none');
  const top = ObjectUtils.getPropertyValue(props.synonymInfo, 'targetTag/e/layerY', 0);
  const left = ObjectUtils.getPropertyValue(props.synonymInfo, 'targetTag/e/layerX', 0);
  return `top: ${top}px; left: ${left}px; display: ${display}`;
})
const synonym = computed(() => ObjectUtils.getPropertyValue(props.synonymInfo, 'synonym', '')) // 툴팁 헤더 이름
const create_user = computed(() => ObjectUtils.getPropertyValue(props.synonymInfo, 'create_user', '')) // 툴팁 내 표시할 등록자 계정
const user_nm = computed(() => ObjectUtils.getPropertyValue(props.synonymInfo, 'user_nm', '')) // 툴팁 내 표시할 등록자명
const create_date = computed(() => ObjectUtils.getPropertyValue(props.synonymInfo, 'create_date', '')) // 툴팁 내 표시할 카테고리 목록

</script>
