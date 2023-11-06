<template>
  <div class="tooltip">
    <span class="tooltip-arrow"></span>
    <div class="tooltip-inner">
      <strong class="tooltip-title">
        <slot name="title"></slot>
      </strong>
      <span class="tooltip-text">
        <slot name="text"></slot>
      </span>
    </div>
  </div>
</template>

<script setup lang="ts">

</script>
