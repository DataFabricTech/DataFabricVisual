<template>
  <div class="section-top-bar">
    <div class="l-top-bar">
      <h3 class="title">분류</h3>
    </div>
  </div>
  <div class="section-contents p-0 bg-white">
    <div class="l-split">
      <classification-list></classification-list>
      <classification-list-modify></classification-list-modify>
    </div>
  </div>
</template>

<script setup lang="ts">
import ClassificationList from "@/components/classification/classification-list/classificationList.vue";
import ClassificationListModify from "@/components/classification/classification-list/classificationList-modify.vue";

</script>

<style lang="scss" scoped></style>
