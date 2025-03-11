<template>
  <div class="category-group">
    <auth-label v-for="(obj, cIndex) in auths" :key="cIndex" :auth="obj" :item="props.item" />
    <auth-control-button buttonType="more" :item="props.item" />
  </div>
</template>

<script setup lang="ts">
import authLabel from "@/pages/admin/manager/system/role/auth/auth-label";
import AuthControlButton from "@/pages/admin/manager/system/role/auth/auth-control-button";

import { computed } from 'vue';

const props = defineProps({
  item: {
    type: Object
  },
  classifications: {
    type: Array,
    default: () => {
      return []
    }
  }
});

// 화면 렌더 대상 카테고리 라벨 목록 계산
const auths = computed(() => {
  const tempAuths = props.item.role_ids.length > 5 ? props.item.role_ids.slice(0, 5) : [...props.item.role_ids];
  return tempAuths;
});
</script>
