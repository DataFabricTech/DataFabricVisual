<template>
  <div class="section-top-bar">
    <div class="l-top-bar">
      <h3 class="title">용어사전</h3>
    </div>
  </div>
  <div class="section-contents p-0 bg-white">
    <div class="l-split">
      <glossary-list></glossary-list>
      <glossary-info></glossary-info>
      <glossary-term-info></glossary-term-info>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from "vue";
import { useGlossaryStore } from "~/store/glossary";
const { getAllTags } = useGlossaryStore();

onMounted(() => {
  getAllTags();
});
</script>
