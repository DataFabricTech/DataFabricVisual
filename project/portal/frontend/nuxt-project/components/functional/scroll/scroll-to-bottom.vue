<template>
  <div>
    <button
      v-show="showButton"
      @click="scrollToBottom"
      class="mu-btn mu-btn-icon-only mu-btn-secondary scroll-to-bottom"
    >
      <i class="mu-icon-svg arrow-bottom"></i><span>이동</span>
    </button>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';

const showButton = ref(false);

const handleScroll = () => {
  const scrollY = window.scrollY;
  const innerHeight = window.innerHeight;
  const scrollHeight = document.documentElement.scrollHeight;
  showButton.value = scrollY + innerHeight < scrollHeight - 1; // 맨 아래로 버튼 숨김
};

const scrollToBottom = () => {
  window.scrollTo({
    top: document.body.scrollHeight,
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
.scroll-to-bottom {
  position: fixed;
  bottom: 95px;
  right: 30px;
  transition: opacity 0.3s ease, visibility 0.3s ease;
  z-index: 999;
}

</style>
