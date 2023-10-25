<template>
  <div class="card is-simple">
    <div class="card-content">
      <div class="h-group justify-between w-full">
        <div class="h-group gap-[8px]">
          <BaseBadge class="bg-marker-cyan">HDFS</BaseBadge>
          <BaseBadge class="bg-marker-purple">공간</BaseBadge>
        </div>
      </div>
      <div class="v-group w-full">
        <a href="#" class="card-link" title="이동">불법 주정차 구간 데이터</a>
        <p class="card-detail">서울시에서 수집되고 있는 불법 주정차 차량 단속 이력 정보</p>
      </div>
    </div>
    <div class="card-content" v-if="props.hasStatus">
      <div class="update-info">
        <BaseBadge class="bg-marker-gray">Inactive(Disconnexted)</BaseBadge>
        <span class="update-info-title">수정일자 2023-09-22</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  hasStatus: { type: Boolean, default: false },
});
</script>
