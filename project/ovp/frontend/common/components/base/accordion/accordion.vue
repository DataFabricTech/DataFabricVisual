<template>
  <div class="accordion">
    <button class="accordion-head is-open" @click="toggleAccordion">
      <span class="accordion-head-title">
        <slot name="title">Accordion Title</slot>
      </span>
      <svg-icon class="accordion-head-icon svg-icon"
                :class="{ 'is-open': isOpen }"
                name="chevron-down-medium"></svg-icon>
    </button>
    <div v-if="isOpen" class="accordion-body">
      <slot name="body">Default body content</slot>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const isOpen = ref(false);

const toggleAccordion = () => {
  isOpen.value = !isOpen.value;
};
</script>

<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
