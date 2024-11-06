<template>
  <div class="detail-section">
    <div class="recommend-section">
      <p>이 데이터를 추천하시겠습니까?</p>
      <div class="recommend-group">
        <button
          type="button"
          :class="['mu-btn', 'mu-btn-round', { active: isFavoriteData }]"
          @click="onToggleFavorite"
        >
          <i class="mu-icon-svg heart"></i><span>데이터 찜하기</span>
        </button>
        <button
          type="button"
          :class="['mu-btn', 'mu-btn-round', { active: isRecommendedData }]"
          @click="onToggleRecommend"
        >
          <i class="mu-icon-svg like"></i><span class="blind">추천</span>
          <em class="view-count">{{ recommendedCounts }}</em>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import ObjectUtils from "@/utils/ObjectUtils";

const { $ofetch } = useNuxtApp();
const emit = defineEmits(["reload"]);
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

// 데이터 추천 버튼 클릭 이벤트 핸들러
const onToggleRecommend = () => {
  const params = {
    ...props.params,
    isDeleting: isRecommendedData.value,
  };

  $ofetch(`/api/search/detail/recommend`, {
    method: "POST",
    body: params,
  }).then(() => emit("reload"));
};

// 데이터 찜하기 버튼 클릭 이벤트 핸들러
const onToggleFavorite = () => {
  const params = {
    ...props.params,
    isDeleting: isFavoriteData.value,
  };

  $ofetch(`/api/search/detail/favorite`, {
    method: "POST",
    body: params,
  }).then(() => emit("reload"));
};
</script>
