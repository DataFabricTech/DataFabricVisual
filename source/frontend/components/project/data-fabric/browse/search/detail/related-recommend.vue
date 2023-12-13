<template>
  <!-- TODO: 퍼블리싱 - 연관/ 추천 갯수 안맞으면 틀어짐 -->
  <article class="category-article">
    <div class="category-subject is-vertical">
      <h3 class="category-title color-primary">연관 / 추천 데이터모델</h3>
      <p class="category-description"></p>
    </div>
    <div class="h-group w-full gap-[60px]">
      <div class="v-group gap-[12px] flex-1">
        <h4 class="page-subtitle">연관 데이터 모델</h4>
        <ul class="card-list" v-if="props.relatedModels">
          <li class="card-item" v-for="(model, index) in props.relatedModels">
            <Card :show-preview-btn="false" :show-statistics="false" :model="model"></Card>
          </li>
        </ul>
        <div class="no-result h-[200px]" v-if="!props.relatedModels">
          <storng class="no-result-title">표시할 데이터 모델이 없습니다.</storng>
        </div>
      </div>
      <div class="v-group gap-[12px] flex-1">
        <h4 class="page-subtitle">추천 데이터 모델</h4>
        <ul class="card-list" v-if="props.recommendedModels">
          <li class="card-item" v-for="(model, index) in props.recommendedModels">
            <Card :show-preview-btn="false" :show-statistics="false" :model="model"></Card>
          </li>
        </ul>
        <div class="no-result h-[200px]" v-if="!props.recommendedModels">
          <storng class="no-result-title">표시할 데이터 모델이 없습니다.</storng>
        </div>
      </div>
    </div>
  </article>
</template>

<script lang="ts" setup>
import { defineProps } from "vue";
import type { DataModel } from "~/components/project/functional/card/dataModel";
const props = defineProps({
  relatedModels: {
    type: Object as () => DataModel
  },
  recommendedModels: {
    type: Object as () => DataModel
  }
});
</script>
