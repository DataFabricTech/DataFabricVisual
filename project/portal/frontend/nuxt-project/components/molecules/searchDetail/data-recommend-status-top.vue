<template>
  <div class="recommend-group">
    <button type="button" title="데이터 찜하기" :class="['mu-btn', { active: isFavoriteData }]" @click="onToggleFavorite">
      <i class="mu-icon-svg heart"></i>
      <em class="view-count">찜</em>
    </button>
    <button type="button" title="데이터 추천" :class="['mu-btn', { active: isRecommendedData }]" @click="onToggleRecommend">
      <i class="mu-icon-svg like"></i>
      <em class="view-count">{{ recommendedCounts }}</em>
    </button>
  </div>
</template>

<script setup lang="ts">
import ObjectUtils from "@/utils/ObjectUtils";

const { $ofetch } = useNuxtApp();
const emit = defineEmits(["reloadFavorite", "reloadRecommend"]);
const props = defineProps(["data", "params"]);

const isFavoriteData = computed(() =>
  ObjectUtils.getPropertyValue(props.data, "isFavored", false)
); // 데이터 찜하기 선택 여부
const isRecommendedData = computed(() =>
  ObjectUtils.getPropertyValue(props.data, "isRecommended", false)
); // 데이터 추천 여부
const recommendedCounts = computed(() =>
  ObjectUtils.getPropertyValue(props.data, "counts", 0)
); // 데이터 추천수

// 데이터 찜하기 버튼 클릭 이벤트 핸들러
const onToggleFavorite = () => {
  const params = {
    ...props.params,
    isDeleting: isFavoriteData.value,
  };

  $ofetch(`/api/search/detail/favorite`, {
    method: "POST",
    body: params,
  }).then(() => emit("reloadFavorite"));
};

// 데이터 추천 버튼 클릭 이벤트 핸들러
const onToggleRecommend = () => {
  const params = {
    ...props.params,
    isDeleting: isRecommendedData.value,
  };

  $ofetch(`/api/search/detail/recommend`, {
    method: "POST",
    body: params,
  }).then(() => emit("reloadRecommend"));
};
</script>
