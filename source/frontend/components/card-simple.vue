<template>
  <div class="card card-simple">
    <div class="card-content">
      <div class="h-group justify-between w-full">
        <div class="h-group gap-[8px]">
          <BaseBadge class="bg-marker-cyan">{{ props.model.storageInfo.storageType }}</BaseBadge>
          <BaseBadge class="bg-marker-purple">{{ props.model.domain }}</BaseBadge>
        </div>
      </div>
      <div class="v-group w-full">
        <a href="#" class="card-link" title="이동">{{ props.model.name }}</a>
        <p class="card-detail">{{ props.model.description }}</p>
      </div>
    </div>
    <div class="card-content" v-if="props.hasStatus">
      <div class="update-info">
        <!-- TODO 데이터 연결상태 리턴값에 따라 클래스 및 명칭 변경 -->
        <BaseBadge class="bg-marker-gray">Inactive(Disconnexted)</BaseBadge>
        <span class="update-info-title">수정일자 {{ props.model.updatedAt }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  hasStatus: {
    type: Boolean,
    default: true
  },
  model: {
    type: Object,
    default: () => ({
      name: '불법 주정차 구간 데이터',
      description: '서울시에서 수집되고 있는 불법 주정차 차량 단속 이력 정보',
      updatedAt: '2023-09-22',
      domain: '공간',
      storageInfo: {
        storageType: 'HDFS'
      }
    })
  }
});
</script>
