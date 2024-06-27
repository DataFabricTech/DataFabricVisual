<template>
  <resource-box />
  <div>
    <div>소유자</div>
    <div>카테고리</div>
  </div>
  <!-- 추천, 비추천, 북마크, 링크 -->
  <div>
    <div>
      추천
      <button @click="recommendCountAdd()">버튼</button>{{ recommendCount }}
    </div>
    <div>
      비추천
      <button @click="nonRecommendCountAdd()">버튼</button
      >{{ nonRecommendCount }}
    </div>
    <div>
      북마크
      <button @click="bookmarkCountAdd()">버튼</button>{{ bookmarkCount }}
    </div>
    <div>
      링크
      <button @click="link()">버튼</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import ResourceBox from "@/components/search/common/resource-box.vue";

const recommendCount = ref(0); // 추천수
const nonRecommendCount = ref(0); // 비추천수
const bookmarkCount = ref(0); // 북마크수

function recommendCountAdd() {
  recommendCount.value = recommendCount.value + 1;
}
function nonRecommendCountAdd() {
  nonRecommendCount.value = nonRecommendCount.value + 1;
}
function bookmarkCountAdd() {
  bookmarkCount.value = bookmarkCount.value + 1;
}
function link() {
  // TODO : 상세 URL 클립보드 복사
}
</script>

<style lang="scss" scoped></style>
