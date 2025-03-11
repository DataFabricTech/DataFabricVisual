<template>
  <div class="comment-btn">
    <button type="button" :class="['mu-btn', { active: isFavorite }]"
      @click="onToggleUserDescriptionFavorite(favoriteId)">
      <i class="mu-icon-svg like"></i><span class="blind">추천</span>
      <em class="view-count">{{ favoriteCounts }}</em>
    </button>
  </div>
</template>

<script setup lang="ts">
const { $ofetch } = useNuxtApp();
const emit = defineEmits(["reload"]);
const props = defineProps(["favoriteId", "params"]);

const isFavorite = ref(false);
const favoriteCounts = ref(0);

onMounted(() => {
  getUserDescriptionFavoriteStatus(props.favoriteId);
});

watch(
  () => props.favoriteId,
  (newVal, oldVal) => {
    getUserDescriptionFavoriteStatus(props.favoriteId);
  }
);

// 데이터 설명 추천 상태 조회
const getUserDescriptionFavoriteStatus = (favoriteId) => {
  $ofetch(`/api/search/detail/userDescriptionFavoriteStatus/${favoriteId}`).then(
    (res) => {
      isFavorite.value = res.data["isFavorite"];
      favoriteCounts.value = res.data["favoriteCounts"];
    }
  );
};

// 데이터 설명 추천 버튼 클릭 이벤트 핸들러
const onToggleUserDescriptionFavorite = (favoriteId) => {
  const params = {
    ...props.params,
    id: favoriteId,
  };

  // 추천을 한 상태
  if (isFavorite.value) {
    // 추천 정보 삭제
    $ofetch(`/api/search/detail/userDescriptionFavoriteDelete`, {
      method: "POST",
      body: params,
    }).then((res) => {
      isFavorite.value = res.data["isFavorite"];
      favoriteCounts.value = res.data["favoriteCounts"];
      emit("reload", props.params.guid, true);
    });

    // 추천을 하지 않은 상태
  } else {
    // 추천 정보 등록
    $ofetch(`/api/search/detail/userDescriptionFavoriteInsert`, {
      method: "POST",
      body: params,
    }).then((res) => {
      isFavorite.value = res.data["isFavorite"];
      favoriteCounts.value = res.data["favoriteCounts"];
      emit("reload", props.params.guid, true);
    });
  }
};
</script>
