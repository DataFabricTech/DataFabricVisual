<template>
  <div class="category-group">
    <category-label v-for="(category, cIndex) in categories" :key="cIndex" :category="category" :item="item" />
    <category-control-button buttonType="more" :item="item" />
    <category-control-button v-if="item.id !== -1" buttonType="add" :item="item" />
  </div>
</template>

<script setup lang="ts">
import ObjectUtils from "@utils/ObjectUtils";

import CategoryLabel from "@functional/categoryLabel/category-label";
import CategoryControlButton from "@atoms/metaDataManage/category-control-button";

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
const categories = computed(() => {
  const targets = ObjectUtils.getPropertyValue(props.item, 'classifications', []);
  targets.sort((a, b) => a - b) // 카테고리 표기 정렬
  const tempCategories = JSON.parse(JSON.stringify(targets));
  // 카테고리 라벨 개수가 5개 초과 시 테이블 내에는 5개만 렌더, 나머지는 더보기 툴팁에 렌더
  return tempCategories.length > 5 ? tempCategories.splice(0, 5) : tempCategories;
});
</script>
