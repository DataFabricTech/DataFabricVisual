<template>
  <div>
    <button
      v-show="showButton"
      @click="scrollToTop"
      class="mu-btn mu-btn-icon-only mu-btn-secondary scroll-to-top"
    >
      <i class="mu-icon-svg arrow-top"></i><span>이동</span>
    </button>
    <button type="button" class="mu-btn mu-btn-icon-only mu-btn-secondary" title="멘위로 이동"><i class="mu-icon-svg arrow-top"></i><span>이동</span></button>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';

const showButton = ref(false);

const handleScroll = () => {
  showButton.value = window.scrollY > 200; // 스크롤 위치가 200px 이상일 때 버튼 표시
};

const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth',
  });
};

onMounted(() => {
  window.addEventListener('scroll', handleScroll);
});

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll);
});

</script>

<style scoped>
.scroll-to-top {
  position: fixed;
  bottom: 40px;
  right: 40px;
  transition: opacity 0.3s ease, visibility 0.3s ease;
}

/* .scroll-to-top[v-cloak] {
  display: none;
} */
</style>
