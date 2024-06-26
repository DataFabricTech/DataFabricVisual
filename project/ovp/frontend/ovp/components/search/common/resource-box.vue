<template>
  <section>
    <div>서비스 아이콘 영역</div>
    <div>
      <ul>
        <li v-for="(item, index) in depth" :key="index">
          {{ item }}
        </li>
      </ul>
    </div>
    <div>
      <div>최초</div>
      <div>데이터모델 명</div>
      <div>Model name</div>
      <div>데이터 모델 설명에 대한 영역</div>
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
  </section>
</template>

<script setup lang="ts">
const depth = ["1depth", "2depth", "3depth"];
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
