<template>
  <div class="kebab-menu">
    <BaseButton @focus="state.isOpen=true" @blur="state.isOpen = false" class="button button-icon button-lg kebab-button" :class="state.isOpen ? 'is-selected':''" title="열기" >
      <span class="hidden-text">상세설정 열기</span>
      <svg-icon name="kebab-menu" class="svg-icon" />
    </BaseButton>
    <BaseContext class="kebab-context" v-if="state.isOpen"></BaseContext>
  </div>
</template>

<script setup lang="ts">
const state = reactive({
  isOpen: false
});
</script>
