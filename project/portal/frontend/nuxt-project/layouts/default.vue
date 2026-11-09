<template>
  <div>
    <slot></slot>
    <modals-container></modals-container>
  </div>
</template>

<script setup lang="ts">
import {ModalsContainer} from "vue-final-modal";
</script>
