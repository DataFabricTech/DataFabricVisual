<template>
  <div class="custom-header">
    현황
    <div class="tooltip" v-tooltip:top="tooltipContent">
      <svg-icon class="svg-icon" name="help-outline"></svg-icon>
    </div>
  </div>
</template>

<script setup>
const tooltipContent = "top tooltip"; // 툴팁 내용
</script>

<style scoped></style>
