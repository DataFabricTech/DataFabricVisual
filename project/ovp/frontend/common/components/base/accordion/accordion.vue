<template>
  <div class="accordion" :class="{ 'is-accordion-open': isAccordionOpen }">
    <button class="accordion-head" @click="toggleAccordion">
      <span class="accordion-head-title">
        <slot name="title">Accordion Title</slot>
      </span>
      <svg-icon class="accordion-head-icon svg-icon"
                name="chevron-down-medium"></svg-icon>
    </button>
    <div v-if="isAccordionOpen" class="accordion-body">
      <slot name="body">Default body content</slot>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const isAccordionOpen = ref(false);

const toggleAccordion = () => {
  isAccordionOpen.value = !isAccordionOpen.value;
};
</script>

<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
