<template>
  <div class="category-group">
    <!-- 유사 속성 카테고리: 삭제 불가 -->
    <category-label
      v-for="(name, i) in similarAttrCategories"
      :key="`sa-${i}`"
      :name="name"
      :item="item"
      :readOnly="true"
      class="similar-attr-tag"
    />
    <!-- 기존 카테고리-->
    <category-label v-for="(category, cIndex) in categories" :key="cIndex" :category="category" :item="item" />
    <category-control-button buttonType="more" :item="item" />
    <category-control-button v-if="item.id !== -1" buttonType="add" :item="item" />
  </div>
</template>

<script setup lang="ts">
import ObjectUtils from "@utils/ObjectUtils";
import CategoryLabel from "@functional/categoryLabel/category-label";
import CategoryControlButton from "@atoms/metaDataManage/category-control-button";
import { computed } from "vue";

const props = defineProps({
  item: {
    type: Object
  },
  classifications: {
    type: Array,
    default: () => []
  }
});

// 일반 카테고리 라벨 목록 (최대 5개)
const categories = computed(() => {
  const targets = ObjectUtils.getPropertyValue(props.item, "classifications", []);
  targets.sort((a, b) => a - b);
  const tempCategories = JSON.parse(JSON.stringify(targets));
  return tempCategories.length > 5 ? tempCategories.splice(0, 5) : tempCategories;
});

// 유사 속성 카테고리 이름 목록
const similarAttrCategories = computed(() => {
  const val = props.item?.similar_attr_categories;
  if (!val || typeof val !== "string") return [];
  return val.split(",").filter(Boolean);
});
</script>

<style scoped>
.similar-attr-tag {
  background-color: #fef9c3;
}

.similar-attr-tag :deep(.category-label) {
  color: #713f12;
}
</style>
