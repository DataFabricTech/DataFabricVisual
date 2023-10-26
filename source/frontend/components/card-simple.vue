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
        <!-- TODO 데이터 연결상태 리턴값에 따라 클래스 및 명칭 변경 -->
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
    type: Object
  }
});
</script>
