<template>
  <div class="card card-simple">
    <div class="card-content">
      <div class="h-group justify-between w-full">
        <div class="h-group gap-[8px]">
          <BaseBadge class="bg-marker-cyan">{{ model.storageInfo.storageType }}</BaseBadge>
          <BaseBadge class="bg-marker-purple">{{ model.domain }}</BaseBadge>
        </div>
      </div>
      <div class="v-group w-full">
        <a href="#" class="card-link" title="이동">{{ model.name }}</a>
        <p class="card-detail">{{ model.description }}</p>
      </div>
    </div>
    <div class="card-content" v-if="props.hasStatus">
      <div class="update-info">
        <BaseBadge class="bg-marker-gray">Inactive(Disconnexted)</BaseBadge>
        <span class="update-info-title">수정일자 {{ model.updatedAt }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps } from "vue/dist/vue";
const props = defineProps({
  hasStatus: {
    type: Boolean,
    default: true
  },
  model: {
    type: Object,
    default: {
      id: 111,
      name: "불법 주정차 구간 데이터",
      description: "서울시에서 수집되고 있는 불법 주정차 차량 단속 이력 정보",
      tags: ["tag1", 'tag2', 'tag3', 'tag4', 'tag5'],
      storageInfo: {
        storageType: "HDFS"
      },
      domain: "공간",
      updatedAt: "2023-09-22",
    }
  }
});
</script>
