<template>
  <div :class="props.isAdmin ? 'data-box-wrap' : ''" style="padding: 0; margin: 10px 0;">
    <div class="mu-tab-box-2">
      <ul class="mu-tab">
        <li :class="activeTab == index ? 'active' : ''" v-for="(tab, index) in props.tabs" :key="index">
          <a class="mu-switch" @click="scrollToSection(tab, index)" :title="activeTab == index ? '선택됨' : ''">{{
            getKrTabName(tab) }}</a>
        </li>
      </ul>
    </div>
    <div v-if="props.isSticky"
      :class="!props.isAdmin ? 'mu-tab-box-2 fixed-mu-tab-box-2' : 'mu-tab-box-2 fixed-mu-tab-box-3'">
      <ul class="mu-tab">
        <li :class="activeTab == index ? 'active' : ''" v-for="(tab, index) in tabs" :key="index">
          <a class="mu-switch" @click="scrollToSection(tab, index)" :title="activeTab == index ? '선택됨' : ''">{{
            getKrTabName(tab) }}</a>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const emit = defineEmits(['scrollToSection'])
const props = defineProps(['isAdmin', 'tabs', 'isSticky', 'sectionRefs'])

const activeTab = ref(0)

// 특정 섹션으로 스크롤
const scrollToSection = (key, index) => {
  activeTab.value = index
  const params = {
    key: key,
  }
  emit('scrollToSection', params)
};

// 탭 한글명
const getKrTabName = (tab) => {
  if (tab == 'data-info-area') return '데이터 정보'
  if (tab == 'tag-keyword-area') return '태그/키워드'
  if (tab == 'user-description-area') return '사용자 데이터 설명'
  if (tab == 'data-profile-area') return '데이터 프로파일링'
  if (tab == 'sample-data-area') return '샘플 데이터'
  if (tab == 'data-lineage-area') return '데이터 흐름조회'
  if (tab == 'related-data') return '연관 데이터'
  if (tab == 'user-review-area') return '사용자 리뷰'
  else tab
}

</script>

<style scoped></style>
