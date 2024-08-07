<template>
  <div class="accordion" :class="{ 'is-accordion-open': isAccordionOpen }">
    <button class="accordion-head" @click="toggleAccordion">
      <span class="accordion-head-title">
        <slot name="title">Accordion Title</slot>
      </span>
      <svg-icon class="accordion-head-icon svg-icon"
                name="chevron-down-medium"></svg-icon>
    </button>
    <transition name="accordion"
                @before-enter="beforeEnter" @enter="enter"
                @before-leave="beforeLeave" @leave="leave">
    <div v-if="isAccordionOpen" class="accordion-body">
      <slot name="body">
          Default body content<br>
          Default body content<br>
          Default body content<br>
          Default body content<br>
          Default body content<br>
          Default body content<br>
          Default body content<br>
      </slot>
    </div>
    </transition>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const isAccordionOpen = ref(false);

const toggleAccordion = () => {
  isAccordionOpen.value = !isAccordionOpen.value;
};

const beforeEnter = (el) => {
  el.style.height = '0';
};

const enter = (el) => {
  el.style.height = el.scrollHeight + 'px';
};

const beforeLeave = (el) => {
  el.style.height = el.scrollHeight + 'px';
};

const leave = (el) => {
  el.style.height = '0';
};


</script>

<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
