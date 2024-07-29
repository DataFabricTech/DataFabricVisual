<template>
  <div class="interaction-buttons">
    <div class="button-group button-group-sm">
      <input
        type="radio"
        id="button-radio-count"
        class="button-group-input"
        name="button-group2"
        :checked="recommendCount === 1"
        @click="recommendCountAdd"
      />
      <label for="button-radio-count" class="button-group-label">
        <svg-icon class="svg-icon" name="thumbs-up"></svg-icon>
        <span class="count-text">추천</span>
        <span class="count-num">{{ recommendCount }}</span>
      </label>
      <input
        type="radio"
        id="button-radio-count-2"
        class="button-group-input"
        name="button-group2"
        :checked="nonRecommendCount === 1"
        @click="nonRecommendCountAdd"
      />
      <label for="button-radio-count-2" class="button-group-label">
        <svg-icon class="svg-icon" name="thumbs-down"></svg-icon>
        <span class="count-text">비추천</span>
        <span class="count-num">{{ nonRecommendCount }}</span>
      </label>
    </div>
    <div class="checkbox-button checkbox-button-sm">
      <input
        type="checkbox"
        id="checkbox3"
        class="checkbox-button-input"
        :checked="bookmarkCount === 1"
        @click="bookmarkCountAdd"
      />
      <label for="checkbox3" class="checkbox-button-label">
        <svg-icon class="svg-icon" name="tag"></svg-icon>
        <span class="count-text">북마크</span>
        <span class="count-num">{{ bookmarkCount }}</span>
      </label>
    </div>
    <button class="button button-neutral-stroke button-sm" @click="copyLink()">
      <svg-icon class="button-icon" name="copy"></svg-icon>
      링크
    </button>
  </div>
  <!--  &lt;!&ndash;  <resource-box />&ndash;&gt;-->
  <!--  <div>-->
  <!--    <div>소유자</div>-->
  <!--    <div>카테고리</div>-->
  <!--  </div>-->
  <!--  &lt;!&ndash; 추천, 비추천, 북마크, 링크 &ndash;&gt;-->
  <!--  <div>-->
  <!--    <div>-->
  <!--      추천-->
  <!--      <button @click="recommendCountAdd()">버튼</button>{{ recommendCount }}-->
  <!--    </div>-->
  <!--    <div>-->
  <!--      비추천-->
  <!--      <button @click="nonRecommendCountAdd()">버튼</button-->
  <!--      >{{ nonRecommendCount }}-->
  <!--    </div>-->
  <!--    <div>-->
  <!--      북마크-->
  <!--      <button @click="bookmarkCountAdd()">버튼</button>{{ bookmarkCount }}-->
  <!--    </div>-->
  <!--    <div>-->
  <!--      링크-->
  <!--      <button @click="link()">버튼</button>-->
  <!--    </div>-->
  <!--  </div>-->
</template>

<script setup lang="ts">
// import ResourceBox from "@/components/search/common/resource-box.vue";

const recommendCount = ref(0); // 추천수
const nonRecommendCount = ref(0); // 비추천수
const bookmarkCount = ref(0); // 북마크수

function recommendCountAdd() {
  recommendCount.value = recommendCount.value === 1 ? 0 : 1;
  if (nonRecommendCount.value === 1) nonRecommendCount.value = 0;
}
function nonRecommendCountAdd() {
  nonRecommendCount.value = nonRecommendCount.value === 1 ? 0 : 1;
  if (recommendCount.value === 1) recommendCount.value = 0;
}
function bookmarkCountAdd() {
  bookmarkCount.value = bookmarkCount.value === 1 ? 0 : 1;
}
function copyLink() {
  navigator.clipboard.writeText(window.location.href);
}
</script>

<style lang="scss" scoped></style>
