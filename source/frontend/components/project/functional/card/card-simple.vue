<template>
  <div class="card card-simple">
    <div class="card-content">
      <div class="h-group justify-between w-full">
        <div class="h-group gap-[8px]">
          <BaseBadge class="bg-marker-cyan">{{ props.model.storageInfo.storageType }}</BaseBadge>
          <BaseBadge class="bg-marker-purple" v-if="props.model?.domain">{{ props.model.domain }}</BaseBadge>
        </div>
      </div>
      <div class="v-group w-full">
        <a href="#" class="card-link" title="이동">{{ props.model.name }}</a>
        <p class="card-detail">{{ props.model.description }}</p>
      </div>
    </div>
    <div class="card-content" v-if="props.hasStatus">
      <div class="update-info">
        <baseBadge :class="isConnected(props.model.status) ? 'bg-marker-cyan' : 'bg-marker-gray'">
          {{ isConnected(props.model.status) ? "Connected" : "Inactive(Disconnected)" }}</baseBadge
        >
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
      },
      status: "CONNECTED"
    })
  }
});

const isConnected = (value: string) => {
  return value === "CONNECTED";
};
</script>
