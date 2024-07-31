<template>
  <div class="section-top-bar">
    <div class="l-top-bar">
      <h3 class="title">용어사전</h3>
      <button class="button button-primary w-20">저장</button>
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

<style scoped></style>
<script>
import MenuSearch from "@extends/menu-seach/menu-search.vue";

export default defineComponent({
  components: { MenuSearch },
  data() {
    return {
      showModalDic: false,
      showModal: false,
    };
  },
});
</script>
